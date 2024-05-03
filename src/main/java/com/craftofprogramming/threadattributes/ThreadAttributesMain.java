package com.craftofprogramming.threadattributes;

import java.util.concurrent.ExecutionException;

/**
 * The {@code ThreadAttributesMain} class demonstrates the use of Thread attributes and ThreadGroup in Java.
 * It uses a custom ThreadGroup {@code MyThreadGroup} to handle uncaught exceptions in threads.
 *
 * <p>The class includes two main methods:
 * <ul>
 *   <li>{@code main()} - This is the entry point of the program. It prints a greeting message and calls the {@code threadGroup()} method.</li>
 *   <li>{@code threadGroup()} - This method creates a custom ThreadGroup and three threads. It starts each thread and waits for it to finish before starting the next one.</li>
 * </ul>
 *
 * <p>The class also includes two inner classes:
 * <ul>
 *   <li>{@code MyThreadGroup} - This is a custom ThreadGroup that overrides the {@code uncaughtException()} method to provide a custom handler for uncaught exceptions.</li>
 *   <li>{@code MyThread} - This is a custom Thread class that executes a sequence of operations in its {@code run()} method.</li>
 * </ul>
 *
 * <p>The {@code execute()} method is used by the {@code MyThread} class to execute a sequence of operations. It throws a RuntimeException when a certain condition is met.
 *
 * @author CraftOfProgramming
 */
public class ThreadAttributesMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.printf("%n'%s' Started%n", Thread.currentThread().getName());
        System.out.println("Hello world!");
        threadGroup(); // Part 2
    }

    private static void threadGroup() throws InterruptedException, ExecutionException {
//         Part 4: thread groups motivation: DefaultUncaughtExceptionHandler
        MyThreadGroup group = new MyThreadGroup("MyThreadGroup");
        MyThread t1 = new MyThread(group, "UserThread-1");
        MyThread t2 = new MyThread(group, "UserThread-2");
        MyThread t3 = new MyThread(group, "UserThread-3");
        t1.start();
        t1.join();

        t2.start();
        t2.join();

        isFirst = true;
        t3.start();
        t3.join();
    }

    // Part 4:
    private static final class MyThreadGroup extends ThreadGroup {
        public MyThreadGroup(String name) {
            super(name);
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.printf("%nException with name '%s' was caught by thread with name '%s' belonging to group '%s'",
                    e.getMessage(), t.getName(), getName());
        }
    }

    private static final class MyThread extends Thread {
        public MyThread(ThreadGroup group, String name) {
            super(group, name);
        }

        @Override
        public void run() {
            execute();
        }
    }

    static boolean isFirst = true;

    private static void execute() {
        System.out.printf("%n%s is executing", Thread.currentThread().getName());
        int i;
        for (i = 2; i <= 42; i++) {
            if (i % 2 == 0) {
                System.out.printf("%n%d", i);
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (i == 8 && isFirst) {
                    isFirst = false;
                    throw new RuntimeException("Uh oh!");
                }
            }
        }
        System.out.printf("%n%s finished execution and will die!", Thread.currentThread().getName());
    }
}