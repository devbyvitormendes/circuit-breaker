FROM openjdk:21-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} foodtosave.jar
ENTRYPOINT ["java","-jar","/foodtosave.jar"]