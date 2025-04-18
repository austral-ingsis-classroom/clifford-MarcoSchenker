package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class MkdirCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (args.size() != 1) {
      return "mkdir expects exactly one argument.";
    }
    String newDirectoryName = args.getFirst();
    return fileSystem.mkdir(newDirectoryName);
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return "Expected arguments for mkdir command.";
  }
}
