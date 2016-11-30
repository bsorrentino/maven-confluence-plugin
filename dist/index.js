"use strict";
var confluence_1 = require("./confluence");
var config = require("./config.json").softphone;
var confluence;
confluence_1.Confluence.createDetectingVersion(config).then(function (c) {
    confluence = c;
}).then(function () {
    console.log("logged in");
    return confluence.getServerInfo();
}).then(function (value) {
    console.log("server majorVersion:", value.majorVersion);
    return confluence.getPage(config.spaceId, config.pageTitle);
}).then(function (value) {
    console.log("page", value);
    var newPage = {
        title: "CLI",
        space: config.spaceId,
        parentId: value.id,
        content: "{TOC}"
    };
    return Promise.all([
        confluence.getDescendents(value.id),
        confluence.storePage(newPage)
    ]);
}).then(function (result) {
    console.log("pages", result[0], "new page", result[1]);
    return confluence.removePage(result[1].id);
}).then(function (removed) {
    console.log("page removed:", removed);
}).then(function () {
    return confluence.logout();
}).then(function (value) {
    console.log("logged out", value);
}).catch(function (error) {
    console.log("error", error);
    return confluence.logout();
});
