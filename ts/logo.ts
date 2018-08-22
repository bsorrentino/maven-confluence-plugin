
import Rx = require("rx");

interface Figlet {
    ( input:string, font:string, callback:(err:any, res:string) => void  ):void;

    fonts( callback:(err:any, res:Array<string>) => void ):void;
    metadata( type:string, callback:(err:any, options:any, headerComment:string) =>void ):void
}

const figlet:Figlet = require('figlet');

interface FigletMetadata {
    font:string
    options:any,
    headerComment:string;
}

let rxFonts = Rx.Observable.fromNodeCallback( figlet.fonts );

let rxFiglet =  Rx.Observable.fromNodeCallback( figlet );

function rxMetadata( font:string ):Rx.Observable<FigletMetadata> {
    return  Rx.Observable.create<FigletMetadata>( (subscriber) => {
        figlet.metadata('Standard', function(err, options, headerComment) {
            if (err) {
                subscriber.onError(err);
                return;
            }

            subscriber.onNext( { font:font, options:options, headerComment:headerComment });
            subscriber.onCompleted();
        });
    } );
} 

const VALUE = 'Confluence\n     CLI';

function rxShowFont(font:string ):Rx.Observable<any> {

    return rxMetadata( font )
        .flatMap( (metadata) => Rx.Observable.combineLatest( 
                                    Rx.Observable.just(metadata), 
                                    rxFiglet( VALUE, metadata.font ), 
                                    ( m, d ) => {     
                                        return {meta:m, data:d} 
                                    }) )
        ;       
}

function rxShowAllFonts():Rx.Observable<any> {
    return rxFonts()
    .flatMap( values => Rx.Observable.fromArray(values) )
    .flatMap( value => rxShowFont(value) );
}


function showAllFont() {
    rxShowAllFonts()
    .filter( (data) => data['meta']['options']['height'] < 8 )
    .subscribe( (data) => {
        console.log( 
            "\n===============================\n",
            "font:", data['meta']['font'], data['meta']['options'],
            "\n===============================\n"); 
        console.log(data['data']) 
    });
}

/**
 * CLEAR SCREEN
 */
function clrscr() {
  //process.stdout.write('\033c');
  process.stdout.write('\x1Bc');

}

clrscr();

Rx.Observable.of( "Larry 3D 2", "Stick Letters")
.flatMap( rxShowFont )
.subscribe( (data) => console.log(data['data']) );

