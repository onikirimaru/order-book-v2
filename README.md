# order-book-v2

# How to build and run the application

This project requires:
- JDK 21
- Maven (maven wrapper provided)
- Spring Web
- Spring Kafka (not yet completed)
- Docker
- lombok
- vavr

## Run app as docker container

To build docker run next command in a shell window:
```
./mvnw jib:dockerBuild
```

Once this finishes, docker image will be stored in local docker repository

To run docker image just run in same shell window:
```
docker run --rm data/order-book 
```
So application logs will be displayed in terminal.

To finish the application you will need to kill the docker container:
```
docker ps
```
To display container names, look for data/order-book and then, container should be killed by running:

```
docker rm <container id> 
```

## Run app as shell java application
Application can be run as well as any Java applation, just build it running:

```
./mvnw package
```

And run the app by running:

```
java -jar target/order-book-0.0.1-SNAPSHOT.jar
```

To stop application push CTRL+C


# Some notes and 

As first candle tends to be incomplete (only a portion of the data gets collected), it is ignored (a convenient info message is added to the log).

There are some bugs and scenarios pending to be fixed, and they will be during next days and versions (for instance, to save memory we are removing ticks related to already dumped candles, as soon as they are generated, but this may cause an issue if ticks are removed before next candle gets generated.

# 