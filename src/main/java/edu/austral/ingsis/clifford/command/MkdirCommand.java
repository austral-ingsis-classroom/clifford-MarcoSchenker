package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import java.util.ArrayList;
import java.util.List;

public final class MkdirCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return "mkdir expects exactly one argument.";
    }
    String directoryName = args.getFirst();
    Directory currentDirectory = fileSystem.getCurrentDirectory();
    if (directoryExists(currentDirectory, directoryName)) {
      return "directory already exists";
    }
    createDirectory(currentDirectory, directoryName);
    return "'" + directoryName + "' directory created";
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return "Expected arguments for mkdir command.";
  }

  @Override
  public boolean isValid(List<String> args) {
    return args.size() == 1;
  }

  @Override
  public String getName() {
    return "mkdir";
  }

  private boolean directoryExists(Directory current, String name) {
    return current.getContent().stream()
        .anyMatch(e -> e instanceof Directory && e.getName().equals(name));
  }

  private void createDirectory(Directory current, String name) {
    Directory newDirectory = new Directory(name, current, new ArrayList<>());
    current.getContent().add(newDirectory);
  }
}
