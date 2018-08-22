"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const traverse = require("traverse");
const Rx = require("rx");
function removeSingleArrays(obj, filter) {
    // Traverse all the elements of the object
    traverse(obj).forEach(function traversing(value) {
        // As the XML parser returns single fields as arrays.
        if (value instanceof Array && value.length === 1) {
            if (filter && !filter(this.key))
                return;
            this.update(value[0]);
        }
    });
}
function rxTraverse(obj) {
    return Rx.Observable.create((observer) => {
        traverse(obj).forEach(function traversing(value) {
            observer.onNext(value);
            // As the XML parser returns single fields as arrays.
            /*
            if (value instanceof Array && value.length === 1) {
              if( filter && !filter(this.key) ) return;
                this.update(value[0]);
            }
            */
        });
        observer.onCompleted();
    });
}
