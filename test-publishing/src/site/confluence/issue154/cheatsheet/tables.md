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

## Issue [#295](https://github.com/bsorrentino/maven-confluence-plugin/issues/295)
| Property  | Default   | Required | Description                                   | Example |
|-----------|-----------|----------|-----------------------------------------------|---------|
| someEntry | `{0,128}` | false    | value that is being misinterpreted as a macro | -       |