"use strict";
var fs = require("fs");
var path = require("path");
var xml = require("xml2js");
var md_1 = require("./md");
function markdownTest() {
    var file = path.join(process.cwd(), "site", "demo1.md");
    console.log("start test ", file);
    fs.readFile(file, function (err, buff) {
        console.log(md_1.markdown2wiki(buff));
    });
}
function xmlParse() {
    var parser = new xml.Parser();
    var file = path.join(process.cwd(), "site", "site.xml");
    console.log("start test ", file);
    fs.readFile(file, function (err, buff) {
        parser.parseString(buff.toString(), function (err, result) {
            console.dir(result, { depth: 4 });
        });
    });
}
xmlParse();
