FROM eclipse-temurin:17-jre-alpine
COPY /target/joboffer.jar /joboffers.jar
ENTRYPOINT ["java","-jar","/joboffers.jar"]