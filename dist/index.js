"use strict";
var confluence_xmlrpc_1 = require("./confluence-xmlrpc");
var confluence_site_1 = require("./confluence-site");
var config = require("./config.json").local;
confluence_xmlrpc_1.XMLRPCConfluenceService.create(config, config.credentials)
    .then(function (confluence) {
    var site = new confluence_site_1.SiteProcessor(confluence, config.spaceId, config.pageTitle, __dirname);
    site.rxStart('site.1.xml')
        .doOnCompleted(function () { return confluence.connection.logout().then(function () { return console.log("logged out!"); }); })
        .subscribe(function (elem) {
        console.log("element", elem, "processed!");
    });
});
