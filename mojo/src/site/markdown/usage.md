## Usage

### Maven Repository

From Release 3.0.1 this plugin is available from [MAVEN CENTRAL REPO](http://repo2.maven.org/maven2/)
If you would like download & test the latest SNAPSHOT, includes the following repository declaration in your POM

```xml

<pluginRepositories>

    <!-- IF YOU WANT STAY TUNED ON UPDATE REMOVE COMMENT -->
    <pluginRepository>
        <id>sonatype-repo</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </pluginRepository>

</pluginRepositories>

```


### Use Site Descriptor

```xml

<configuration>
    <endPoint>${confluence.home}/rest/api</endPoint>
    <spaceKey>DOCS</spaceKey>
    <serverId>server_id_configured_in_settings_xml</serverId>
    <parentPageTitle>Home</parentPageTitle><!-- PARENT PAGE IN THE GIVEN SPACE -->
    <wikiFilesExt>.confluence</wikiFilesExt>
    <siteDescriptor>${basedir}/src/site/confluence/site.yaml</siteDescriptor>
    <failOnError>true</failOnError>
</configuration>

```
###  Inject custom properties within template

```xml
 <configuration>
    <endPoint>${confluence.home}/rest/api</endPoint>
    <spaceKey>DOCS</spaceKey>
    <serverId>server_id_configured_in_settings_xml</serverId>
    <parentPageTitle>Home</parentPageTitle><!-- PARENT PAGE IN THE GIVEN SPACE -->
    <title>custom_title</title><!-- PAGE TITLE (default ${project.build.finalName})  - SINCE 3.1.3 -->

    <properties>

       <scm-url>${project.scm.url}</scm-url>
       <version>${project.version}</version>
       <build>${build.number}</build>

        <myprop>classpath:plugin-report.properties</myprop><!-- SINCE 3.2.4 -->
        <htmlpage>file://${basedir}/src/main/resources/confluence.html</htmlpage><!-- SINCE 3.2.4 -->

     </properties>

 </configuration>
```

### Maven tip : Use multiple executions

```xml
<!-- shared configuration -->
 <configuration>
    <endPoint>${confluence.home}/rest/api</endPoint>
    <spaceKey>DOCS</spaceKey>
    <serverId>server_id_configured_in_settings_xml</serverId>
    <title>custom_title</title><!-- PAGE TITLE (default ${project.build.finalName})  - SINCE 3.1.3 -->
    <wikiFilesExt>.confluence</wikiFilesExt>
    <failOnError>true</failOnError>
</configuration>
<executions>
    <!--
    mvn confluence-reporting:deploy@topic1
    mvn confluence-reporting:delete@topic1
    -->
   <execution>
        <id>topic1</id>
        <goals>
            <goal>deploy</goal>
            <goal>delete</goal>
        </goals>
        <configuration>
            <parentPageTitle>Home1</parentPageTitle>
            <siteDescriptor>${basedir}/src/site/confluence/topic1.yaml</siteDescriptor>
        </configuration>
    </execution>    
    <!--
    mvn confluence-reporting:deploy@topic2
    mvn confluence-reporting:delete@topic2
    -->
   <execution>
        <id>topic2</id>
        <goals>
            <goal>deploy</goal>
            <goal>delete</goal>
        </goals>
        <configuration>
            <parentPageTitle>Home2</parentPageTitle>
            <siteDescriptor>${basedir}/src/site/confluence/topic2.yaml</siteDescriptor>
        </configuration>
    </execution>    
</executions>

```

### Change output Locale
By default system Locale is used to generate the output. It can be changed by `locale` attribute.

Given value will be parsed according to the following [specification](https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html).

```xml
 <configuration>
    <locale>en_US</locale>
 </configuration>
```

## Authentication Tip

Put yours confluence credential within `settings.xml` as server.

** Note: <u>Personal Access Token</u>**
> When required, **instead of password** you've to specify your **personal access token**

### Template
```xml
    <server>
        <id>my_confluence_server</id>
        <username>my_user</username>
        <password>my_password or my_personal_access_token</password>
    </server>
```

Take note that also [maven encryption](http://maven.apache.org/guides/mini/guide-encryption.html#How_to_encrypt_server_passwords) is supported

### Specifying additional HTTP headers

If you are in a corporate environment **where server doesn't accept the basic auth scheme for tokens**  you can specify required custom authentication headers following the [standard maven approach](https://maven.apache.org/guides/mini/guide-http-settings.html#HTTP_Headers) .

#### Example setting Bearer Scheme
```xml
    <server>
      <id>confluence</id>
      <configuration>
        <httpHeaders>
          <property>
            <name>Authentication</name>
            <value>Bearer xxxxxxx</value>
          </property>
        </httpHeaders>
      </configuration>
    </server>
```

## Use template variables

By default the plugin use an internal template to generate confluence page. You can customize the generated page creating a personal template into  folder `${basedir}/src/site/confluence` named `template.wiki`.
The template can include all valid confluence contents plus the following built-in variables

### Built-In template variables

Variable | Description
-----------------|-----------------
 `${project.summary}`| project summary
 `${project.team}`| project team information
 `${project.scmManager}`   | scm information
 `${project.dependencies}` | dependencies    
 `${artifactId}`          | artifactId      
 `${version}`            | version         
 `${gitlog.jiraIssues}`     | list of JIRA issuses, extracted from gitlog since start tag
 `${gitlog.sinceTagName}`   | name of version tag to start extract JIRA issues   
 `${home.title}`            | title of home page
 `${page.title}`           | title of the current page
 `${pageTitle}`            | title of home page **DEPRECATED** (use `home.title` instead)
 `${childTitle}`           | title of the current child page **DEPRECATED** (use `page.title` instead)

#### Only available for plugin documentation

Variable | Description
-----------------|-----------------
`${plugin.summary}`| plugin summary **DEPRECATED** (use `project.summary` instead)
`${plugin.goals}`| plugin goals


### Images

* How to refer to an image
> ``` !${home.title}^image_name! ```

* How to refer to an child's image within child page
> ``` !${page.title}^image_name! ```

### Attachments

* How to refer to an attachment
> ``` [${home.title}^attachment_name] ```

* How to refer to an child's attachment within child page
> ``` [${page.title}^attachment_name] ```

### Tips & Tricks

In order to **escape a variable** so that in the same time it not will be translated and not will be considered a confluence's macro, use the syntax below
```
$\{varName\}
```


### Git log template variables

Main idea is automated creating of release notes with list of resolved JIRA issues utilizing integration between JIRA and Confluence.

#### Prerequisites:

* Include JIRA issue key in git commit message
* Using git as SCM and using version tags in git

#### Git log configuration options

* `gitLogJiraIssuesEnable`
>  Set it to true for enabling substitution of ```${gitlog.jiraIssues}``` build-in variable.  Default value is  false.

* `gitLogSinceTagName`
> Parse git log commits since last occurrence of specified tag name.

* `gitLogUntilTagName`
> Parse git log commits until first occurrence of specified tag name.

* `gitLogCalculateRuleForSinceTagName`

> If specified, plugin will try to calculate and replace actual gitLogSinceTagName value based on current project version ```${project.version}``` and provided rule.

> Possible values are:
>
>
> * `NO_RULE`.
> * `CURRENT_MAJOR_VERSION`. For example 1.2.3 will be resolved to 1.0.0
> * `CURRENT_MINOR_VERSION`. For example 1.2.3 will be resolved to 1.2.0
> * `LATEST_RELEASE_VERSION`. For example, if latest known version from version tags is 1.0.1 and current artifact (not released) version is 2.0.0, it will be resolved to 1.0.1

* `gitLogJiraProjectKeyList`
 > JIRA projects keys to extract issues from gitlog. By default it will try extract all strings that match pattern (A-Za-z+)-\d+

* `gitLogTagNamesPattern`
> The pattern to filter out tagName. Can be used for filter only version tags

* `gitLogGroupByVersions`
> Enable grouping by versions tag


#### Sample produced output of `${gitlog.jiraIssues}`  with  `gitLogGroupByVersions=true`

![${childTitle}^gitlog-sample02.png](./images/gitlog-sample02.png "Sample Output")


### Wiki Template example

```
{info:title=Useful Information}
This page has been generated by [maven-confluence-plugin|https://github.com/bsorrentino/maven-confluence-plugin]
{info}

{toc}

h1. Introduction

{panel}
project description
{panel}

h1. Usage

{panel}
How to use the project
{panel}

${project.summary}


${project.team}


${project.scmManager}


${project.dependencies}

```

