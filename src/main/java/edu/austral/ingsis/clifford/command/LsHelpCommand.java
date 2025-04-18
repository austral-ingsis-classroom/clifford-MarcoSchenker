package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class LsHelpCommand implements Command {

  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    return "No arguments expected for lsHelp command.";
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return """
                ls: List files in the current directory.
                ls --ord=<asc|desc>: List files in the current directory in ascending or descending order.
                lsHelp: Show this help message.
                """;
  }
}
