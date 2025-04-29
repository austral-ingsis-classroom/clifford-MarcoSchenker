package edu.austral.ingsis.clifford.command;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {
  private final List<Command> commands = new ArrayList<>();

  public void registerCommand(Command command) {
    if (command != null && findCommandByName(command.getName()) == null) {
      commands.add(command);
    }
  }

  public Command findCommandByName(String name) {
    return commands.stream().filter(cmd -> cmd.getName().equals(name)).findFirst().orElse(null);
  }
}
