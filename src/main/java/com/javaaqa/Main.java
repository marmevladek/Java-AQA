package com.javaaqa;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TestPlanScanner scanner = new TestPlanScanner();

        List<TestStep> testSteps = scanner.buildPlan(Checklist.class);

        System.out.println("План прогона для Checklist:");

        if (testSteps.isEmpty()) {
            System.out.println(" Кейсов нет");
        } else {
            for (TestStep step : testSteps) {
                System.out.printf(
                        " %d. %s -> %s%n",
                        step.order(),
                        step.id(),
                        step.methodName()
                );
            }
        }
    }
}
