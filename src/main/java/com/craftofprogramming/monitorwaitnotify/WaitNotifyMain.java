package com.craftofprogramming.monitorwaitnotify;

/**
 * The {@code WaitNotifyMain} class demonstrates the use of {@code wait()} and {@code notify()} methods for managing concurrent access to a shared resource.
 * It uses intrinsic locks (synchronized methods) to ensure mutual exclusion when accessing the shared resource, which is a counter.
 *
 * <p>The class includes two main methods:
 * <ul>
 *   <li>{@code read()} - This method is used by a consumer thread to read the counter value. If the counter value is not available (already read and not yet updated), the thread waits until it becomes available.</li>
 *   <li>{@code increment()} - This method is used by a producer thread to increment the counter value. If the counter value is still available (not yet read), the thread waits until it has been read.</li>
 * </ul>
 *
 * <p>The main method of the class creates a producer thread and a consumer thread that concurrently increment and read the counter value.
 *
 * @author CraftOfProgramming
 */
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
