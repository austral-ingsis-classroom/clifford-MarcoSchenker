package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public class FileSystem {
  Directory root;
  Directory currentDirectory;

  public FileSystem() {
    this.root = new Directory("/", new ArrayList<>());
    this.currentDirectory = root;
  }

  public String ls(Directory currentDirectory) {
    StringBuilder stringBuilder = getStringBuilder(currentDirectory.getContent());
    return stringBuilder.toString();
  }

  public String lsOrd(Directory currentDirectory, String order) {
    if (order.equals("asc") || order.equals("desc")) {
      return getFilesInOrder(currentDirectory, order);
    } else {
      return "Invalid order value, use 'asc' or 'desc'.";
    }
  }

  public String cd(String path) {
    Directory targetDir = resolvePath(path);
    if (targetDir == null) {
      return "'" + path + "' directory does not exist";
    }
    currentDirectory = targetDir;
    return "moved to directory '" + currentDirectory.getName() + "'";
  }

  public String pwd() {
    Directory current = currentDirectory;
    List<String> path = new ArrayList<>();

    while (current != null && current.getFather() != null) {
      path.add(0, current.getName());
      current = current.getFather();
    }

    return "/" + String.join("/", path);
  }

  public String mkdir(String name) {
    List<Element> content = currentDirectory.getContent();
    for (Element element : content) {
      if (element instanceof Directory directory && directory.getName().equals(name)) {
        return "directory already exists";
      }
    }
    Directory newDirectory = new Directory(name, currentDirectory, new ArrayList<>());
    content.add(newDirectory);
    return "'" + name + "' directory created";
  }

  public String touch(String name) {
    List<Element> content = currentDirectory.getContent();
    for (Element element : content) {
      if (element instanceof File file && file.getName().equals(name)) {
        return "file already exists";
      }
    }
    File newFile = new File(name, currentDirectory);
    content.add(newFile);
    return "'" + name + "' file created";
  }

  public String rm(String name) {
    List<Element> content = currentDirectory.getContent();
    for (Element element : content) {
      if (element.getName().equals(name)) {
        if (element instanceof File) {
          content.remove(element);
          return "'" + name + "' removed";
        } else {
          return "cannot remove '" + name + "', is a directory";
        }
      }
    }
    return "'" + name + "' does not exist";
  }

  public String rmRecursive(String name) {
    List<Element> content = currentDirectory.getContent();
    for (Element element : new ArrayList<>(content)) {
      if (element.getName().equals(name)) {
        if (element instanceof File) {
          content.remove(element);
          return "'" + name + "' removed";
        } else if (element instanceof Directory directory) {
          deleteDirectoryRecursively(directory);
          content.remove(directory);
          return "'" + name + "' removed";
        }
      }
    }
    return "'" + name + "' does not exist";
  }

  private void deleteDirectoryRecursively(Directory directory) {
    for (Element element : new ArrayList<>(directory.getContent())) {
      switch (element) {
        case File file -> directory.getContent().remove(file);
        case Directory subDir -> {
          deleteDirectoryRecursively(subDir);
          directory.getContent().remove(subDir);
        }
      }
    }
  }

  private static String getFilesInOrder(Directory directory, String order) {
    if (directory.getContent().size() > 1) {
      List<Element> content = new ArrayList<>(directory.getContent());
      ordering(order, content);
      StringBuilder stringBuilder = getStringBuilder(content);
      return stringBuilder.toString().trim();
    } else if (directory.getContent().size() == 1) {
      return directory.getContent().get(0).getName();
    } else {
      return "";
    }
  }

  private static void ordering(String order, List<Element> content) {
    content.sort(
        (element1, element2) -> {
          if (order.equals("asc")) {
            return element1.getName().compareTo(element2.getName());
          } else {
            return element2.getName().compareTo(element1.getName());
          }
        });
  }

  private static StringBuilder getStringBuilder(List<Element> content) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < content.size(); i++) {
      stringBuilder.append(content.get(i).getName());
      if (i < content.size() - 1) {
        stringBuilder.append(" ");
      }
    }
    return stringBuilder;
  }

  private Directory resolvePath(String path) {
    if (path == null || path.isEmpty()) return currentDirectory;

    String[] parts = path.split("/");
    Directory startingPoint = getStartingDirectory(path);
    return traversePath(parts, startingPoint);
  }

  private Directory getStartingDirectory(String path) {
    return path.startsWith("/") ? root : currentDirectory;
  }

  private Directory traversePath(String[] parts, Directory startingDir) {
    Directory current = startingDir;
    for (String part : parts) {
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

  public Directory getCurrentDirectory() {
    return currentDirectory;
  }
}
