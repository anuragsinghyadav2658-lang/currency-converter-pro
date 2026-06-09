# Step 1: Build the application using Eclipse Temurin JDK
FROM maven:3-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the application using Eclipse Temurin JRE (Lightweight)
FROM eclipse-temurin:17-jre
COPY --from=build /target/currencyconverter-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
