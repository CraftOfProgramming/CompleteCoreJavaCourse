package com.craftofprogramming.volatileandfinal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * The {@code VolatileFinalMain} class demonstrates the use of {@code volatile} and {@code final} keywords in Java.
 * It uses a {@code volatile} boolean to control the execution of a thread and a {@code final} class to demonstrate immutability.
 *
 * <p>The class includes the following methods:
 * <ul>
 *   <li>{@code main()} - This is the entry point of the program. It creates a thread that continuously increments a counter until the {@code volatile} boolean is set to false.</li>
 *   <li>{@code sleep()} - This method is used to pause the execution of a thread for a specified duration.</li>
 *   <li>{@code increment()} - This method increments the counter and prints the current value. Note that this method is not thread-safe.</li>
 * </ul>
 *
 * <p>The class also includes a {@code final} inner class:
 * <ul>
 *   <li>{@code ImmutableEx} - This class demonstrates the concept of immutability in Java. It uses a {@code final} Map to store name-age pairs and provides a method to get an unmodifiable view of the Map.</li>
 * </ul>
 *
 * <p>The {@code main()} method demonstrates the use of the {@code volatile} keyword by creating a thread that continuously increments a counter until the {@code volatile} boolean is set to false.
 *
 * @author CraftOfProgramming
 */
public class VolatileFinalMain {
    private static volatile boolean shouldRun = true;
    int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        VolatileFinalMain finalMain = new VolatileFinalMain();
        Thread t = new Thread(()->{
            while(shouldRun) {
                finalMain.increment();
                sleep();
            }
        }, "T1");
        t.start();
        Thread.sleep(10_000L);
        shouldRun = false;
        t.join();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // this method is NOT thread safe
    private void increment() {
        System.out.printf("%n%s wrote val=%d", Thread.currentThread().getName(), ++counter);
    }

    private static final class ImmutableEx {
        private final Map<String, Integer> nameToAge;

        ImmutableEx(Foo foo) {
//            foo.setObj(this);
            nameToAge = new HashMap<>();
            nameToAge.put("Nilton", 38);
            nameToAge.put("Cassandra", 32);
            nameToAge.put("Melanie", 10);
        }

//        ImmutableEx(Map<String, Integer> nameToAge) {
//            this.nameToAge = new HashMap<>(nameToAge);
//        }

        Integer ageOf(String name) {
            return nameToAge.get(name);
        }

        public Map<String, Integer> getNameToAge() {
            return Collections.unmodifiableMap(nameToAge);
        }
    }

    private static final class Foo {
        ImmutableEx obj;

        public void setObj(ImmutableEx obj) {
            this.obj = obj;
            System.out.println(this.obj.ageOf("Nilton"));
        }
    }
}
