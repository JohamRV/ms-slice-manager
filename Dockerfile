FROM openjdk:11
ARG JAR_FILE=target/*.jar
EXPOSE 8083
COPY ${JAR_FILE} ms-slice-manager.jar
ENTRYPOINT ["java","-jar","/ms-slice-manager.jar"]