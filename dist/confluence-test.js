"use strict";
var confluence_xmlrpc_1 = require("./confluence-xmlrpc");
var confluence_site_1 = require("./confluence-site");
var path = require("path");
var Rx = require("rx");
var config = require("./config.json").local;
var sitePath = __dirname;
confluence_xmlrpc_1.XMLRPCConfluenceService.create(config, config.credentials)
    .then(function (confluence) {
    Rx.Observable.fromPromise(confluence.getOrCreatePage(config.spaceId, config.pageTitle, "home"))
        .flatMap(function (page) {
        if (page.id) {
            var attachment_1 = {
                comment: "attachment test",
                contentType: "image/png",
                fileName: "appicon.png"
            };
            return confluence_site_1.rxReadFile(path.join(sitePath, "pages", attachment_1.fileName))
                .flatMap(function (buffer) { return Rx.Observable.fromPromise(confluence.addAttachment(page, attachment_1, buffer)); });
        }
        return Rx.Observable.throw(new Error("page home doesn't exist!"));
    })
        .doOnCompleted(function () { return confluence.connection.logout().then(function () { return console.log("logged out!"); }); })
        .subscribe(function (page) {
        console.log("attachment", page, "stored!");
    });
});
