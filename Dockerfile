FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/*.jar app.jar

ENV PORT=3000

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "app.jar"]
