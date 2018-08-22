"use strict";
/// <reference path="preferences.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
const confluence_xmlrpc_1 = require("./confluence-xmlrpc");
const confluence_site_1 = require("./confluence-site");
const config_1 = require("./config");
const URL = require("url");
const path = require("path");
const fs = require("fs");
const util = require("util");
const chalk = require("chalk");
const request = require("request");
const minimist = require("minimist");
const Rx = require("rx");
const figlet = require('figlet');
const LOGO = 'Confluence Site';
const LOGO_FONT = 'Stick Letters';
let rxFiglet = Rx.Observable.fromNodeCallback(figlet);
let argv = process.argv.slice(2);
let args = minimist(argv, {});
var commands;
(function (commands) {
    //
    // COMMAND
    //
    function deploy() {
        //console.dir( args );
        rxFiglet(LOGO, undefined)
            .doOnNext((logo) => console.log(chalk.magenta(logo)))
            //.map( (logo) => args['config'] || false )
            //.doOnNext( (v) => console.log( "force config", v, args))
            .flatMap((logo) => config_1.rxConfig(args['config'] || false))
            .flatMap(([config, credentials]) => rxConfluenceConnection(config, credentials))
            .flatMap(([confluence, config]) => rxGenerateSite(config, confluence))
            .subscribe(
        //(result) => console.dir( result, {depth:2} ),
        (result) => { }, (err) => console.error(chalk.red(err)));
    }
    commands.deploy = deploy;
    function init() {
        rxFiglet(LOGO, undefined)
            .doOnNext((logo) => console.log(chalk.magenta(logo)))
            .flatMap(() => config_1.rxConfig(true, args['serverid']))
            .subscribe((value) => { }, (err) => console.error(chalk.red(err)));
    }
    commands.init = init;
    function info() {
        rxFiglet(LOGO, undefined)
            .doOnNext((logo) => console.log(chalk.magenta(logo)))
            .flatMap(() => config_1.rxConfig(false))
            .subscribe((value) => { }, (err) => console.error(chalk.red(err)));
    }
    commands.info = info;
    function remove() {
        rxFiglet(LOGO, undefined)
            .doOnNext((logo) => console.log(chalk.magenta(logo)))
            .flatMap(() => config_1.rxConfig(false))
            .flatMap((result) => rxConfluenceConnection(result[0], result[1]))
            .flatMap((result) => rxDelete(result[0], result[1]))
            .subscribe((value) => { console.log("# page(s) removed ", value); }, (err) => console.error(chalk.red(err)));
    }
    commands.remove = remove;
    function download(pageId, fileName, isStorageFormat = true) {
        function rxRequest(config, credentials) {
            return Rx.Observable.create((observer) => {
                let pathname = isStorageFormat ?
                    "/plugins/viewstorage/viewpagestorage.action" :
                    "pages/viewpagesrc.action";
                let input = URL.format({
                    protocol: config.protocol,
                    host: config.host,
                    port: String(config.port),
                    auth: credentials.username + ":" + credentials.password,
                    pathname: config.path + pathname,
                    query: { pageId: pageId }
                });
                console.log(input);
                request({
                    url: input
                })
                    .pipe(fs.createWriteStream(fileName))
                    .on("end", () => observer.onCompleted())
                    .on("error", (err) => observer.onError(err));
            });
        }
        rxFiglet(LOGO, undefined)
            .doOnNext((logo) => console.log(chalk.magenta(logo)))
            .flatMap(() => config_1.rxConfig(false))
            .flatMap(([config, credentials]) => rxRequest(config, credentials))
            .subscribe((res) => {
            console.log(res);
        }, err => console.error(chalk.red(err)));
        /*
          .flatMap( ([config,credentials]) => rxConfluenceConnection( config, credentials ) )
          .flatMap( ([confluence,config]) => Rx.Observable.fromPromise( confluence.getPageById( pageId )) )
            .subscribe(
              (res) => {
                console.log(res.title)
               } ,
              err => console.error( chalk.red(err) )
            );
         */
    }
    commands.download = download;
})(commands || (commands = {})); // end namespace command
clrscr();
//console.dir( args );
let command = (args._.length === 0) ? "help" : args._[0];
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
        {
            let pageid = args['pageid'];
            commands.download(pageid, args["file"] || pageid, args["wiki"] || true);
        }
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
function usageCommand(cmd, desc, ...args) {
    desc = chalk.italic.gray(desc);
    return args.reduce((previousValue, currentValue, currentIndex, array) => {
        return util.format("%s %s", previousValue, chalk.yellow(currentValue));
    }, "\n\n" + cmd) + desc;
}
/**
 *
 */
function usage() {
    rxFiglet(LOGO, LOGO_FONT)
        .doOnCompleted(() => process.exit(-1))
        .subscribe((logo) => {
        console.log(chalk.bold.magenta(logo), "\n" +
            chalk.cyan("Usage:") +
            " confluence-site " +
            usageCommand("init", "\t// create/update configuration", "--serverid <serverid>") +
            usageCommand("deploy", "\t\t// deploy site to confluence", "[--config]") +
            usageCommand("delete", "\t\t\t\t// delete site") +
            usageCommand("download", " // download page content", "--pageid <pageid>", "[--file]", "[--wiki]") +
            usageCommand("info", "\t\t\t\t// show configuration") +
            "\n\n" +
            chalk.cyan("Options:") +
            "\n\n" +
            " --serverid \t" + chalk.italic.gray("// it is the credentials' profile.") +
            "\n" +
            " --config\t" + chalk.italic.gray("// force reconfiguration.") +
            "\n" +
            " --pageid \t" + chalk.italic.gray("// the page identifier.") +
            "\n" +
            " --file \t" + chalk.italic.gray("// the output file name.") +
            "\n" +
            " --wiki \t" + chalk.italic.gray("// indicate deprecated wiki content format ") +
            "\n");
    });
}
/**
 *
 */
function newSiteProcessor(confluence, config) {
    let siteHome = (path.isAbsolute(config.sitePath)) ?
        path.dirname(config.sitePath) :
        path.join(process.cwd(), path.dirname(config.sitePath));
    let site = new confluence_site_1.SiteProcessor(confluence, config.spaceId, config.parentPageTitle, siteHome);
    return site;
}
/**
 *
 */
function rxConfluenceConnection(config, credentials) {
    let service = confluence_xmlrpc_1.XMLRPCConfluenceService.create(config, credentials);
    let rxConnection = Rx.Observable.fromPromise(service);
    let rxCfg = Rx.Observable.just(config);
    return Rx.Observable.combineLatest(rxConnection, rxCfg, (conn, conf) => { return [conn, conf]; });
}
/**
 *
 */
function rxDelete(confluence, config) {
    //let recursive = args['recursive'] || false;
    let siteFile = path.basename(config.sitePath);
    let site = newSiteProcessor(confluence, config);
    let rxParentPage = Rx.Observable.fromPromise(confluence.getPage(config.spaceId, config.parentPageTitle));
    let rxParseSite = site.rxParse(siteFile);
    return Rx.Observable.combineLatest(rxParentPage, rxParseSite, (parent, home) => [parent, home])
        //.doOnNext( (result) => console.dir( result ) )
        .flatMap((result) => {
        let [parent, pages] = result;
        let first = pages[0];
        let getHome = Rx.Observable.fromPromise(confluence.getPageByTitle(parent.id, first.$.name));
        return getHome
            .filter((home) => home != null)
            .flatMap((home) => Rx.Observable.fromPromise(confluence.getDescendents(home.id))
            .flatMap(summaries => Rx.Observable.fromArray(summaries))
            .flatMap((page) => Rx.Observable.fromPromise(confluence.removePageById(page.id))
            .doOnNext(r => console.log("page:", page.title, "removed!", r)))
            .reduce((acc, x) => ++acc, 0)
            .flatMap(n => Rx.Observable.fromPromise(confluence.removePageById(home.id))
            .doOnNext((r) => console.log("page:", home.title, "removed!", r))
            .map((value) => ++n)));
    });
}
/**
 *
 */
function rxGenerateSite(config, confluence) {
    let siteFile = path.basename(config.sitePath);
    let site = newSiteProcessor(confluence, config);
    return site.rxStart(siteFile)
        .doOnCompleted(() => confluence.connection.logout().then(() => {
        //console.log("logged out!");
    }));
}
