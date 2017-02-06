"use strict";
var fs = require("fs");
var path = require("path");
var md_1 = require("./md");
var file = path.join(process.cwd(), "site", "demo1.md");
console.log("start test ", file);
fs.readFile(file, function (err, buff) {
    console.log(md_1.markdown2wiki(buff));
});
