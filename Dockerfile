# Step 1: Build the application
FROM maven:3-eclipse-temurin-17 AS build
COPY . .
# Is line se Docker pehle tumhare sub-folder ke andar jayega
WORKDIR /currencyconverter
RUN mvn clean package -DskipTests

# Step 2: Run the application
FROM eclipse-temurin:17-jre
# Yahan bhi path thoda badal diya hai sub-folder ke hisaab se
COPY --from=build /currencyconverter/target/currencyconverter-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
