package edu.austral.ingsis.clifford.results;

public final class Failure<T> implements Result<T> {
    private final String message;

    public Failure(String message) {
        this.message = message;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }

    @Override
    public T getValue() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
