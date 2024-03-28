
FROM openjdk:21-slim

ADD target/kikeriki.jar /kikeriki.jar
CMD ["java", "-jar", "/kikeriki.jar"]
EXPOSE 8080