"use strict";
var confluence_site_1 = require("./confluence-site");
var path = require("path");
confluence_site_1.rxSite(path.join(__dirname, 'site.xml'))
    .subscribe(function (data) {
    console.log("element", data['$']['name'] || data['$']['uri'], data['type']);
});
