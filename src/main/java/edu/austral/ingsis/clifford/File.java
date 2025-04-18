package edu.austral.ingsis.clifford;

public final class File implements Element {
  private final String name;
  private final Directory father;

  public File(String name, Directory father) {
    this.name = name;
    this.father = father;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Directory getFather() {
    return father;
  }
}
