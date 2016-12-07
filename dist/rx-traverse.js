"use strict";
var traverse = require("traverse");
var Rx = require("rx");
function removeSingleArrays(obj, filter) {
    traverse(obj).forEach(function traversing(value) {
        if (value instanceof Array && value.length === 1) {
            if (filter && !filter(this.key))
                return;
            this.update(value[0]);
        }
    });
}
function rxTraverse(obj) {
    return Rx.Observable.create(function (observer) {
        traverse(obj).forEach(function traversing(value) {
            observer.onNext(value);
        });
        observer.onCompleted();
    });
}
