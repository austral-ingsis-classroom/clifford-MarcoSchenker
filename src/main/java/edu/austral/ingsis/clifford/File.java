package edu.austral.ingsis.clifford;

public final class File implements Element {
  private final String name;
  private final String parentPath;

  public File(String name, String parentPath) {
    this.name = name;
    this.parentPath = parentPath;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    return parentPath + (parentPath.endsWith("/") ? "" : "/") + name;
  }

  @Override
  public String getParentPath() {
    return parentPath;
  }

  @Override
  public boolean isRoot() {
    return false;
  }
}