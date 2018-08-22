"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const xml = require("xml2js");
const filesystem = require("fs");
const path = require("path");
const Rx = require("rx");
const md_1 = require("./md");
let parser = new xml.Parser();
let rxParseString = Rx.Observable.fromNodeCallback(parser.parseString);
exports.rxReadFile = Rx.Observable.fromNodeCallback(filesystem.readFile);
class SiteProcessor {
    /**
     *
     */
    constructor(confluence, spaceId, parentTitle, sitePath) {
        this.confluence = confluence;
        this.spaceId = spaceId;
        this.parentTitle = parentTitle;
        this.sitePath = sitePath;
    }
    /**
     *
     */
    rxParse(fileName) {
        return exports.rxReadFile(path.join(this.sitePath, fileName))
            .flatMap((value) => rxParseString(value.toString()))
            //.doOnNext( (value) => console.dir( value, { depth:4 }) )
            .map((value) => {
            for (let first in value)
                return value[first]['home'];
        });
    }
    /**
     *
     */
    rxStart(fileName) {
        return this.rxParse(fileName)
            .flatMap((value) => this.rxProcessChild(value));
    }
    /**
     *
     */
    rxReadContent(filePath) {
        return exports.rxReadFile(filePath)
            .map((value) => {
            let storage;
            let ext = path.extname(filePath);
            switch (ext) {
                case ".md":
                    storage = {
                        value: md_1.markdown2wiki(value),
                        representation: 1 /* WIKI */
                    };
                    break;
                default:
                    storage = { value: value.toString(), representation: 1 /* WIKI */ };
                    break;
            }
            return storage;
        });
    }
    /**
     *
     */
    rxCreateAttachment(ctx) {
        let confluence = this.confluence;
        let attachment = {
            comment: ctx.meta.$['comment'],
            contentType: ctx.meta.$['contentType'],
            fileName: ctx.meta.$.name
        };
        return exports.rxReadFile(path.join(this.sitePath, ctx.meta.$.uri))
            .doOnCompleted(() => console.log("created attachment:", attachment.fileName))
            .flatMap((buffer) => Rx.Observable.fromPromise(confluence.addAttachment(ctx.parent, attachment, buffer)));
    }
    /**
     *
     */
    rxCreatePage(ctx) {
        let confluence = this.confluence;
        let getOrCreatePage = (!ctx.parent) ?
            Rx.Observable.fromPromise(confluence.getOrCreatePage(this.spaceId, this.parentTitle, ctx.meta.$.name)) :
            Rx.Observable.fromPromise(confluence.getOrCreatePage2(ctx.parent, ctx.meta.$.name));
        return getOrCreatePage
            .doOnNext((page) => console.log("creating page:", page.title))
            .flatMap((page) => {
            return this.rxReadContent(path.join(this.sitePath, ctx.meta.$.uri))
                .flatMap((storage) => Rx.Observable.fromPromise(confluence.storePageContent(page, storage)));
        });
    }
    rxProcessLabels(ctx) {
        return Rx.Observable.fromArray(ctx.meta.label || [])
            .flatMap((data) => Rx.Observable.fromPromise(this.confluence.addLabelByName(ctx.parent, data)));
    }
    rxProcessAttachments(ctx) {
        return Rx.Observable.fromArray(ctx.meta.attachment || [])
            .map((data) => { return { meta: data, parent: ctx.parent }; })
            .flatMap((ctx) => this.rxCreateAttachment(ctx));
    }
    rxProcessChild(child, parent) {
        if (!child || child.length == 0)
            return Rx.Observable.empty();
        let first = child[0];
        let childObservable = this.rxCreatePage({ meta: first, parent: parent })
            .flatMap((page) => {
            let o1 = this.rxProcessAttachments({ meta: first, parent: page });
            let o2 = this.rxProcessLabels({ meta: first, parent: page });
            let o3 = Rx.Observable.fromArray(first.child || [])
                .map((data) => { return { meta: data, parent: page }; })
                .concatMap((ctx) => {
                return this.rxCreatePage(ctx)
                    .flatMap((child) => {
                    let o1 = this.rxProcessAttachments({ meta: ctx.meta, parent: child });
                    let o2 = this.rxProcessLabels({ meta: ctx.meta, parent: child });
                    let o3 = this.rxProcessChild(ctx.meta.child || [], child);
                    return Rx.Observable.concat(o1, o2, o3);
                });
            });
            return Rx.Observable.concat(o1, o2, o3);
        });
        return childObservable;
    }
}
exports.SiteProcessor = SiteProcessor;
