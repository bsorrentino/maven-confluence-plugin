/// <reference path="preferences.d.ts" />

import Preferences = require("preferences");
 
interface MyData {
        data: { url:string, info?:string, cycle:number }
}


let prefs = new Preferences( "org.bsc.test", { 
        cycle:0,
        confluence: { 
                password:"mypassword",
        } 
} ) ;
 
prefs.cycle++;

console.dir( prefs );

