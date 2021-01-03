#!/bin/sh

mvn site:site --projects plugin-reporting
mvn javadoc:javadoc --projects core -DreportOutputDirectory=$(pwd)/plugin-reporting/target/site/core/apidocs
mvn -Preport --projects plugin-reporting site
