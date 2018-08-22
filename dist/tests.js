"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const fs = require("fs");
const path = require("path");
const xml = require("xml2js");
const md_1 = require("./md");
function markdownTest() {
    let file = path.join(process.cwd(), "site", "demo1.md");
    console.log("start test ", file);
    fs.readFile(file, (err, buff) => {
        console.log(md_1.markdown2wiki(buff));
    });
}
function readme2confluenceTest() {
    let file = path.join(process.cwd(), "README.md");
    console.log("start test ", file);
    fs.readFile(file, (err, buff) => {
        console.log(md_1.markdown2wiki(buff));
    });
}
function xmlParse() {
    let parser = new xml.Parser();
    let file = path.join(process.cwd(), "site", "site.xml");
    console.log("start test ", file);
    fs.readFile(file, (err, buff) => {
        parser.parseString(buff.toString(), (err, result) => {
            console.dir(result, { depth: 4 });
        });
    });
}
//markdownTest()
//xmlParse();
readme2confluenceTest();
