
import {XMLRPCConfluenceService} from "./confluence-xmlrpc";
import {SiteProcessor} from "./confluence-site";
import * as path from "path";
import * as fs from "fs";
import minimist = require("minimist");
import * as chalk from "chalk";
import Rx = require("rx");
const figlet = require('figlet');
import {rxConfig} from "./config";
import Preferences = require("preferences");

const LOGO = 'Confluence CLI';
const LOGO_FONT = 'Stick Letters';

let rxFiglet =  Rx.Observable.fromNodeCallback( figlet );

let argv = process.argv.slice(2);

let args = minimist( argv, {
});

clrscr();

if( args._['help'] || false ) {
  usage();
}
else {
  main();
}

/**
 * CLEAR SCREEN
 */
function clrscr() {
  //process.stdout.write('\033c');
  process.stdout.write('\x1Bc');

}


function usage() {
  
  rxFiglet( LOGO, LOGO_FONT )
  .doOnCompleted( () => process.exit(-1) )
  .subscribe( (logo) => {

    console.log( logo,
      "\n" +
      chalk.cyan( "Usage:") +
      " confluence-cli " + 
      chalk.yellow( "[--config]" ) + 
      "\n\n" +
      chalk.cyan("Options:") + 
      "\n\n" +
      " --config\t force reconfiguration" + 
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
                          path.join( __dirname, path.dirname(config.sitePath ));

    let siteFile = path.basename( config.sitePath );

    console.log( "siteHome", siteHome, "siteFile", siteFile );

    //return Rx.Observable.just( [ siteHome, siteFile ]);

    let site = new SiteProcessor( confluence, 
                                  config.spaceId, 
                                  config.parentPageTitle, 
                                  siteHome 
                                );

    return site.rxStart( siteFile )
      .doOnCompleted( () => confluence.connection.logout().then( () => console.log("logged out!") ));
 
}

function main() {
  
  //console.log( "args", process.argv.slice(2));
  //console.dir( args );

  rxFiglet( LOGO, )
    .doOnNext( (logo) => console.log(logo) )
    .map( (logo) => args['config'] || false )
    //.doOnNext( (v) => console.log( "force config", v, args))
    .flatMap( rxConfig )
    .flatMap( (result) => rxConfluenceConnection( result[0] as Config, result[1] as Credentials ) )
    .flatMap( (result) => rxGenerateSite( result[1] as Config, result[0] as XMLRPCConfluenceService ) )
    .subscribe( (result) => {
      console.dir( result, {depth:2} );
    });

}
