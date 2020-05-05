#!/bin/sh

mvn site:site --projects maven-confluence-reporting-plugin
mvn javadoc:javadoc --projects maven-confluence-core -DreportOutputDirectory=$(pwd)/maven-confluence-reporting-plugin/target/site/core/apidocs