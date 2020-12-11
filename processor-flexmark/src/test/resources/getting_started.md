
# Table of Content
{toc}

# Getting Started with ${version}

GPWS (aka SPWS) stands for Genesys PoWer Script. Below the features available  

## Scripts' modularisation

From this release GPWS support the [requirejs](http://requirejs.org/) module inclusion.

### Inclusion

To include a module or more modules (i.e. external javascript files)  you have to use **require** function as shown in example below:

```javascript
require(['module1','module2'], function () {

  print( "module1.js and module2.js loaded!");

  // now you can use the functions/objects imported
});
```
### Definition

It is also possible to define a module. Definition allowing module to publish object/function during require phase.

To define a module you have to use **define** function as shown in example below:

> example: _module.js_

```javascript

define(["require", "exports"], function (require, exports) {

  print("MODULE DEFINED!");

});
```

### links

#### External Link

* This is an external link [Google](http://www.google.com)

#### Ref link

* This is a ref link [My page]
* This is a ref link [My page][My Page Reference] 

