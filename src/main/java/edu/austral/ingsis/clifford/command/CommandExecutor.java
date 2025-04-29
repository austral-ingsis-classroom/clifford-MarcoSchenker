package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

public class CommandExecutor {
  private final FileSystem fileSystem;
  private final CommandRegistry commandRegistry;

  public CommandExecutor() {
    this.fileSystem = new FileSystem();
    this.commandRegistry = new CommandRegistry();
    setupCommands();
  }

  private void setupCommands() {
    // El Service Loader me agrega todas las clases que implementan Command a la lista
    List<Command> discoveredCommands =
        ServiceLoader.load(Command.class).stream().map(ServiceLoader.Provider::get).toList();

    if (!discoveredCommands.isEmpty()) {
      discoveredCommands.forEach(commandRegistry::registerCommand);
    } else {
      registerDefaultCommands();
    }
  }

  // Los comandos que ya cree. Si quiero registrar un comando simplemento debería registrarlo
  // aparte, no tengo que modificar codigo
  private void registerDefaultCommands() {
    commandRegistry.registerCommand(new LsCommand());
    commandRegistry.registerCommand(new LsHelpCommand());
    commandRegistry.registerCommand(new CdCommand());
    commandRegistry.registerCommand(new PwdCommand());
    commandRegistry.registerCommand(new MkdirCommand());
    commandRegistry.registerCommand(new TouchCommand());
    commandRegistry.registerCommand(new RmCommand());
  }

  public String execute(String input) {
    if (input == null || input.isBlank()) {
      return "Unknown command: ";
    }
    String[] parts = input.strip().split(" ");
    if (parts.length == 0) {
      return "No command provided.";
    }
    return executeCommand(parts);
  }

  private String executeCommand(String[] parts) {
    String commandName = parts[0];
    List<String> args = extractArgs(parts);
    Command command = commandRegistry.findCommandByName(commandName);
    if (command == null) {
      return "Unknown command: " + commandName;
    }
    return args.isEmpty() ? command.execute(fileSystem) : command.execute(fileSystem, args);
  }

  private List<String> extractArgs(String[] parts) {
    return Arrays.asList(parts).subList(1, parts.length);
  }

  public void registerCommand(Command command) {
    commandRegistry.registerCommand(command);
  }
}
