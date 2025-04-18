package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class TouchCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (args.size() != 1) {
      return "touch expects exactly one argument.";
    }
    String newFileName = args.getFirst();
    return fileSystem.touch(newFileName);
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return "Expected arguments for touch command.";
  }
}
