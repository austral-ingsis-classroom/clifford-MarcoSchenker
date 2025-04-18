package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandExecutor {
  private final Map<String, Command> commands = new HashMap<>();

  public CommandExecutor() {
    commands.put("ls", new LsCommand());
    commands.put("lsHelp", new LsHelpCommand());
    commands.put("cd", new CdCommand());
    commands.put("pwd", new PwdCommand());
    commands.put("mkdir", new MkdirCommand());
    commands.put("touch", new TouchCommand());
    commands.put("rm", new RmCommand());
  }

  public String execute(String input, FileSystem fileSystem) {
    String[] parts = getSplit(input);
    if (parts.length == 0) {
      return "No command provided.";
    }
    String commandName = parts[0];
    List<String> args = getArgs(parts);

    Command command = commands.get(commandName);
    if (command == null) {
      return "Unknown command: " + commandName;
    }
    if (args.isEmpty()) {
      return getExecute(fileSystem, command);
    }
    String excuted = getExecute(fileSystem, command, args);
    return excuted;
  }

  private static List<String> getArgs(String[] parts) {
    return Arrays.asList(parts).subList(1, parts.length);
  }

  private static String getExecute(FileSystem fileSystem, Command command, List<String> args) {
    return command.execute(fileSystem, args);
  }

  private static String getExecute(FileSystem fileSystem, Command command) {
    return command.execute(fileSystem);
  }

  private static String[] getSplit(String input) {
    return input.strip().split(" ");
  }
}
