FROM openjdk:21
COPY . /usr/src/working-time-measurement
WORKDIR /usr/src/working-time-measurement
CMD ["./gradlew", "build"]
COPY /build/libs/working-time-measurement-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar"]