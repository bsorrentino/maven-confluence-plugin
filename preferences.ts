
import Preferences = require("preferences");


interface MyData extends Preferences.Data {
        data: { url:string, info?:string, cycle:number }
}


let init:MyData = { 
        account: { username:"bartolo", password:"password"}, 
        data: { 
                url:"http://localhost",
                cycle:0
        } 
};

let prefs = new Preferences( "org.bsc.test", init ) ;
 
prefs.data.cycle++;

console.dir( prefs );