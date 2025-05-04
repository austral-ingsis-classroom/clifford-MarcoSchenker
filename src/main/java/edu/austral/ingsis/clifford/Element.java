package edu.austral.ingsis.clifford;

public sealed interface Element permits Directory, File {
  String getName();

  String getPath();

  String getParentPath();

  boolean isRoot();
}
