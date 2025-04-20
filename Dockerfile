FROM openjdk:21
WORKDIR /app
VOLUME /tmp
EXPOSE 8080
ADD ./target/igniteshop-0.0.1-SNAPSHOT.jar igniteshop.jar
ENTRYPOINT ["java", "-jar", "igniteshop.jar"]