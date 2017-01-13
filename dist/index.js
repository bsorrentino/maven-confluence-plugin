"use strict";
var confluence_xmlrpc_1 = require("./confluence-xmlrpc");
var confluence_site_1 = require("./confluence-site");
var path = require("path");
var minimist = require("minimist");
var chalk = require("chalk");
var Rx = require("rx");
var figlet = require('figlet');
var config_1 = require("./config");
var LOGO = 'Confluence CLI';
var LOGO_FONT = 'Stick Letters';
var rxFiglet = Rx.Observable.fromNodeCallback(figlet);
var argv = process.argv.slice(2);
var args = minimist(argv, {});
clrscr();
if (args._['help'] || false) {
    usage();
}
else {
    main();
}
function clrscr() {
    process.stdout.write('\x1Bc');
}
function usage() {
    rxFiglet(LOGO, LOGO_FONT)
        .doOnCompleted(function () { return process.exit(-1); })
        .subscribe(function (logo) {
        console.log(logo, "\n" +
            chalk.cyan("Usage:") +
            " confluence-cli " +
            chalk.yellow("[--config]") +
            "\n\n" +
            chalk.cyan("Options:") +
            "\n\n" +
            " --config\t force reconfiguration" +
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
    console.log("siteHome", siteHome, "siteFile", siteFile);
    var site = new confluence_site_1.SiteProcessor(confluence, config.spaceId, config.parentPageTitle, siteHome);
    return site.rxStart(siteFile)
        .doOnCompleted(function () { return confluence.connection.logout().then(function () { return console.log("logged out!"); }); });
}
function main() {
    rxFiglet(LOGO)
        .doOnNext(function (logo) { return console.log(logo); })
        .map(function (logo) { return args['config'] || false; })
        .flatMap(config_1.rxConfig)
        .flatMap(function (result) { return rxConfluenceConnection(result[0], result[1]); })
        .flatMap(function (result) { return rxGenerateSite(result[1], result[0]); })
        .subscribe(function (result) {
        console.dir(result, { depth: 2 });
    });
}
function test() {
    var config = require("./config.json").local;
    confluence_xmlrpc_1.XMLRPCConfluenceService.create(config, config.credentials)
        .then(function (confluence) {
        var site = new confluence_site_1.SiteProcessor(confluence, config.spaceId, config.pageTitle, __dirname);
        site.rxStart('site.1.xml')
            .doOnCompleted(function () { return confluence.connection.logout().then(function () { return console.log("logged out!"); }); })
            .subscribe(function (elem) {
        });
    });
}
