"use strict";
var xmlrpc = require("xmlrpc");
var Confluence = (function () {
    function Confluence(config, servicePrefix) {
        if (servicePrefix === void 0) { servicePrefix = "confluence1."; }
        this.servicePrefix = servicePrefix;
        config.path += '/rpc/xmlrpc';
        this.client = xmlrpc.createClient(config);
    }
    Confluence.prototype.login = function (user, password) {
        return this.call("login", [user, password]);
    };
    Confluence.prototype.logout = function () {
        return this.call("logout", [this.token]);
    };
    Confluence.prototype.getServerInfo = function () {
        return this.call("getServerInfo", [this.token]);
    };
    Confluence.prototype.getPage = function (spaceKey, pageTitle) {
        return this.call("getPage", [this.token, spaceKey, pageTitle]);
    };
    Confluence.prototype.getPageById = function (id) {
        return this.call("getPage", [this.token, id]);
    };
    Confluence.prototype.getDescendents = function (pageId) {
        return this.call("getDescendents", [this.token, pageId]);
    };
    Confluence.prototype.storePage = function (page) {
        return this.call2("confluence1.", "storePage", [page]);
    };
    Confluence.prototype.call = function (op, args) {
        return this.call2(this.servicePrefix, op, args);
    };
    Confluence.prototype.call2 = function (servicePrefix, op, args) {
        var _this = this;
        var operation = servicePrefix.concat(op);
        return new Promise(function (resolve, reject) {
            _this.client.methodCall(operation, args, function (error, value) {
                if (error) {
                    console.log('error:', error);
                    console.log('req headers:', error.req && error.req._header);
                    console.log('res code:', error.res && error.res.statusCode);
                    console.log('res body:', error.body);
                    reject(error);
                }
                else {
                    resolve(value);
                }
            });
        });
    };
    Confluence.createDetectingVersion = function (config) {
        return new Promise(function (resolve, reject) {
            var confluence = new Confluence(config);
            confluence.login(config.user, config.password).then(function (token) {
                confluence.token = token;
                return confluence.getServerInfo();
            }).then(function (value) {
                if (value.majorVersion >= 4) {
                    confluence.servicePrefix = "confluence2.";
                }
                resolve(confluence);
            }).catch(function (error) {
                reject(error);
            });
        });
    };
    return Confluence;
}());
exports.Confluence = Confluence;
