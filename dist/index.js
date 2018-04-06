"use strict";
/// <reference path="preferences.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
var confluence_xmlrpc_1 = require("./confluence-xmlrpc");
var confluence_site_1 = require("./confluence-site");
var config_1 = require("./config");
var URL = require("url");
var path = require("path");
var fs = require("fs");
var util = require("util");
var chalk = require("chalk");
var request = require("request");
var minimist = require("minimist");
var Rx = require("rx");
var figlet = require('figlet');
var LOGO = 'Confluence Site';
var LOGO_FONT = 'Stick Letters';
var rxFiglet = Rx.Observable.fromNodeCallback(figlet);
var argv = process.argv.slice(2);
var args = minimist(argv, {});
var commands;
(function (commands) {
    //
    // COMMAND
    //
    function deploy() {
        //console.dir( args );
        rxFiglet(LOGO, undefined)
            .doOnNext(function (logo) { return console.log(chalk.magenta(logo)); })
            .flatMap(function (logo) { return config_1.rxConfig(args['config'] || false); })
            .flatMap(function (result) { return rxConfluenceConnection(result[0], result[1]); })
            .flatMap(function (result) { return rxGenerateSite(result[1], result[0]); })
            .subscribe(
        //(result) => console.dir( result, {depth:2} ),
        function (result) { }, function (err) { return console.error(chalk.red(err)); });
    }
    commands.deploy = deploy;
    function init() {
        rxFiglet(LOGO, undefined)
            .doOnNext(function (logo) { return console.log(chalk.magenta(logo)); })
            .flatMap(function () { return config_1.rxConfig(true, args['serverid']); })
            .subscribe(function (value) { }, function (err) { return console.error(chalk.red(err)); });
    }
    commands.init = init;
    function info() {
        rxFiglet(LOGO, undefined)
            .doOnNext(function (logo) { return console.log(chalk.magenta(logo)); })
            .flatMap(function () { return config_1.rxConfig(false); })
            .subscribe(function (value) { }, function (err) { return console.error(chalk.red(err)); });
    }
    commands.info = info;
    function remove() {
        rxFiglet(LOGO, undefined)
            .doOnNext(function (logo) { return console.log(chalk.magenta(logo)); })
            .flatMap(function () { return config_1.rxConfig(false); })
            .flatMap(function (result) { return rxConfluenceConnection(result[0], result[1]); })
            .flatMap(function (result) { return rxDelete(result[0], result[1]); })
            .subscribe(function (value) { console.log("# page(s) removed ", value); }, function (err) { return console.error(chalk.red(err)); });
    }
    commands.remove = remove;
    function download(pageId, fileName, isStorageFormat) {
        if (isStorageFormat === void 0) { isStorageFormat = false; }
        function rxRequest(config, credentials) {
            return Rx.Observable.create(function (observer) {
                var input = URL.format({
                    protocol: config.protocol,
                    host: config.host,
                    port: String(config.port),
                    auth: credentials.username + ":" + credentials.password,
                    pathname: config.path + "pages/viewpagesrc.action",
                    query: { pageId: pageId }
                });
                var auth = "Basic " + new Buffer(credentials.username + ":" + credentials.password).toString("base64");
                console.log(input);
                request({
                    url: input,
                    headers: {
                        "Authorization": auth
                    }
                })
                    .pipe(fs.createWriteStream(fileName))
                    .on("end", function () { return observer.onCompleted(); })
                    .on("error", function (err) { return observer.onError(err); });
            });
        }
        rxFiglet(LOGO, undefined)
            .doOnNext(function (logo) { return console.log(chalk.magenta(logo)); })
            .flatMap(function () { return config_1.rxConfig(false); })
            .flatMap(function (_a) {
            var config = _a[0], credentials = _a[1];
            return rxRequest(config, credentials);
        })
            .subscribe(function (res) {
            console.log(res);
        }, function (err) { return console.error(chalk.red(err)); });
    }
    commands.download = download;
})(commands || (commands = {})); // end namespace command
clrscr();
//console.dir( args );
var command = (args._.length === 0) ? "help" : args._[0];
switch (command) {
    case "deploy":
        commands.deploy();
        break;
    case "init":
        commands.init();
        break;
    case "delete":
        commands.remove();
        break;
    case "info":
        commands.info();
        break;
    case "download":
        commands.download("37324124", "download.txt");
        break;
    default:
        usage();
}
/**
 * CLEAR SCREEN
 */
function clrscr() {
    //process.stdout.write('\033c');
    process.stdout.write('\x1Bc');
}
/**
 *
 */
function usageCommand(cmd, desc) {
    var args = [];
    for (var _i = 2; _i < arguments.length; _i++) {
        args[_i - 2] = arguments[_i];
    }
    desc = chalk.italic.gray(desc);
    return args.reduce(function (previousValue, currentValue, currentIndex, array) {
        return util.format("%s %s", previousValue, chalk.yellow(currentValue));
    }, "\n\n" + cmd) + desc;
}
/**
 *
 */
function usage() {
    rxFiglet(LOGO, LOGO_FONT)
        .doOnCompleted(function () { return process.exit(-1); })
        .subscribe(function (logo) {
        console.log(chalk.bold.magenta(logo), "\n" +
            chalk.cyan("Usage:") +
            " confluence-site " +
            usageCommand("init", "\t// create/update configuration", "--serverid <serverid>") +
            usageCommand("deploy", "\t\t// deploy site to confluence", "[--config]") +
            usageCommand("delete", "\t\t\t\t// delete site") +
            usageCommand("info", "\t\t\t\t// show configuration") +
            "\n\n" +
            chalk.cyan("Options:") +
            "\n\n" +
            " --serverid \t" + chalk.italic.gray("// it is the credentials' profile.") +
            "\n" +
            " --config\t" + chalk.italic.gray("// force reconfiguration") +
            "\n");
    });
}
/**
 *
 */
function newSiteProcessor(confluence, config) {
    var siteHome = (path.isAbsolute(config.sitePath)) ?
        path.dirname(config.sitePath) :
        path.join(process.cwd(), path.dirname(config.sitePath));
    var site = new confluence_site_1.SiteProcessor(confluence, config.spaceId, config.parentPageTitle, siteHome);
    return site;
}
/**
 *
 */
function rxConfluenceConnection(config, credentials) {
    var service = confluence_xmlrpc_1.XMLRPCConfluenceService.create(config, credentials);
    var rxConnection = Rx.Observable.fromPromise(service);
    var rxCfg = Rx.Observable.just(config);
    return Rx.Observable.combineLatest(rxConnection, rxCfg, function (conn, conf) { return [conn, conf]; });
}
/**
 *
 */
function rxDelete(confluence, config) {
    //let recursive = args['recursive'] || false;
    var siteFile = path.basename(config.sitePath);
    var site = newSiteProcessor(confluence, config);
    var rxParentPage = Rx.Observable.fromPromise(confluence.getPage(config.spaceId, config.parentPageTitle));
    var rxParseSite = site.rxParse(siteFile);
    return Rx.Observable.combineLatest(rxParentPage, rxParseSite, function (parent, home) { return [parent, home]; })
        .flatMap(function (result) {
        var parent = result[0], pages = result[1];
        var first = pages[0];
        var getHome = Rx.Observable.fromPromise(confluence.getPageByTitle(parent.id, first.$.name));
        return getHome
            .filter(function (home) { return home != null; })
            .flatMap(function (home) {
            return Rx.Observable.fromPromise(confluence.getDescendents(home.id))
                .flatMap(function (summaries) { return Rx.Observable.fromArray(summaries); })
                .flatMap(function (page) { return Rx.Observable.fromPromise(confluence.removePageById(page.id))
                .doOnNext(function (r) { return console.log("page:", page.title, "removed!", r); }); })
                .reduce(function (acc, x) { return ++acc; }, 0)
                .flatMap(function (n) {
                return Rx.Observable.fromPromise(confluence.removePageById(home.id))
                    .doOnNext(function (r) { return console.log("page:", home.title, "removed!", r); })
                    .map(function (value) { return ++n; });
            });
        });
    });
}
/**
 *
 */
function rxGenerateSite(config, confluence) {
    var siteFile = path.basename(config.sitePath);
    var site = newSiteProcessor(confluence, config);
    return site.rxStart(siteFile)
        .doOnCompleted(function () { return confluence.connection.logout().then(function () {
        //console.log("logged out!");
    }); });
}
