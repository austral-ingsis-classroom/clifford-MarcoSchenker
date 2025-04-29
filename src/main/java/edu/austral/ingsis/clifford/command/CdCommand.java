package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.Element;
import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class CdCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return "cd expects exactly one argument.";
    }

    String path = args.getFirst();
    Directory targetDir = resolvePath(fileSystem, path);
    if (targetDir == null) {
      return "'" + path + "' directory does not exist";
    }

    fileSystem.setCurrentDirectory(targetDir);
    return "moved to directory '" + targetDir.getName() + "'";
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return "Expected arguments for cd command.";
  }

  @Override
  public boolean isValid(List<String> args) {
    return args.size() == 1;
  }

  @Override
  public String getName() {
    return "cd";
  }

  private Directory resolvePath(FileSystem fileSystem, String path) {
    if (path == null || path.isEmpty()) {
      return fileSystem.getCurrentDirectory();
    }

    String[] parts = path.split("/");
    Directory startingPoint = getStartingDirectory(fileSystem, path);
    return traversePath(parts, startingPoint);
  }

  private Directory getStartingDirectory(FileSystem fileSystem, String path) {
    return path.startsWith("/") ? fileSystem.getRoot() : fileSystem.getCurrentDirectory();
  }

  private Directory traversePath(String[] parts, Directory startingDir) {
    Directory current = startingDir;
    for (String part : parts) {
      if (part.isEmpty()) {
        continue;
      }
      current = resolvePathPart(part, current);
      if (current == null) return null;
    }
    return current;
  }

  private Directory resolvePathPart(String part, Directory current) {
    if (part.isEmpty() || part.equals(".")) {
      return current;
    } else if (part.equals("..")) {
      return current.getFather() != null ? current.getFather() : current;
    } else {
      return findSubdirectoryByName(current, part);
    }
  }

  private Directory findSubdirectoryByName(Directory current, String name) {
    for (Element element : current.getContent()) {
      if (element instanceof Directory dir && dir.getName().equals(name)) {
        return dir;
      }
    }
    return null;
  }
}
