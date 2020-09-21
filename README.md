# IMDB TCP Movie Search

This application exposes an IMDB movie search API via
TCP.

## Building and Running

### Jar

To build the JAR, run `./gradlew shadowJar`. Then, you
can run it by using `java -jar build/libs/imdb-tcp-movie-search-${VERSION}-all.jar`,
where `VERSION` is the version specified in the gradle
configuration (probably 1.0-SNAPSHOT).

The server will be running on port 8000 (in the real
world this would be customizable).

### Docker

If you prefer to run things via Docker, you can build
the image by using the command `docker build -t ${TAG} .`.
`TAG` can be whatever you want (given it's a valid docker tag).

With that done, simply run the server by running
`docker run -it -p ${PORT}:8000 ${TAG}`. You can use
whatever `PORT` value you wish.

## Testing the server

The source includes a (very) simple client (com.tomchinsky.imdb.Client).
The class defines a client that will connect on port 8000 and allow you
to submit queries to the server
