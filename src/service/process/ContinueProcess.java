package src.service.process;

import java.util.Scanner;

import src.util.PrintColor;

/*
 * Process which simply waits for the user to press Enter.
 */
public class ContinueProcess extends Process<ScreenResult<Void>> {

  private final Scanner scanner;
  private final String prompt;
  private final boolean checkQuit;

  public ContinueProcess(Scanner scanner) {
    this(scanner, false);
  }

  public ContinueProcess(Scanner scanner, boolean checkQuit) {
    this(scanner, checkQuit, "Press Enter to continue...");
  }

  public ContinueProcess(Scanner scanner, boolean checkQuit, String prompt) {
    super(scanner);
    this.scanner = scanner;
    this.prompt = prompt;
    this.checkQuit = checkQuit;
  }

  @Override
  public ScreenResult<Void> run() {
    if (checkQuit) {
      PrintColor.red("[q] Quit");
      System.out.println();
    }
    System.out.println(prompt);
    String input = scanner.nextLine().trim();
    if (checkQuit && input.equalsIgnoreCase("q")) {
      return ScreenResult.quit();
    }
    return ScreenResult.success(null);
  }
}
