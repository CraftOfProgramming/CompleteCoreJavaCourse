package com.craftofprogramming.multiconditions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
