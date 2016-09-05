Maven's  plugin that allow to generate "project's documentation" directly to confluence allowing, in the same time, to keep in-sync both project &amp; documentation

originally hosted to [google code](https://code.google.com/p/maven-confluence-plugin/) from release 4.0.0 has been moved to github

[Confluence Wiki Notation Guide](http://bsorrentino.github.io/maven-confluence-plugin/Notation%20Guide%20-%20Confluence.html)

|Please donate whether you wish support us to give more time to plugin's growth | [![](https://www.paypal.com/en_US/IT/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=H44UTN3G6DAX6) |
|:------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------|

<a href="http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22confluence-reporting-maven-plugin%22"><img src="https://img.shields.io/maven-central/v/org.bsc.maven/confluence-reporting-maven-plugin.svg">
</a>&nbsp;<img src="https://img.shields.io/github/forks/bsorrentino/maven-confluence-plugin.svg">&nbsp;
<img src="https://img.shields.io/github/stars/bsorrentino/maven-confluence-plugin.svg">&nbsp;<a href="https://github.com/bsorrentino/maven-confluence-plugin/issues"><img src="https://img.shields.io/github/issues/bsorrentino/maven-confluence-plugin.svg">
</a>&nbsp;

[![Join the chat at https://gitter.im/bsorrentino/maven-confluence-plugin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/bsorrentino/maven-confluence-plugin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### News ###

 - | **Release 5.0-SNAPSHOT** | available from  **[MAVEN CENTRAL REPO](https://oss.sonatype.org/content/repositories/snapshots/org/bsc/maven/confluence-reporting-maven-plugin/5.0-SNAPSHOT/)**  |
---- | ---- | ---- |

 * Refer to [Issue 108](https://github.com/bsorrentino/maven-confluence-plugin/issues/108) - StorageFormat Support

 > Starting to implement REST API support

Sep 5,2016  | **Release 4.13** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.13%7Cmaven-plugin)**  
---- | ---- | ----

#### Issues

  * Refer to [Issue 122](https://github.com/bsorrentino/maven-confluence-plugin/issues/122) - Table not generated correctly 
  * Refer to [Issue 124](https://github.com/bsorrentino/maven-confluence-plugin/issues/124) - Render templates variables (ie. plugin.* ) in any page 

#### Pull Requests - Thanks to [wattazoum](https://github.com/wattazoum) for providing such features

  * Refer to [Pull Request 123](https://github.com/bsorrentino/maven-confluence-plugin/pull/123) - Add some line breaks (fixes #122) 

Aug 11,2016  | **Release 4.12** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.12%7Cmaven-plugin)**  
---- | ---- | ----

#### Issues

  * Refer to [Issue 114](https://github.com/bsorrentino/maven-confluence-plugin/issues/114) - Generation plugin doc ignores home page from site inline code format
  * Refer to [Issue 116](https://github.com/bsorrentino/maven-confluence-plugin/issues/116) - Attachments wrongly assigned
  * Refer to [Issue 119](https://github.com/bsorrentino/maven-confluence-plugin/issues/119) - Using an Horizontal rules in a MD page breaks

#### Pull Requests - Thanks to [wattazoum](https://github.com/wattazoum) for providing such features

  * Refer to [Pull Request 113](https://github.com/bsorrentino/maven-confluence-plugin/pull/113) - More flexibility to panels
  * Refer to [Pull Request 115](https://github.com/bsorrentino/maven-confluence-plugin/pull/115) - support more tags in the plugin doc generation
  * Refer to [Pull Request 117](https://github.com/bsorrentino/maven-confluence-plugin/pull/117) - Fix attachments on homepage
  * Refer to [Pull Request 120](https://github.com/bsorrentino/maven-confluence-plugin/pull/120) - Support simpleNode (Horizontal rules + forced line breaks)
  * Refer to [Pull Request 121](https://github.com/bsorrentino/maven-confluence-plugin/pull/121) - Some small improvement to the plugin goal page


Aug 5,2016  | **Release 4.11** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.11%7Cmaven-plugin)**  
---- | ---- | ----

  * Refer to [Issue 111](https://github.com/bsorrentino/maven-confluence-plugin/issues/111) - Fix confluence macro clashing in markdown inline code format
  * Refer to [Pull Request 112](https://github.com/bsorrentino/maven-confluence-plugin/pull/112) - Markdown **referencenode** support

   > **Thanks to [wattazoum](https://github.com/wattazoum) for providing such feature**.

|Jul 1,2016  | **Release 4.10** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.10%7Cmaven-plugin)**  |
|:-----------|:-----------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 107](https://github.com/bsorrentino/maven-confluence-plugin/issues/107) - Use pageId instead of SpaceKey and parent title
  * Refer to [Issue 109](https://github.com/bsorrentino/maven-confluence-plugin/issues/109) - Markdown **sublist** support

   > **Thanks to [esivres](https://github.com/esivres) for providing patch**.


|Jun 21,2016 | **Release 4.9.1** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.9.1%7Cmaven-plugin)**  |
|:-----------|:-----------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Hot Fix on [Issue 99](https://github.com/bsorrentino/maven-confluence-plugin/issues/99) - Allow additional tags in comments (_concerning javadoc translation_)

|Jun 14,2016 | **Release 4.9** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.9%7Cmaven-plugin)**  |
|:-----------|:-----------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 99](https://github.com/bsorrentino/maven-confluence-plugin/issues/99) - Allow additional tags in comments (_concerning javadoc translation_)
  * Refer to [Pull Request 105](https://github.com/bsorrentino/maven-confluence-plugin/pull/105) - Add property ```childrenTitlesPrefixed```

   > **Thanks to [pbaris](https://github.com/pbaris) for contribution**.

|May 21,2016 | **Release 4.8** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.8%7Cmaven-plugin)**  |
|:-----------|:-----------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 100](https://github.com/bsorrentino/maven-confluence-plugin/issues/100) - Enable builtin ```project.scmManager/dependencies/team``` for ```maven-plugin```
  * Refer to [Issue 102](https://github.com/bsorrentino/maven-confluence-plugin/issues/102) - ```Title``` element in plugin configuration ignored for ```maven-plugin``` type projects

|May 9,2016 | **Release 4.7** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.7%7Cmaven-plugin)**  |
|:-----------|:-----------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 97](https://github.com/bsorrentino/maven-confluence-plugin/issues/97) - Skip home page (wontfix)
  * Refer to [Issue 98](https://github.com/bsorrentino/maven-confluence-plugin/issues/98) - Improve markdown ```refLink``` support

|Apr 6,2016 | **Release 4.6** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.6%7Cmaven-plugin)**  |
|:-----------|:-----------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 93](https://github.com/bsorrentino/maven-confluence-plugin/issues/93) - Add Developers section
  * Refer to [Issue 94](https://github.com/bsorrentino/maven-confluence-plugin/issues/94) - skip variable substitution

|Feb 11,2016 | **Release 4.5** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.5%7Cmaven-plugin)**  |
|:-----------|:-----------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 91](https://github.com/bsorrentino/maven-confluence-plugin/issues/91) - scm web access not shown as actual link

|Dec 29,2015 | **Release 4.4.3** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.4.3%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

 * refer to [Pull Request 90](https://github.com/bsorrentino/maven-confluence-plugin/pull/90) - Fix [issue 89](https://github.com/bsorrentino/maven-confluence-plugin/issues/89) - Confluence Serialization of lists is broken

  > **Thanks to [gmuecke](https://github.com/gmuecke) for providing fix**.


|Nov 21,2015 | **Release 4.4.2** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.4.2%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

Markdown support refinements

|Oct 03,2015 | **Release 4.4.1** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.4.1%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

Markdown support refinements, refer to documentation from:

> * [Confluence](http://support.softphone.it/confluence/display/~bsorrentino/confluence-reporting-maven-plugin-4.4.1+-+Use+Markdown)
> * [Wiki](https://github.com/bsorrentino/maven-confluence-plugin/wiki/Use-Markdown)
> * [Html](http://bsorrentino.github.io/maven-confluence-plugin/markdown_guide.html)

|Sep 04,2015 | **Release 4.4** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.4%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Pull Request 85](https://github.com/bsorrentino/maven-confluence-plugin/pull/85)
  * Refer to [Issue 84](https://github.com/bsorrentino/maven-confluence-plugin/issues/84) - Evaluate writing documentation in Markdown


|Jul 18,2015 | **Release 4.3** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.3%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 80](https://github.com/bsorrentino/maven-confluence-plugin/issues/80) - Add macro to manage child page title
  * Refer to [Issue 82](https://github.com/bsorrentino/maven-confluence-plugin/issues/82) - nonProxyHosts Settings not respected

 Note:

 >  form 4.3 I've updated the XML's namespace, so goes in your site xml file  and update the namespace from

 > ` http://code.google.com/p/maven-confluence-plugin `

 > to

 > ` https://github.com/bsorrentino/maven-confluence-plugin `



|Jun 5,2015 | [All Codehaus services have been terminated](http://www.codehaus.org/termination/) |
|:-----------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 77] - http://docs.codehaus.org/display/MVNCONFSITE is dead

|May 18,2015 | **Release 4.2** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.2%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Pull Request 76](https://github.com/bsorrentino/maven-confluence-plugin/pull/76)

  > New build-in variables for automated release version publishing in Confluence of maven projects that managed under GIT and JIRA.
  The plugin will automaticaly extract list of resolved JIRA issues since previous version. Thanks to [Anton Reshetnikov](mailto:resheto@gmail.com) for feature development.
  * Refer to [Issue 24](https://github.com/bsorrentino/maven-confluence-plugin/issues/24) -  Add Issue Tracking build in report.


|Jan 20,2015 | **Release 4.1.1** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.1.1%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 69](https://github.com/bsorrentino/maven-confluence-plugin/issues/69) -  allow PRE tag in comments.
  * Refer to [Issue 73](https://github.com/bsorrentino/maven-confluence-plugin/issues/73) -  SSL configuration   - **Thanks to [nadja.blaettel](https://code.google.com/u/110324327599883714561) for providing fix**.

|Jul 9,2014 | **Release 4.1.0** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.1.0%7Cmaven-plugin)**  |
|:----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 68](https://github.com/bsorrentino/maven-confluence-plugin/issues/68) -  Support of https protocol  - **Thanks to [rmannibucau](mailto:rmannibucau@gmail.com) for providing fix**.

|May 23,2014 | **[Forge2](https://github.com/forge/core#jboss-forge-20) Addon Release 4.0.0**. | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-forge-plugin%7C4.0.0%7Cjar)**  |
|:-----------|:--------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 67](https://github.com/bsorrentino/maven-confluence-plugin/issues/67) - move forge plugin to forge2 addon [see documentation](https://github.com/bsorrentino/maven-confluence-reporting-plugin/tree/v4/confluence-forge-plugin)

|Mar 27,2014 | **Release 4.0.0** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.0.0%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 66](https://github.com/bsorrentino/maven-confluence-plugin/issues/66) - Allow to generate one child page for each goal

|Mar 8,2014 | **Release 4.0.0-beta1** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cconfluence-reporting-maven-plugin%7C4.0.0-beta1%7Cmaven-plugin)**  |
|:----------|:-------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------|

To be compliant with the Maven naming convention, from release 4 the artifactId
has been renamed from **maven-confluence-reporting-plugin** to **confluence-reporting-maven-plugin**

  * Refer to [Issue 58](https://github.com/bsorrentino/maven-confluence-plugin/issues/58) - Rename Plugin
  * See [documentation](http://bsorrentino.github.io/maven-confluence-reporting-plugin/)

|March 2014 | **Branch for release 4 has been moved to [GITHUB](https://github.com/bsorrentino/maven-confluence-reporting-plugin)** |
|:----------|:----------------------------------------------------------------------------------------------------------------------|

|Feb 4,2014 | **Release 3.4.4-rc1** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cmaven-confluence-reporting-plugin%7C3.4.4-rc1%7Cmaven-plugin)**  |
|:----------|:-----------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 63](https://github.com/bsorrentino/maven-confluence-plugin/issues/63) - encoding propagation

|Jan 17,2014 | **JBoss Forge plugin 1.2** | available for installation |
|:-----------|:----------------------------|:---------------------------|

  * Refer to [Issue 65](https://github.com/bsorrentino/maven-confluence-plugin/issues/65) - Support download page content

|Dec 17,2014 | **Release 3.4.3** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cmaven-confluence-reporting-plugin%7C3.4.3%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 62](https://github.com/bsorrentino/maven-confluence-plugin/issues/62) - Support for  exporting the  pages either in PDF and DOC

|Oct 29,2013 | **JBoss Forge plugin 1.1** | available for installation |
|:-----------|:----------------------------|:---------------------------|

  * fix upon site.xml namespace

|Oct 20,2013 | **JBoss Forge plugin 1.0** | available for installation |
|:-----------|:----------------------------|:---------------------------|

  * Refer to [Issue 52](https://github.com/bsorrentino/maven-confluence-plugin/issues/52)  -  see [GettingStarted\_with\_JBoss\_Forge](GettingStarted_with_JBoss_Forge.md)

|Sep 11,2013 | **Release 3.4.2** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cmaven-confluence-reporting-plugin%7C3.4.2%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 59](https://github.com/bsorrentino/maven-confluence-plugin/issues/59) - builds should fail on connection error (thanks to [alexander strikhalev](mailto:alexander.strikhalev@mind.com) for patch)

|Sep 6,2013 | **Release 3.4.1** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cmaven-confluence-reporting-plugin%7C3.4.1%7Cmaven-plugin)**  |
|:----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 57](https://github.com/bsorrentino/maven-confluence-plugin/issues/57) - Bug fix

|Aug 16,2013 | **Release 3.4.0** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cmaven-confluence-reporting-plugin%7C3.4.0%7Cmaven-plugin)**  |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 56](https://github.com/bsorrentino/maven-confluence-plugin/issues/56)

|Aug 1,2013 | **Release 3.4.0-beta1** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cmaven-confluence-reporting-plugin%7C3.4.0-beta1%7Cmaven-plugin)**  |
|:----------|:-------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Refer to [Issue 55](https://github.com/bsorrentino/maven-confluence-plugin/issues/55)

> From release 3.4.x the plugin provides new **goals** not longer related to **site phase** (no loger need maven-site-plugin). Such goals are :

> | confluence-reporting:**delete** |  -  | confluence-reporting:**deploy** |
|:--------------------------------|:----|:--------------------------------|

> Refer to [maven documentation](http://bsc-documentation-repo.googlecode.com/svn/trunk/maven-confluence-plugin/maven-confluence-reporting-plugin/index.html) for details.

> Please take a look to a [Getting Started](GettingStarted_3_4_X.md)

|Jun 21,2013 | **Tested with success on confluence's version 5.2-m19** |
|:-----------|:--------------------------------------------------------|

|May 28,2013 | **Release 3.3.0-rc1** | available from  **[MAVEN CENTRAL REPO](http://search.maven.org/#artifactdetails%7Corg.bsc.maven%7Cmaven-confluence-reporting-plugin%7C3.3.0-rc1%7Cmaven-plugin)**  |
|:-----------|:-----------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------|

  * fixed [Issue 41](https://github.com/bsorrentino/maven-confluence-plugin/issues/41), [Issue 53](https://github.com/bsorrentino/maven-confluence-plugin/issues/53)

|Apr 25,2013 | **Start developing [JBoss Forge](http://forge.jboss.org/) Integration** |
|:-----------|:------------------------------------------------------------------------|

  * [Issue 52](https://github.com/bsorrentino/maven-confluence-plugin/issues/52)

|Feb 15,2013 | **Release 3.3.0-beta3** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------------|:-------------------------------------------------------------------------|

  * fixed [Issue 51](https://github.com/bsorrentino/maven-confluence-plugin/issues/51) - 3.2.4 backward compatibility

|Dec 22,2012 | **Release 3.3.0-beta2** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------------|:-------------------------------------------------------------------------|

  * See [What's new in 3.3.0 release](WhatsNewInRelease3_3.md)

|Oct 12,2012 | **export code from svn to git**    |
|:-----------|:-----------------------------------|

|Oct 8,2012 | **Release 3.2.4** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [Issue 46](https://github.com/bsorrentino/maven-confluence-plugin/issues/46) - allow that custom properties refers to external resources

|Aug 22,2012 | **Release 3.2.3** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [Issue 44](https://github.com/bsorrentino/maven-confluence-plugin/issues/44) - render problem upon SCM report
  * documentation improvements

|Jul 31,2012 | **Release 3.2.2** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [Issue 39](https://github.com/bsorrentino/maven-confluence-plugin/issues/39) -Support for encrypted password

|Jul 19,2012 | **Release 3.2.1** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 37](https://github.com/bsorrentino/maven-confluence-plugin/issues/37) - Expose additional project properties to templates
  * fixed [issue 40](https://github.com/bsorrentino/maven-confluence-plugin/issues/40) - Support of custom wiki files extension

|May 25,2012 | **Release 3.2.0** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 36](https://github.com/bsorrentino/maven-confluence-plugin/issues/36) - add support of children tree

|Mar 17,2012 | **Release 3.1.5** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 35](https://github.com/bsorrentino/maven-confluence-plugin/issues/35) - add support of ${project.`*`} vars within children

|Mar 17,2012 | **Release 3.1.4** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 27](https://github.com/bsorrentino/maven-confluence-plugin/issues/27) - add label support
  * fixed [issue 33](https://github.com/bsorrentino/maven-confluence-plugin/issues/33)

|Dec 28,2011 | **Release 3.1.3** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 30](https://github.com/bsorrentino/maven-confluence-plugin/issues/30) - add parentPageTitle as build-in variable
  * fixed [issue 32](https://github.com/bsorrentino/maven-confluence-plugin/issues/32) - Add parameter to override page title.

|Dec 15,2011 | Project has been approved in [CODEHAUS FOUNDATION](http://xircles.codehaus.org/) with name **Maven Confluence Site Plugin** |
|:-----------|:----------------------------------------------------------------------------------------------------------------------------|

  * [Codehaus Site Home](http://xircles.codehaus.org/projects/maven-confluence-site)
  * [Codehaus Confluence Home](http://docs.codehaus.org/display/MVNCONFSITE)

|Dec 09,2011 | **Release 3.1.2** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 29](https://github.com/bsorrentino/maven-confluence-plugin/issues/29) - Wrong render in Confluence 4

|Nov 21,2011 | **Release 3.1.1** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 26](https://github.com/bsorrentino/maven-confluence-plugin/issues/26) - Works through proxy

|Oct 20,2011 | **Release 3.1.0** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 25](https://github.com/bsorrentino/maven-confluence-plugin/issues/25) - Confluence 4.x compatibility. Detect confluence version and use right communication protocol

|Oct 14,2011 | start working on Confluence 4.x compatibility | see [Issue 25](https://github.com/bsorrentino/maven-confluence-plugin/issues/25)  |
|:-----------|:----------------------------------------------|:---------------------------------------------------------------------------------------|

|Oct 4,2011 | **Release 3.0.4** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 23](https://github.com/bsorrentino/maven-confluence-plugin/issues/23) - add children from folder

|Sep 29,2011 | **Release 3.0.3** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 22](https://github.com/bsorrentino/maven-confluence-plugin/issues/22)

|Ago 5,2011 | **Release 3.0.2** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:----------|:-------------------|:-------------------------------------------------------------------------|

  * fixed [issue 20](https://github.com/bsorrentino/maven-confluence-plugin/issues/20)

|Jun 18,2011 | **Release 3.0.1** | available from  **[MAVEN CENTRAL REPO](http://repo2.maven.org/maven2)**  |
|:-----------|:-------------------|:-------------------------------------------------------------------------|

|Jun 16,2011 | submitted request to [SONATYPE](http://oss.sonatype.org/) to allow, artifact deploying, within central repository|
|:-----------|:-----------------------------------------------------------------------------------------------------------------|

|Apr 1,2011 | Created a branch to continue support of maven2 - the trunk now contains the 3.x code-line for support of maven3 (see [issue 15](https://github.com/bsorrentino/maven-confluence-plugin/issues/15))|
|:----------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

|Apr 1,2011 | **Release 1.3.2** | Available from [internal repository](http://maven-confluence-plugin.googlecode.com/svn/mavenrepo)  |
|:----------|:-------------------|:----------------------------------------------------------------------------------------------------|

  * fixed [issue 8](https://github.com/bsorrentino/maven-confluence-plugin/issues/8) - add attachments support

|Aug 10,2010 | **Release 1.3.1** | Available from **JAVA.NET REPOSITORY** ( see [Issue 10](https://github.com/bsorrentino/maven-confluence-plugin/issues/10) ) |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------|

  * fixed [issue 14](https://github.com/bsorrentino/maven-confluence-plugin/issues/14) - add properties to template processor

|Jun 2,2010 | **Release 1.3** | Available from **JAVA.NET REPOSITORY** ( see [Issue 10](https://github.com/bsorrentino/maven-confluence-plugin/issues/10) ) |
|:----------|:-----------------|:---------------------------------------------------------------------------------------------------------------------------------|

  * fixed [issue 11](https://github.com/bsorrentino/maven-confluence-plugin/issues/11) - integrate the plugin documentation in site phase
  * fixed [issue 12](https://github.com/bsorrentino/maven-confluence-plugin/issues/12)

|May 20,2010 | **Release 1.2.1** | Available from **JAVA.NET REPOSITORY** ( see [Issue 10](https://github.com/bsorrentino/maven-confluence-plugin/issues/10) ) |
|:-----------|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------|

  * fixed [issue 9](https://github.com/bsorrentino/maven-confluence-plugin/issues/9) - fix encoding text
  * fixed [issue 10](https://github.com/bsorrentino/maven-confluence-plugin/issues/10) - porting on java net repo

|Mar 2010|**Release 1.2 is out** this release allow to add child pages. (see [Issue 7](https://github.com/bsorrentino/maven-confluence-plugin/issues/7)) |
|:-------|:---------------------------------------------------------------------------------------------------------------------------------------------------|
|Dec 2009| **Release 1.1 is out** this release allow to document both projects and plugin |
|Oct 2009| This plugin has been accepted and published on [Atlassian plugin center](https://plugins.atlassian.com/plugin/home). Open page and search for **maven-confluence-plugin** |
