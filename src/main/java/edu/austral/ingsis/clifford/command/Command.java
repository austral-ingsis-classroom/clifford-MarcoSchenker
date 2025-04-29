package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public interface Command {

  String execute(FileSystem fileSystem, List<String> args);

  String execute(FileSystem fileSystem);

  boolean isValid(List<String> args);

  String getName();
}
