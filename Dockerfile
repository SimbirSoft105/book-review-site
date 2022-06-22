FROM adoptopenjdk/openjdk8

ARG JAR_FILE=book-review-site.jar

WORKDIR /app

COPY ./target/${JAR_FILE} /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]