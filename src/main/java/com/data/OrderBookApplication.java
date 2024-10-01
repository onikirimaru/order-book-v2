package com.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@EnableScheduling
@SpringBootApplication
public class OrderBookApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(OrderBookApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        addSignalHandler();
        Thread.currentThread().join();
    }

    private static void addSignalHandler() {
        SignalHandler signalHandler = new SignalHandlerImpl();
        Signal.handle(new Signal("TERM"), signalHandler);
        Signal.handle(new Signal("INT"), signalHandler);
    }

    private static class SignalHandlerImpl implements SignalHandler {

        @Override
        public void handle(Signal signal) {
            System.exit(-1);
        }
    }
}
