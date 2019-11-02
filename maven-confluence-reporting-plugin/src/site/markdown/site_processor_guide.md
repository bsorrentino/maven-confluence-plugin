
# Introduction

From **release 6.7** we have the possibility to pre-process the site descriptor file before execute it to coordinate its deployment. Such processor (or _template engine_) can be plugged into processing using a module (i.e. _java library_) containing processor implementation that is published using [Java Service Provide Interface (SPI)](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html) specification.

## Site processor usage

### Configure a site processor

To plug a **site processor** you have to **declare as plugin's dependency** the library containing it.
> Currently a processor based on [Freemarker](https://freemarker.apache.org/) template engine is already available trought the module **maven-confluence-processor-freemarker**

#### Example below declare freemarker as site processor

```xml

<plugin>
    <groupId>org.bsc.maven</groupId>
    <artifactId>confluence-reporting-maven-plugin</artifactId>
    <dependencies>
        <!-- Plug the freemarker processor -->
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>maven-confluence-processor-freemarker</artifactId>
            <version>${version}</version>
            <scope>runtime</scope>
        </dependency>

        <configuration>
            <wikiFilesExt>.confluence</wikiFilesExt>
            <siteDescriptor>${basedir}/src/site/confluence/site_to_process.yml</siteDescriptor>
            <failOnError>true</failOnError>
        </configuration>
    </dependencies>
</plugin>

```

### Use  the markup language in site definition

After declared a site processor in plugin's configuration we can start to use the related markup language in site's definition

#### Example below use freemarker markup language in site definition

```yaml
#freemaker
home:
  name: "issue202"
  uri: issue202.md
  attachments:
<#if Files.exists(Paths.get('src/site/confluence/issue202/images/dashboard.png')) == true>
    - name: "dashboard.png"
      uri: images/dashboard.png
      contentType: "image/png"
      version: 1
      comment: image
</#if>
<#if Files.exists(Paths.get('src/site/confluence/issue202/images/unknown.png')) == true>
    - name: "unknown.png"
      uri: images/dashboard.png
      contentType: "image/png"
      version: 1
      comment: unknown image
</#if>

```


## Implement a new 'site processor' service

To implement a new **site processor service** we have to follow steps below:

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

### 3. Implement the interface `org.bsc.confluence.preprocessor.SitePocessorService`       

```java
public interface SitePocessorService {
    /**
     * name of Preprocessor service
     *
     */
    String getName();
    /**
     * Handles preprocessing of the input using a markup library
     *
     * Variables are added to the markup model
     */
    CompletableFuture<String> process(String input, Map<String, Object> variables);
}

```

### 4. Publish implementation following [SPI](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html) specification

The [SPI](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html) specification **publish a service creating a mapping file a mapping file in a specially named directory `META-INF/services`**. The name of the file is the name of the SPI class being subclassed, and the file contains the names of the new subclasses of that SPI abstract class (see documentation for more details).

#### META-INF/services generator
However there is a great library that extremely simplify publishing of SPI service named [META-INF/services generator](http://metainf-services.kohsuke.org/) that use behind the scene a **java annotation processor** that automatically generates the required mapping file using a java annotation `@MetaInfServices` as shown below

```java
@MetaInfServices(SitePocessorService.class)
public class PreprocessorImpl implements SitePocessorService {

}
```
