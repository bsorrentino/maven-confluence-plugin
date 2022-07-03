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
> * [Use JSON Site Definition](http://bsorrentino.github.io/maven-confluence-plugin/site_json_guide.html)
### Format
> * [Markdown Syntax Support](http://bsorrentino.github.io/maven-confluence-plugin/markdown_guide.html)
> * [Storage Format Support](http://bsorrentino.github.io/maven-confluence-plugin/storageformat_guide.html)
> * [Confluence Wiki Support](http://bsorrentino.github.io/maven-confluence-plugin/Notation%20Guide%20-%20Confluence.html)
### Processor
> * [Site Processor Usage](http://bsorrentino.github.io/maven-confluence-plugin/site_processor_guide.html)
> * [Markdown Processor usage](http://bsorrentino.github.io/maven-confluence-plugin/markdown_processor_guide.html)

## Examples

For pratical samples refer to folder/module [test-publishing](https://github.com/bsorrentino/maven-confluence-plugin/tree/master/test-publishing)

## News

 Date  | Release                                                                                             | Info   
--- |-----------------------------------------------------------------------------------------------------| ---
 **Jul 3, 2022** | [Release 7.7](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.7)             | Merged PR [#266](https://github.com/bsorrentino/maven-confluence-plugin/pull/266) "**Adding JSON Support**". Thanks to [jksevend](https://github.com/jksevend) for contribution.
 **Jun 3, 2022** | [Release 7.6](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.6)             | Merged PR [#267](https://github.com/bsorrentino/maven-confluence-plugin/pull/267) "**added function to define jira instance baseurl**", that fix issue [#136](https://github.com/bsorrentino/maven-confluence-plugin/issues/136). Thanks to [tspindler](https://github.com/tspindler) for contribution
 **Apr 1, 2022** | [Release 7.5](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.5)             | Fix empty table cell not handled properly. Refer to [#264](https://github.com/bsorrentino/maven-confluence-plugin/issues/264).
 **Jan 10, 2022** | [Release 7.4](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.4)             | Fix problem with **encoding**. Refer to [#261](https://github.com/bsorrentino/maven-confluence-plugin/issues/261).
 **Aug 09, 2021** | [Release 7.3.2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.3.2)         | Fix problem with **ReadTimeout** & **WriteTimeout**. Refer to [#256](https://github.com/bsorrentino/maven-confluence-plugin/issues/256). see the PR [#257](https://github.com/bsorrentino/maven-confluence-plugin/pull/257) for details. Thanks to [qwazer](https://github.com/qwazer) for contribution
 **Aug 07, 2021** | [Release 7.3.1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.3.1)         | Refer to  [#256](https://github.com/bsorrentino/maven-confluence-plugin/issues/256) for details
 **Jul 31, 2021** | [Release 7.3](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.3)             | Refer to discussion [#247](https://github.com/bsorrentino/maven-confluence-plugin/discussions/247) for details
 **Jul 19, 2021** | [Release 7.2.1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.2.1)         | Refer to [#253](https://github.com/bsorrentino/maven-confluence-plugin/issues/253) for details
 **Jun 10, 2021** | [Release 7.2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.2)             | Refer to [#252](https://github.com/bsorrentino/maven-confluence-plugin/issues/252) for details
 **Jun 01, 2021** | [Release 7.1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.1)             | Refer to [#248](https://github.com/bsorrentino/maven-confluence-plugin/issues/248) for details
 **May 18, 2021** | [Release 7.0](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.0)             | Refer to [Major Release 7.0](https://github.com/bsorrentino/maven-confluence-plugin/projects/1) for details
 **Mar 07, 2021** | [Release 7.0-rc2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.0-rc2)     | Refer to [#245](https://github.com/bsorrentino/maven-confluence-plugin/issues/245) npe when children uri from classpath
 **Feb 11, 2021** | [Release 7.0-rc1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.0-rc1)     | Refer to [#244](https://github.com/bsorrentino/maven-confluence-plugin/issues/244) processor-freemarker doesn't work on 7.0-beta2.
 **Feb 09, 2021** | [Release 7.0-beta2](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.0-beta2) | Refer to [#240](https://github.com/bsorrentino/maven-confluence-plugin/issues/240) add the `page id` to the deploy history(state) manager. <br> Refer to [#241](https://github.com/bsorrentino/maven-confluence-plugin/issues/241) Increase socket timeout. <br> Refer to [#242](https://github.com/bsorrentino/maven-confluence-plugin/issues/242) add i18n titles to ProjectSummaryRenderer and ScmRenderer. **Thanks to [3a04huk ](https://github.com/bsorrentino/maven-confluence-plugin/issues?q=is%3Apr+author%3A3a04huk) for the [PR](https://github.com/bsorrentino/maven-confluence-plugin/pull/243)**
 **Jan 03, 2021** | [Release 7.0-beta1](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.0-beta1) | This release introduce [multi version jar](http://openjdk.java.net/jeps/238) support. Refer to [#224](https://github.com/bsorrentino/maven-confluence-plugin/issues/224).<br> From this release the **Pegdown markdown parser** has been removed as dependency and it will be not supported anymore.It has been replaced by **Commonmark**

### [Release History](HISTORY.md)
