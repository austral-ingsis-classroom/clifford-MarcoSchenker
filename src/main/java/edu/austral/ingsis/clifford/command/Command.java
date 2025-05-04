package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public interface Command {
  CommandResult execute(FileSystem fileSystem, List<String> args);

  CommandResult execute(FileSystem fileSystem);

  boolean isValid(List<String> args);

  String getName();
}