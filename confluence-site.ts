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

let toPage = ( v:Object ) => {
    v['type'] = ElementType.PAGE; 
    return v as Element;
}

let toAttachment = ( v:Object ) => {
    v['type'] = ElementType.ATTACHMENT; 
    return v as Element;
}

function rxProcessChild( child:Array<Object> ):Rx.Observable<Element> {
  if( !child || child.length == 0 ) return Rx.Observable.empty<Element>();

  let first = child[0];

  let childObservable = Rx.Observable.just(first).map(toPage);
  let attachmentsObservable = Rx.Observable.fromArray( first['attachment'] || []).map(toAttachment);

  let childrenObservable = Rx.Observable.fromArray( first['child'] || [] )
        .concatMap( value => {
          let o1 = Rx.Observable.just(value).map(toPage);
          let o2 = Rx.Observable.fromArray(value['attachment'] || []).map(toAttachment);
          let o3 = rxProcessChild(value['child'] || []);
          return Rx.Observable.concat( o1, o2, o3  );
        });


  return Rx.Observable.concat( childObservable,
                               attachmentsObservable,
                               childrenObservable,
                             );
}

let parser = new xml.Parser();
let rxReadFile    = Rx.Observable.fromNodeCallback( filesystem.readFile );
let rxParseString = Rx.Observable.fromNodeCallback( parser.parseString );

/**
 * 
 */
export function rxSite( sitePath:string ):Rx.Observable<Element> {
    return rxReadFile( sitePath )
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
export function rxReadContent(sitePath:string, data:Element):Rx.Observable<ContentStorage> {
    
    return rxReadFile( path.join(sitePath,data.$.uri) )
        .map( (value:Buffer) => {
            let storage:ContentStorage = {value:value.toString(), representation:Representation.WIKI};
            return storage;
        });
    
}