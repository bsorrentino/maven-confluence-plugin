FROM cptactionhank/atlassian-confluence:latest

USER root:root

RUN $JAVA_HOME/bin/keytool \
  -genkeypair  \
  -keysize 2048 \
  -alias tomcat \
  -keyalg RSA -sigalg SHA256withRSA \
  -dname "CN=bsorrentino,OU=development,O=soulsoftware,L=City,S=State,C=IT" \
  -keypass password -storepass password

#USER daemon:daemon

#CONF_INSTALL=/opt/atlassian/confluence

COPY server.xml /opt/atlassian/confluence/conf
COPY web.xml /opt/atlassian/confluence/confluence/WEB-INF

EXPOSE 8443

#CMD ["/opt/atlassian/confluence/bin/catalina.sh", "run"]
