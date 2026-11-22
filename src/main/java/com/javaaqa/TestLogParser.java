package com.javaaqa;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestLogParser {

    private final List<ParseError> errors = new ArrayList<>();

    public List<TestResult> parse(List<String> lines) {

        errors.clear();

        Map<String, TestResult> results = new LinkedHashMap<>();

        for (String line : lines) {

            if (line == null) {
                errors.add(new ParseError("null", "пустая строка"));
                continue;
            }

            String[] parts = line.split("\\|", -1);

            if (parts.length != 3) {
                errors.add(new ParseError(line, "ожидалось три поля, разделённых символом |"));
                continue;
            }

            String name = parts[0].trim();
            String statusValue = parts[1].trim();
            String durationValue = parts[2].trim();

            if (name.isEmpty()) {
                errors.add(new ParseError(line, "не указано имя теста"));
                continue;
            }

            Status status;

            try {
                status = Status.valueOf(statusValue);
            } catch (IllegalArgumentException e) {
                errors.add(new ParseError(line,
                        "неизвестный статус: " + statusValue));
                continue;
            }

            long duration;

            try {
                duration = Long.parseLong(durationValue);
            } catch (NumberFormatException e) {
                errors.add(new ParseError(line,
                        "не число в длительности"));
                continue;
            }

            if (duration < 0) {
                errors.add(new ParseError(line,
                        "длительность не может быть отрицательной"));
                continue;
            }

            TestResult result = new TestResult(name, status, duration);

            results.put(name, result);
        }

        return new ArrayList<>(results.values());
    }

    public static Optional<TestResult> firstFailed(
            List<TestResult> results) {

        return results.stream()
                .filter(result -> result.status() == Status.FAILED)
                .findFirst();
    }

    public List<ParseError> getErrors() {
        return List.copyOf(errors);
    }

    public static void main(String[] args) {

        Path path = Path.of("run.log");

        TestLogParser parser = new TestLogParser();

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return;
        }

        List<TestResult> results = parser.parse(lines);

        System.out.println("Результаты:");

        for (TestResult result : results) {
            System.out.printf(
                    "  %s — %s (%d ms)%n",
                    result.name(),
                    result.status(),
                    result.durationMs()
            );
        }

        if (parser.getErrors().isEmpty()) {
            System.out.println("Битые строки: нет");
        } else {
            for (ParseError error : parser.getErrors()) {
                System.out.printf(
                        "Битые строки: %s — %s%n",
                        error.line(),
                        error.message()
                );
            }
        }

        Optional<TestResult> firstFailed = firstFailed(results);

        System.out.println(
                "Первый упавший: " +
                        firstFailed.map(TestResult::name)
                                .orElse("Упадений нет")
        );
    }
}