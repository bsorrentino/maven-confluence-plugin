"use strict";
var confluence_xmlrpc_1 = require("./confluence-xmlrpc");
var confluence_site_1 = require("./confluence-site");
var path = require("path");
var Rx = require("rx");
var config = require("./config.json").local;
confluence_xmlrpc_1.XMLRPCConfluenceService.create(config, config.credentials)
    .then(function (confluence) {
    confluence_site_1.rxSite(path.join(__dirname, 'site.xml'))
        .filter(function (data) { return data.type == 0; })
        .concatMap(function (data) {
        return Rx.Observable.fromPromise(confluence.getOrCreatePage(config.spaceId, config.pageTitle, data.$.name))
            .flatMap(function (page) {
            if (page.id) {
                console.log("remove page", page.id);
                return Rx.Observable.fromPromise(confluence.connection.removePage(page.id)).map(function () { return page; });
            }
            console.log("create page", page.title, page.id);
            var storage = { value: data.$.uri, representation: 1 };
            return Rx.Observable.fromPromise(confluence.storePageContent(page, storage));
        });
    })
        .doOnCompleted(function () { return confluence.connection.logout().then(function () { return console.log("logged out!"); }); })
        .subscribe(function (page) {
        console.log("page", page.title, "stored!");
    });
});
