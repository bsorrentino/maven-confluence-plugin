
import * as xmlrpc from 'xmlrpc';

export interface Config {
  user:string,
  password:string,
  host:string,
  port:number,
  path:string

}

export interface ServerInfo {
  patchLevel:boolean,
  baseUrl: string,
  minorVersion: number,
  buildId: number,
  majorVersion: number,
  developmentBuild: boolean
}

export interface PageSummary {
    url:string;
    id:string;
    version:number;
    title:string;
    space:string;
    parentId:number;
    permissions:string;
}

export interface Page extends PageSummary {
    current:boolean;
    content:string;
    modifier:string;
    homePage:boolean;
    creator:string;
    contentStatus:string;
    modified: Date;
    created: Date;
}

export class Confluence {

  client:any;
  token:string; // auth token


  constructor( config:{host:string, port:number, path:string}, private servicePrefix:string = "confluence1." ) {
    config.path += '/rpc/xmlrpc';
    //let data = Object.assign( info, {path: '/rpc/xmlrpc'});
    this.client = xmlrpc.createClient(config);
  }

  login( user:string, password:string ):Promise<string> {
    return this.call("login", [user,password] );
  }

  logout():Promise<boolean> {
    return this.call("logout", [this.token] );
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
       return this.call2("confluence1.", "storePage", [page] );
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


  // Sends a method call to the XML-RPC server

  static createDetectingVersion(config:Config):Promise<Confluence> {

    return new Promise<Confluence>( (resolve: (value?: Confluence | Thenable<Confluence>) => void, reject: (error?: any) => void) => {

      let confluence = new Confluence(config);
      confluence.login( config.user, config.password ).then( (token:string) => {

          confluence.token = token;

          return confluence.getServerInfo();

      }).then( (value:ServerInfo) => {

          if( value.majorVersion >= 4 ) {
            confluence.servicePrefix = "confluence2.";
          }
          resolve(confluence);

      }).catch( (error) => {
        reject(error);
      });

    });
  }

}
