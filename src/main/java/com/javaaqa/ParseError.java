package com.javaaqa;

public record ParseError(
        String line,
        String message
) {
}
