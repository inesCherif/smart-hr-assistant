FROM maven:3.9-eclipse-temurin-11
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean compile
CMD ["mvn", "exec:java", "-Dexec.mainClass=com.smarthr.gateway.WebGateway"]
