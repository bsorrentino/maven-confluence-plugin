
import {XMLRPCConfluenceService} from "./confluence-xmlrpc";
import {ElementType, rxSite, rxReadContent} from "./confluence-site";
import * as path from "path";
import Rx = require("rx");

interface ConfigTest extends Config {
  credentials:Credentials;
  spaceId:string;
  pageTitle:string;
}

let config:ConfigTest = require( "./config.json").local;
//let config:ConfigTest = require( "./config.json").softphone;
let sitePath = __dirname;

XMLRPCConfluenceService.create(config,config.credentials)
.then( (confluence:XMLRPCConfluenceService) => {

  rxSite(path.join(sitePath,'site.1.xml'))
    .filter( (data) => data.type==ElementType.PAGE )
    .doOnNext( (data => console.log( "name", data.$.name, "type", data.type)))
    .concatMap( (data) =>  {
      return Rx.Observable.fromPromise(confluence.getOrCreatePage( config.spaceId, config.pageTitle, data.$.name ))
              .flatMap( (page) => {
                  if( page.id ) {
                    console.log( "remove page", page.id );
                    return Rx.Observable.fromPromise( confluence.connection.removePage(page.id) ).map( () => page) ;
                  }
                  console.log( "create page", page.title, page.id );
                  return rxReadContent( path.join(sitePath, data.$.uri) )
                    .flatMap( (storage) => Rx.Observable.fromPromise(confluence.storePageContent( page, storage )));
              })
    })
    .doOnCompleted( () => confluence.connection.logout().then( ()=> console.log("logged out!") ))
    .subscribe( (page) => {
      console.log( "page", page.title, "stored!" )
    });


});
