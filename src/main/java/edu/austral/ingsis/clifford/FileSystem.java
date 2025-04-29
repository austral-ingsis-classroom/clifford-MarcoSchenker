package edu.austral.ingsis.clifford;

import java.util.ArrayList;

public class FileSystem {
  private final Directory root;
  private Directory currentDirectory;

  public FileSystem() {
    this.root = new Directory("/", new ArrayList<>());
    this.currentDirectory = root;
  }

  public Directory getRoot() {
    return root;
  }

  public Directory getCurrentDirectory() {
    return currentDirectory;
  }

  public void setCurrentDirectory(Directory directory) {
    if (directory != null) {
      this.currentDirectory = directory;
    }
  }
}
