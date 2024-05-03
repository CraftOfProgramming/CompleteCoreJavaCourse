package com.craftofprogramming.threadcreation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@code ThreadCreationMain} class demonstrates different ways of creating and managing threads in Java.
 * It includes examples of extending the {@code Thread} class, implementing the {@code Runnable} interface, and using the {@code ExecutorService}.
 *
 * <p>The class includes the following methods:
 * <ul>
 *   <li>{@code main()} - This is the entry point of the program. It creates and starts threads using different methods, and manages their execution.</li>
 *   <li>{@code execute()} - This method is executed by each thread. It prints a sequence of even numbers and throws a {@code RuntimeException} when a certain condition is met.</li>
 *   <li>{@code sleepFor()} - This method is used to pause the execution of a thread for a specified duration.</li>
 * </ul>
 *
 * <p>The class also includes two inner classes:
 * <ul>
 *   <li>{@code ThreadSubclassExample} - This is a custom {@code Thread} class that overrides the {@code run()} method to execute the {@code execute()} method.</li>
 *   <li>{@code RunnableImplementationExample} - This is a class that implements the {@code Runnable} interface. Its {@code run()} method also executes the {@code execute()} method.</li>
 * </ul>
 *
 * <p>The {@code main()} method demonstrates the use of the {@code ExecutorService} to manage a pool of threads and submit tasks to them.
 *
 * @author CraftOfProgramming
 */
public class ThreadCreationMain {
    public static void main(String[] args) throws InterruptedException {
        // option 1: extend Thread class
        ThreadSubclassExample subclassExample = new ThreadSubclassExample("ThreadSubclassExample");
        subclassExample.start();
        subclassExample.join();

        // option 2: implement  the Runnable interface
        Thread runnableImplementationEx = new Thread(new RunnableImplementationExample(), "RunnableImplementationEx");
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

        // option 3: use the ExecutorService. Why is this the most powerful and flexible option of the 3?
        // R1: simple to use: no need to sublass the Tread class or to instantiate Thread class (no new Thread(...) !)
        // R2: manages the thread for you: in case the T dies it automatically creates another one
        //        isFirst = true;
        ExecutorService service = Executors.newFixedThreadPool(4);
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

        // R3: it manages the workload for you: since the T works off of a queue, you can submit a task to it while the T is busy executing another task
        service.submit(ThreadCreationMain::execute);
        service.submit(ThreadCreationMain::execute);
        service.submit(ThreadCreationMain::execute);
        service.submit(ThreadCreationMain::execute);

        // R4: unlike option 1 and option 2, the executor service does not "hard-code" the task with the thread: so you can submit "many" different
//        tasks/runnables to the same thread and it will happy execute them for you
//        service.submit(()->{ soutprinln("Task one") });
//        service.submit(()->{ soutprinln("Task Two") });
//        service.submit(()->{ soutprinln("Task Three") });

        // R5: you can submit Runnable and Callable (return a value and support checked exceptions)
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

        // R6: very flexible: supports thread pools too
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
        for (int i = 2; i <= 42 && i % 2 == 0; i++) {
            System.out.printf("%n%d", i);
            sleepFor(1_000L);
            if (i == 8 && isFirst) {
                isFirst = false;
                throw new RuntimeException("Uh oh!");
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