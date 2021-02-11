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
> * [Use YAML Site Definition](http://bsorrentino.github.io/maven-confluence-plugin/site_yaml_guide.html)
> * [Use XML Site Definition](http://bsorrentino.github.io/maven-confluence-plugin/site_xml_guide.html)
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
 **Feb 11, 2021** | [Release 7.0-rc1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.0-rc1) | Refer to [#244](https://github.com/bsorrentino/maven-confluence-plugin/issues/244) processor-freemarker doesn't work on 7.0-beta2.
 **Feb 9, 2021** | [Release 7.0-beta2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.0-beta2) | Refer to [#240](https://github.com/bsorrentino/maven-confluence-plugin/issues/240) add the `page id` to the deploy history(state) manager. <br> Refer to [#241](https://github.com/bsorrentino/maven-confluence-plugin/issues/241) Increase socket timeout. <br> Refer to [#242](https://github.com/bsorrentino/maven-confluence-plugin/issues/242) add i18n titles to ProjectSummaryRenderer and ScmRenderer. **Thanks to [3a04huk ](https://github.com/bsorrentino/maven-confluence-plugin/issues?q=is%3Apr+author%3A3a04huk) for the [PR](https://github.com/bsorrentino/maven-confluence-plugin/pull/243)**
 **Jan 3, 2021** | [Release 7.0-beta1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.0-beta1) | This release introduce [multi version jar](http://openjdk.java.net/jeps/238) support. Refer to [#224](https://github.com/bsorrentino/maven-confluence-plugin/issues/224).<br> From this release the **Pegdown markdown parser** has been removed as dependency and it will be not supported anymore.It has been replaced by **Commonmark**
 **Dec 11, 2020** | [Release 6.20](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.20) | Fix issue [#235](https://github.com/bsorrentino/maven-confluence-plugin/issues/235), see section **Include custom macro(s)** in [Markdown Syntax Support](http://bsorrentino.github.io/maven-confluence-plugin/markdown_guide.html) for details
 **Nov 02, 2020** | [Release 6.11](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.11) | Bump junit from 4.12 to 4.13.1
 **Sep 25, 2020** | [Release 6.10](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.10) | Fix issue [#229](https://github.com/bsorrentino/maven-confluence-plugin/issues/229) .
 **Sep 18, 2020** | [Release 6.9.1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9.1) | Fix issue [#226](https://github.com/bsorrentino/maven-confluence-plugin/issues/226) .
 **Aug 5, 2020** | [Release 6.9](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9) | Fix issue [#223](https://github.com/bsorrentino/maven-confluence-plugin/issues/223) .
 **Jul 5, 2020** | [Release 6.9-rc2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9-rc2) | publication performance improvements.
 **Jun 18, 2020** | [Release 6.9-rc1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9-rc1) | add support for [blogbost goal](http://bsorrentino.github.io/maven-confluence-plugin/blogpost-mojo.html).
 **May 05, 2020** | [Release 6.9-beta1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.9-beta1) | improved support of  [Scroll Versions Addon](https://marketplace.atlassian.com/apps/1210818/scroll-versions-for-confluence?hosting=server&tab=overview) .
 **Apr 26, 2020** | [Release 6.8](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.8) | Added [markdown processor](http://bsorrentino.github.io/maven-confluence-plugin/markdown_processor_guide.html) to support future removal of **deprecated** [Pegdown](https://github.com/sirthias/pegdown) implementation.
 **Mar 23, 2020** | [Release 6.7.3](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.7.3) |
 **Mar 11, 2020** | [Release 6.7.2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v6.7.2) |

### [Release History](HISTORY.md)
