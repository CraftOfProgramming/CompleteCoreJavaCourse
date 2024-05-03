package com.craftofprogramming.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The {@code ThreadLocalMain} class demonstrates the use of {@code ThreadLocal} for creating thread-safe date formatters in Java.
 * It uses a {@code SimpleDateFormat} instance stored in a {@code ThreadLocal} to ensure that each thread has its own instance of the formatter.
 *
 * <p>The class includes the following methods:
 * <ul>
 *   <li>{@code main()} - This is the entry point of the program. It creates two threads that continuously print the current date and time using the thread-safe date formatter.</li>
 *   <li>{@code sleep()} - This method is used to pause the execution of a thread for a specified duration.</li>
 * </ul>
 *
 * <p>The class also includes an inner class:
 * <ul>
 *   <li>{@code ThreadSafeDateFormatter} - This is a utility class that provides a thread-safe date formatter. It uses a {@code ThreadLocal} to store a {@code SimpleDateFormat} instance for each thread.</li>
 * </ul>
 *
 * <p>The {@code main()} method demonstrates the use of the {@code ThreadSafeDateFormatter} by creating two threads that continuously print the current date and time.
 *
 * @author CraftOfProgramming
 */
public class ThreadLocalMain {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            while(true) {
                String dateTime = ThreadSafeDateFormatter.format(new Date());
                System.out.printf("%n%s has date=%s", Thread.currentThread().getName(),
                        dateTime);
                sleep();
            }
        }, "T1");
        Thread t2 = new Thread(()->{
            while(true) {
                String dateTime = ThreadSafeDateFormatter.format(new Date());
                System.out.printf("%n%s has date=%s", Thread.currentThread().getName(),
                        dateTime);
                sleep();
            }
        }, "T2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private static void sleep() {
        try {
            Thread.sleep(2_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class ThreadSafeDateFormatter {
        private static final ThreadLocal<SimpleDateFormat> FORMATTER =
                ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMDD:HHmmss"));

        public static String format(Date date) {
            return FORMATTER.get().format(date);
        }
    }
}