package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class LsHelpCommand implements Command {

  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return "No arguments expected for lsHelp command.";
    }
    return getHelpText();
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return getHelpText();
  }

  @Override
  public boolean isValid(List<String> args) {
    return args.isEmpty();
  }

  @Override
  public String getName() {
    return "lsHelp";
  }

  private String getHelpText() {
    return """
                ls: List files in the current directory.
                ls --ord=<asc|desc>: List files in the current directory in ascending or descending order.
                lsHelp: Show this help message.
                """;
  }
}
