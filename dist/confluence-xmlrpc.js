"use strict";
/// <reference path="confluence.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
const xmlrpc = require("xmlrpc");
class Confluence {
    constructor(config, servicePrefix = "confluence1.") {
        this.servicePrefix = servicePrefix;
        config.path += '/rpc/xmlrpc';
        //let data = Object.assign( info, {path: '/rpc/xmlrpc'});
        this.client = (config.protocol === "https:") ?
            xmlrpc.createSecureClient(config) :
            xmlrpc.createClient(config);
    }
    login(user, password) {
        if (this.token != null)
            return Promise.resolve(this.token);
        return this.call("login", [user, password])
            .then(token => {
            this.token = token;
            return Promise.resolve(token);
        });
    }
    logout() {
        if (this.token == null)
            return Promise.resolve(true);
        return this.call("logout", [this.token])
            .then(success => {
            this.token = undefined;
            return Promise.resolve(success);
        });
    }
    getServerInfo() {
        return this.call("getServerInfo", [this.token]);
    }
    getPage(spaceKey, pageTitle) {
        return this.call("getPage", [this.token, spaceKey, pageTitle]);
    }
    getPageById(id) {
        return this.call("getPage", [this.token, id]);
    }
    getChildren(pageId) {
        return this.call("getChildren", [this.token, pageId]);
    }
    getDescendents(pageId) {
        return this.call("getDescendents", [this.token, pageId]);
    }
    storePage(page) {
        return this.call2("confluence1.", "storePage", [this.token, page]);
    }
    removePage(pageId) {
        return this.call("removePage", [this.token, pageId]);
    }
    addAttachment(parentId, attachment, data) {
        return this.call("addAttachment", [this.token, parentId, attachment, data]);
    }
    /**
     * Adds a label to the object with the given ContentEntityObject ID.
     */
    addLabelByName(page, labelName) {
        return this.call("addLabelByName", [this.token, labelName, page.id]);
    }
    call(op, args) {
        return this.call2(this.servicePrefix, op, args);
    }
    call2(servicePrefix, op, args) {
        let operation = servicePrefix.concat(op);
        return new Promise((resolve, reject) => {
            this.client.methodCall(operation, args, (error, value) => {
                if (error) {
                    console.log('error:', error);
                    console.log('req headers:', error.req && error.req._header);
                    console.log('res code:', error.res && error.res.statusCode);
                    console.log('res body:', error.body);
                    reject(error);
                }
                else {
                    //console.log('value:', value);
                    resolve(value);
                }
            });
        });
    }
}
class XMLRPCConfluenceService /*Impl*/ {
    constructor(connection, credentials) {
        this.connection = connection;
    }
    static create(config, credentials /*, ConfluenceProxy proxyInfo, SSLCertificateInfo sslInfo*/) {
        if (config == null)
            throw "config argument is null!";
        if (credentials == null)
            throw "credentials argument is null!";
        /*
        if( sslInfo == null ) throw new IllegalArgumentException("sslInfo argument is null!");
  
        if (!sslInfo.isIgnore() && url.startsWith("https")) {
            HttpsURLConnection.setDefaultSSLSocketFactory( sslInfo.getSSLSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier( sslInfo.getHostnameVerifier() );
        }
        */
        return new Promise((resolve, reject) => {
            let confluence = new Confluence(config);
            confluence.login(credentials.username, credentials.password).then((token) => {
                return confluence.getServerInfo();
            }).then((value) => {
                if (value.majorVersion >= 4) {
                    confluence.servicePrefix = "confluence2.";
                }
                resolve(new XMLRPCConfluenceService(confluence, credentials));
            }).catch((error) => {
                reject(error);
            });
        });
    }
    get credentials() {
        return this.credentials;
    }
    getPage(spaceKey, pageTitle) {
        return this.connection.getPage(spaceKey, pageTitle);
    }
    getPageByTitle(parentPageId, title) {
        if (parentPageId == null)
            throw "parentPageId argument is null!";
        if (title == null)
            throw "title argument is null!";
        return this.connection.getChildren(parentPageId)
            .then((children) => {
            for (let i = 0; i < children.length; ++i) {
                if (title === children[i].title) {
                    return Promise.resolve(children[i]);
                }
            }
            return Promise.reject("page not found!");
        });
    }
    getPageById(pageId) {
        if (pageId == null)
            throw "pageId argument is null!";
        return this.connection.getPageById(pageId);
    }
    getDescendents(pageId) {
        return this.connection.getDescendents(pageId);
    }
    getAttachment(pageId, name, version) {
        return Promise.reject("getAttachment not implemented yet");
    }
    getOrCreatePage(spaceKey, parentPageTitle, title) {
        return this.connection.getPage(spaceKey, parentPageTitle)
            .then((parentPage) => this.getOrCreatePage2(parentPage, title));
    }
    getOrCreatePage2(parentPage, title) {
        return this.getPageByTitle(parentPage.id, title)
            .then((result) => {
            if (result != null)
                return this.connection.getPageById(result.id);
            let p = {
                space: parentPage.space,
                parentId: parentPage.id,
                title: title
            };
            return Promise.resolve(p);
        });
    }
    removePage(parentPage, title) {
        return Promise.reject("removePage not implemented yet");
        ;
    }
    removePageById(pageId) {
        return this.connection.removePage(pageId);
    }
    addLabelByName(page, label) {
        return this.connection.addLabelByName(page, label);
    }
    addAttachment(page, attachment, content) {
        return this.connection.addAttachment(page.id, attachment, content);
    }
    storePageContent(page, content) {
        if (content == null) {
            throw "content argument is null!";
        }
        let p = page;
        p.content = content.value;
        return this.connection.storePage(p);
    }
    storePage(page) {
        let p = page;
        return this.connection.storePage(p);
    }
}
exports.XMLRPCConfluenceService = XMLRPCConfluenceService;
