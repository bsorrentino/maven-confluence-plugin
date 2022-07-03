
# Introduction

From release 7.7 we can describe a complete site's layout using a **json format**. Through site schema you can set root page (called home) its children's tree, add attachments and labels.

Use it is pretty straightforward, put your **`site.json`** in **`${basedir}/src/site/confluence`** folder and describe your preferred layout following the [site schema](https://raw.githubusercontent.com/bsorrentino/maven-confluence-plugin/master/schemas/site-schema-7.7.json).

## Site template

To simplify understanding, below there is a simple site descriptor template

```json
{
  "home": {
    "uri": "encoding.confluence",
    "children": [
      {
        "name": "page 1",
        "uri": "page1.md"
      },
      {
        "name": "page 2",
        "uri": "page2.md",
        "attachments": [
          {
            "name": "attach1.png",
            "uri": "attach1.png",
            "comment": "attach1_comment",
            "contentType": "image/png",
            "version": 1
          }
        ]
      }
    ],
    "attachments": [
      {
        "name": "home.png",
        "uri": "home.png",
        "comment": "home_comment",
        "contentType": "image/png",
        "version": 1
      }
    ]
  },
  "labels": [
    "encoding",
    "test"
  ]
}
```

## properties description

### site

| Attribute| Description | mandatory |
|-------------|----------------|--------------|
| space-key | space key (if set overrides the equivalent pom configuration)  | no |


### home

| Attribute| Description | mandatory |
|-------------|----------------|--------------|
| uri | Content's source | no |
| name | Title of page | no (if uri is defined)|
| parentPageId | parent page id (if set overrides the equivalent pom configuration) | no |
| parentPage | parent page name (if set overrides the equivalent pom configuration) | no |
| ignoreVariables | if it is `true` the variables `${...}` are not injected during page processing | no |

### children array

`children` is an array of object with the following schema:

| Attribute| Description | mandatory |
|-------------|----------------|--------------|
| uri | Content's source | no |
| name | Title of page | no (if uri is defined)|
| parentPage | parent page name (if set overrides the equivalent pom configuration) | no |
| ignoreVariables | if it is `true` the variables `${...}` are not injected during page processing | no |

### attachments array

`attachments` is an array of object with the following schema:

 Attribute| Description | mandatory
 ---- | ----- | ----
 uri | Content's source or a **directory** | no
 name | Name of attachment or a [glob pattern][1] | no (if uri is defined)
 comment |  | no
 contentType |  | yes
 version | | no

#### Directory support

from version `5.0-rc4` the  `attachment` supports also the file inclusion from directory

**Example**

```json
"attachments": [
    {
        "name": "*.png",
        "uri": "myfolder",
        "comment": "file from myfolder",
        "contentType": "image/png",
        "version": 1
    },
    {
        "uri": "myfolder",
        "comment": "file from myfolder",
        "contentType": "image/png",
        "version": 1
    }
]

```

### labels array

`labels` is an array of string 

## Note:

### The  **URI** format

The **uri** attribute could refer to

* **File** resource  
> We can refer to file in **absolute** way using **file** scheme (e.g. ` file:///Documents/page.confluence `) or in **relative** way not using any scheme. In that case the path will be resolved starting from **site home**

* **Classpath** resource
> We can refer to resource using **classpath** schema. (e.g. ` classpath:page.confluence `)

* **Network** resource
> We can refer to resource using **http** scheme. (e.g. ` http://www.thesite.com/page.confluence `)

## Using Freemarker in the Site Definition
See YAML definition for details. Freemarker is not dependent on the output format and can be used in the same fashion.

[1]: https://docs.oracle.com/javase/7/docs/api/java/nio/file/FileSystem.html#getPathMatcher(java.lang.String)