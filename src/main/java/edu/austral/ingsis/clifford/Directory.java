package edu.austral.ingsis.clifford;

import java.util.List;

public final class Directory implements Element {
  private final String name;
  private final Directory father;
  private final List<Element> content;

  public Directory(String name, Directory father, List<Element> content) {
    this.name = name;
    this.father = father;
    this.content = content;
  }

  public Directory(String name, List<Element> content) {
    this.name = name;
    this.content = content;
    this.father = null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Directory getFather() {
    return father;
  }

  public List<Element> getContent() {
    return content;
  }
}
