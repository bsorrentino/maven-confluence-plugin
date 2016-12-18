import {XMLRPCConfluenceService} from "./confluence-xmlrpc";
import * as xml from "xml2js";
import * as filesystem from "fs";
import * as path from "path";
import Rx = require("rx");

export const enum ElementType {
    PAGE = 0,
    ATTACHMENT = 1
}

export interface ElementAttributes {
    name?:string;
    uri?:string;
    [n: string]: any;
}
export interface Element {
    $:ElementAttributes;
    type:ElementType;
}

interface PageContext {
    meta:Element;
    parent?:Model.Page;
}

let toPage = ( v:Object ) => {
    v['type'] = ElementType.PAGE; 
    return v as Element;
}

let toAttachment = ( v:Object ) => {
    v['type'] = ElementType.ATTACHMENT; 
    return v as Element;
}

let parser = new xml.Parser();
let rxParseString = Rx.Observable.fromNodeCallback( parser.parseString );
let rxReadFile    = Rx.Observable.fromNodeCallback( filesystem.readFile );

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
    rxStart():Rx.Observable<Element> {
        return rxReadFile( this.sitePath )
                .flatMap( (value:Buffer) => rxParseString( value.toString() ) )
                //.doOnCompleted( () => console.log('Done') )
                .map( (value:Object) => {
                    for( let first in value ) return value[first]['home'];
                })
                .flatMap( (value:Array<Object>) => rxProcessChild(value) );
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
    rxCreatePage( ctx:PageContext ) {
        let confluence = this.confluence;

        let getOrCreatePage = 
            ( !ctx.parent ) ? 
                    Rx.Observable.fromPromise(confluence.getOrCreatePage( this.spaceId, this.parentTitle, ctx.meta.$.name )) :
                    Rx.Observable.fromPromise(confluence.getOrCreatePage2( ctx.parent, ctx.meta.$.name ))
                    ;
        return getOrCreatePage
                .flatMap( (page) => {
                    return this.rxReadContent( path.join(this.sitePath, ctx.meta.$.uri) )
                        .flatMap( (storage) => Rx.Observable.fromPromise(confluence.storePageContent( page, storage )));
                })                   
    }   


    rxProcessChild( child:Array<Object>, parent?:Model.Page ):Rx.Observable<Element> {
        if( !child || child.length == 0 ) return Rx.Observable.empty<Element>();

        let first = child[0];

        let childObservable = Rx.Observable.just(first).map(toPage);
        let attachmentsObservable = Rx.Observable.fromArray( first['attachment'] || []).map(toAttachment);

        let childrenObservable = Rx.Observable.fromArray( first['child'] || [] )
                .concatMap( value => {
                let o1 = Rx.Observable.just(value).map(toPage);
                let o2 = Rx.Observable.fromArray(value['attachment'] || []).map(toAttachment);
                let o3 = this.rxProcessChild(value['child'] || [], parent );
                return Rx.Observable.concat( o1, o2, o3  );
                });

        return Rx.Observable.concat( childObservable,
                                    attachmentsObservable,
                                    childrenObservable,
                                    );
    }
       
        
}








