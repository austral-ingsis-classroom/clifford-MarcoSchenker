package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.results.Result;
import java.util.List;

public final class CdCommand implements Command {
  @Override
  public CommandResult execute(FileSystem fileSystem, List<String> args) {
    if (args.isEmpty()) {
      return CommandResult.error(fileSystem, "Expected arguments for cd command.");
    }

    if (!isValid(args)) {
      return CommandResult.error(fileSystem, "cd expects exactly one argument.");
    }

    String path = args.get(0);

    // Verificamos primero si el directorio existe antes de cambiar
    String normalizedPath = fileSystem.normalizePath(path);
    Result<Directory> directoryResult = fileSystem.getDirectoryByPath(normalizedPath);

    if (!directoryResult.isSuccess()) {
      return CommandResult.error(fileSystem, "'" + path + "' directory does not exist");
    }

    FileSystem newFileSystem = fileSystem.withCurrentDirectory(path);

    if (newFileSystem == fileSystem) {
      return CommandResult.error(fileSystem, "'" + path + "' directory does not exist");
    }

    return getCommandResult(fileSystem, path, directoryResult, newFileSystem);
  }

  @Override
  public CommandResult execute(FileSystem fileSystem) {
    return CommandResult.error(fileSystem, "Expected arguments for cd command.");
  }

  @Override
  public boolean isValid(List<String> args) {
    return args.size() == 1;
  }

  @Override
  public String getName() {
    return "cd";
  }

  private String getDirectoryName(String path) {
    if (path.equals("/")) {
      return "/";
    }
    String[] pathParts = path.split("/");
    for (int i = pathParts.length - 1; i >= 0; i--) {
      if (!pathParts[i].isEmpty()) {
        return pathParts[i];
      }
    }
    return "/";
  }

  private CommandResult getCommandResult(
      FileSystem fileSystem, String path, Result<Directory> dirResult, FileSystem newFileSystem) {
    String dirName;

    switch (path) {
      case "..":
        dirName = "/";
        String parentPath = dirResult.getValue().getParentPath();
        if (!parentPath.equals("/")) {
          dirName = getDirectoryName(parentPath);
        }
        break;
      case ".":
        dirName = getDirectoryName(fileSystem.getCurrentPath());
        break;
      case "/":
        dirName = "/";
        break;
      default:
        if (path.endsWith("/")) {
          dirName = getDirectoryName(path.substring(0, path.length() - 1));
        } else {
          dirName = getDirectoryName(path);
        }
    }

    return CommandResult.success(newFileSystem, "moved to directory '" + dirName + "'");
  }
}
