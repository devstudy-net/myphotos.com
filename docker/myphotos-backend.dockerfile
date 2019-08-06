# Build:
# docker build -t devstudy/myphotos-backend:1.0 -f docker/myphotos-backend.dockerfile .
#
FROM devstudy/wildfly:10.1.0

MAINTAINER devstudy.net

ARG WILDFLY_HOME=/opt/wildfly

ARG POSTGRES_JDBC_DRIVER_VERSION=42.1.1
ARG POSTGRES_JDBC_DRIVER_JAR=postgresql-${POSTGRES_JDBC_DRIVER_VERSION}.jar
ARG POSTGRES_JDBC_DRIVER_DOWNLOAD_LINK=http://central.maven.org/maven2/org/postgresql/postgresql/${POSTGRES_JDBC_DRIVER_VERSION}/${POSTGRES_JDBC_DRIVER_JAR}

# Customize Wildfly
ADD docker/wildfly/standalone-full.xml ${WILDFLY_HOME}/standalone/configuration/
ADD docker/wildfly/myphotos.properties ${WILDFLY_HOME}/
ADD docker/wildfly/org ${WILDFLY_HOME}/modules/org
ADD docker/wildfly/wait-for-service-up.sh /wait-for-service-up.sh

# Deploy project 
ADD myphotos/myphotos-ear/target/myphotos-ear-1.0.ear ${WILDFLY_HOME}/standalone/deployments/
ADD myphotos-mdb/myphotos-mdb-ear/target/myphotos-mdb-ear-1.0.ear ${WILDFLY_HOME}/standalone/deployments/
ADD myphotos-remote-project/target/myphotos-remote-project-1.0.war ${WILDFLY_HOME}/standalone/deployments/

RUN cd $WILDFLY_HOME && \
    # Display postgresql jdbc driver download link
    echo Download from ${POSTGRES_JDBC_DRIVER_DOWNLOAD_LINK} && \
    wget ${POSTGRES_JDBC_DRIVER_DOWNLOAD_LINK} && \
    # Display $WILDFLY_HOME
    ls -lh && \
    mv -f ./${POSTGRES_JDBC_DRIVER_JAR} ./modules/org/postgresql/jdbc/main/postgresql.jar && \
    chmod +x /wait-for-service-up.sh
