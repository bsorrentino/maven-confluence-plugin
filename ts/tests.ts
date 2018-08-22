import * as fs from "fs";
import * as path from "path";
import * as xml from "xml2js";
import {markdown2wiki} from "./md";

function markdownTest() {

    let file = path.join( process.cwd(), "site", "demo1.md" );

    console.log( "start test ", file );
    fs.readFile( file, (err, buff)=> {

        console.log( markdown2wiki( buff ) );

    })

}

function readme2confluenceTest() {

    let file = path.join( process.cwd(), "README.md" );

    console.log( "start test ", file );
    fs.readFile( file, (err, buff)=> {

        console.log( markdown2wiki( buff ) );

    })

}

function xmlParse() {
    let parser = new xml.Parser();
    let file = path.join( process.cwd(), "site", "site.xml" );

    console.log( "start test ", file );
    fs.readFile( file, (err, buff)=> {

        parser.parseString(buff.toString(), (err:any, result:any) => {
            console.dir( result, { depth: 4 } );
        } );

    })
}

//markdownTest()
//xmlParse();
readme2confluenceTest();