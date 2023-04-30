FROM adoptopenjdk/openjdk11:latest
COPY target/usermanagement-0.0.1.jar usermanagement-0.0.1.jar
ENTRYPOINT ["java","-Dspring.profiles.active=local", "-jar", "usermanagement-0.0.1.jar"]