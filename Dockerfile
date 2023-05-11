FROM eclipse-temurin:19-jre-alpine
COPY /target/joboffer.jar /joboffers.jar
ENTRYPOINT ["java","-jar","/joboffers.jar"]