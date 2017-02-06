import * as fs from "fs";
import * as path from "path";
import {markdown2wiki} from "./md";

let file = path.join( process.cwd(), "site", "demo1.md" );

console.log( "start test ", file );
fs.readFile( file, (err, buff)=> {

    console.log( markdown2wiki( buff ) );

})


