FROM maven:3.9-eclipse-temurin-11
WORKDIR /app

# Copy Maven config and source files
COPY pom.xml .
COPY src ./src

# Pre-compile the client application
RUN mvn clean compile

# The client needs STDIN to remain interactive
CMD ["mvn", "exec:java", "-Dexec.mainClass=com.smarthr.client.HRClient"]
