package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class PwdCommand implements Command {
  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    return "No arguments expected for pwd command.";
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return fileSystem.pwd();
  }
}
