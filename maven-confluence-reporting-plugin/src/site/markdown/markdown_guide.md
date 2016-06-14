## Usage

To use markdown it is enough to specify a **.md** file in the **site's uri**.

Below the supported format:


### Header
```
# h1
## h2
### h3
#### h4
```
......

### Text Format

#### strikethrough
```
~~Mistaken text.~~
```

#### bold
```
**Bold text**
```

#### italic
```
_Italic text_
```

### Bullet List

```
 * Element 1
 * Element 2
 * Element 3
```

### Numbered List

```
 1. Element 1
 1. Element 2
 1. Element 3
```


### Links

#### external link

```
[this is a link](https://github.com/bsorrentino/maven-confluence-plugin/wiki/Use-Markdown/)
```

#### internal link

```
This is an ref link [README.MD]

This is an ref link [README.MD][My Readme]

This is an ref link [README.MD][1]
```

### Image

#### external image
```
![alt](https://github.com/bsorrentino/..../raw/images/icon48.png "Title")
```

#### confluence specific
```
![ ](${pageTitle}^image-name.png )
```

#### portable image (can be used for confluence and not)
```
![${pageTitle}^image-name.png](.images/image-name.png "")
```

### Code / Verbatim

#### code block

<pre><![CDATA[
```xml
<developers>
  <developer>
    <id>bsorrentino</id>
    <name>Bartolomeo Sorrentino</name>
    <email>bartolomeo.sorrentino@gmail.com</email>
  </developer>
</developers>
```
]]></pre>

#### inline
<pre><![CDATA[
```this is inline```  
]]></pre>

### table

```
| First Header  | Second Header |
| ------------- | ------------- |
| Content Cell  | Content Cell  |
| Content Cell  | Content Cell  |
| Content Cell  | Content Cell  |
```

### Blockquote

```
> Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
Aenean massa. Cum sociis natoque

> Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus.
Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt.
```
