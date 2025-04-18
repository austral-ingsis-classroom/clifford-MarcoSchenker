package edu.austral.ingsis.clifford;

public sealed interface Element permits Directory, File {
  String getName();

  Directory getFather();
}
