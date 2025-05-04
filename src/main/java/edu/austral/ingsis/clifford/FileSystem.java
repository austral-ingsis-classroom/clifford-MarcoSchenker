package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.results.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSystem {
  private final Directory root;
  private final String currentPath;
  private final Map<String, Directory> directoryPaths;

  public FileSystem() {
    this.root = new Directory("/");
    this.currentPath = "/";
    this.directoryPaths = new HashMap<>();
    this.directoryPaths.put("/", root);
  }

  private FileSystem(Directory root, String currentPath, Map<String, Directory> directoryCache) {
    this.root = root;
    this.currentPath = currentPath;
    this.directoryPaths = new HashMap<>(directoryCache);
  }

  public Directory getRoot() {
    return root;
  }

  public String getCurrentPath() {
    return currentPath;
  }

  public Result<Directory> getCurrentDirectory() {
    return getDirectoryByPath(currentPath);
  }

  public FileSystem withCurrentDirectory(String path) {
    String normalizedPath = normalizePath(path);
    Result<Directory> dirResult = getDirectoryByPath(normalizedPath);
    if (!dirResult.isSuccess()) {
      return this;
    }
    return new FileSystem(root, normalizedPath, directoryPaths);
  }

  public Result<Directory> getDirectoryByPath(String path) {
    String normalizedPath = normalizePath(path);
    if (directoryPaths.containsKey(normalizedPath)) {
      return Result.success(directoryPaths.get(normalizedPath));
    }
    return resolveDirectoryPath(normalizedPath);
  }

  private Result<Directory> resolveDirectoryPath(String path) {
    if (path.equals("/")) {
      return Result.success(root);
    }

    String normalizedPath = path;
    while (normalizedPath.startsWith("//")) {
      normalizedPath = normalizedPath.substring(1);
    }

    return traversePathToDirectory(normalizedPath);
  }

  private Result<Directory> traversePathToDirectory(String path) {
    String[] parts = path.substring(1).split("/");
    Directory current = root;
    StringBuilder currentPathBuilder = new StringBuilder("/");

    for (String part : parts) {
      if (part.isEmpty()) continue;

      Result<Element> elementResult = current.findElementByName(part);
      if (!elementResult.isSuccess()) {
        return Result.failure(elementResult.getErrorMessage());
      }

      Element element = elementResult.getValue();
      if (!(element instanceof Directory)) {
        return Result.failure("'" + part + "' is not a directory");
      }

      current = (Directory) element;
      updatePathBuilder(currentPathBuilder, part);
      directoryPaths.put(currentPathBuilder.toString(), current);
    }

    return Result.success(current);
  }

  private void updatePathBuilder(StringBuilder builder, String part) {
    if (builder.length() > 1) {
      builder.append("/");
    }
    builder.append(part);
  }

  public FileSystem createDirectory(String directoryName) {
    Result<Directory> currentDirResult = getCurrentDirectory();
    if (!currentDirResult.isSuccess()) {
      return this;
    }

    Directory currentDir = currentDirResult.getValue();
    if (currentDir.containsElementWithName(directoryName)) {
      return this;
    }

    return createDirectoryInCurrentPath(directoryName, currentDir);
  }

  private FileSystem createDirectoryInCurrentPath(String directoryName, Directory currentDir) {
    Directory newDir = new Directory(directoryName, currentPath, new ArrayList<>());
    Directory updatedCurrentDir = currentDir.addElement(newDir);

    String fullPath = currentPath.equals("/") ? "/" + directoryName : currentPath + "/" + directoryName;
    Map<String, Directory> newCache = new HashMap<>(directoryPaths);
    newCache.put(fullPath, newDir);

    return updateDirectoryAndCache(currentPath, updatedCurrentDir, newCache);
  }

  public FileSystem createFile(String fileName) {
    Result<Directory> currentDirResult = getCurrentDirectory();
    if (!currentDirResult.isSuccess()) {
      return this;
    }

    Directory currentDir = currentDirResult.getValue();
    if (currentDir.containsElementWithName(fileName)) {
      return this;
    }

    return createFileInCurrentPath(fileName, currentDir);
  }

  private FileSystem createFileInCurrentPath(String fileName, Directory currentDir) {
    File newFile = new File(fileName, currentPath);
    Directory updatedCurrentDir = currentDir.addElement(newFile);

    return updateDirectoryAndCache(currentPath, updatedCurrentDir, new HashMap<>(directoryPaths));
  }

  public FileSystem removeElement(String elementName) {
    Result<Directory> currentDirResult = getCurrentDirectory();
    if (!currentDirResult.isSuccess()) {
      return this;
    }

    Directory currentDir = currentDirResult.getValue();
    Result<Element> elementResult = currentDir.findElementByName(elementName);
    if (!elementResult.isSuccess()) {
      return this;
    }

    Element element = elementResult.getValue();
    if (element instanceof Directory) {
      return this;
    }

    Directory updatedDir = currentDir.removeElement(elementName);
    return updateDirectoryAndCache(currentPath, updatedDir, new HashMap<>(directoryPaths));
  }

  public FileSystem removeDirectoryRecursively(String directoryName) {
    Result<Directory> currentDirResult = getCurrentDirectory();
    if (!currentDirResult.isSuccess()) {
      return this;
    }

    Directory currentDir = currentDirResult.getValue();
    Result<Element> elementResult = currentDir.findElementByName(directoryName);
    if (!elementResult.isSuccess()) {
      return this;
    }

    Element element = elementResult.getValue();
    if (!(element instanceof Directory)) {
      return removeElement(directoryName);
    }

    String fullPath = currentPath.equals("/") ?
            "/" + directoryName :
            currentPath + "/" + directoryName;

    Map<String, Directory> newCache = new HashMap<>(directoryPaths);
    removeDirectoryFromCache(fullPath, newCache);

    Directory updatedDir = currentDir.removeElement(directoryName);
    return updateDirectoryAndCache(currentPath, updatedDir, newCache);
  }

  private void removeDirectoryFromCache(String path, Map<String, Directory> cache) {
    List<String> keysToRemove = new ArrayList<>();

    for (String key : cache.keySet()) {
      if (key.equals(path) || key.startsWith(path + "/")) {
        keysToRemove.add(key);
      }
    }

    for (String key : keysToRemove) {
      cache.remove(key);
    }
  }

  private FileSystem updateDirectoryAndCache(String path, Directory updatedDirectory, Map<String, Directory> newCache) {
    newCache.put(path, updatedDirectory);

    if (path.equals("/")) {
      return new FileSystem(updatedDirectory, currentPath, newCache);
    }

    return updateParentDirectories(path, updatedDirectory, newCache);
  }

  private FileSystem updateParentDirectories(
          String path, Directory updatedDirectory, Map<String, Directory> newCache) {
    String parentPath = getParentPath(path);
    Result<Directory> parentResult = getDirectoryByPath(parentPath);

    if (!parentResult.isSuccess()) {
      return new FileSystem(root, currentPath, newCache);
    }

    Directory parent = parentResult.getValue();
    String dirName = getElementName(path);

    Directory updatedParent = parent.removeElement(dirName).addElement(updatedDirectory);
    newCache.put(parentPath, updatedParent);

    if (parentPath.equals("/")) {
      Directory newRoot = updatedParent;
      return new FileSystem(newRoot, currentPath, newCache);
    }

    return updateParentDirectories(parentPath, updatedParent, newCache);
  }

  public String normalizePath(String path) {
    String fullPath = resolvePath(path);
    if (fullPath.equals("/")) {
      return fullPath;
    }
    String[] parts = fullPath.split("/");
    List<String> cleanParts = new ArrayList<>();

    for (String part : parts) {
      if (part.isEmpty() || part.equals(".")) {
        continue;
      }
        cleanParts.add(part);
    }

    // Devuelve "/" si la lista cleanParts está vacía
    return cleanParts.isEmpty() ? "/" : buildNormalizedPath(cleanParts);
  }

  private String resolvePath(String path) {
    if (path.startsWith("/")) {
      return path;
    } else if (path.equals("..")) {
      return getParentPath(currentPath);
    } else if (path.equals(".")) {
      return currentPath;
    } else {
      return currentPath.equals("/") ? "/" + path : currentPath + "/" + path;
    }
  }

  private String buildNormalizedPath(List<String> cleanParts) {
    if (cleanParts.isEmpty()) {
      return "/";
    }

    StringBuilder result = new StringBuilder("/");
    for (int i = 0; i < cleanParts.size(); i++) {
      result.append(cleanParts.get(i));
      if (i < cleanParts.size() - 1) {
        result.append("/");
      }
    }

    return result.toString();
  }

  private String getParentPath(String path) {
    if (path.equals("/")) {
      return "/";
    }

    int lastSlash = path.lastIndexOf("/");
    if (lastSlash <= 0) {
      return "/";
    }

    return path.substring(0, lastSlash);
  }

  private String getElementName(String path) {
    if (path.equals("/")) {
      return "/";
    }

    int lastSlash = path.lastIndexOf("/");
    if (lastSlash == -1) {
      return path;
    }

    return path.substring(lastSlash + 1);
  }
}