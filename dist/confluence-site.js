"use strict";
var xml = require("xml2js");
var filesystem = require("fs");
var path = require("path");
var Rx = require("rx");
var parser = new xml.Parser();
var rxParseString = Rx.Observable.fromNodeCallback(parser.parseString);
exports.rxReadFile = Rx.Observable.fromNodeCallback(filesystem.readFile);
var SiteProcessor = (function () {
    function SiteProcessor(confluence, spaceId, parentTitle, sitePath) {
        this.confluence = confluence;
        this.spaceId = spaceId;
        this.parentTitle = parentTitle;
        this.sitePath = sitePath;
    }
    SiteProcessor.prototype.rxStart = function (fileName) {
        var _this = this;
        return exports.rxReadFile(path.join(this.sitePath, fileName))
            .flatMap(function (value) { return rxParseString(value.toString()); })
            .map(function (value) {
            for (var first in value)
                return value[first]['home'];
        })
            .flatMap(function (value) { return _this.rxProcessChild(value); });
    };
    SiteProcessor.prototype.rxReadContent = function (filePath) {
        return exports.rxReadFile(filePath)
            .map(function (value) {
            var storage = { value: value.toString(), representation: 1 };
            return storage;
        });
    };
    SiteProcessor.prototype.rxCreateAttachment = function (ctx) {
        var confluence = this.confluence;
        var attachment = {
            comment: ctx.meta.$['comment'],
            contentType: ctx.meta.$['contentType'],
            fileName: ctx.meta.$.name
        };
        return exports.rxReadFile(path.join(this.sitePath, ctx.meta.$.uri))
            .doOnCompleted(function () { return console.log("created attachment:", attachment.fileName); })
            .flatMap(function (buffer) {
            return Rx.Observable.fromPromise(confluence.addAttachment(ctx.parent, attachment, buffer));
        });
    };
    SiteProcessor.prototype.rxCreatePage = function (ctx) {
        var _this = this;
        var confluence = this.confluence;
        var getOrCreatePage = (!ctx.parent) ?
            Rx.Observable.fromPromise(confluence.getOrCreatePage(this.spaceId, this.parentTitle, ctx.meta.$.name)) :
            Rx.Observable.fromPromise(confluence.getOrCreatePage2(ctx.parent, ctx.meta.$.name));
        return getOrCreatePage
            .doOnNext(function (page) { return console.log("creating page:", page.title); })
            .flatMap(function (page) {
            return _this.rxReadContent(path.join(_this.sitePath, ctx.meta.$.uri))
                .flatMap(function (storage) { return Rx.Observable.fromPromise(confluence.storePageContent(page, storage)); });
        });
    };
    SiteProcessor.prototype.rxProcessLabels = function (ctx) {
        var _this = this;
        return Rx.Observable.fromArray(ctx.meta.label || [])
            .flatMap(function (data) {
            return Rx.Observable.fromPromise(_this.confluence.addLabelByName(ctx.parent, data));
        });
    };
    SiteProcessor.prototype.rxProcessAttachments = function (ctx) {
        var _this = this;
        return Rx.Observable.fromArray(ctx.meta.attachment || [])
            .map(function (data) { return { meta: data, parent: ctx.parent }; })
            .flatMap(function (ctx) { return _this.rxCreateAttachment(ctx); });
    };
    SiteProcessor.prototype.rxProcessChild = function (child, parent) {
        var _this = this;
        if (!child || child.length == 0)
            return Rx.Observable.empty();
        var first = child[0];
        var childObservable = this.rxCreatePage({ meta: first, parent: parent })
            .flatMap(function (page) {
            var o1 = _this.rxProcessAttachments({ meta: first, parent: page });
            var o2 = _this.rxProcessLabels({ meta: first, parent: page });
            var o3 = Rx.Observable.fromArray(first.child || [])
                .map(function (data) { return { meta: data, parent: page }; })
                .concatMap(function (ctx) {
                return _this.rxCreatePage(ctx)
                    .flatMap(function (child) {
                    var o1 = _this.rxProcessAttachments({ meta: ctx.meta, parent: child });
                    var o2 = _this.rxProcessLabels({ meta: ctx.meta, parent: child });
                    var o3 = _this.rxProcessChild(ctx.meta.child || [], child);
                    return Rx.Observable.concat(o1, o2, o3);
                });
            });
            return Rx.Observable.concat(o1, o2, o3);
        });
        return childObservable;
    };
    return SiteProcessor;
}());
exports.SiteProcessor = SiteProcessor;
