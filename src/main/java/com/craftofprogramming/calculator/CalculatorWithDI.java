package com.craftofprogramming.calculator;

public class CalculatorWithDI {
    private final Processor processor;

    public CalculatorWithDI(Processor processor) {
        this.processor = processor;
    }

    public long add(int a, int b) {
        return processor.add(a, b);
    }

    public long subtract(int a, int b) {
        return processor.subtract(a, b);
    }

    public long multiply(int a, int b) {
        return processor.multiply(a, b);
    }

    public long divide(int a, int b) {
        return processor.divide(a, b);
    }

    public static final class Processor {
        public long add(int a, int b) {
            return a + b;
        }

        public long subtract(int a, int b) {
            return a - b;
        }

        public long multiply(int a, int b) {
            return a * b;
        }

        public long divide(int a, int b) {
            return a / b;
        }
    }
}
