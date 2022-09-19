package com.craftofprogramming.volatileandfinal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
