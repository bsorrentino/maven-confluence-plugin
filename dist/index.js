"use strict";
var confluence_xmlrpc_1 = require("./confluence-xmlrpc");
var confluence_site_1 = require("./confluence-site");
var config_1 = require("./config");
var path = require("path");
var util = require("util");
var chalk = require("chalk");
var minimist = require("minimist");
var Rx = require("rx");
var figlet = require('figlet');
var LOGO = 'Confluence CLI';
var LOGO_FONT = 'Stick Letters';
var rxFiglet = Rx.Observable.fromNodeCallback(figlet);
var argv = process.argv.slice(2);
var args = minimist(argv, {});
clrscr();
var command = (args._.length === 0) ? "help" : args._[0];
switch (command) {
    case "deploy":
        main();
        break;
    case "config":
        config_1.rxConfig(args['update']).subscribe(function (value) { }, function (err) { return console.error(err); });
        break;
    default:
        usage();
}
function clrscr() {
    process.stdout.write('\x1Bc');
}
function usageCommand(cmd, desc) {
    var args = [];
    for (var _i = 2; _i < arguments.length; _i++) {
        args[_i - 2] = arguments[_i];
    }
    desc = chalk.italic.gray("// " + desc);
    return args.reduce(function (previousValue, currentValue, currentIndex, array) {
        return util.format("%s %s", previousValue, chalk.yellow(currentValue));
    }, "\n\n" + cmd) + "\t" + desc;
}
function usage() {
    rxFiglet(LOGO, LOGO_FONT)
        .doOnCompleted(function () { return process.exit(-1); })
        .subscribe(function (logo) {
        console.log(chalk.bold.magenta(logo), "\n" +
            chalk.cyan("Usage:") +
            " confluence-cli " +
            usageCommand("deploy", "deploy site to confluence", "[--config]") +
            usageCommand("config", "show/create/update configuration", "[--update]") +
            "\n\n" +
            chalk.cyan("Options:") +
            "\n\n" +
            " --config | --update\t" + chalk.italic.gray("// force reconfiguration") +
            "\n");
    });
}
function rxConfluenceConnection(config, credentials) {
    var p = confluence_xmlrpc_1.XMLRPCConfluenceService.create(config, credentials);
    var rxConnection = Rx.Observable.fromPromise(p);
    var rxCfg = Rx.Observable.just(config);
    return Rx.Observable.combineLatest(rxConnection, rxCfg, function (conn, conf) { return [conn, conf]; });
}
function rxGenerateSite(config, confluence) {
    var siteHome = (path.isAbsolute(config.sitePath)) ?
        path.dirname(config.sitePath) :
        path.join(__dirname, path.dirname(config.sitePath));
    var siteFile = path.basename(config.sitePath);
    var site = new confluence_site_1.SiteProcessor(confluence, config.spaceId, config.parentPageTitle, siteHome);
    return site.rxStart(siteFile)
        .doOnCompleted(function () { return confluence.connection.logout().then(function () {
    }); });
}
function main() {
    rxFiglet(LOGO)
        .doOnNext(function (logo) { return console.log(chalk.magenta(logo)); })
        .map(function (logo) { return args['config'] || false; })
        .flatMap(config_1.rxConfig)
        .flatMap(function (result) { return rxConfluenceConnection(result[0], result[1]); })
        .flatMap(function (result) { return rxGenerateSite(result[1], result[0]); })
        .subscribe(function (result) { }, function (err) { return console.error(err); });
}
