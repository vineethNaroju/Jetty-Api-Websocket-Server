package org.example;

import org.junit.Before;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class Main {


    public void print(Object o) {
        System.out.println(new Date() + "|" + o);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");

        JettyServer jettyServer = new JettyServer();

        jettyServer.start();

        CountDownLatch latch = new CountDownLatch(1);

        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}