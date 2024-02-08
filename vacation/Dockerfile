FROM openjdk:21
COPY . /usr/src/vacation
WORKDIR /usr/src/vacation
CMD ["./gradlew", "build"]
COPY /build/libs/vacation-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar","/app.jar"]