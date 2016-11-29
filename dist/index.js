"use strict";
var confluence_1 = require("./confluence");
var config = require("./config.json").local;
var confluence;
confluence_1.Confluence.createDetectingVersion(config).then(function (c) {
    confluence = c;
}).then(function () {
    console.log("logged in");
    return confluence.getServerInfo();
}).then(function (value) {
    console.log("server majorVersion:", value.majorVersion);
    return confluence.getPage("TEST", "TEST");
}).then(function (value) {
    console.log("page", value);
    return confluence.getDescendents(value.id);
}).then(function (pages) {
    console.log("pages", pages);
}).then(function () {
    return confluence.logout();
}).then(function (value) {
    console.log("logged out", value);
}).catch(function (error) {
    console.log("error", error);
    return confluence.logout();
});
