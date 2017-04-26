
# Introduction

From release 3.3.0 we can describe a complete site's layout using an xml file. Through site schema you can set root page (called home) its children's tree, add attachments and labels.

Use it is pretty straightforward, put your **`site.xml`** in **`${basedir}/src/site/confluence`** folder and describe your preferred layout following the [site schema](https://raw.githubusercontent.com/bsorrentino/maven-confluence-plugin/master/schemas/site-schema-5.0.xsd).

## Site template

To simplify understanding, below there is a simple site descriptor template

```xml

<?xml version="1.0" encoding="UTF-8"?>

<bsc:site
    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
    xmlns:bsc='https://github.com/bsorrentino/maven-confluence-plugin'
    xsi:schemaLocation='https://github.com/bsorrentino/maven-confluence-plugin https://raw.githubusercontent.com/bsorrentino/maven-confluence-plugin/master/schemas/site-schema-5.0.xsd'>
    <home name="" uri="">

        <attachment name="" uri="" comment="" contentType="" version=""></attachment>
        <attachment name="" uri="" comment="" contentType="" version=""></attachment>

        <child name="" uri="">

          <attachment name="" uri="" comment="" contentType="" version=""></attachment>
          <attachment name="" uri="" comment="" contentType="" version=""></attachment>

            <child name="" uri="">
              <attachment name="" uri="" comment="" contentType="" version=""></attachment>
              <attachment name="" uri="" comment="" contentType="" version=""></attachment>
            </child>

            <child name="" uri=""></child>

       </child>

        <child name="" uri=""></child>

    </home>

    <label>label1</label>
    <label>label2</label>
    <label>label3</label>

</bsc:site>

```

## Tags description

### home

| Attribute| Description | mandatory |
|-------------|----------------|--------------|
| uri | Content's source | no |
| name | Title of page | no (if uri is defined)|

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

```xml
<!-- include all png files from myfolder -->
<attachment
  name="*.png"
  uri="myfolder"
  comment="file from myfolder"
  contentType="image/png"
  version="1"/>

<!-- include all files from myfolder -->
<attachment  
  uri="myfolder"
  comment="file from myfolder"  
  version="1"/>

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
> ```xml
<home uri="index.confluence">
    <child name="Summary" uri="summary.confluence"/>
    <child name="Goals" uri="goals.confluence">
        <generated ref="plugin.goals"/>
    </child>
    <child name="PluginsSummary" uri="plugins-summary.confluence"/>
</home>
```

## Example

The example below is the [site.xml](https://raw.githubusercontent.com/bsorrentino/maven-confluence-plugin/master/maven-confluence-reporting-plugin/src/site/confluence/site.xml) used by plugin to document itself

```xml
<?xml version="1.0" encoding="UTF-8"?>
<bsc:site
    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
    xmlns:bsc='https://github.com/bsorrentino/maven-confluence-plugin'
    xsi:schemaLocation='https://github.com/bsorrentino/maven-confluence-plugin https://raw.githubusercontent.com/bsorrentino/maven-confluence-plugin/master/schemas/site-schema-5.0.xsd'>

    <home  uri="codehaus-home.confluence">

        <child name="Usage" uri="usage.confluence">
            <attachment name="gitlog-sample02.png" uri="../resources/images/gitlog-sample02.png" comment="gitlog to jira sample" contentType="image/png" version="1"></attachment>
        </child>

    </home>

    <label>maven</label>
    <label>confluence</label>
    <label>documentation</label>

</bsc:site>


```

***

## Note:

### The  **URI** format

The **uri** attribute could refer to

* **File** resource  
> We can refer to file in **absolute** way using **file** scheme (e.g. ` file:///Documents/page.confluence `) or in **relative** way not using any scheme. In that case the path will be resolved starting from **site home**

* **Classpath** resource
> We can refer to resource using **classpath** schema. (e.g. ` classpath:page.confluence `)

* Network resource
> We can refer to resource using **http** scheme. (e.g. ` http://www.thesite.com/page.confluence `)
