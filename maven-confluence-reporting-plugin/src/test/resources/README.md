## Yeoman generator to scaffold out [Polymer 1.0](http://www.polymer-project.org/)'s elements using Typescript based on [PolymerTS](https://github.com/nippur72/PolymerTS) project

## Introduction

[PolymerTS](https://github.com/nippur72/PolymerTS) is a project that allow to develop [Polymer 1.0](http://www.polymer-project.org/) element using Typescript @decorated classes.

It is thought to work joined with [Polymer Starter Kit](https://developers.google.com/web/tools/polymer-starter-kit/)

## Features

 * PolymerTS's element scaffold

## Installation

`` npm install -g generator-polymerts ``

## Generators

 * [polymerts:el](#element-alias-el)
 * [polymerts:gen](#generate-typescript-from-element)


### Element (alias: El)
Generates a polymer element in `app/elements` and optionally appends an import to `app/elements/elements.html`.

Example:
```bash

yo polymerts:el my-element [--path ]
```

**Note: You must pass in an element name, and the name must contain a dash "-"**

#### Options

```
--path, element output path. By default is 'app' and will put your element in folder 'app/elements'.
```

### Generate Typescript from Element
Generates a Typescript definition (.d.ts) from an installed Polymer element present in `bower_components`.

Example:
```bash

yo polymerts:gen polymer-element [--path ] [--elpath ]
```

**Note:**
> It generates also the related **Polymer Behaviors** but only if they are in the same element's folder (eg. iron-selector).

> In other cases you have to generate each requested Behavior. So find it and rerun the generator.


#### Options

```
--path, element output path. By default generated element (and dependencies) will put  in folder 'typings/polymer'.
--elpath, element source path. Just in case (eg. Behaviors ) the element folder hasn't the same name of the element
```
