package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.Element;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.results.Result;
import java.util.ArrayList;
import java.util.List;

public final class LsCommand implements Command {
  @Override
  public CommandResult execute(FileSystem fileSystem, List<String> args) {
    Result<Directory> dirResult = fileSystem.getCurrentDirectory();

    if (!dirResult.isSuccess()) {
      return CommandResult.error(fileSystem, dirResult.getErrorMessage());
    }

    Directory currentDirectory = dirResult.getValue();

    if (args.isEmpty()) {
      return CommandResult.success(fileSystem, listContents(currentDirectory));
    }

    return handleLsWithArguments(fileSystem, currentDirectory, args);
  }

  private CommandResult handleLsWithArguments(
      FileSystem fileSystem, Directory directory, List<String> args) {
    String arg = args.get(0);

    if (!arg.startsWith("--ord=")) {
      return CommandResult.error(
          fileSystem, "Invalid ls usage. Use 'ls', 'ls --ord=asc' or 'ls --ord=desc'.");
    }

    return handleOrderedListing(fileSystem, directory, arg);
  }

  private CommandResult handleOrderedListing(
      FileSystem fileSystem, Directory directory, String arg) {
    String[] split = arg.split("=");

    if (split.length != 2) {
      return CommandResult.error(fileSystem, "Invalid flag usage. Use --ord=asc or --ord=desc.");
    }

    String order = split[1];

    if (!order.equals("asc") && !order.equals("desc")) {
      return CommandResult.error(
          fileSystem, "Invalid ls usage. Use 'ls', 'ls --ord=asc' or 'ls --ord=desc'.");
    }

    return CommandResult.success(fileSystem, listContentsInOrder(directory, order));
  }

  @Override
  public CommandResult execute(FileSystem fileSystem) {
    Result<Directory> dirResult = fileSystem.getCurrentDirectory();

    if (!dirResult.isSuccess()) {
      return CommandResult.error(fileSystem, dirResult.getErrorMessage());
    }

    return CommandResult.success(fileSystem, listContents(dirResult.getValue()));
  }

  @Override
  public boolean isValid(List<String> args) {
    if (args.isEmpty()) {
      return true;
    }

    if (args.size() != 1) {
      return false;
    }

    String arg = args.get(0);
    return arg.equals("--ord=asc") || arg.equals("--ord=desc");
  }

  @Override
  public String getName() {
    return "ls";
  }

  private String listContents(Directory directory) {
    return formatContentList(directory.getContent());
  }

  private String listContentsInOrder(Directory directory, String order) {
    List<Element> content = directory.getContent();

    if (content.isEmpty()) {
      return "";
    }

    List<Element> sortedContent = new ArrayList<>(content);
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
