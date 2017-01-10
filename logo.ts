
import Rx = require("rx");
import clean = require("clean");

const figlet = require('figlet');

interface FigletMetadata {
    font:string
    options:any,
    headerComment:string;
}

let rxFonts = Rx.Observable.fromNodeCallback<Array<string>>( figlet.fonts );
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

function rxShowFont(font:string ):Rx.Observable<Object> {

        return rxMetadata( font )
        .flatMap( (metadata) => Rx.Observable.combineLatest( 
                                    Rx.Observable.just(metadata), 
                                    rxFiglet( VALUE, metadata.font ), 
                                    ( m, d ) => {     
                                        return {meta:m, data:d} 
                                    }) )
        ;       
}

function rxShowAllFonts():Rx.Observable<Object> {
    return rxFonts()
    .flatMap( Rx.Observable.fromArray )
    .flatMap( rxShowFont );
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

clean();

Rx.Observable.of( "Larry 3D 2", "Stick Letters")
.flatMap( rxShowFont )
.subscribe( (data) => console.log(data['data']) );

