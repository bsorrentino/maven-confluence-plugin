
// - offset: {Number} the offset of the argv at which we should begin to parse
// - schema: {Object}
// - shorthands: {Object}
// X - context: {Object} the context of the helper functions
// - parallel: {boolean=false} whether checker should check the properties in parallel, default to false
// - limit: {boolean=false} limit to the schema
declare interface CleanOptions {
    offset?:number;
    schema?:Object;
    shorthands?:Object;
    context?:Object;
    parallel?:boolean;
    limit?:boolean;
}

declare function clean( options?:boolean|CleanOptions );

declare module "clean" {
    export = clean;
}