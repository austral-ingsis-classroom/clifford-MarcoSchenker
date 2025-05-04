package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;

public class CommandResult {
  private final FileSystem fileSystem;
  private final String message;

  public CommandResult(FileSystem fileSystem, String message) {
    this.fileSystem = fileSystem;
    this.message = message;
  }

  public FileSystem getFileSystem() {
    return fileSystem;
  }

  public String getMessage() {
    return message;
  }

  public static CommandResult success(FileSystem fileSystem, String message) {
    return new CommandResult(fileSystem, message);
  }

  public static CommandResult error(FileSystem fileSystem, String errorMessage) {
    return new CommandResult(fileSystem, errorMessage);
  }
}
