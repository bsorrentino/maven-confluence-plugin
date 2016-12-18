import {XMLRPCConfluenceService} from "./confluence-xmlrpc";
import {ElementType, rxSite, rxReadContent, rxReadFile} from "./confluence-site";
import * as path from "path";
import Rx = require("rx");
import * as filesystem from "fs";

interface ConfigTest extends Config {
  credentials:Credentials;
  spaceId:string;
  pageTitle:string;
}

let config:ConfigTest = require( "./config.json").local;
//let config:ConfigTest = require( "./config.json").softphone;
let sitePath = __dirname;

//let rxReadFile = Rx.Observable.fromNodeCallback( filesystem.readFile );

XMLRPCConfluenceService.create(config,config.credentials)
.then( (confluence:XMLRPCConfluenceService) => {

    Rx.Observable.fromPromise(confluence.getOrCreatePage( config.spaceId, config.pageTitle, "home" ))
              .flatMap( (page) => {
                  if( page.id ) {
              
                    let attachment:Model.Attachment =  {
                        comment:"attachment test",
                        contentType:"image/png",
                        fileName:"appicon.png"
                    };

                    return rxReadFile( path.join(sitePath, "pages", attachment.fileName) )
                            .flatMap( (buffer:Buffer) => Rx.Observable.fromPromise(confluence.addAttachment( page, attachment, buffer )));

                  }
                  return Rx.Observable.throw( new Error("page home doesn't exist!" ));
    })
    .doOnCompleted( () => confluence.connection.logout().then( ()=> console.log("logged out!") ))
    .subscribe( (page) => {
      console.log( "attachment", page, "stored!" )
    });


});
