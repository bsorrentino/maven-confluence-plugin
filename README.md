<a href="http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22confluence-reporting-maven-plugin%22"><img src="https://img.shields.io/maven-central/v/org.bsc.maven/confluence-reporting-maven-plugin.svg">
</a>&nbsp;<img src="https://img.shields.io/github/forks/bsorrentino/maven-confluence-plugin.svg">&nbsp;
<img src="https://img.shields.io/github/stars/bsorrentino/maven-confluence-plugin.svg">&nbsp;<a href="https://github.com/bsorrentino/maven-confluence-plugin/issues"><img src="https://img.shields.io/github/issues/bsorrentino/maven-confluence-plugin.svg">
</a>&nbsp;[![Join the chat at https://gitter.im/bsorrentino/maven-confluence-plugin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/bsorrentino/maven-confluence-plugin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## What is
It is a **Maven plugin** that generates **project's documentation directly to confluence** allowing, in the same time, to **keep in-sync project evolution with its documentation**.
> Originally hosted to [google code](https://code.google.com/p/maven-confluence-plugin/) from release 4.0.0 has been moved to github

## Documentation
> The standard maven documentation is [here](http://bsorrentino.github.io/maven-confluence-plugin/)
> * [Goals](http://bsorrentino.github.io/maven-confluence-plugin/plugin-info.html)
> * [Usage](http://bsorrentino.github.io/maven-confluence-plugin/usage.html)
> * [Use XML Site Definition](http://bsorrentino.github.io/maven-confluence-plugin/site_xml_guide.html)
> * [Use YAML Site Definition](http://bsorrentino.github.io/maven-confluence-plugin/site_yaml_guide.html)
### Format
> * [Markdown Syntax Support](http://bsorrentino.github.io/maven-confluence-plugin/markdown_guide.html)
> * [Storage Format Support](http://bsorrentino.github.io/maven-confluence-plugin/storageformat_guide.html)
> * [Confluence Wiki Support](http://bsorrentino.github.io/maven-confluence-plugin/Notation%20Guide%20-%20Confluence.html)
### Processor
> * [Site Processor Usage](http://bsorrentino.github.io/maven-confluence-plugin/site_processor_guide.html)
> * [Markdown Processor usage](http://bsorrentino.github.io/maven-confluence-plugin/markdown_processor_guide.html)

### News

 Date  | Release | Info   
--- | --- | ---
 **Sep 18, 2020** | [Release 6.9.1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9.1) | Fix issue [#226](https://github.com/bsorrentino/maven-confluence-plugin/issues/226) .
  **Aug 5, 2020** | [Release 6.9](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9) | Fix issue [#223](https://github.com/bsorrentino/maven-confluence-plugin/issues/223) .
 **Jul 5, 2020** | [Release 6.9-rc2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9-rc2) | publication performance improvements.
 **Jun 18, 2020** | [Release 6.9-rc1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9-rc1) | add support for [blogbost goal](http://bsorrentino.github.io/maven-confluence-plugin/blogpost-mojo.html).
 **May 05, 2020** | [Release 6.9-beta1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9-beta1) | improved support of  [Scroll Versions Addon](https://marketplace.atlassian.com/apps/1210818/scroll-versions-for-confluence?hosting=server&tab=overview) .
 **Apr 26, 2020** | [Release 6.8](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.8) | Added [markdown processor](http://bsorrentino.github.io/maven-confluence-plugin/markdown_processor_guide.html) to support future removal of **deprecated** [Pegdown](https://github.com/sirthias/pegdown) implementation.
 **Mar 23, 2020** | [Release 6.7.3](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.7.3) | 
 **Mar 11, 2020** | [Release 6.7.2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.7.2) | 
 **Nov 13, 2019** | [Release 6.7.1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.7.1) | 
 **Nov 02, 2019** | [Release 6.7](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.7) | Added [site processor](http://bsorrentino.github.io/maven-confluence-plugin/site_processor_guide.html) support - enhanced implementation inspired on PR *202* by [LZaruba](https://github.com/LZaruba).
 **Oct 30, 2019** | [Release 6.6](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.6) | Merged PR *201*,*205*,*207* by [LZaruba](https://github.com/LZaruba)
 **Oct 29, 2019** | [Release 6.5](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.5) | 
 **Oct 21, 2019** | [Release 6.5-beta2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.5-beta2) | 
 **Oct 14, 2019** | [Release 6.5-beta1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.5-beta1) | Added support for [Scroll Versions for Confluence](https://marketplace.atlassian.com/apps/1210818/scroll-versions-for-confluence?hosting=server&tab=overview).
 **Oct 01, 2019** | [Release 6.4.1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.4.1) | 
 **Aug 02, 2019** | [Release 6.4](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.4) | 
 **Jul 15, 2019** | [Release 6.3.2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.3.2) | 
 **Jun 12, 2019** | [Release 6.3.1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.3.1) | 
 **Mar 26, 2019** | [Release 6.3](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.3) | 
 **Feb 07, 2019** | [Release 6.2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.2) | 
 **Jan 16, 2019**  |[Release 6.1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.1) | 
 **Jan 14,2019**  | **[confluence-site](https://github.com/bsorrentino/maven-confluence-plugin/tree/cli)** |  Has been moved to an independent repository [confluence-site-publisher](https://github.com/bsorrentino/confluence-site-publisher). 
 **jan 08, 2019** | [Release 6.0](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.0) | 

### [Release History](HISTORY.md)
