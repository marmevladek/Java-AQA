package com.javaaqa;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;

public class FlakyDetector {

    public Set<String> failed(List<String> run) {
        Set<String> failedTests = new LinkedHashSet<>();

        for (String line : run) {

            String[] parts = line.split("\\|", -1);

            if (parts.length == 2 && parts[1].trim().equals("FAILED")) {
                failedTests.add(parts[0].trim());
            }
        }

        return failedTests;
    }

    public Set<String> passed(List<String> run) {
        Set<String> passedTests = new LinkedHashSet<>();

        for (String line : run) {

            String[] parts = line.split("\\|", -1);

            if (parts.length == 2 && parts[1].trim().equals("PASSED")) {
                passedTests.add(parts[0].trim());
            }
        }

        return passedTests;
    }

    public List<String> flaky(List<String> run1, List<String> run2) {
        Set<String> failedTests = failed(run1);
        Set<String> passedTests = passed(run2);

        failedTests.retainAll(passedTests);

        return List.copyOf(failedTests);
    }

    public Map<String, Long> statusSummary(List<String> run) {
        Map<String, Long> summary = new LinkedHashMap<>();

        for (String line : run) {

            String[] parts = line.split("\\|", -1);

            if (parts.length == 2 && !parts[0].trim().isEmpty() && !parts[1].trim().isEmpty()) {
                String status = parts[1].trim();
                summary.merge(status, 1L, Long::sum);
            }
        }

        return summary;
    }
}