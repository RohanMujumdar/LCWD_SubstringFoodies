# Use OpenJDK image
FROM openjdk:23-jdk-slim

# Add a volume (optional)
VOLUME /tmp

# Copy the built JAR file into the container
# With the help of below statement dockerdemoapp-0.0.1-SNAPSHOT.jar file will get copied
# into app.jar file.
COPY target/lcwd_substringFoodies-0.0.1-SNAPSHOT.jar app.jar

# To run the app
ENTRYPOINT ["java", "-jar", "/app.jar"]
