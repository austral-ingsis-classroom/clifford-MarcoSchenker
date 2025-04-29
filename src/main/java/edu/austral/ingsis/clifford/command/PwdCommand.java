package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import java.util.ArrayList;
import java.util.List;

public final class PwdCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return "No arguments expected for pwd command.";
    }
    return buildPathString(fileSystem.getCurrentDirectory());
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return buildPathString(fileSystem.getCurrentDirectory());
  }

  @Override
  public boolean isValid(List<String> args) {
    return args.isEmpty();
  }

  @Override
  public String getName() {
    return "pwd";
  }

  private String buildPathString(Directory currentDirectory) {
    Directory current = currentDirectory;
    List<String> pathComponents = new ArrayList<>();

    while (current != null && current.getFather() != null) {
      pathComponents.add(0, current.getName());
      current = current.getFather();
    }

    return "/" + String.join("/", pathComponents);
  }
}
