"use strict";
var Rx = require("rx");
var figlet = require('figlet');
var rxFonts = Rx.Observable.fromNodeCallback(figlet.fonts);
var rxFiglet = Rx.Observable.fromNodeCallback(figlet);
function rxMetadata(font) {
    return Rx.Observable.create(function (subscriber) {
        figlet.metadata('Standard', function (err, options, headerComment) {
            if (err) {
                subscriber.onError(err);
                return;
            }
            subscriber.onNext({ font: font, options: options, headerComment: headerComment });
            subscriber.onCompleted();
        });
    });
}
var VALUE = 'Confluence\n     CLI';
function rxShowFont(font) {
    return rxMetadata(font)
        .flatMap(function (metadata) { return Rx.Observable.combineLatest(Rx.Observable.just(metadata), rxFiglet(VALUE, metadata.font), function (m, d) {
        return { meta: m, data: d };
    }); });
}
function rxShowAllFonts() {
    return rxFonts()
        .flatMap(Rx.Observable.fromArray)
        .flatMap(rxShowFont);
}
function showAllFont() {
    rxShowAllFonts()
        .filter(function (data) { return data['meta']['options']['height'] < 8; })
        .subscribe(function (data) {
        console.log("\n===============================\n", "font:", data['meta']['font'], data['meta']['options'], "\n===============================\n");
        console.log(data['data']);
    });
}
Rx.Observable.of("Larry 3D 2", "Stick Letters")
    .flatMap(rxShowFont)
    .subscribe(function (data) { return console.log(data['data']); });
