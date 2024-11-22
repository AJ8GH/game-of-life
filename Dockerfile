FROM maven:3.8.6-amazoncorretto-17
WORKDIR /app
COPY . /app
RUN mvn -B package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "/app/launcher/target/gol.jar"]
