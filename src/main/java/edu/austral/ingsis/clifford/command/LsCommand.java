package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.List;

public final class LsCommand implements Command {

  @Override
  public String execute(FileSystem fileSystem, List<String> args) {
    if (args.isEmpty()) {
      return fileSystem.ls(fileSystem.getCurrentDirectory());
    }
    String arg = args.getFirst();
    if (flagIsOrd(arg)) {
      String[] split = arg.split("=");
      if (split.length != 2) {
        return "Invalid flag usage. Use --ord=asc or --ord=desc.";
      }
      String order = split[1];
      String lsOrdResult = fileSystem.lsOrd(fileSystem.getCurrentDirectory(), order);
      return lsOrdResult;
    }

    return "Invalid ls usage. Use 'ls', 'ls --ord=asc' or 'ls --ord=desc'.";
  }

  @Override
  public String execute(FileSystem fileSystem) {
    return fileSystem.ls(fileSystem.getCurrentDirectory());
  }

  private static boolean flagIsOrd(String flag) {
    return flag.equals("--ord=asc") || flag.equals("--ord=desc");
  }
}
