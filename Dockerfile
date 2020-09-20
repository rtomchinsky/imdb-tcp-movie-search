FROM adoptopenjdk:8-jdk-openj9 as builder
COPY . .
RUN ./gradlew clean shadowJar

FROM adoptopenjdk:8-jre-openj9
COPY --from=builder build/libs/imdb-tcp-movie-search-*-all.jar imdb-tcp-movie-search.jar
EXPOSE 8000
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar imdb-tcp-movie-search.jar