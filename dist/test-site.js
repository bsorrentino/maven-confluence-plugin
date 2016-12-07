"use strict";
var xml = require("xml2js");
var filesystem = require("fs");
var path = require("path");
var traverse = require("traverse");
var Rx = require("rx");
var parser = new xml.Parser();
function removeSingleArrays(obj, filter) {
    traverse(obj).forEach(function traversing(value) {
        if (value instanceof Array && value.length === 1) {
            if (filter && !filter(this.key))
                return;
            this.update(value[0]);
        }
    });
}
function rxTraverse(obj) {
    return Rx.Observable.create(function (observer) {
        traverse(obj).forEach(function traversing(value) {
            observer.onNext(value);
        });
        observer.onCompleted();
    });
}
function rxProcessChild(child) {
    if (!child || child.length == 0)
        return Rx.Observable.empty();
    var first = child[0];
    var childObservable = Rx.Observable.just(first);
    var attachmentsObservable = Rx.Observable.fromArray(first['attachment'] || []);
    var childrenObservable = Rx.Observable.fromArray(first['child'] || [])
        .concatMap(function (value) {
        var o1 = Rx.Observable.just(value);
        var o2 = Rx.Observable.fromArray(value['attachment'] || []);
        var o3 = rxProcessChild(value['child'] || []);
        return Rx.Observable.concat(o1, o2, o3);
    });
    return Rx.Observable.concat(childObservable, attachmentsObservable, childrenObservable);
}
var rxReadFile = Rx.Observable.fromNodeCallback(filesystem.readFile);
var rxParseString = Rx.Observable.fromNodeCallback(parser.parseString);
rxReadFile(path.join(__dirname, 'site.xml'))
    .flatMap(function (value) { return rxParseString(value.toString()); })
    .doOnCompleted(function () { return console.log('Done'); })
    .map(function (value) {
    for (var first in value)
        return value[first]['home'];
})
    .flatMap(function (value) { return rxProcessChild(value); })
    .subscribe(function (data) {
    console.log("element", data['$']['name'] || data['$']['uri']);
});
