
import {Confluence,PageSummary,Page,ServerInfo,Config} from "./confluence";

interface ConfigTest extends Config {
  spaceId:string;
  pageTitle:string;
}

//let config:ConfigTest = require( "./config.json").local;
let config:ConfigTest = require( "./config.json").softphone;

let confluence;

Confluence.createDetectingVersion(config).then( (c:Confluence) => {
  confluence = c;
}).then( () => {
  console.log( "logged in");
  return confluence.getServerInfo();
}).then( (value:ServerInfo) => {
  console.log( "server majorVersion:", value.majorVersion);
  return confluence.getPage( config.spaceId, config.pageTitle);
}).then( (value:Page) => {
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
