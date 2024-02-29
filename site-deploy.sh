#!/bin/sh

mvn site:site --projects mojo
mvn javadoc:javadoc --projects core -DreportOutputDirectory=$(pwd)/plugin-reporting/target/site/core/apidocs
mvn -Preport --projects mojo site
