FROM maven:3.9-eclipse-temurin-11
WORKDIR /app

# Copy Maven config and source files
COPY pom.xml .
COPY src ./src

# Pre-compile the application to save startup time
RUN mvn clean compile

# the default command will run the Server
CMD ["mvn", "exec:java", "-Dexec.mainClass=com.smarthr.rmi.HRServer"]
