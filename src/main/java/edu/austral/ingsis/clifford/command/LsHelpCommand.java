package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class LsHelpCommand implements Command {

  @Override
  public CommandResult execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return CommandResult.error(fileSystem, "No arguments expected for lsHelp command.");
    }
    return CommandResult.success(fileSystem, getHelpText());
  }

  @Override
  public CommandResult execute(FileSystem fileSystem) {
    return CommandResult.success(fileSystem, getHelpText());
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
