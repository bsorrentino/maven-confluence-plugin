"use strict";
var fs = require("fs");
var path = require("path");
var url = require("url");
var util = require("util");
var assert = require("assert");
var Rx = require("rx");
var Preferences = require("preferences");
var inquirer = require("inquirer");
var CONFIG_FILE = "config.json";
var PREFERENCES_ID = "org.bsc.confluence-cli";
function modulePath() {
    return "." + path.sep + CONFIG_FILE;
}
var ConfigUtils;
(function (ConfigUtils) {
    var MaskedValue = (function () {
        function MaskedValue(value) {
            this.value = value;
            this._value = (util.isNullOrUndefined(value)) ? "" :
                (util.isObject(value) ? value['_value'] : value);
        }
        MaskedValue.prototype.mask = function () {
            return Array(this._value.length + 1).join("*");
        };
        MaskedValue.prototype.toString = function () {
            return this.mask();
        };
        MaskedValue.validate = function (value) {
            if (util.isNullOrUndefined(value))
                return false;
            if (util.isObject(value))
                return MaskedValue.validate(value["_value"]);
            return true;
        };
        MaskedValue.getValue = function (value) {
            assert(MaskedValue.validate(value));
            return (util.isObject(value)) ? value["_value"] : value;
        };
        return MaskedValue;
    }());
    ConfigUtils.MaskedValue = MaskedValue;
    var Port;
    (function (Port) {
        function isValid(port) {
            return (util.isNullOrUndefined(port) || util.isNumber(port) || Number(port) !== NaN);
        }
        Port.isValid = isValid;
        function value(port, def) {
            if (def === void 0) { def = 80; }
            assert(isValid(port));
            return (util.isNullOrUndefined(port)) ? def : Number(port);
        }
        Port.value = value;
    })(Port = ConfigUtils.Port || (ConfigUtils.Port = {}));
    var Url;
    (function (Url) {
        function format(config) {
            assert(!util.isNullOrUndefined(config));
            var port = util.isNull(config.port) ? "" : (config.port === 80) ? "" : ":" + config.port;
            return util.format("%s//%s%s%s", config.protocol, config.host, port, config.path);
        }
        Url.format = format;
    })(Url = ConfigUtils.Url || (ConfigUtils.Url = {}));
})(ConfigUtils || (ConfigUtils = {}));
function rxConfig(force) {
    if (force === void 0) { force = false; }
    var configPath = path.join(__dirname, CONFIG_FILE);
    var defaultConfig = {
        host: "", path: "", port: null, protocol: "http", spaceId: "", parentPageTitle: "Home", "sitePath": "site/site.xml"
    };
    var defaultCredentials = {
        username: "",
        password: ""
    };
    if (fs.existsSync(configPath)) {
        console.log("found!");
        defaultConfig = require(modulePath());
        defaultCredentials = new Preferences(PREFERENCES_ID, defaultCredentials);
        if (!force) {
            var data = [defaultConfig, defaultCredentials];
            return Rx.Observable.just(data);
        }
    }
    var answers = inquirer.prompt([
        {
            type: "input",
            name: "url",
            message: "confluence url:",
            default: ConfigUtils.Url.format(defaultConfig),
            validate: function (value) {
                var p = url.parse(value);
                var valid = (p.protocol && p.host && ConfigUtils.Port.isValid(p.port));
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
            validate: function (value) {
                return value.length == 0 ? "username must be specified!" : true;
            }
        },
        {
            type: "password",
            name: "password",
            message: "confluence password:",
            default: new ConfigUtils.MaskedValue(defaultCredentials.password),
            validate: function (value) { return ConfigUtils.MaskedValue.validate(value); },
            filter: function (value) { return ConfigUtils.MaskedValue.getValue(value); }
        }
    ]);
    var rxCreateConfigFile = Rx.Observable.fromNodeCallback(fs.writeFile);
    return Rx.Observable.fromPromise(answers)
        .map(function (answers) {
        var p = url.parse(answers['url']);
        var config = {
            path: p.path || "",
            protocol: p.protocol,
            host: p.hostname,
            port: ConfigUtils.Port.value(p.port),
            spaceId: answers['spaceId'],
            parentPageTitle: answers['parentPageTitle'],
            sitePath: "site/site.xml"
        };
        var c = new Preferences(PREFERENCES_ID, defaultCredentials);
        c.username = answers['username'];
        c.password = answers['password'];
        return [config, c];
    })
        .flatMap(function (result) {
        return rxCreateConfigFile(configPath, JSON.stringify(result[0]))
            .map(function (res) { return result; });
    });
}
exports.rxConfig = rxConfig;
function main() {
    rxConfig(true)
        .subscribe(function (result) {
        console.dir(result, { depth: 2 });
    });
}
