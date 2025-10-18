FROM openjdk:17-alpine
WORKDIR /opt
COPY target/*.jar /opt/app.jar
# Use exec form to ensure proper signal handling
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
