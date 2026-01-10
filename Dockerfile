# ================================
# 1️⃣ BUILD STAGE (Maven inside Docker)
# ================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# copy pom.xml first (dependency cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# copy source code
COPY src ./src

# build spring boot jar
RUN mvn clean package -DskipTests


# ================================
# 2️⃣ RUN STAGE (Only Java + JAR)
# ================================
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
