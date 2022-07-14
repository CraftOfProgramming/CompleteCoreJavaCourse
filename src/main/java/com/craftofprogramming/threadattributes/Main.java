package com.craftofprogramming.threadattributes;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.printf("%n'%s' Started%n", Thread.currentThread().getName());  // Part 1
        System.out.println("Hello world!");
        threadGroup(); // Part 2
    }

    private static void threadGroup() throws InterruptedException, ExecutionException {
//        Thread.currentThread().setPriority(Thread.MAX_PRIORITY); // Part 2: priority

        // Part 3: using ExecService with ThreadFactory to set name, priority, daemoness
//        String execT3 = newName();
//        ExecutorService service = Executors.newSingleThreadExecutor(new MyThreadFactory(execT3));
//        service.submit(() -> {
//            Main.execute(execT3);
//        });


        // Part 4: thread groups motivation: DefaultUncaughtExceptionHandler
//        MyThreadGroup group = new MyThreadGroup("MyThreadGroup");
//        ThreadSubclassExample t1 = new ThreadSubclassExample(group, "UserThread-1");
//        ThreadSubclassExample t2 = new ThreadSubclassExample(group, "UserThread-2");
//        ThreadSubclassExample t3 = new ThreadSubclassExample(group, "UserThread-3");
//        t1.start();
//        t1.join();
//
//        t2.start();
//        t2.join();
//
//        isFirst = true;
//        t3.start();
//        t3.join();
    }

    // Part 4:
    private static final class MyThreadGroup extends ThreadGroup {
        public MyThreadGroup(String name) {
            super(name);
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.printf("%nException with name '%s' was caught by thread with name '%s'", e.getMessage(), t.getName());
        }
    }

    private static final class ThreadSubclassExample extends Thread {
        public ThreadSubclassExample(ThreadGroup group, String name) {
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