/// <reference path="confluence.d.ts" />

import * as xmlrpc from 'xmlrpc';

interface ServerInfo {
  patchLevel:boolean,
  baseUrl: string,
  minorVersion: number,
  buildId: number,
  majorVersion: number,
  developmentBuild: boolean
}

interface PageSummary extends Model.PageSummary {
    url?:string;
    version?:number;
    permissions?:string;
}

interface Page extends PageSummary, Model.Page {
    current?:boolean;
    content?:string;
    modifier?:string;
    homePage?:boolean;
    creator?:string;
    contentStatus?:string;
    modified?: Date;
    created?: Date;
}

interface Attachment extends Model.Attachment {
  creator?:string;
  fileSize?:number;
  url?:string;
}

class Confluence {

  client:any;
  token?:string; // auth token

  constructor( config:BaseConfig, public servicePrefix:string = "confluence1." ) {
    config.path += '/rpc/xmlrpc';
    //let data = Object.assign( info, {path: '/rpc/xmlrpc'});
    this.client = ( config.protocol === "https:") ? 
        xmlrpc.createSecureClient(config) :
        xmlrpc.createClient(config);
  }

  login( user:string, password:string ):Promise<string> {
    if( this.token != null ) return Promise.resolve(this.token);
    return this.call<string>("login", [user,password] )
      .then( token => {
        this.token = token;
        return Promise.resolve(token);
      })
      ;
  }

  logout():Promise<boolean> {
    if( this.token == null ) return Promise.resolve(true);
    return this.call<boolean>("logout", [this.token] )
      .then( success => {
        this.token = undefined;
        return Promise.resolve(success);
      })
      ;
  }

  getServerInfo():Promise<ServerInfo>  {
    return this.call("getServerInfo", [this.token]);
  }

  getPage( spaceKey:string, pageTitle:string):Promise<Page> {
    return this.call("getPage", [this.token,spaceKey,pageTitle] );
  }

  getPageById( id:string ):Promise<Page> {
    return this.call("getPage", [this.token,id] );
  }

  getChildren(pageId:string):Promise<Array<PageSummary>> {
    return this.call("getChildren", [this.token,pageId]);
  }

  getDescendents(pageId:string):Promise<Array<PageSummary>> {
    return this.call("getDescendents", [this.token,pageId] );
  }

  storePage(page:Page):Promise<Page>  {
    return this.call2("confluence1.", "storePage", [this.token,page] );
  }

  removePage(pageId:string):Promise<boolean> {
    return this.call("removePage", [this.token,pageId]);
  }

  addAttachment(parentId:string, attachment:Attachment, data:Buffer):Promise<Attachment>   {
    return this.call("addAttachment", [this.token,parentId, attachment, data]);
  }

  /**
   * Adds a label to the object with the given ContentEntityObject ID.
   */
  addLabelByName(page:Model.Page, labelName:string):Promise<boolean> {
      return this.call("addLabelByName", [this.token,labelName,page.id]);
  }

 
  private call<T>( op:string, args:Array<any> ):Promise<T> {
    return this.call2( this.servicePrefix, op, args );
  }

  private call2<T>( servicePrefix:string, op:string, args:Array<any> ):Promise<T> {
    let operation = servicePrefix.concat( op );

    return new Promise<T>( (resolve, reject) => {

      this.client.methodCall(operation, args, (error:any, value:any) => {
        if (error) {
            console.log('error:', error);
            console.log('req headers:', error.req && error.req._header);
            console.log('res code:', error.res && error.res.statusCode);
            console.log('res body:', error.body);
            reject(error);
          } else {
            //console.log('value:', value);
            resolve( value );
          }
        });
    });

  }

}

export class XMLRPCConfluenceService/*Impl*/ implements ConfluenceService {

  static  create( config:BaseConfig, credentials:Credentials /*, ConfluenceProxy proxyInfo, SSLCertificateInfo sslInfo*/ ):Promise<XMLRPCConfluenceService> {
      if( config == null ) throw "config argument is null!";
      if( credentials == null ) throw "credentials argument is null!";
      
      /*
      if( sslInfo == null ) throw new IllegalArgumentException("sslInfo argument is null!");

      if (!sslInfo.isIgnore() && url.startsWith("https")) {
          HttpsURLConnection.setDefaultSSLSocketFactory( sslInfo.getSSLSocketFactory());
          HttpsURLConnection.setDefaultHostnameVerifier( sslInfo.getHostnameVerifier() );
      }
      */

      return new Promise<XMLRPCConfluenceService>( (resolve, reject) => {

        let confluence = new Confluence(config);
        confluence.login( credentials.username, credentials.password ).then( (token:string) => {

            return confluence.getServerInfo();

        }).then( (value:ServerInfo) => {

            if( value.majorVersion >= 4 ) {
              confluence.servicePrefix = "confluence2.";
            }
            resolve( new XMLRPCConfluenceService(confluence,credentials) );

        }).catch( (error) => {
          reject(error);
        });

      });

  }

  private constructor( public connection:Confluence, credentials:Credentials) {
  }

  get credentials():Credentials {
    return this.credentials;
  }

  getPage( spaceKey:string, pageTitle:string ):Promise<Model.Page>
  {
    return this.connection.getPage(spaceKey,pageTitle);
  }

  getPageByTitle( parentPageId:string, title:string):Promise<Model.PageSummary>
  {
    if( parentPageId == null ) throw "parentPageId argument is null!";
    if( title == null ) throw "title argument is null!";

    return this.connection.getChildren(parentPageId)
    .then( (children:Array<PageSummary>) => {

        for( let i = 0 ; i<children.length; ++i ) {
          if( title === children[i].title ) {
            return Promise.resolve( children[i] );
          }
        }

        return Promise.reject( "page not found!");
    });
  }

  getPageById( pageId:string ):Promise<Model.Page>
  {
    if( pageId == null ) throw "pageId argument is null!";
    return this.connection.getPageById( pageId );
  }

  getDescendents(pageId:string):Promise<Array<Model.PageSummary>>
  {
    return this.connection.getDescendents( pageId );
  }

  getAttachment?( pageId:string, name:string, version:string ):Promise<Model.Attachment>
  {
    return Promise.reject("getAttachment not implemented yet");
  }

  getOrCreatePage( spaceKey:string , parentPageTitle:string , title:string  ):Promise<Model.Page>
  {
    return this.connection.getPage(spaceKey, parentPageTitle)
    .then( (parentPage:Page) => this.getOrCreatePage2(parentPage, title) )
    ;
  }

  getOrCreatePage2( parentPage:Model.Page , title:string  ):Promise<Model.Page>
  {
    return this.getPageByTitle(parentPage.id as string, title)
    .then( (result:PageSummary) => {
      if( result != null )
        return this.connection.getPageById(result.id as string);

      let p:Page = {
        space:parentPage.space,
        parentId:parentPage.id,
        title:title
      };

      return Promise.resolve(p);
    })
    ;

  }

  removePage( parentPage:Model.Page , title:string  ):Promise<boolean>
  {
    return Promise.reject("removePage not implemented yet");;
  }

  removePageById( pageId:string  ):Promise<boolean>
  {
    return this.connection.removePage( pageId );
  }

  addLabelByName( page:Model.Page, label:string  ):Promise<boolean>
  {
    return this.connection.addLabelByName(page,label);
  }

  addAttachment( page:Model.Page, attachment:Model.Attachment, content:Buffer ):Promise<Model.Attachment>
  {
    return this.connection.addAttachment( page.id as string, attachment, content) ;
  }

  storePageContent( page:Model.Page, content:ContentStorage  ):Promise<Model.Page>
  {
    if( content == null ) {
        throw "content argument is null!";
    }

    let p = page as Page;
    p.content = content.value;

    return this.connection.storePage(p);
  }

  storePage( page:Model.Page ):Promise<Model.Page>
  {
    let p = page as Page;

    return this.connection.storePage(p);
  }

  /*
  call( task:(ConfluenceService) => void ) {
    this.connection.login( this.credentials.username, this.credentials.password )
      .then( (token) => {
          console.log( "session started!");
          task( this );
          return this.connection.logout();
      })
      .then( () => console.log( "session ended!") );
  }
  */
}
