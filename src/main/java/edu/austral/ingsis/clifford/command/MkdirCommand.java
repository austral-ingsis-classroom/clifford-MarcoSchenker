package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.results.Result;
import java.util.List;

public final class MkdirCommand implements Command {
  @Override
  public CommandResult execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return CommandResult.error(fileSystem, "mkdir expects exactly one argument.");
    }

    String directoryName = args.get(0);
    return createDirectory(fileSystem, directoryName);
  }

  private CommandResult createDirectory(FileSystem fileSystem, String directoryName) {
    Result<Directory> currentDirResult = fileSystem.getCurrentDirectory();

    if (!currentDirResult.isSuccess()) {
      return CommandResult.error(fileSystem, currentDirResult.getErrorMessage());
    }

    Directory currentDir = currentDirResult.getValue();

    if (currentDir.containsElementWithName(directoryName)) {
      return CommandResult.error(fileSystem, "directory already exists");
    }

    FileSystem newFileSystem = fileSystem.createDirectory(directoryName);
    return CommandResult.success(newFileSystem, "'" + directoryName + "' directory created");
  }

  @Override
  public CommandResult execute(FileSystem fileSystem) {
    return CommandResult.error(fileSystem, "Expected arguments for mkdir command.");
  }

  @Override
  public boolean isValid(List<String> args) {
    return args.size() == 1;
  }

  @Override
  public String getName() {
    return "mkdir";
  }
}
