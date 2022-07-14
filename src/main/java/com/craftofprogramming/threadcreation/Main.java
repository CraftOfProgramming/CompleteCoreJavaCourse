package com.craftofprogramming.threadcreation;

public class Main {
    public static final String THREAD_NAME_TEMPLATE = "UserThread%d";
    public static int COUNT = 0;

    public static void main(String[] args) throws InterruptedException {
        // option 1: extend Thread class
        ThreadSubclassExample subclassExample = new ThreadSubclassExample("ThreadSubclassExample");
        subclassExample.start();
        subclassExample.join();

        // option 2: implement  the Runnable interface
//        Thread runnableImplementationEx = new Thread(new RunnableImplementationExample(), "RunnableImplementationEx");
//        Thread runnableImplementationEx = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                execute();
//            }
//        }, "RunnableImplementationEx");
//        isFirst = true;
//        Thread runnableImplementationEx = new Thread(Main::execute, "RunnableImplementationEx");
//        runnableImplementationEx.start();
//        runnableImplementationEx.join();

        // option 3: use the ExecutorService. Why is this the best option of the 3?
        // R1: simple to use: no need to sublass or instantiate Thread class (no new Thread(...) !)
        // R2: manages threads for you: in case the T dies it automatically creates another one
        // R3: you can submit Runnable and Callable (return a value and support checked exceptions)
//        isFirst = true;
//        ExecutorService service = Executors.newFixedThreadPool(4);
//        Future<Integer> future = service.submit(Main::execute);
//        try {
//            System.out.printf("%nResult=%d", future.get());
//        } catch (ExecutionException e) {
//            System.out.println(e);
//        }
//        future = service.submit(Main::execute);
//        try {
//            System.out.printf("%nResult=%d", future.get());
//        } catch (ExecutionException e) {
//            System.out.println(e);
//        }
//        service.awaitTermination(1, TimeUnit.SECONDS);

        // R4: very flexible: supports thread pools too
//        isFirst = false;
//        ExecutorService service = Executors.newFixedThreadPool(4);
//        service.submit(Main::execute);
//        service.submit(Main::execute);
//        service.submit(Main::execute);
//        service.submit(Main::execute);
    }

    private static final class ThreadSubclassExample extends Thread {
        public ThreadSubclassExample(String name) {
            super(name);
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
                sleepFor(1_000L);
                if (i == 8 && isFirst) {
                    isFirst = false;
                    throw new RuntimeException("Uh oh!");
                }
            }
        }
        System.out.printf("%n%s finished execution and will die!", Thread.currentThread().getName());
    }
//    private static int execute() throws Exception {
//        System.out.printf("%n%s is executing", Thread.currentThread().getName());
//        int i;
//        for (i = 2; i <= 42; i++) {
//            if (i % 2 == 0) {
//                System.out.printf("%n%s-%d", Thread.currentThread().getName(), i);
//                sleepFor(1_000L);
//                if (i == 8 && isFirst) {
//                    isFirst = false;
//                    throw new Exception("Uh oh!");
//                }
//            }
//        }
//        System.out.printf("%n%s finished execution and will die!", Thread.currentThread().getName());
//        return i;
//    }

    private static void sleepFor(long napDuration) {
        try {
            Thread.sleep(napDuration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class RunnableImplementationExample implements Runnable {
        @Override
        public void run() {
            execute();
        }
    }

}