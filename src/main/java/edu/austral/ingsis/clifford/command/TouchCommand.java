package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.Element;
import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class TouchCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return "touch expects exactly one argument.";
    }

    String fileName = args.getFirst();
    Directory currentDirectory = fileSystem.getCurrentDirectory();

    for (Element element : currentDirectory.getContent()) {
      if (element instanceof File && element.getName().equals(fileName)) {
        return "file already exists";
      }
    }

    File newFile = new File(fileName, currentDirectory);
    currentDirectory.getContent().add(newFile);
    return "'" + fileName + "' file created";
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return "Expected arguments for touch command.";
  }

  @Override
  public boolean isValid(List<String> args) {
    return args.size() == 1;
  }

  @Override
  public String getName() {
    return "touch";
  }
}
