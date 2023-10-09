# Stage 1: Build the Maven project
FROM maven:3.8.1-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

# Stage 2: Create the final Docker image with Tomcat
FROM tomcat:9.0-jdk11-corretto-al2
RUN rm -rf /usr/local/tomcat/webapps/ROOT
RUN rm -rf /usr/local/tomcat/webapps/examples
COPY --from=build /app/target/home.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]