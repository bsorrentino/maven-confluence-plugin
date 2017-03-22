"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var xmlrpc = require("xmlrpc");
var Confluence = (function () {
    function Confluence(config, servicePrefix) {
        if (servicePrefix === void 0) { servicePrefix = "confluence1."; }
        this.servicePrefix = servicePrefix;
        config.path += '/rpc/xmlrpc';
        console.log("==> PROTOCOL", config.protocol);
        this.client = (config.protocol === "https:") ?
            xmlrpc.createSecureClient(config) :
            xmlrpc.createClient(config);
    }
    Confluence.prototype.login = function (user, password) {
        var _this = this;
        if (this.token != null)
            return Promise.resolve(this.token);
        return this.call("login", [user, password])
            .then(function (token) {
            _this.token = token;
            return Promise.resolve(token);
        });
    };
    Confluence.prototype.logout = function () {
        var _this = this;
        if (this.token == null)
            return Promise.resolve(true);
        return this.call("logout", [this.token])
            .then(function (success) {
            _this.token = null;
            return Promise.resolve(success);
        });
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
    Confluence.prototype.getChildren = function (pageId) {
        return this.call("getChildren", [this.token, pageId]);
    };
    Confluence.prototype.getDescendents = function (pageId) {
        return this.call("getDescendents", [this.token, pageId]);
    };
    Confluence.prototype.storePage = function (page) {
        return this.call2("confluence1.", "storePage", [this.token, page]);
    };
    Confluence.prototype.removePage = function (pageId) {
        return this.call("removePage", [this.token, pageId]);
    };
    Confluence.prototype.addAttachment = function (parentId, attachment, data) {
        return this.call("addAttachment", [this.token, parentId, attachment, data]);
    };
    Confluence.prototype.addLabelByName = function (page, labelName) {
        return this.call("addLabelByName", [this.token, labelName, page.id]);
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
    return Confluence;
}());
var XMLRPCConfluenceService = (function () {
    function XMLRPCConfluenceService(connection, credentials) {
        this.connection = connection;
    }
    XMLRPCConfluenceService.create = function (config, credentials) {
        if (config == null)
            throw "config argument is null!";
        if (credentials == null)
            throw "credentials argument is null!";
        return new Promise(function (resolve, reject) {
            var confluence = new Confluence(config);
            confluence.login(credentials.username, credentials.password).then(function (token) {
                return confluence.getServerInfo();
            }).then(function (value) {
                if (value.majorVersion >= 4) {
                    confluence.servicePrefix = "confluence2.";
                }
                resolve(new XMLRPCConfluenceService(confluence, credentials));
            }).catch(function (error) {
                reject(error);
            });
        });
    };
    Object.defineProperty(XMLRPCConfluenceService.prototype, "credentials", {
        get: function () {
            return this.credentials;
        },
        enumerable: true,
        configurable: true
    });
    XMLRPCConfluenceService.prototype.getPage = function (spaceKey, pageTitle) {
        return this.connection.getPage(spaceKey, pageTitle);
    };
    XMLRPCConfluenceService.prototype.getPageByTitle = function (parentPageId, title) {
        if (parentPageId == null)
            throw "parentPageId argument is null!";
        if (title == null)
            throw "title argument is null!";
        return this.connection.getChildren(parentPageId)
            .then(function (children) {
            for (var i = 0; i < children.length; ++i) {
                if (title === children[i].title) {
                    return Promise.resolve(children[i]);
                }
            }
            return Promise.resolve(null);
        });
    };
    XMLRPCConfluenceService.prototype.getPageById = function (pageId) {
        return null;
    };
    XMLRPCConfluenceService.prototype.getDescendents = function (pageId) {
        return this.connection.getDescendents(pageId);
    };
    XMLRPCConfluenceService.prototype.getAttachment = function (pageId, name, version) {
        return null;
    };
    XMLRPCConfluenceService.prototype.getOrCreatePage = function (spaceKey, parentPageTitle, title) {
        var _this = this;
        return this.connection.getPage(spaceKey, parentPageTitle)
            .then(function (parentPage) { return _this.getOrCreatePage2(parentPage, title); });
    };
    XMLRPCConfluenceService.prototype.getOrCreatePage2 = function (parentPage, title) {
        var _this = this;
        return this.getPageByTitle(parentPage.id, title)
            .then(function (result) {
            if (result != null)
                return _this.connection.getPageById(result.id);
            var p = {
                space: parentPage.space,
                parentId: parentPage.id,
                title: title
            };
            return Promise.resolve(p);
        });
    };
    XMLRPCConfluenceService.prototype.removePage = function (parentPage, title) {
        return null;
    };
    XMLRPCConfluenceService.prototype.removePageById = function (pageId) {
        return this.connection.removePage(pageId);
    };
    XMLRPCConfluenceService.prototype.addLabelByName = function (page, label) {
        return this.connection.addLabelByName(page, label);
    };
    XMLRPCConfluenceService.prototype.addAttachment = function (page, attachment, content) {
        return this.connection.addAttachment(page.id, attachment, content);
    };
    XMLRPCConfluenceService.prototype.storePageContent = function (page, content) {
        if (content == null) {
            throw "content argument is null!";
        }
        var p = page;
        p.content = content.value;
        return this.connection.storePage(p);
    };
    XMLRPCConfluenceService.prototype.storePage = function (page) {
        var p = page;
        return this.connection.storePage(p);
    };
    return XMLRPCConfluenceService;
}());
exports.XMLRPCConfluenceService = XMLRPCConfluenceService;
