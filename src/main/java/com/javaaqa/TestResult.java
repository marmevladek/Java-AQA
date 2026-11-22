package com.javaaqa;

public record TestResult(
        String name,
        Status status,
        long durationMs
) {
}
