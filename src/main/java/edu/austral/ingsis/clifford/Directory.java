package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.results.Result;
import java.util.ArrayList;
import java.util.List;

public final class Directory implements Element {
  private final String name;
  private final String parentPath;
  private final List<Element> content;
  private final boolean isRoot;

  public Directory(String name) {
    this.name = name;
    this.parentPath = "/";
    this.content = new ArrayList<>();
    this.isRoot = true;
  }

  public Directory(String name, String parentPath, List<Element> content) {
    this.name = name;
    this.parentPath = parentPath;
    this.content = new ArrayList<>(content);
    this.isRoot = false;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    if (isRoot) {
      return "/";
    }
    return parentPath + (parentPath.endsWith("/") ? "" : "/") + name;
  }

  @Override
  public String getParentPath() {
    return parentPath;
  }

  @Override
  public boolean isRoot() {
    return isRoot;
  }

  public List<Element> getContent() {
    return new ArrayList<>(content);
  }

  public Directory addElement(Element element) {
    List<Element> newContent = new ArrayList<>(content);
    newContent.add(element);
    return new Directory(name, parentPath, newContent);
  }

  public Directory removeElement(String elementName) {
    List<Element> newContent = new ArrayList<>();
    for (Element element : content) {
      if (!element.getName().equals(elementName)) {
        newContent.add(element);
      }
    }
    return new Directory(name, parentPath, newContent);
  }

  public Result<Element> findElementByName(String elementName) {
    for (Element element : content) {
      if (element.getName().equals(elementName)) {
        return Result.success(element);
      }
    }
    return Result.failure("Element '" + elementName + "' not found");
  }

  public boolean containsElementWithName(String elementName) {
    for (Element element : content) {
      if (element.getName().equals(elementName)) {
        return true;
      }
    }
    return false;
  }
}
