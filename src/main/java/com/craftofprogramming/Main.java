package com.craftofprogramming;

import java.util.concurrent.ThreadFactory;

public class Main {
    public static final String THREAD_NAME_TEMPLATE = "UserThread%d";
    public static int COUNT = 0;

    public static void main(String[] args) {
        System.out.printf("'%s' Started%n", Thread.currentThread().getName());  // Part 2
        System.out.println("Hello world!");
        createThreads(); // Part 2
        System.out.printf("'%s' will terminate%n", Thread.currentThread().getName());  // Part 2
    }

    private static void createThreads() {
//        Thread.currentThread().setPriority(Thread.MAX_PRIORITY); // Part 2: priority
//        new ThreadSubclassExample(newName(), false).start(); // Part 2.5: extend thread class
        // Part 3
        // Part 3.1: implement runnable using SIC
//        String runnableT1 = newName();
//        new Thread(new RunnableImplementationExample(runnableT1), runnableT1).start();
        // Part 3.21: implement runnable using lambda
//        String runnableT2 = newName();
//        new Thread(() -> {
//            Main.execute(runnableT2);
//        }, runnableT2).start();

        // Part 4: using ExecService
//        String execT1 = newName();
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        service.submit(() -> {
//            Main.execute(execT1);
//        });
//
        //        String execT2 = newName();
//        Executors.newFixedThreadPool(1).submit(() -> {
//            Main.execute(execT2);
//        });

        // Part 5: using ExecService with ThreadFactory to set name, priority, daemoness
//        String execT3 = newName();
//        ExecutorService service = Executors.newSingleThreadExecutor(new MyThreadFactory(execT3));
//        service.submit(() -> {
//            Main.execute(execT3);
//        });

        // Part 6: thread groups motivation: DefaultUncaughtExceptionHandler
        MyThreadGroup group = new MyThreadGroup("MyThreadGroup");
        new CountingThread(group, newName(), 5).start();
        new CountingThread(group, newName(), 8).start();
        new CountingThread(group, newName(), 13).start();
    }

    private static String newName() {
        return String.format(THREAD_NAME_TEMPLATE, ++COUNT);
    }

    private static final class ThreadSubclassExample extends Thread {
        public ThreadSubclassExample(String name, boolean isDaemon) {
            super(name);
            setDaemon(isDaemon);
        }

        @Override
        public void run() {
            execute(getName());
        }
    }

    private static final class RunnableImplementationExample implements Runnable {
        private final String name;

        private RunnableImplementationExample(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            execute(name);
        }
    }

    private static void execute(String name) {
        while (true) {
            try {
                System.out.printf("'%s' Running%n", name);
                Thread.sleep(1_000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void count(String name) {
        while (true) {
            try {
                System.out.printf("'%s' Counting%n", name);
                Thread.sleep(1_000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final class MyThreadFactory implements ThreadFactory {
        private String name;

        public MyThreadFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, name);
            thread.setDaemon(true);
//            thread.setPriority(Thread.MAX_PRIORITY);
            return thread;
        }
    }

    // Part 6:
    private static final class MyThreadGroup extends ThreadGroup {
        public MyThreadGroup(String name) {
            super(name);
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            if (t instanceof CountingThread) {
                CountingThread counter = (CountingThread) t;
                System.out.printf("'%s' was at count:%d%n", t.getName(), counter.getCount());
            }
            super.uncaughtException(t, e);
        }
    }

    // Part 6:
    private static final class CountingThread extends Thread {
        private long count = 0;
        private final int limit;

        public CountingThread(ThreadGroup group, String name, int limit) {
            super(group, name);
            this.limit = limit;
        }

        @Override
        public void run() {
            while (true) {
                if (count == limit) {
                    throw new RuntimeException(String.format("'%s' reached limit:%d%n", getName(), limit));
                }
                System.out.printf("'%s' is counting: %d%n", getName(), ++count);
                try {
                    Thread.sleep(1_000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public long getCount() {
            return count;
        }
    }
}