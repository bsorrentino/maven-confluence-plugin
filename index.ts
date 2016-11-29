
import {Confluence,PageSummary,Page,ServerInfo,Config} from "./confluence";

let config:Config = require( "./config.json").local;

let confluence;

Confluence.createDetectingVersion(config).then( (c:Confluence) => {
  confluence = c;
}).then( () => {
  console.log( "logged in");
  return confluence.getServerInfo();
}).then( (value:ServerInfo) => {
  console.log( "server majorVersion:", value.majorVersion);
  return confluence.getPage( "TEST", "TEST");
}).then( (value:Page) => {
  console.log( "page", value);
  return confluence.getDescendents( value.id );
}).then( ( pages:Array<PageSummary> ) => {
  console.log( "pages", pages);
}).then( () => {
  return confluence.logout();
}).then( (value) => {
  console.log( "logged out", value);
}).catch( (error) => {
  console.log( "error", error);
  return confluence.logout();
});
