
import {XMLRPCConfluenceService} from "./confluence-xmlrpc";

interface ConfigTest extends Config {
  credentials:Credentials;
  spaceId:string;
  pageTitle:string;
}

//let config:ConfigTest = require( "./config.json").local;
let config:ConfigTest = require( "./config.json").softphone;

let confluence;

XMLRPCConfluenceService.create(config,config.credentials)
.then( (c:ConfluenceService) => confluence = c )
.then( () => {
  console.log( "logged in");
  return confluence.getPage( config.spaceId, config.pageTitle);
})
.then( (value:Model.Page) => console.log( "page", value) )
.then( () => {
  return confluence.getOrCreatePage(config.spaceId, config.pageTitle, "mytitle")
          .then( (result) => console.log( "mytitle", result) )
          ;
})
.then( () => confluence.connection.logout() )
.then( (value) => console.log( "logged out", value) )
.catch( (error) => {
  console.log( "error", error);
  return confluence.connection.logout();
});

/*
}).then( (value:Model.Page) => {
  console.log( "page", value);
  let newPage:Page = {
    title:"CLI",
    space:config.spaceId,
    parentId:value.id,
    content:"{TOC}"
  };

  return Promise.all([
    confluence.getDescendents( value.id ),
    confluence.storePage(newPage)
    ]);

}).then( ( result:[Array<PageSummary>, Page] ) => {
  console.log( "pages", result[0], "new page", result[1]);
  return confluence.removePage( result[1].id );
}).then( removed => {
  console.log( "page removed:", removed);
}).then( () => {
  return confluence.logout();
}).then( (value) => {
  console.log( "logged out", value);
}).catch( (error) => {
  console.log( "error", error);
  return confluence.logout();
});
*/
