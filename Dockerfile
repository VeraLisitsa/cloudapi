FROM openjdk:17-alpine

EXPOSE 8080

COPY target/cloud_api_v3-0.0.1-SNAPSHOT.jar CloudApi.jar

CMD ["java", "-jar", "CloudApi.jar"]