
declare const enum Representation {
  STORAGE=0 , WIKI=1
}

type ServiceProtocol = "http:"|"https:";

interface BaseConfig {
  protocol:string, // ServiceProtocol
  host:string,
  port:number,
  path:string,

}

interface Config extends BaseConfig {
  spaceId:string,
  parentPageTitle:string,
  sitePath:string,
  serverId?:string
}

interface ContentStorage {
  representation:Representation;
  value:string;
}

/*
declare var ContentStorage: {
    prototype: ContentStorage;
    new( value:string, rapresentation:Representation ): ContentStorage;
}
*/

interface Credentials {
  username:string;
  password:string;
}

declare module Model {

  export interface Attachment {
    id?:string;
    fileName:string;
    contentType:string;
    comment:string;
    created?:Date;
  }

  export interface PageSummary {
    id?:string;
    title:string;
    space:string;
    parentId:any;
  }

  export interface Page extends PageSummary {
    version?:number;
    content?:string;
  }

}

interface ConfluenceService {

    readonly credentials:Credentials;

    getPage( spaceKey:string, pageTitle:string ):Promise<Model.Page>;

    getPageByTitle( parentPageId:string, title:string):Promise<Model.PageSummary>;

    getPageById( pageId:string ):Promise<Model.Page>;

    getDescendents(pageId:string):Promise<Array<Model.PageSummary>>;

    getAttachment?( pageId:string, name:string, version:string ):Promise<Model.Attachment>;

    getOrCreatePage( spaceKey:string , parentPageTitle:string , title:string  ):Promise<Model.Page>;

    getOrCreatePage2( parentPage:Model.Page , title:string  ):Promise<Model.Page>;

    removePage( parentPage:Model.Page , title:string  ):Promise<boolean>;

    removePageById( pageId:string  ):Promise<boolean>;

    addLabelByName( page:Model.Page, label:string ):Promise<boolean>;

    addAttachment( page:Model.Page, attachment:Model.Attachment, content:Buffer ):Promise<Model.Attachment>;

    storePageContent( page:Model.Page, content:ContentStorage  ):Promise<Model.Page>;

    storePage( page:Model.Page ):Promise<Model.Page>;

    //call?( task:(ConfluenceService) => void );



}
