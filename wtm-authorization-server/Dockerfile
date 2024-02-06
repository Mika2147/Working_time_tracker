FROM openjdk:21
COPY . /usr/src/wtm-authorization-server
WORKDIR /usr/src/wtm-authorization-server
CMD ["./gradlew", "build"]
COPY /build/libs/wtm-authorization-server-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar","/app.jar"]