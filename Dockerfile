
FROM openjdk:17-slim

ADD target/kikeriki.jar /kikeriki.jar
CMD ["java", "-jar", "/kikeriki.jar"]
EXPOSE 8080