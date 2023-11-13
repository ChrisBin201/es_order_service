
### STAGE 1: MAVEN BUILD ###
FROM maven:3.8.5-openjdk-11 AS builders
# create app directory in images and copies pom.xml into it
COPY pom.xml /app/
# copy all required dependencies into one layer
#RUN mvn -B dependency:resolve dependency:resolve-plugins
# copies source code into the app directort in image
COPY src /app/src
# sets app as the directory into the app
WORKDIR /app/
# run mvn
# update db with liquibase
RUN mvn clean compile -P staging
RUN mvn liquibase:update
RUN mvn clean install -P staging


### STAGE 2: DEPLOY APPLICATION
FROM openjdk:11.0.14-jdk
WORKDIR /app
COPY --from=builders /app/target/product_service-0.0.1.jar /app/
ENTRYPOINT ["java","-jar", "product_service-0.0.1.jar"]


