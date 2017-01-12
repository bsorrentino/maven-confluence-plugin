import * as fs from "fs";
import * as path from "path";
import * as url from "url";
import * as util from "util";
import * as assert from "assert";

import Rx = require("rx");
import Preferences = require("preferences");
import * as inquirer from "inquirer";

const CONFIG_FILE       = "config.json";
const PREFERENCES_ID    = "org.bsc.confluence-cli";

function modulePath() {
    return  "." + path.sep + CONFIG_FILE;
}


namespace ConfigUtils {

    export namespace Port {
        export function isValid(port:string|number):boolean {
        return (util.isNullOrUndefined(port) || util.isNumber(port) || Number(port) !== NaN ) 
        }

        export function value( port:string|number, def:number = 80 ) {
            assert( isValid(port) );

            return ( util.isNullOrUndefined(port) ) ?  def : Number( port );
        }

    }

    export namespace Url {

        export function format( config:Config ):string {

                assert( !util.isNullOrUndefined(config) );

                let port = util.isNull(config.port) ? "" : (config.port===80 ) ? "" : ":" + config.port
                return util.format( "%s//%s%s%s", 
                                config.protocol, 
                                config.host, 
                                port,
                                config.path);
        }

    }

}

function config( force:boolean = false ):Rx.Observable<(Config|Credentials)[]> {
    
    let configPath = path.join(__dirname, CONFIG_FILE);
    
    console.log( "configPath", configPath );
    console.log( "relative",  modulePath() );

    let defaultConfig:Config = {
        host:"",path:"",port:null,protocol:"http"
    };

    let defaultCredentials:Credentials = {
        username:"",
        password:""
    };
    
    if( fs.existsSync( configPath ) ) {

        console.log( "found!" );

        defaultConfig = require( modulePath() );
        defaultCredentials = new Preferences( PREFERENCES_ID, defaultCredentials) ;

        if( !force ) {

            let data = [ defaultConfig, defaultCredentials ];

            return Rx.Observable.just(data);
        }
    }

    let answers = inquirer.prompt( [
            { 
                type: "input",
                name: "url",
                message: "confluence url:",
                default: ConfigUtils.Url.format( defaultConfig ), 
                validate: ( value ) => { 
                        let p = url.parse(value);
                        //console.log( "parsed url", p );
                        let valid = (p.protocol && p.host  && ConfigUtils.Port.isValid(p.port) );
                        return (valid) ? true : "url is not valid!";
                    }
            },
            { 
                type: "input",
                name: "username",
                message: "confluence username:",
                default: defaultCredentials.username,
                validate: ( value ) => { 
                    return value.length==0 ? "username must be specified!" : true;
                }
            },
            { 
                type: "password",
                name: "password",
                message: "confluence password:",
                default: defaultCredentials.password,
                validate: ( value ) => { 
                    return util.isNullOrUndefined(value) ? "password must be specified!" : true;
                }
            }
            
        ] );

    let rxCreateConfigFile = 
        Rx.Observable.fromNodeCallback( fs.writeFile )
        ;

    return Rx.Observable.fromPromise( answers )
                    .map( (answers) => {
                        let p = url.parse(answers['url']);
                        //console.log( p );
                        let config:Config = {
                            path:p.path || "",
                            protocol:p.protocol,
                            host:p.hostname,
                            port:ConfigUtils.Port.value(p.port)
                        }
                        /*
                        let credentials:Credentials = {
                            username:answers['username'],
                            password:answers['password']
                        };
                        */
                        let c = new Preferences(PREFERENCES_ID, defaultCredentials );
                        c.username = answers['username']
                        c.password = answers['password'];

                        return [ config, c ];
                    })
                    .flatMap( ( result ) =>  
                        rxCreateConfigFile( configPath, JSON.stringify(result[0]) )
                            .map( (res) => result ) );
}

config( true )
.subscribe( ( result ) => {

    console.dir( result, {depth:2} );
});