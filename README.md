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
./mvnw clean package jib:dockerBuild
```

Once this finishes, docker image will be stored in local docker repository

To run docker image just run in same shell window:
```
docker run --rm data/order-book 
```
So application logs will be displayed in terminal (check below to display candle ticks as Kafka events).

To finish the application you will need to kill the docker container:
```
docker ps
```
To display container names, look for data/order-book and then, container should be killed by running:

```
docker rm <container id> 
```

To generate candle ticks as Kafka events, you need a Kafka cluster available, to create a simple one just run:

```
docker run -p 9094:9092 apache/kafka:3.8.0
```
 
And then run the application image using next command
```
docker run --rm data/order-book -e ORDERBOOK_CANDLE_OUTPUT=kafka
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

This will log candles in console, if we want to produce cancle ticks as events:

```
export ORDERBOOK_CANDLE_OUTPUT=kafka
java -D ORDERBOOK_CANDLE_OUTPUT=kafka -jar target/order-book-0.0.1-SNAPSHOT.jar 
```

To stop application push CTRL+C


# Some notes about implementation and current version

Implementation is not yet completed as the Order Book levels are not correctly updated!!!!!!  

As first candle tends to be incomplete (only a portion of the data gets collected), it is ignored (a convenient info message is added to the log).

There are some bugs and scenarios pending to be fixed, and they will be during next days and versions (for instance, to save memory we are removing ticks related to already dumped candles, as soon as they are generated, but this may cause an issue if ticks are removed before next candle gets generated.

# 