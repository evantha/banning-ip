FROM openjdk:14-alpine
COPY target/*.jar ./
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "myapp-0.1.jar"]