mvn -pl maven-confluence-reporting-plugin \
confluence-reporting:deploy \
-P confluence \
-Dconfluence.serverId=server \
-Dconfluence.endPoint=https://localhost:8090/confluence/rest/api \
-Dconfluence.spaceKey='~bsorrentino' \
-Dconfluence.parentPage='Bartolo Sorrentino’s Home'
