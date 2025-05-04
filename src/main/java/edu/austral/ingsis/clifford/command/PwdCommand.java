package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class PwdCommand implements Command {
  @Override
  public CommandResult execute(FileSystem fileSystem, List<String> args) {
    if (!isValid(args)) {
      return CommandResult.error(fileSystem, "No arguments expected for pwd command.");
    }

    return CommandResult.success(fileSystem, fileSystem.getCurrentPath());
  }

  @Override
  public CommandResult execute(FileSystem fileSystem) {
    return CommandResult.success(fileSystem, fileSystem.getCurrentPath());
  }

  @Override
  public boolean isValid(List<String> args) {
    return args.isEmpty();
  }

  @Override
  public String getName() {
    return "pwd";
  }
}