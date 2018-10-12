
# Introduction

From release 6.0-rc3 we can describe a complete site's layout using an yaml file. Through site schema you can set root page (called home) its children's tree, add attachments and labels.

Use it is pretty straightforward, put your **`site.yaml`** in **`${basedir}/src/site/confluence`** folder and describe your preferred layout following the [site schema](https://raw.githubusercontent.com/bsorrentino/maven-confluence-plugin/master/schemas/site-schema-6.0.xsd).

## Site template

To simplify understanding, below there is a simple site descriptor

```yaml
home:
  uri: encoding.confluence
  children:
    - name: "page 1"
      uri: page1.md    
    - name: "page 2"
      uri: page2.md    
      attachments:
        - name: "attach1.png"
          uri: attach1.png
          comment: attach1_comment
          contentType: "image/png"
          version: 1  
  attachments:
    - name: "home.png"
      uri: home.png
      comment: home_comment
      contentType: "image/png"
      version: 1  
labels:    
  - encoding
  - test
```

## Tags description

### site

| Attribute| Description | mandatory |
|-------------|----------------|--------------|
| space-key | space key (if set overrides the equivalent pom configuration)  | no |


### home

| Attribute| Description | mandatory |
|-------------|----------------|--------------|
| uri | Content's source | no |
| name | Title of page | no (if uri is defined)|
| parent-page-id | parent page id (if set overrides the equivalent pom configuration) | no |
| parent-page | parent page name (if set overrides the equivalent pom configuration) | no |

### child

| Attribute| Description | mandatory |
|-------------|----------------|--------------|
| uri | Content's source | no |
| name | Title of page | no (if uri is defined)|

### attachment

 Attribute| Description | mandatory
 ---- | ----- | ----
 uri | Content's source or a **directory** | no
 name | Name of attachment or a [glob pattern]( https://docs.oracle.com/javase/7/docs/api/java/nio/file/FileSystem.html#getPathMatcher(java.lang.String) ) | no (if uri is defined)
 comment |  | no
 contentType |  | yes
 version | | no

#### Directory support

from version `5.0-rc4` the tag `attachment` supports also the file inclusion from directory

**Example**

```yaml
# include all png files from myfolder
attachments:
  - name: "*.png"
    uri: myfolder
    comment: file from myfolder
    contentType: "image/png"
    version: 1  

# include all files from myfolder
attachments:  
  - uri: myfolder
    comment: "file from myfolder"  
    version: 1

```


### label

> The content of label's tag is the label's value

### generated
> This tag has been added from version **5.0-rc3** and allows to choose where put the **built-in generated pages**
> Currently only **plugin.goals** is supported therefore such tag is useful when we are dealing with **Maven Plugin Documentation**

| Attribute| Description | mandatory |
|:-------------|:----------------|:--------------|
| ref | built-in generated pages Id. Currently only **plugin.goals** is supported | yes |

#### Usage example
```yaml
home:
  uri: index.confluence
  children:
    - name: Summary
      uri: summary.confluence   
    - name: Goals
      uri: goals.confluence
          generated:
            ref: plugin.goals
    - name: PluginsSummary
      uri: plugins-summary.confluence
```

## Note:

### The  **URI** format

The **uri** attribute could refer to

* **File** resource  
> We can refer to file in **absolute** way using **file** scheme (e.g. ` file:///Documents/page.confluence `) or in **relative** way not using any scheme. In that case the path will be resolved starting from **site home**

* **Classpath** resource
> We can refer to resource using **classpath** schema. (e.g. ` classpath:page.confluence `)

* **Network** resource
> We can refer to resource using **http** scheme. (e.g. ` http://www.thesite.com/page.confluence `)
