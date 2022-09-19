package com.craftofprogramming.monitorwaitnotify;

public class WaitNotifyMain {
    int counter;
    boolean isAvailable;

    public static void main(String[] args) throws InterruptedException {
        WaitNotifyMain notifyMain = new WaitNotifyMain();
        Thread consumer = new Thread(()->{
            while(true) {
                try {
                    sleep();
                    System.out.printf("%n%s read val=%d",
                            Thread.currentThread().getName(), notifyMain.read());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "C");

        Thread producer = new Thread(()->{
            while(true) {
                try {
                    sleep();
                    notifyMain.increment();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "P");

        consumer.start();
        producer.start();

        consumer.join();
        producer.join();
    }

    private static void sleep() {
        try {
            Thread.sleep((long) (2_000L*Math.random()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized int read() throws InterruptedException {
        while (!isAvailable) {
            System.out.printf("%n%s Value is not available", Thread.currentThread().getName());
            wait();
        }
        isAvailable = false;
        notify();
        return counter;
    }

    synchronized void increment() throws InterruptedException {
        while (isAvailable) {
            System.out.printf("%n%s Value has not been read", Thread.currentThread().getName());
            wait();
        }
        System.out.printf("%n%s wrote val=%d", Thread.currentThread().getName(), ++counter);
        isAvailable = true;
        notify();
    }
}
