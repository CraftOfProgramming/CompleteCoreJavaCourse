package com.craftofprogramming.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;

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