package com.craftofprogramming.multiconditions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class demonstrates the use of Condition objects for managing concurrent access to a shared resource.
 * It uses a Lock to ensure mutual exclusion when accessing the shared resource, which is an array of integers.
 * The class defines two Condition objects: "canWrite" and "canRead", which are used to control access to the shared resource.
 *
 * <p>The canWrite condition is used to signal that a thread can write to the shared resource when it is not full.
 * The canRead condition is used to signal that a thread can read from the shared resource when it is not empty.
 *
 * <p>The class also includes methods for writing to and reading from the shared resource, which use the lock to ensure mutual exclusion and the Condition objects to control access.
 *
 * <p>The main method of the class creates several threads that concurrently write to and read from the shared resource.
 *
 * @author CraftOfProgramming
 */
public class ConditionsMain {
    Lock lock = new ReentrantLock();
    Condition canWrite = lock.newCondition();
    Condition canRead = lock.newCondition();

    final int[] data;
    final int capacity;
    int size = 0;

    @Override
    public String toString() {
        return super.toString();
    }

    public ConditionsMain(int capacity) {
        this.capacity = capacity;
        this.data = new int[capacity];
    }

    public static void main(String[] args) {
        ConditionsMain conditionsMain = new ConditionsMain(6);

        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(()->{
                while(true) {
                    try {
                        sleep();
                        conditionsMain.write((int) (Math.random()*42));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, "W"+i);
            t.start();

            t = new Thread(()->{
                while(true) {
                    try {
                        sleep();
                        int val = conditionsMain.read();
                        System.out.printf("%n%s read val=%d", Thread.currentThread().getName(),
                                val);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, "R"+i);
            t.start();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep((long) (2_000L*Math.random()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    int read() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) {
                System.out.printf("%nBuffer is empty. %s will wait",
                        Thread.currentThread().getName());
                canRead.await();
            }
            int val = data[--size];
            canWrite.signal();
            return val;
        } finally {
            lock.unlock();
        }
    }

    void write(int val) throws InterruptedException {
        lock.lock();
        try {
            while (size == capacity) {
                System.out.printf("%nBuffer is full. %s will wait",
                        Thread.currentThread().getName());
                canWrite.await();
            }
            data[size++] = val;
            System.out.printf("%n%s wrote val=%d", Thread.currentThread().getName(), val);
            canRead.signal();
        } finally {
            lock.unlock();
        }
    }
}
