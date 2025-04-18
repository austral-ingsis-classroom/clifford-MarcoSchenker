package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public sealed interface Command
    permits CdCommand, LsCommand, LsHelpCommand, MkdirCommand, PwdCommand, RmCommand, TouchCommand {
  String execute(FileSystem fileSystem, List<String> args);

  String execute(FileSystem fileSystem);
}
