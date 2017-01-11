
import {XMLRPCConfluenceService} from "./confluence-xmlrpc";
import {SiteProcessor} from "./confluence-site";
import * as path from "path";
import * as fs from "fs";
import Rx = require("rx");

import Preferences = require("preferences");

interface ConfigTest extends Config {
  credentials:Credentials;
  spaceId:string;
  pageTitle:string;
}


let config:ConfigTest = require( "./config.json").local;
//let config:ConfigTest = require( "./config.json").softphone;

XMLRPCConfluenceService.create(config,config.credentials)
.then( (confluence:XMLRPCConfluenceService) => {

  let site = new SiteProcessor( confluence, config.spaceId, config.pageTitle, __dirname);

  site.rxStart( 'site.1.xml' )
    .doOnCompleted( () => confluence.connection.logout().then( () => console.log("logged out!") ))
    .subscribe( (elem) => {
      //console.log( "element", elem , "processed!" )
    });

});
