package edu.austral.ingsis.clifford.command;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {
  private final List<Command> commands;

  public CommandRegistry() {
    this.commands = new ArrayList<>();
  }

  private CommandRegistry(List<Command> commands) {
    this.commands = new ArrayList<>(commands);
  }

  public void registerCommand(Command command) {
    if (command != null && !commandExists(command.getName())) {
      commands.add(command);
    }
  }

  private boolean commandExists(String name) {
    for (Command cmd : commands) {
      if (cmd.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public Command findCommandByName(String name) {
    for (Command cmd : commands) {
      if (cmd.getName().equals(name)) {
        return cmd;
      }
    }
    return null;
  }

  public CommandRegistry withCommand(Command command) {
    List<Command> newCommands = new ArrayList<>(commands);

    if (command != null && !commandExists(command.getName())) {
      newCommands.add(command);
    }

    return new CommandRegistry(newCommands);
  }
}
