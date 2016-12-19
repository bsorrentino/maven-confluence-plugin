import {XMLRPCConfluenceService} from "./confluence-xmlrpc";
import * as xml from "xml2js";
import * as filesystem from "fs";
import * as path from "path";
import Rx = require("rx");

interface ElementAttributes {
    name?:string;
    uri?:string;
    [n: string]: any;
}

interface Element {
    $:ElementAttributes;
    attachment?:Array<Element>;
    child?:Array<Element>
    label?:Array<string>
}

interface PageContext {
    meta:Element;
    parent?:Model.Page;
}

let parser = new xml.Parser();
let rxParseString = Rx.Observable.fromNodeCallback( parser.parseString );
export let rxReadFile    = Rx.Observable.fromNodeCallback( filesystem.readFile );

export class SiteProcessor {
    /**
     * 
     */
    constructor( 
        public confluence:XMLRPCConfluenceService,
        public spaceId:string,
        public parentTitle:string,
        public sitePath:string 
        ) {} 


    /**
     * 
     */
    rxStart( fileName:string ):Rx.Observable<any> {
        return rxReadFile( path.join(this.sitePath, fileName) )
                .flatMap( (value:Buffer) => rxParseString( value.toString() ) )
                //.doOnNext( (value) => console.dir( value, { depth:4 }) )
                .map( (value:Object) => {
                    for( let first in value ) return value[first]['home'];
                })
                .flatMap( (value:Array<Object>) => this.rxProcessChild(value) );
    }

    /**
     * 
     */
    rxReadContent( filePath:string ):Rx.Observable<ContentStorage> {
        
        return rxReadFile( filePath )
            .map( (value:Buffer) => {
                let storage:ContentStorage = {value:value.toString(), representation:Representation.WIKI};
                return storage;
            });
        
    }

    /**
     * 
     */
    rxCreateAttachment( ctx:PageContext ) {
        let confluence = this.confluence;

        let attachment:Model.Attachment =  {
                comment:ctx.meta.$['comment'],
                contentType:ctx.meta.$['contentType'],
                fileName:ctx.meta.$.name
            };
        return rxReadFile( path.join(this.sitePath, ctx.meta.$.uri) )
                .doOnCompleted( () => console.log( "created attachment ", attachment.fileName ))
                .flatMap( (buffer:Buffer) => 
                            Rx.Observable.fromPromise(confluence.addAttachment( ctx.parent, attachment, buffer )));

    } 

    /**
     * 
     */
    rxCreatePage( ctx:PageContext ) {
        let confluence = this.confluence;

        let getOrCreatePage = 
            ( !ctx.parent ) ? 
                    Rx.Observable.fromPromise(confluence.getOrCreatePage( this.spaceId, this.parentTitle, ctx.meta.$.name )) :
                    Rx.Observable.fromPromise(confluence.getOrCreatePage2( ctx.parent, ctx.meta.$.name ))
                    ;
        return getOrCreatePage
                .doOnNext( (page) => console.log( "creating page ", page.title ))
                .flatMap( (page) => {
                    return this.rxReadContent( path.join(this.sitePath, ctx.meta.$.uri) )
                        .flatMap( (storage) => Rx.Observable.fromPromise(confluence.storePageContent( page, storage )));
                })                   
    }   

    private rxProcessLabels( ctx:PageContext ) {
        return Rx.Observable.fromArray( ctx.meta.label || [])
                    .flatMap( (data:string) => 
                        Rx.Observable.fromPromise(this.confluence.addLabelByName( ctx.parent, data )) ) 
                    ;        
    } 

    private rxProcessAttachments( ctx:PageContext ) {
        return Rx.Observable.fromArray( ctx.meta.attachment || [])
                    .map( (data:Element) => { return { meta:data, parent:ctx.parent }} )
                    .flatMap( (ctx:PageContext) => this.rxCreateAttachment( ctx ) ) 
                    ;        
    } 

    rxProcessChild( child:Array<Object>, parent?:Model.Page ):Rx.Observable<any> {
        if( !child || child.length == 0 ) return Rx.Observable.empty<any>();

        let first = child[0] as Element ;
        
        let childObservable = 
            this.rxCreatePage( {meta:first, parent:parent } )
                .flatMap( (page:Model.Page) => {

                    let o1 = this.rxProcessAttachments( {meta:first, parent:page} ); 
                    let o2 = this.rxProcessLabels( {meta:first, parent:page} ); 
                    let o3 = Rx.Observable.fromArray( first.child || [] )
                            .map( (data:Element) => { return { meta:data, parent:page } })                            
                            .concatMap( (ctx:PageContext) => {
                    
                                return this.rxCreatePage( ctx )
                                        .flatMap( (child:Model.Page) => {
                                            let o1 = this.rxProcessAttachments( {meta:ctx.meta, parent:child} );    
                                            let o2 = this.rxProcessLabels( {meta:ctx.meta, parent:child} ); 
                                            let o3 = this.rxProcessChild(ctx.meta.child || [], child );
                                            return Rx.Observable.concat( o1, o2, o3 );
                                        })
                            });
                                
                                                
                    return Rx.Observable.concat( o1, o2, o3 );
                });

                return childObservable;

    }
       
        
}








