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
    content:string;
    modifier?:string;
    homePage?:boolean;
    creator?:string;
    contentStatus?:string;
    modified?: Date;
    created?: Date;
}

class Confluence {

  client:any;
  token:string; // auth token


  constructor( config:{host:string, port:number, path:string}, public servicePrefix:string = "confluence1." ) {
    config.path += '/rpc/xmlrpc';
    //let data = Object.assign( info, {path: '/rpc/xmlrpc'});
    this.client = xmlrpc.createClient(config);
  }

  login( user:string, password:string ):Promise<string> {
    if( this.token != null ) return Promise.resolve(this.token);
    return this.call("login", [user,password] )
      .then( (token:string) => this.token = token )
      ;
  }

  logout():Promise<boolean> {
    if( this.token == null ) return Promise.resolve(true);
    return this.call("logout", [this.token] )
      .then( (success:boolean) => this.token = null )
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

  getDescendents(pageId:string):Promise<Array<PageSummary>> {
    return this.call("getDescendents", [this.token,pageId] );
  }

  storePage(page:Page):Promise<Page>  {
       return this.call2("confluence1.", "storePage", [this.token,page] );
  }

  removePage(pageId:string):Promise<boolean> {
    return this.call("removePage", [this.token,pageId]);
  }

  private call<T>( op:string, args:Array<any> ):Promise<T> {
    return this.call2( this.servicePrefix, op, args );
  }

  private call2<T>( servicePrefix:string, op:string, args:Array<any> ):Promise<T> {
    let operation = servicePrefix.concat( op );

    return new Promise<T>( (resolve: (value?: T | Thenable<T>) => void, reject: (error?: any) => void) => {

      this.client.methodCall(operation, args, (error, value) => {
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

  static  create( config:{host:string, port:number, path:string}, credentials:Credentials /*, ConfluenceProxy proxyInfo, SSLCertificateInfo sslInfo*/ ):Promise<XMLRPCConfluenceService> {
      if( config == null ) {
          throw "config argument is null!";
      }
      if( credentials == null ) {
          throw "credentials argument is null!";
      }
      /*
      if( sslInfo == null ) {
          throw new IllegalArgumentException("sslInfo argument is null!");
      }

      if (!sslInfo.isIgnore() && url.startsWith("https")) {

          HttpsURLConnection.setDefaultSSLSocketFactory( sslInfo.getSSLSocketFactory());

          HttpsURLConnection.setDefaultHostnameVerifier( sslInfo.getHostnameVerifier() );
      }
      */

      return new Promise<XMLRPCConfluenceService>( (resolve: (value?: XMLRPCConfluenceService | Thenable<XMLRPCConfluenceService>) => void, reject: (error?: any) => void) => {

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

  private constructor( private connection:Confluence, credentials:Credentials) {
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
    return null;
  }

  getPageById( pageId:string ):Promise<Model.Page>
  {
    return null;
  }

  getDescendents(pageId:string):Promise<Array<Model.PageSummary>>
  {
    return null;
  }

  getAttachment?( pageId:string, name:string, version:string ):Promise<Model.Attachment>
  {
    return null;
  }

  getOrCreatePage( spaceKey:string , parentPageTitle:string , title:string  ):Promise<Model.Page>
  {
    return null;
  }

  getOrCreatePage2( parentPage:Model.Page , title:string  ):Promise<Model.Page>
  {
    return null;
  }

  removePage( parentPage:Model.Page , title:string  ):Promise<boolean>
  {
    return null;
  }

  removePageById( pageId:string  ):Promise<boolean>
  {
    return null;
  }

  addLabelByName( label:string , id:number ):Promise<boolean>
  {
    return null;
  }

  addAttchment( page:Model.Page, attachment:Model.Attachment, content:any ):Promise<Model.Attachment>
  {
    return null;
  }

  storePage2( page:Model.Page, content:Storage  ):Promise<Model.Page>
  {
    return null;
  }

  storePage( page:Model.Page ):Promise<Model.Page>
  {
    return null;
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
