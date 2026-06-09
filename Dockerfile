# Step 1: Build the application
FROM maven:3-eclipse-temurin-26 AS build
COPY . .
WORKDIR /currencyconverter
RUN mvn clean package -DskipTests

# Step 2: Run the application
FROM eclipse-temurin:26-jre
COPY --from=build /currencyconverter/target/currencyconverter-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
