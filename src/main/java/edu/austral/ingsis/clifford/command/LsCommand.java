package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.Element;
import edu.austral.ingsis.clifford.FileSystem;
import java.util.ArrayList;
import java.util.List;

public final class LsCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    Directory currentDirectory = fileSystem.getCurrentDirectory();
    if (args.isEmpty()) {
      return listContents(currentDirectory);
    }
    String arg = args.getFirst();
    if (arg.startsWith("--ord=")) {
      return handleOrderedListing(currentDirectory, arg);
    }
    return "Invalid ls usage. Use 'ls', 'ls --ord=asc' or 'ls --ord=desc'.";
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return listContents(fileSystem.getCurrentDirectory());
  }

  @Override
  public boolean isValid(List<String> args) {
    if (args.isEmpty()) {
      return true;
    }
    if (args.size() != 1) {
      return false;
    }
    String arg = args.getFirst();
    return arg.equals("--ord=asc") || arg.equals("--ord=desc");
  }

  @Override
  public String getName() {
    return "ls";
  }

  private String handleOrderedListing(Directory currentDirectory, String arg) {
    String[] split = arg.split("=");
    if (split.length != 2) {
      return "Invalid flag usage. Use --ord=asc or --ord=desc.";
    }
    String order = split[1];
    if (!order.equals("asc") && !order.equals("desc")) {
      return "Invalid ls usage. Use 'ls', 'ls --ord=asc' or 'ls --ord=desc'.";
    }
    return listContentsInOrder(currentDirectory, order);
  }

  private String listContents(Directory directory) {
    return formatContentList(directory.getContent());
  }

  private String listContentsInOrder(Directory directory, String order) {
    if (directory.getContent().isEmpty()) {
      return "";
    }
    List<Element> sortedContent = new ArrayList<>(directory.getContent());
    sortElementsByName(sortedContent, order);
    return formatContentList(sortedContent);
  }

  private void sortElementsByName(List<Element> content, String order) {
    content.sort(
        (e1, e2) -> {
          if (order.equals("asc")) {
            return e1.getName().compareTo(e2.getName());
          }
          return e2.getName().compareTo(e1.getName());
        });
  }

  private String formatContentList(List<Element> content) {
    if (content.isEmpty()) {
      return "";
    }
    StringBuilder result = new StringBuilder();
    formatContentListElements(content, result);
    return result.toString();
  }

  private void formatContentListElements(List<Element> content, StringBuilder result) {
    for (int i = 0; i < content.size(); i++) {
      result.append(content.get(i).getName());
      if (i < content.size() - 1) {
        result.append(" ");
      }
    }
  }
}
