package edu.austral.ingsis;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.command.CommandExecutor;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunner {

  private final FileSystem fileSystem = new FileSystem();
  private final CommandExecutor commandExecutor = new CommandExecutor();

  public List<String> executeCommands(List<String> commands) {
    List<String> results = new ArrayList<>();

    for (String command : commands) {
      String result = commandExecutor.execute(command, fileSystem);
      results.add(result);
    }

    return results;
  }
}
