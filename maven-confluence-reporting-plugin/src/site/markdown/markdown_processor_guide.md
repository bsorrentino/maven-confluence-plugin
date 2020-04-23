# Introduction

From **release 6.8** we have the possibility to plug different **Markdown Processor** so you can add extra features other than which provided built-in. Such processor can be plugged into processing using a module (i.e. _java library_) containing processor implementation that is published using [Java Service Provide Interface (SPI)](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html) specification.

## Markdown processor usage

Currently the available Markdown processors compliant with plugin are:

1. [Pegdown](https://github.com/sirthias/pegdown)
    > This implementation is the default one. Since it has been **DEPRECATED** we encourage to plug the new **Commonmark** implementation because in the next major release pegdown will be removed
1. [Commonmark](https://github.com/atlassian/commonmark-java)
   > Is the new implementation available from plugin version `6.8`
                                                                 
### Configure a new Markdown Processor

To plug a **markdown processor** you have to declare library **as plugin's dependency**. 

#### Plug `commonmark` implementation
```xml

<plugin>
    <groupId>org.bsc.maven</groupId>
    <artifactId>confluence-reporting-maven-plugin</artifactId>
    <version>${confluence.plugin.version}</version>
    <dependencies>
        <dependency>
            <groupIdorg.bsc.maven</groupId>
            <artifactId>confluence-markdown-processor-commonmark</artifactId>
            <version>${confluence.plugin.version}</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
    <configuration>
        <wikiFilesExt>.confluence</wikiFilesExt>
        <siteDescriptor>${basedir}/src/site/confluence/site_to_process.yml</siteDescriptor>
        <failOnError>true</failOnError>
        <markdownProcessor>
            <name>commonmark</name>
        </markdownProcessor>
    </configuration>
</plugin>

```

## Implement a new 'Markdown processor' service

To implement a new **Markdown processor service** we have to follow steps below:

### 1. Create a new Project

Create a simple java library project maven compliant

### 2. Add dependency to **maven-confluence-core** module      

```xml
    <dependency>
        <groupId>org.bsc.maven</groupId>
        <artifactId>maven-confluence-core</artifactId>
        <version>${version}</version>
    </dependency>
```

### 3. Implement the interface `org.bsc.markdown.MarkdownProcessor`       

```java
public interface MarkdownProcessor {

    /**
     * markdown processor identifier used to choose which procerror use at run-time
     *
     * @return identifier
     */
    String getName();

    /**
     *
     * @param site
     * @param child
     * @param page
     * @param content
     * @param homePageTitle
     * @return
     * @throws IOException
     */
    String processMarkdown(
            final Site site,
            final Site.Page child,
            final Optional<ConfluenceService.Model.Page> page,
            final String content,
            final String homePageTitle) throws IOException;

```

### 4. Publish implementation following [SPI](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html) specification

The [SPI](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html) specification **publish a service creating a mapping file a mapping file in a specially named directory `META-INF/services`**. The name of the file is the name of the SPI class being subclassed, and the file contains the names of the new subclasses of that SPI abstract class (see documentation for more details).

#### META-INF/services generator
However there is a great library that extremely simplify publishing of SPI service named [META-INF/services generator](http://metainf-services.kohsuke.org/) that use behind the scene a **java annotation processor** that automatically generates the required mapping file using a java annotation `@MetaInfServices` as shown below

```java
@MetaInfServices(MarkdownProcessor.class)
public class CommonmarkMarkdownProcessorImpl implements MarkdownProcessor {

}
```
