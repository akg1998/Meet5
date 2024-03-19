FROM openjdk:17-oracle

WORKDIR /app

COPY target/spring_rest_docker_user_service.jar /app/

EXPOSE 8080

# Command to run the microservice
CMD ["java", "-jar", "spring_rest_docker_user_service.jar"]