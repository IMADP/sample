FROM openjdk:11-jre-slim-buster

# Add the security policies
ADD lib/aspectjweaver-1.9.6.jar aspectj-weaver.jar
ADD lib/spring-instrument-5.3.8.jar spring-instrument.jar

VOLUME /tmp
ADD sample-api-1.0.jar app.jar
RUN sh -c 'touch /app.jar'
CMD ["java","-Djava.security.egd=file:/dev/./urandom", "-javaagent:/aspectj-weaver.jar", "-javaagent:/spring-instrument.jar", "-jar","/app.jar"]

EXPOSE 8080