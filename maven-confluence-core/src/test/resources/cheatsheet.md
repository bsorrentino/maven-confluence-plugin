

{toc}

${pageTtitle}


## strikethrough

~~Mistaken text.~~

## bold

this bold text **Note: You must pass in an element name, and the name must contain a dash "-"**

## table

| First Header  | Second Header |
| ------------- | ------------- |
| Content Cell  | Content Cell  |
| Content Cell  | Content Cell  |
| Content Cell  | Content Cell  |

## Links 

### External 
Yeoman generator to scaffold out [Polymer 1.0](http://www.polymer-project.org/)'s elements using Typescript based on [PolymerTS](https://github.com/nippur72/PolymerTS) project

[PolymerTS](https://github.com/nippur72/PolymerTS) is a project that allow to develop [Polymer 1.0](http://www.polymer-project.org/) element using Typescript @decorated classes.

It is thought to work joined with [Polymer Starter Kit](https://developers.google.com/web/tools/polymer-starter-kit/)

### Ref 

This is an ref link [README.MD]

This is an ref link [README.MD][My Readme]

This is an ref link [README.MD][1]


## Bullet List

 * [polymerts:el](#element-alias-el)
 * [polymerts:gen](#generate-typescript-from-element)
 * PolymerTS's element scaffold

### Element (alias: El)

Generates a polymer element in `app/elements` and optionally appends an import to `app/elements/elements.html`.





## Inline HTML

<br/>

## Blocks

**Note:** *Normal*

> It generates also the related **Polymer Behaviors** but only if they are in the same element's folder (eg. iron-selector).

> In other cases you have to generate each requested Behavior. So find it and rerun the generator.


> **Note:** *Special*

>> Special Note


## Image 

![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Logo Title Text 1")

![ ](${pageTitle}^image-name.png )

![${pageTitle}^image-name.png](.images/image-name.png "")

[ref link node]


## Code / Verbatim

XML:

```xml
  <developers>
    <developer>
      <id>bsorrentino</id>
      <name>Bartolomeo Sorrentino</name>
      <email>bartolomeo.sorrentino@gmail.com</email>
    </developer>
  </developers>

```

this is inline `` npm install -g generator-polymerts `` command

```
--path, element output path. By default generated element (and dependencies) will put  in folder 'typings/polymer'.
--elpath, element source path. Just in case (eg. Behaviors ) the element folder hasn't the same name of the element

```

Example:
```bash
yo polymerts:gen polymer-element [--path ] [--elpath ]
```

