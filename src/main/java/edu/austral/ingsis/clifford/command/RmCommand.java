package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.Element;
import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.FileSystem;
import java.util.ArrayList;
import java.util.List;

public final class RmCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return handleInvalidArguments(args);
    }
    if (isRecursiveFlag(args)) {
      return removeRecursively(fileSystem, args.get(1));
    }
    return removeElement(fileSystem, args.getFirst());
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return "Expected arguments for rm command.";
  }

  @Override
  public boolean isValid(List<String> args) {
    if (args.size() == 1 && !args.getFirst().equals("--recursive")) {
      return true;
    }
    return args.size() == 2 && args.getFirst().equals("--recursive");
  }

  @Override
  public String getName() {
    return "rm";
  }

  private String handleInvalidArguments(List<String> args) {
    if (args.isEmpty()) {
      return "Expected arguments for rm command.";
    }
    if (args.getFirst().equals("--recursive") && args.size() != 2) {
      return "Usage: rm --recursive <directoryName>";
    }
    return "rm expects a single argument or --recursive <directoryName>";
  }

  private boolean isRecursiveFlag(List<String> args) {
    return args.getFirst().equals("--recursive");
  }

  private String removeElement(FileSystem fileSystem, String name) {
    Directory currentDirectory = fileSystem.getCurrentDirectory();
    Element element = findElementByName(currentDirectory, name);
    return processElementRemoval(element, name, currentDirectory);
  }

  private Element findElementByName(Directory directory, String name) {
    return directory.getContent().stream()
        .filter(e -> e.getName().equals(name))
        .findFirst()
        .orElse(null);
  }

  private String processElementRemoval(Element element, String name, Directory currentDirectory) {
    if (element == null) {
      return "'" + name + "' does not exist";
    }
    if (element instanceof File) {
      currentDirectory.getContent().remove(element);
      return "'" + name + "' removed";
    }
    return "cannot remove '" + name + "', is a directory";
  }

  private String removeRecursively(FileSystem fileSystem, String name) {
    Directory currentDirectory = fileSystem.getCurrentDirectory();
    Element element = findElementByName(currentDirectory, name);
    return processRecursiveRemoval(element, name, currentDirectory);
  }

  private String processRecursiveRemoval(Element element, String name, Directory currentDirectory) {
    if (element == null) {
      return "'" + name + "' does not exist";
    }
    if (element instanceof File) {
      currentDirectory.getContent().remove(element);
      return "'" + name + "' removed";
    }
    Directory directory = (Directory) element;
    deleteDirectoryRecursively(directory);
    currentDirectory.getContent().remove(directory);
    return "'" + name + "' removed";
  }

  private void deleteDirectoryRecursively(Directory directory) {
    List<Element> elementsToProcess = new ArrayList<>(directory.getContent());
    processElements(directory, elementsToProcess);
  }

  private void processElements(Directory directory, List<Element> elements) {
    for (Element element : elements) {
      if (element instanceof File) {
        directory.getContent().remove(element);
      } else if (element instanceof Directory subDir) {
        deleteDirectoryRecursively(subDir);
        directory.getContent().remove(subDir);
      }
    }
  }
}
