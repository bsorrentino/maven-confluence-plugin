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

### Unordered List

```
* Element 1
* Element 2
* Element 3
* Sublist
   1. item 1
   1. item 2
   1. item 3
```

### Ordered List

```
1. Element 1
1. Element 2
1. Element 3
1. Sublist
   * item 1
   * item 2
   * item 3
```

### Site Ref Links

```

* This one is [inline](http://google.com "Google").
* This one is [inline **wo** title](http://google.com).
* This is my [google] link defined after.
* This is my [more complex google] link defined after.
* This is my [relative](relativepage) link defined after.
* This is my [rel] link defined after.

[rel]: relativeagain
[more complex google]: http://google.com "Other google"
[google]: http://google.com

```

### Image Ref Link

```
* add an absolute ![conf-icon](http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png "My conf-icon") with title.
* add a relative ![conf-icon](conf-icon-64.png "My conf-icon") with title.
* add a relative ![conf-icon](conf-icon-64.png) without title.
* add a ref img ![conf-icon-y][y] with title.
* add a ref img ![conf-icon-y1][y1] without title.
* add a ref img ![conf-icon-y2][y2] relative.
* add a ref img ![conf-icon-none] relative with default refname.

[y]: http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png "My conf-icon"
[y1]: http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png
[y2]: conf-icon-64.png
[conf-icon-none]: conf-icon-64.png
```

#### confluence specific (DEPRECATED)
```
![](${page.title}^image-name.png)
```

#### portable image

**Original Size**

```
![](image-name.png)
```

**Thumbnail**

```
![thumbnail](image-name.png)
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
`this is inline`  
]]></pre>

### table

```
Colons can be used to align columns.

| Tables        | Are           | Cool  |
| ------------- |:-------------:| -----:|
| col 3 is      | right-aligned | $1600 |
| col 2 is      | centered      |   $12 |
| zebra stripes | are neat      |    $1 |

There must be at least 3 dashes separating each header cell.
The outer pipes (|) are optional, and you don't need to make the 
raw Markdown line up prettily. You can also use inline Markdown.

Markdown | Less | Pretty
--- | --- | ---
*Still* | `renders` | **nicely**
1 | 2 | 3
```

## Notice Block Syntax 

### Info

<pre><![CDATA[
> **info:** About me
>
>> tposidufsqdf qsfpqs dfopqsdijf q
>>  mldjkflqsdif sqj
>

]]></pre>

### Note without title

<pre><![CDATA[
> **Note:**
>
> Contents of my note
>

]]></pre>

### Tip without imbrication

<pre><![CDATA[
> **tip:** About you
>
> tposidufsqdf qsfpqs dfopqsdijf q
>  mldjkflqsdif sqj
>

]]></pre>


### Warning with complex content

<pre><![CDATA[
> **warning:** About him
>
> tposidufsqdf qsfpqs dfopqsdijf q
>  mldjkflqsdif sqj
>
> - one
> - two
>
> have a **strong** and _pure_ feeling

]]></pre>


### oneline blockquote

<pre><![CDATA[
> test a simple
> blockquote

]]></pre>

### blockquote block

<pre><![CDATA[
> test a 2 paragraph block
>
> this is my second part

]]></pre>
