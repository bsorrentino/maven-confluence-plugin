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
var readFile = Rx.Observable.fromNodeCallback(filesystem.readFile);
var parseString = Rx.Observable.fromNodeCallback(parser.parseString);
readFile(path.join(__dirname, 'site.xml'))
    .flatMap(function (value) { return parseString(value.toString()); })
    .doOnCompleted(function () { return console.log('Done'); })
    .subscribe(function (data) {
    console.dir(data, { depth: 8 });
});
