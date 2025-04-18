package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class RmCommand implements Command {

  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (isRecursiveFlag(args)) {
      return handleRecursive(fileSystem, args);
    }
    return handleNonRecursive(fileSystem, args);
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return "Expected arguments for rm command.";
  }

  private boolean isRecursiveFlag(List<String> args) {
    return args.getFirst().equals("--recursive");
  }

  private String handleRecursive(FileSystem fileSystem, List<String> args) {
    if (args.size() != 2) {
      return "Usage: rm --recursive <directoryName>";
    }
    String target = args.get(1);
    return fileSystem.rmRecursive(target);
  }

  private String handleNonRecursive(FileSystem fileSystem, List<String> args) {
    if (args.size() != 1) return "rm expects a single argument or --recursive <directoryName>";
    String target = args.getFirst();
    return fileSystem.rm(target);
  }
}
