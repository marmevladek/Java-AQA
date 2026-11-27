package com.javaaqa;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        FlakyDetector detector = new FlakyDetector();

        List<String> run1 = List.of(
                "LoginTest|PASSED",
                "CreateTaskTest|FAILED",
                "CommentTest|FAILED",
                "LogoutTest|PASSED"
        );

        List<String> run2 = List.of(
                "LoginTest|PASSED",
                "CreateTaskTest|PASSED",
                "CommentTest|FAILED",
                "StatsTest|PASSED"
        );

        Set<String> failedTests = detector.failed(run1);
        System.out.println("Упавшие тесты в прогоне 1: " + failedTests);

        Set<String> passedTests = detector.passed(run2);
        System.out.println("Прошедшие тесты в прогоне 2: " + passedTests);

        List<String> flakyTests = detector.flaky(run1, run2);
        System.out.println("Flaky-кандидаты: " + flakyTests);

        Map<String, Long> summary = detector.statusSummary(run2);
        System.out.println("Прогон 2, сводка: " + summary);
    }
}