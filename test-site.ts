

import * as xml from "xml2js";
import * as filesystem from "fs";
import * as path from "path";
import Rx = require("rx");


function rxProcessChild( child:Array<Object> ) {
  if( !child || child.length == 0 ) return Rx.Observable.empty();

  let first = child[0];

  let childObservable = Rx.Observable.just(first);
  let attachmentsObservable = Rx.Observable.fromArray( first['attachment'] || []);

  let childrenObservable = Rx.Observable.fromArray( first['child'] || [] )
        .concatMap( value => {
          let o1 = Rx.Observable.just(value);
          let o2 = Rx.Observable.fromArray(value['attachment'] || []);
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


rxReadFile( path.join(__dirname,'site.xml') )
  .flatMap( (value:Buffer) => rxParseString( value.toString() ) )
  .doOnCompleted( () => console.log('Done') )
  .map( (value:Object) => {
    for( let first in value ) return value[first]['home'];
  })
  .flatMap( (value:Array<Object>) => rxProcessChild(value) )
  .subscribe( (data) => {
    console.log( "element", data['$']['name'] || data['$']['uri']);
  });
