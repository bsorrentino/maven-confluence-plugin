/// <reference path="preferences.d.ts" />

import {XMLRPCConfluenceService} from "./confluence-xmlrpc";
import {SiteProcessor, Element, ElementAttributes} from "./confluence-site";
import {rxConfig, ConfigAndCredentials} from "./config";

import * as URL from "url";
import * as path from "path";
import * as fs from "fs";
import * as util from "util";
import * as chalk from "chalk";

import request = require("request");

import minimist     = require("minimist");
import Rx           = require("rx");
import Preferences  = require("preferences");

interface Figlet {
  ( input:string, font:string|undefined, callback:(err:any, res:string) => void  ):void;

  fonts( callback:(err:any, res:Array<string>) => void ):void;
  metadata( type:string, callback:(err:any, options:any, headerComment:string) =>void ):void
}
const figlet:Figlet = require('figlet');

const LOGO      = 'Confluence Site';
const LOGO_FONT = 'Stick Letters';

let rxFiglet = Rx.Observable.fromNodeCallback( figlet );

let argv = process.argv.slice(2);

let args = minimist( argv, {} );


namespace commands {

//
// COMMAND
//
export function deploy() {

  //console.dir( args );

  rxFiglet( LOGO, undefined )
    .doOnNext( (logo) => console.log( chalk.magenta(logo as string) ) )
    //.map( (logo) => args['config'] || false )
    //.doOnNext( (v) => console.log( "force config", v, args))
    .flatMap( (logo) => rxConfig(args['config'] || false ) )
    .flatMap( ([config,credentials]) => rxConfluenceConnection( config, credentials  ) )
    .flatMap( ([confluence,config]) => rxGenerateSite( config, confluence ) )
    .subscribe(
      //(result) => console.dir( result, {depth:2} ),
      (result) => {},
      (err) => console.error( chalk.red(err) )
    );

}

export function init() {
    rxFiglet( LOGO, undefined )
    .doOnNext( (logo) => console.log( chalk.magenta(logo as string) ) )
    .flatMap( () => rxConfig( true, args['serverid']) )
    .subscribe(
      (value)=> {},
      (err)=> console.error( chalk.red(err) )
    );

}

export function info() {
    rxFiglet( LOGO, undefined )
    .doOnNext( (logo) => console.log( chalk.magenta(logo as string) ) )
    .flatMap( () => rxConfig( false ) )
    .subscribe(
      (value)=> {},
      (err)=> console.error( chalk.red(err) )
    );

}

export function remove() {
    rxFiglet( LOGO, undefined )
    .doOnNext( (logo) => console.log( chalk.magenta(logo as string) ) )
    .flatMap( () => rxConfig(false) )
    .flatMap( (result) => rxConfluenceConnection( result[0], result[1]  ) )
    .flatMap( (result) => rxDelete( result[0], result[1] ) )
    .subscribe(
      (value)=> { console.log( "# page(s) removed ", value )},
      (err)=> console.error( chalk.red(err) )
    ); 
}


export function download( pageId:string, fileName:string, isStorageFormat = true ) {

  function rxRequest( config:Config, credentials:Credentials ):Rx.Observable<string> {
    return Rx.Observable.create( (observer) => {

      let pathname = isStorageFormat ?
      "/plugins/viewstorage/viewpagestorage.action" :
      "pages/viewpagesrc.action";

      let input = URL.format({ 
        protocol:config.protocol,
        host: config.host,
        port: String(config.port),
        auth: credentials.username + ":" + credentials.password,
        pathname: config.path + pathname,
        query:{ pageId:pageId}    
      });

      console.log(input);

      request( { 
        url:input
      } )
      .pipe( fs.createWriteStream(fileName) )
      .on("end", () => observer.onCompleted() )
      .on("error", (err) => observer.onError(err) );
    });
  }


  rxFiglet( LOGO, undefined )
  .doOnNext( (logo) => console.log( chalk.magenta(logo as string) ) )
  .flatMap( () => rxConfig( false ) )
  .flatMap( ([config,credentials]) => rxRequest( config, credentials) )
  .subscribe( 
    (res) => {
      console.log(res)
     } ,
    err => console.error( chalk.red(err) )
  );
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
} // end namespace command


clrscr();

//console.dir( args );

let command = (args._.length===0) ? "help" : args._[0];

switch( command ) {
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
    commands.download( pageid, args["file"] || pageid, args["wiki"] || true );
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
function usageCommand( cmd:string, desc:string, ...args: string[]) {
  desc = chalk.italic.gray(desc);
  return args.reduce( (previousValue, currentValue, currentIndex, array)=> {
    return util.format( "%s %s", previousValue, chalk.yellow(currentValue) );
  }, "\n\n" + cmd ) + desc;
}

/**
 * 
 */
function usage() {

  rxFiglet( LOGO, LOGO_FONT )
  .doOnCompleted( () => process.exit(-1) )
  .subscribe( (logo) => {

    console.log( chalk.bold.magenta(logo as string),
      "\n" +
      chalk.cyan( "Usage:") +
      " confluence-site " +
      usageCommand( "init", "\t// create/update configuration", "--serverid <serverid>" ) +
      usageCommand( "deploy", "\t\t// deploy site to confluence", "[--config]" ) +
      usageCommand( "delete", "\t\t\t\t// delete site" ) +
      usageCommand( "download", " // download page content", "--pageid <pageid>", "[--file]", "[--wiki]" ) +
      usageCommand( "info", "\t\t\t\t// show configuration" ) +
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
      "\n"
    );

  });
}

/**
 * 
 */
function newSiteProcessor( confluence:XMLRPCConfluenceService, config:Config ):SiteProcessor {

    let siteHome = ( path.isAbsolute(config.sitePath) ) ?
                        path.dirname(config.sitePath) :
                        path.join( process.cwd(), path.dirname(config.sitePath ));

    let site = new SiteProcessor( confluence,
                              config.spaceId,
                              config.parentPageTitle,
                              siteHome
                            );
    return site;
                  
}

/**
 * 
 */
function rxConfluenceConnection(
                config:Config,
                credentials:Credentials ):Rx.Observable<[XMLRPCConfluenceService,Config]>
{

      let service = XMLRPCConfluenceService.create( config, credentials );

      let rxConnection = Rx.Observable.fromPromise( service );

      let rxCfg = Rx.Observable.just( config );

      return Rx.Observable.combineLatest( rxConnection, rxCfg, 
                (conn, conf) => { return [conn, conf] as [XMLRPCConfluenceService,Config]; } );

}

/**
 * 
 */
function rxDelete( confluence:XMLRPCConfluenceService, config:Config  ):Rx.Observable<number> {
    //let recursive = args['recursive'] || false;

    let siteFile = path.basename( config.sitePath );

    let site = newSiteProcessor( confluence, config );
    
    let rxParentPage = Rx.Observable.fromPromise( confluence.getPage( config.spaceId, config.parentPageTitle) );
    let rxParseSite = site.rxParse( siteFile );

    return Rx.Observable.combineLatest( rxParentPage, rxParseSite, 
            (parent,home) => [parent,home] as [Model.Page,Array<Object>])
              //.doOnNext( (result) => console.dir( result ) )
              .flatMap( (result) => {
                
                let [parent,pages] = result;
                let first = pages[0] as Element ;
                
                let getHome = Rx.Observable.fromPromise( confluence.getPageByTitle( parent.id as string, first.$.name as string) );

                return getHome
                        .filter( (home) => home!=null )
                        .flatMap( (home) => 
                              Rx.Observable.fromPromise(confluence.getDescendents( home.id as string))
                                        .flatMap( summaries => Rx.Observable.fromArray(summaries) )
                                        .flatMap( (page:Model.PageSummary) => Rx.Observable.fromPromise(confluence.removePageById( page.id as string))
                                        .doOnNext( r => console.log( "page:", page.title, "removed!", r )) )
                                        .reduce( ( acc, x ) => ++acc, 0 )
                                        .flatMap( n => 
                                              Rx.Observable.fromPromise(confluence.removePageById(home.id as string) )
                                                              .doOnNext( (r) => console.log( "page:", home.title, "removed!", r ))
                                                              .map( (value) => ++n )
                                        )
                          )
                        ;
                        
              })                        
}

/**
 * 
 */
function rxGenerateSite( config:Config, confluence:XMLRPCConfluenceService ):Rx.Observable<any> {

    let siteFile = path.basename( config.sitePath );

    let site = newSiteProcessor( confluence, config );

    return site.rxStart( siteFile )
      .doOnCompleted( () => confluence.connection.logout().then( () => {
        //console.log("logged out!");
      }));

}

