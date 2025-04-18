package edu.austral.ingsis;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FileSystemTests {

  private final FileSystemRunner runner = new FileSystemRunner();

  private void executeTest(List<Map.Entry<String, String>> commandsAndResults) {
    final List<String> commands = commandsAndResults.stream().map(Map.Entry::getKey).toList();
    final List<String> expectedResult =
        commandsAndResults.stream().map(Map.Entry::getValue).toList();

    final List<String> actualResult = runner.executeCommands(commands);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void test1() {
    executeTest(
        List.of(
            entry("ls", ""),
            entry("mkdir horace", "'horace' directory created"),
            entry("ls", "horace"),
            entry("mkdir emily", "'emily' directory created"),
            entry("ls", "horace emily"),
            entry("ls --ord=asc", "emily horace")));
  }

  @Test
  void test2() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("mkdir emily", "'emily' directory created"),
            entry("mkdir jetta", "'jetta' directory created"),
            entry("ls", "horace emily jetta"),
            entry("cd emily", "moved to directory 'emily'"),
            entry("pwd", "/emily"),
            entry("touch elizabeth.txt", "'elizabeth.txt' file created"),
            entry("mkdir t-bone", "'t-bone' directory created"),
            entry("ls", "elizabeth.txt t-bone")));
  }

  @Test
  void test3() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("mkdir emily", "'emily' directory created"),
            entry("mkdir jetta", "'jetta' directory created"),
            entry("cd emily", "moved to directory 'emily'"),
            entry("touch elizabeth.txt", "'elizabeth.txt' file created"),
            entry("mkdir t-bone", "'t-bone' directory created"),
            entry("ls", "elizabeth.txt t-bone"),
            entry("rm t-bone", "cannot remove 't-bone', is a directory"),
            entry("rm --recursive t-bone", "'t-bone' removed"),
            entry("ls", "elizabeth.txt"),
            entry("rm elizabeth.txt", "'elizabeth.txt' removed"),
            entry("ls", "")));
  }

  @Test
  void test4() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("mkdir emily", "'emily' directory created"),
            entry("cd horace", "moved to directory 'horace'"),
            entry("mkdir jetta", "'jetta' directory created"),
            entry("cd ..", "moved to directory '/'"),
            entry("cd horace/jetta", "moved to directory 'jetta'"),
            entry("pwd", "/horace/jetta"),
            entry("cd /", "moved to directory '/'")));
  }

  @Test
  void test5() {
    executeTest(
        List.of(
            entry("mkdir emily", "'emily' directory created"),
            entry("cd horace", "'horace' directory does not exist")));
  }

  @Test
  void test6() {
    executeTest(List.of(entry("cd ..", "moved to directory '/'")));
  }

  @Test
  void test7() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("cd horace", "moved to directory 'horace'"),
            entry("touch emily.txt", "'emily.txt' file created"),
            entry("touch jetta.txt", "'jetta.txt' file created"),
            entry("ls", "emily.txt jetta.txt"),
            entry("rm emily.txt", "'emily.txt' removed"),
            entry("ls", "jetta.txt")));
  }

  @Test
  void test8() {
    executeTest(
        List.of(
            entry("mkdir emily", "'emily' directory created"),
            entry("cd emily", "moved to directory 'emily'"),
            entry("mkdir emily", "'emily' directory created"),
            entry("touch emily.txt", "'emily.txt' file created"),
            entry("touch jetta.txt", "'jetta.txt' file created"),
            entry("ls", "emily emily.txt jetta.txt"),
            entry("rm --recursive emily", "'emily' removed"),
            entry("ls", "emily.txt jetta.txt"),
            entry("ls --ord=desc", "jetta.txt emily.txt")));
  }

  @Test
  void test9_removeNonExistentFile() {
    executeTest(List.of(entry("rm file.txt", "'file.txt' does not exist")));
  }

  @Test
  void test10_removeNonExistentDirWithRecursive() {
    executeTest(List.of(entry("rm --recursive dir", "'dir' does not exist")));
  }

  @Test
  void test11_touchDuplicateFile() {
    executeTest(
        List.of(
            entry("touch file.txt", "'file.txt' file created"),
            entry("touch file.txt", "file already exists")));
  }

  @Test
  void test12_mkdirDuplicate() {
    executeTest(
        List.of(
            entry("mkdir dir", "'dir' directory created"),
            entry("mkdir dir", "directory already exists")));
  }

  @Test
  void test13_cdToFile() {
    executeTest(
        List.of(
            entry("touch file.txt", "'file.txt' file created"),
            entry("cd file.txt", "'file.txt' directory does not exist")));
  }

  @Test
  void test14_invalidLsFlag() {
    executeTest(
        List.of(
            entry("ls --ord=a", "Invalid ls usage. Use 'ls', 'ls --ord=asc' or 'ls --ord=desc'.")));
  }

  @Test
  void test15_invalidCommand() {
    executeTest(List.of(entry("fly", "Unknown command: fly")));
  }

  @Test
  void test16_emptyInput() {
    executeTest(List.of(entry("", "Unknown command: ")));
  }

  @Test
  void test17_cdToInvalidPath() {
    executeTest(
        List.of(entry("cd nonexistent/path", "'nonexistent/path' directory does not exist")));
  }

  @Test
  void test18_rmWithExtraArgs() {
    executeTest(
        List.of(
            entry(
                "rm file1 file2", "rm expects a single argument or --recursive <directoryName>")));
  }

  @Test
  void test19_CdCommandNoArguments() {
    executeTest(List.of(entry("cd", "Expected arguments for cd command.")));
  }

  @Test
  void test20_LsHelpCommand() {
    executeTest(
        List.of(
            entry(
                "lsHelp",
                "ls: List files in the current directory.\n"
                    + "ls --ord=<asc|desc>: List files in the current directory in ascending or descending order.\n"
                    + "lsHelp: Show this help message.\n")));
  }

  @Test
  void test21_MkdirCommandNoArguments() {
    executeTest(List.of(entry("mkdir", "Expected arguments for mkdir command.")));
  }

  @Test
  void test21Bis_MkdirCommandMoreArguments() {
    executeTest(List.of(entry("mkdir a b", "mkdir expects exactly one argument.")));
  }

  @Test
  void test22_PwdCommandWithArguments() {
    executeTest(List.of(entry("pwd extraArg", "No arguments expected for pwd command.")));
  }

  @Test
  void test23_RmCommandNoArguments() {
    executeTest(List.of(entry("rm", "Expected arguments for rm command.")));
  }

  @Test
  void test23Bis_RmCommandTwoArgumentsNoRecursive() {
    executeTest(
        List.of(entry("rm a b", "rm expects a single argument or --recursive <directoryName>")));
  }

  @Test
  void test23Ter_RmCommandThreeArguments() {
    executeTest(List.of(entry("rm --recursive", "Usage: rm --recursive <directoryName>")));
  }

  @Test
  void test24_TouchCommandNoArguments() {
    executeTest(List.of(entry("touch", "Expected arguments for touch command.")));
  }

  @Test
  void test24Bis_TouchCommandMoreThan1Argument() {
    executeTest(List.of(entry("touch a b", "touch expects exactly one argument.")));
  }

  @Test
  void testFileGetFather() {
    Directory parent = new Directory("parent", null, new ArrayList<>());
    File file = new File("file.txt", parent);
    assertEquals(parent, file.getFather());
  }
}
