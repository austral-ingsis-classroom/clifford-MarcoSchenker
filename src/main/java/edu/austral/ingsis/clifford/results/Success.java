package edu.austral.ingsis.clifford.results;

public final class Success<T> implements Result<T> {
    private final T value;

    public Success(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
}
