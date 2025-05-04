package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.results.Result;
import java.util.List;

public final class TouchCommand implements Command {
  @Override
  public CommandResult execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return CommandResult.error(fileSystem, "touch expects exactly one argument.");
    }

    String fileName = args.get(0);
    return createFile(fileSystem, fileName);
  }

  private CommandResult createFile(FileSystem fileSystem, String fileName) {
    Result<Directory> currentDirResult = fileSystem.getCurrentDirectory();

    if (!currentDirResult.isSuccess()) {
      return CommandResult.error(fileSystem, currentDirResult.getErrorMessage());
    }

    Directory currentDir = currentDirResult.getValue();

    if (currentDir.containsElementWithName(fileName)) {
      return CommandResult.error(fileSystem, "file already exists");
    }

    FileSystem newFileSystem = fileSystem.createFile(fileName);
    return CommandResult.success(newFileSystem, "'" + fileName + "' file created");
  }

  @Override
  public CommandResult execute(FileSystem fileSystem) {
    return CommandResult.error(fileSystem, "Expected arguments for touch command.");
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
