"use strict";
var xml = require("xml2js");
var filesystem = require("fs");
var path = require("path");
var traverse = require("traverse");
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
filesystem.readFile(path.join(__dirname, 'site.xml'), function (err, data) {
    parser.parseString(data.toString(), function (err, result) {
        console.dir(result, { depth: 8 });
        console.log('Done');
    });
});
