FROM maven:3.6.3 AS maven

WORKDIR /app
COPY . /app

RUN mvn package -Dmaven.test.skip


FROM adoptopenjdk/openjdk8

ARG JAR_FILE=book-review-site.jar

WORKDIR /app

COPY --from=maven /app/target/${JAR_FILE} /app/

ENTRYPOINT ["java","-jar","book-review-site.jar"]