FROM amazoncorretto:21-alpine-jdk

RUN mkdir /app

COPY build/libs/accounting-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

CMD ["java", "-jar", "app.jar"]