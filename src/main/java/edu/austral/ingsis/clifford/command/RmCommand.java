package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.Element;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.results.Result;
import java.util.List;

public final class RmCommand implements Command {
  @Override
  public CommandResult execute(FileSystem fileSystem, List<String> args) {
    if (args.isEmpty()) {
      return CommandResult.error(fileSystem, "Expected arguments for rm command.");
    }

    if (!isValid(args)) {
      return handleInvalidArguments(fileSystem, args);
    }

    if (isRecursiveFlag(args)) {
      return executeRecursiveRemove(fileSystem, args.get(1));
    }

    return executeSimpleRemove(fileSystem, args.get(0));
  }

  @Override
  public CommandResult execute(FileSystem fileSystem) {
    return CommandResult.error(fileSystem, "Expected arguments for rm command.");
  }

  @Override
  public boolean isValid(List<String> args) {
    if (args.size() == 1 && !args.get(0).equals("--recursive")) {
      return true;
    }
    return args.size() == 2 && args.get(0).equals("--recursive");
  }

  @Override
  public String getName() {
    return "rm";
  }

  private CommandResult handleInvalidArguments(FileSystem fileSystem, List<String> args) {
    if (args.get(0).equals("--recursive") && args.size() != 2) {
      return CommandResult.error(fileSystem, "Usage: rm --recursive <directoryName>");
    }

    return CommandResult.error(
        fileSystem, "rm expects a single argument or --recursive <directoryName>");
  }

  private boolean isRecursiveFlag(List<String> args) {
    return args.get(0).equals("--recursive");
  }

  private CommandResult executeSimpleRemove(FileSystem fileSystem, String elementName) {
    Result<Directory> dirResult = fileSystem.getCurrentDirectory();

    if (!dirResult.isSuccess()) {
      return CommandResult.error(fileSystem, dirResult.getErrorMessage());
    }

    Directory currentDir = dirResult.getValue();
    Result<Element> elementResult = currentDir.findElementByName(elementName);

    if (!elementResult.isSuccess()) {
      return CommandResult.error(fileSystem, "'" + elementName + "' does not exist");
    }

    Element element = elementResult.getValue();

    if (element instanceof Directory) {
      return CommandResult.error(fileSystem, "cannot remove '" + elementName + "', is a directory");
    }

    FileSystem newFileSystem = fileSystem.removeElement(elementName);
    return CommandResult.success(newFileSystem, "'" + elementName + "' removed");
  }

  private CommandResult executeRecursiveRemove(FileSystem fileSystem, String dirName) {
    Result<Directory> currentDirResult = fileSystem.getCurrentDirectory();

    if (!currentDirResult.isSuccess()) {
      return CommandResult.error(fileSystem, currentDirResult.getErrorMessage());
    }

    Directory currentDir = currentDirResult.getValue();
    Result<Element> elementResult = currentDir.findElementByName(dirName);

    if (!elementResult.isSuccess()) {
      return CommandResult.error(fileSystem, "'" + dirName + "' does not exist");
    }

    FileSystem newFileSystem = fileSystem.removeDirectoryRecursively(dirName);
    return CommandResult.success(newFileSystem, "'" + dirName + "' removed");
  }
}
