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

 Date             | Release                                                                                   | Info                                                                                                                                                                                                                                                                                                                                
------------------|-------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| **Mar 4, 2023**  | [Release 7.10](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.10) | Allow to skip html tags in markdown processing. Refer to [#284](https://github.com/bsorrentino/maven-confluence-plugin/issues/284)                                                                                                                                                                                                  |
| **Jan 4, 2023**  | [Release 7.9](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.9)   | Fix problem Problem parsing `%` character from markdown to wiki. Refer to [#282](https://github.com/bsorrentino/maven-confluence-plugin/issues/282)                                                                                                                                                                                 |
| **Dec 9, 2022**  | [Release 7.8](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.8)   | Merged PR [#281](https://github.com/bsorrentino/maven-confluence-plugin/pull/281) that fix [#280](https://github.com/bsorrentino/maven-confluence-plugin/issue/280) "**allows specifying additional HTTP headers in the servers section of settings.xml**". Thanks to [DirkMahler](https://github.com/DirkMahler) for contribution. |
| **Jul 3, 2022**  | [Release 7.7](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.7)   | Merged PR [#266](https://github.com/bsorrentino/maven-confluence-plugin/pull/266) "**Adding JSON Support**". Thanks to [jksevend](https://github.com/jksevend) for contribution.                                                                                                                                                    |
| **Jun 3, 2022**  | [Release 7.6](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.6)   | Merged PR [#267](https://github.com/bsorrentino/maven-confluence-plugin/pull/267) "**added function to define jira instance baseurl**", that fix issue [#136](https://github.com/bsorrentino/maven-confluence-plugin/issues/136). Thanks to [tspindler](https://github.com/tspindler) for contribution.                             |
| **Apr 1, 2022**  | [Release 7.5](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.5)   | Fix empty table cell not handled properly. Refer to [#264](https://github.com/bsorrentino/maven-confluence-plugin/issues/                                                                                                                                                                                                           |
| **Jan 10, 2022** | [Release 7.4](https://github.com/bsorrentino/maven-confluence-plugin/releases/tag/v7.4)   | Fix problem with **encoding**. Refer to [#261](https://github.com/bsorrentino/maven-confluence-plugin/issues/261).                                                                                                                                                                                                                  |

### [Release History](HISTORY.md)
