"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const fs = require("fs");
const path = require("path");
const url = require("url");
const util = require("util");
const assert = require("assert");
const chalk = require("chalk");
const inquirer = require("inquirer");
const Rx = require("rx");
const Preferences = require("preferences");
const CONFIG_FILE = "config.json";
const SITE_PATH = "site.xml";
var ConfigUtils;
(function (ConfigUtils) {
    /**
     * masked password
     */
    function maskPassword(value) {
        assert.ok(!util.isNullOrUndefined(value));
        return Array(value.length + 1).join("*");
    }
    ConfigUtils.maskPassword = maskPassword;
    /**
     * MaskedValue
     */
    class MaskedValue {
        constructor(value) {
            this.value = value;
            this._value = (util.isNullOrUndefined(value)) ? "" :
                (util.isObject(value) ? value['_value'] : value);
        }
        mask() {
            return maskPassword(this._value);
        }
        toString() {
            return this.mask();
        }
        static validate(value) {
            if (util.isNullOrUndefined(value))
                return false;
            if (util.isObject(value))
                return MaskedValue.validate(value["_value"]);
            return true;
        }
        static getValue(value) {
            assert(MaskedValue.validate(value));
            return (util.isObject(value)) ? value["_value"] : value;
        }
    }
    ConfigUtils.MaskedValue = MaskedValue;
    let Port;
    (function (Port) {
        function isValid(port) {
            return (util.isNullOrUndefined(port) || util.isNumber(port) || Number(port) !== NaN);
        }
        Port.isValid = isValid;
        function value(port, def = 80) {
            assert(isValid(port));
            return (util.isNullOrUndefined(port)) ? def : Number(port);
        }
        Port.value = value;
    })(Port = ConfigUtils.Port || (ConfigUtils.Port = {}));
    let Url;
    (function (Url) {
        function format(config) {
            assert(!util.isNullOrUndefined(config));
            let port = util.isNull(config.port) ? "" : (config.port === 80) ? "" : ":" + config.port;
            return util.format("%s//%s%s%s", config.protocol, config.host, port, config.path);
        }
        Url.format = format;
    })(Url = ConfigUtils.Url || (ConfigUtils.Url = {}));
})(ConfigUtils || (ConfigUtils = {}));
function printConfig(value) {
    let [cfg, crd] = value;
    let out = [
        ["site path:\t", cfg.sitePath],
        ["confluence url:\t", ConfigUtils.Url.format(cfg)],
        ["confluence space id:", cfg.spaceId],
        ["confluence parent page:", cfg.parentPageTitle],
        ["serverid:\t", cfg.serverId],
        ["confluence username:", crd.username],
        ["confluence password:", ConfigUtils.maskPassword(crd.password)]
    ].reduce((prev, curr, index, array) => {
        let [label, value] = curr;
        return util.format("%s%s\t%s\n", prev, chalk.cyan(label), chalk.yellow(value));
    }, "\n\n");
    console.log(out);
}
/**
 *
 */
function rxConfig(force, serverId) {
    let configPath = path.join(process.cwd(), CONFIG_FILE);
    //console.log( "configPath", configPath );
    //console.log( "relative",  modulePath() );
    let defaultConfig = {
        host: "",
        path: "",
        port: -1,
        protocol: "http",
        spaceId: "",
        parentPageTitle: "Home",
        sitePath: SITE_PATH,
        serverId: serverId
    };
    let defaultCredentials = {
        username: "",
        password: ""
    };
    if (fs.existsSync(configPath)) {
        //console.log( configPath, "found!" );
        defaultConfig = require(path.join(process.cwd(), CONFIG_FILE));
        if (util.isNullOrUndefined(defaultConfig.serverId)) {
            return Rx.Observable.throw(new Error("'serverId' is not defined!"));
        }
        defaultCredentials = new Preferences(defaultConfig.serverId, defaultCredentials);
        if (!force) {
            let data = [defaultConfig, defaultCredentials];
            return Rx.Observable.just(data)
                .do(printConfig);
        }
    }
    else {
        if (util.isNullOrUndefined(defaultConfig.serverId)) {
            return Rx.Observable.throw(new Error("'serverId' is not defined!"));
        }
    }
    console.log(chalk.green(">"), chalk.bold("serverId:"), chalk.cyan(defaultConfig.serverId));
    let answers = inquirer.prompt([
        {
            type: "input",
            name: "url",
            message: "confluence url:",
            default: ConfigUtils.Url.format(defaultConfig),
            validate: (value) => {
                let p = url.parse(value);
                //console.log( "parsed url", p );
                let valid = (p.protocol && p.host && ConfigUtils.Port.isValid(p.port));
                return (valid) ? true : "url is not valid!";
            }
        },
        {
            type: "input",
            name: "spaceId",
            message: "confluence space id:",
            default: defaultConfig.spaceId
        },
        {
            type: "input",
            name: "parentPageTitle",
            message: "confluence parent page title:",
            default: defaultConfig.parentPageTitle
        },
        {
            type: "input",
            name: "username",
            message: "confluence username:",
            default: defaultCredentials.username,
            validate: (value) => {
                return value.length == 0 ? "username must be specified!" : true;
            }
        },
        {
            type: "password",
            name: "password",
            message: "confluence password:",
            default: new ConfigUtils.MaskedValue(defaultCredentials.password),
            validate: (value) => { return ConfigUtils.MaskedValue.validate(value); },
            filter: (value) => { return ConfigUtils.MaskedValue.getValue(value); }
        }
    ]);
    function rxCreateConfigFile(path, data, onSuccessReturn) {
        return Rx.Observable.create((observer) => fs.writeFile(path, data, (err) => {
            if (err) {
                observer.onError(err);
                return;
            }
            observer.onNext(onSuccessReturn);
            observer.onCompleted();
        }));
    }
    return Rx.Observable.fromPromise(answers)
        .map((answers) => {
        let p = url.parse(answers['url']);
        //console.log( p );
        let config = {
            path: p.path || "",
            protocol: p.protocol,
            host: p.hostname,
            port: ConfigUtils.Port.value(p.port),
            spaceId: answers['spaceId'],
            parentPageTitle: answers['parentPageTitle'],
            sitePath: SITE_PATH,
            serverId: defaultConfig.serverId
        };
        /*
        let credentials:Credentials = {
            username:answers['username'],
            password:answers['password']
        };
        */
        let c = new Preferences(config.serverId, defaultCredentials);
        c.username = answers['username'];
        c.password = answers['password'];
        return [config, c];
    })
        .flatMap(result => rxCreateConfigFile(configPath, JSON.stringify(result[0]), result));
}
exports.rxConfig = rxConfig;
function main() {
    rxConfig(true)
        .subscribe((result) => {
        console.dir(result, { depth: 2 });
    });
}
