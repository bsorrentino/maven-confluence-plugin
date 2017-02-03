
import {XMLRPCConfluenceService} from "./confluence-xmlrpc";
import {SiteProcessor} from "./confluence-site";
import {rxConfig} from "./config";

import * as path from "path";
import * as fs from "fs";
import * as util from "util";
import * as chalk from "chalk";

import minimist = require("minimist");
import Rx = require("rx");
import Preferences = require("preferences");

const figlet = require('figlet');

const LOGO = 'Confluence CLI';
const LOGO_FONT = 'Stick Letters';

let rxFiglet =  Rx.Observable.fromNodeCallback( figlet );

let argv = process.argv.slice(2);

let args = minimist( argv, {
});


namespace commands {

//
//
// COMMAND
//
//
export function deploy() {

  //console.log( "args", process.argv.slice(2));
  //console.dir( args );

  rxFiglet( LOGO )
    .doOnNext( (logo) => console.log( chalk.magenta(logo as string) ) )
    .map( (logo) => args['config'] || false )
    //.doOnNext( (v) => console.log( "force config", v, args))
    .flatMap( rxConfig )
    .flatMap( (result) => rxConfluenceConnection( result[0] as Config, result[1] as Credentials ) )
    .flatMap( (result) => rxGenerateSite( result[1] as Config, result[0] as XMLRPCConfluenceService ) )
    .subscribe(
      //(result) => console.dir( result, {depth:2} ),
      (result) => {},
      (err) => console.error( err )
    );

}

export function config() {
    rxFiglet( LOGO )
    .doOnNext( (logo) => console.log( chalk.magenta(logo as string) ) )
    .map( (logo) => args['update'] || false )
    .flatMap( rxConfig )
    .subscribe(
      (value)=> {},
      (err)=> console.error( err )
    );

}

export function remove() {
    rxFiglet( LOGO )
    .doOnNext( (logo) => console.log( chalk.magenta(logo as string) ) )
    .map( (logo) => args['recursive'] || false )
    .subscribe(
      (value)=> {},
      (err)=> console.error( err )
    );
    

}

} // end namespace command


clrscr();

//console.dir( args );

let command = (args._.length===0) ? "help" : args._[0];

switch( command ) {
  case "deploy":
    commands.deploy();
  break;
  case "config":
    commands.config();
  break;
  case "delete":
    commands.remove();
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

function usageCommand( cmd:string, desc:string, ...args: string[]) {
  desc = chalk.italic.gray("// " + desc);
  return args.reduce( (previousValue, currentValue, currentIndex, array)=> {
    return util.format( "%s %s", previousValue, chalk.yellow(currentValue) );
  }, "\n\n" + cmd ) + "\t" + desc;
}

function usage() {

  rxFiglet( LOGO, LOGO_FONT )
  .doOnCompleted( () => process.exit(-1) )
  .subscribe( (logo) => {

    console.log( chalk.bold.magenta(logo as string),
      "\n" +
      chalk.cyan( "Usage:") +
      " confluence-cli " +
      usageCommand( "deploy", "deploy site to confluence", "[--config]" ) +
      usageCommand( "config", "show/create/update configuration", "[--update]" ) +
      usageCommand( "delete", "delete page tree", "[--recursive]" ) +
      "\n\n" +
      chalk.cyan("Options:") +
      "\n\n" +
      " --config | --update\t" + chalk.italic.gray("// force reconfiguration") +
      "\n" +
      " --recursive\t" + chalk.italic.gray("// delete page tree") +
      "\n"
    );

  });
}

function rxConfluenceConnection(
                config:Config,
                credentials:Credentials ):Rx.Observable<(XMLRPCConfluenceService | Config)[]>
{

      let p = XMLRPCConfluenceService.create( config, credentials );

      let rxConnection = Rx.Observable.fromPromise( p );

      let rxCfg = Rx.Observable.just( config );

      return Rx.Observable.combineLatest( rxConnection, rxCfg, ( conn, conf) => { return [conn, conf]; } );

}

function rxGenerateSite( config:Config, confluence:XMLRPCConfluenceService ):Rx.Observable<any> {

    let siteHome = ( path.isAbsolute(config.sitePath) ) ?
                          path.dirname(config.sitePath) :
                          path.join( process.cwd(), path.dirname(config.sitePath ));

    let siteFile = path.basename( config.sitePath );

    //console.log( "siteHome", siteHome, "siteFile", siteFile );

    let site = new SiteProcessor( confluence,
                                  config.spaceId,
                                  config.parentPageTitle,
                                  siteHome
                                );

    return site.rxStart( siteFile )
      .doOnCompleted( () => confluence.connection.logout().then( () => {
        //console.log("logged out!");
      }));

}

