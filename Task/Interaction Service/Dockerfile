FROM openjdk:17-oracle

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar /app/

EXPOSE 9090

# Command to run the microservice
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]