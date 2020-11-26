# Runner configuration

The repository configuration is meant to specify where to get/download the specification from.

```xml
<runner>
    <name/>
    <forkCount/>
    <cmdLineTemplate/>
    <mainClass/>
    <serverName/>
    <serverPort/>
    <secured/>
    <classpaths>
        <classpath/>
    </classpaths>
</runner>
```

## Nodes details 

### runner

| Element  | Description | Type |
|---|---|---|
| **name** | The name of this runner. It will be used for assigning the runner to a [repository](./repository-ref.html) . | String |
| **forkCount** | | Integer |
| **cmdLineTemplate** | The command line that will be launched for running the test. <br/> eg: `java -cp {classpaths} {mainClass} {inputPath} {outputPath} -r {repository} -f {fixtureFactory}` | String |
| **mainClass** | | String |
| **serverName** | | String |
| **serverPort** | | Integer |
| **secured** | | boolean |
| **classpaths** | | List of `<classpath>` . Each **classpath** is a string. |
| **includeProjectClasspath** | Include in the `{classpath}` value the current project runtime classpath. Defaults to **false**. | boolean |

### cmdLineTemplate

The following template parameters can be used and will be changed at execution time.

* **`{classpaths}`**: will be replaced by the classpath specified in the runner, in addition to the project classpath if **includeProjectClasspath** is set.
* **`{mainClass}`**: will be replaced by the runner's **mainClass**.
* **`{inputPath}`**: will be replaced by the specification to test. (**MANDATORY**)
* **`{outputPath}`**: will be replaced by the output path. (**MANDATORY**)
* **`{repository}`**: will be replaced by the repository information comming from the associated repository.
* **`{fixtureFactory}`**: will be replaced by the **systemUnderDevelopment** specified in the plugin configuation.

Note:

> In this cmdLineTemplate, the template parameters are written without the dollar sign `$`. That is not the case for the same cmdLineTemplate you would put in the wiki plugin configuration.
> * Confluence example: `java -cp ${classpaths} ${mainClass} ${inputPath} ${outputPath} -r ${repository} -f ${fixtureFactory}`
> * Maven equivalent: `java -cp {classpaths} {mainClass} {inputPath} {outputPath} -r {repository} -f {fixtureFactory}`
>
> For a compatibility reason, we will try to use the `${key}` if we find it. However the risk for you is that the string might be replaced by maven.

## Default runner configuration

Here is the default runner configuration 

```xml
    <runner>
        <name>GP Core ${greenpepper.version}</name>
        <forkCount>${maven.greenpepper.forkCount}</forkCount>
        <cmdLineTemplate>${maven.greenpepper.jvm} -cp {classpaths} {mainClass} {inputPath} {outputPath} -r {repository} -f {fixtureFactory}</cmdLineTemplate>
        <mainClass>com.greenpepper.runner.Main</mainClass>
        <includeProjectClasspath>true</includeProjectClasspath>
    </runner>
```

Note: 

> Refer to the maven greenpepper:run goal documentation to get (or set) the value of `maven.greenpepper.forkCount` and `maven.greenpepper.jvm`.
