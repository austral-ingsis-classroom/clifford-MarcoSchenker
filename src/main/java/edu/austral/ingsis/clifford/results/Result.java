package edu.austral.ingsis.clifford.results;

public sealed interface Result<T> permits Success, Failure {
  static <T> Result<T> success(T value) {
    return new Success<>(value);
  }

  static <T> Result<T> failure(String errorMessage) {
    return new Failure<>(errorMessage);
  }

  boolean isSuccess();

  T getValue();

  String getErrorMessage();
}
