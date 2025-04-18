package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class CdCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (args.size() != 1) {
      return "cd expects exactly one argument.";
    }
    String target = args.getFirst();
    return fileSystem.cd(target);
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return "Expected arguments for cd command.";
  }
}
