package src.service.screens;

import java.util.Scanner;

import src.service.screens.ScreenInterfaces.Process;

public class ContinueProcess implements Process<ContinueProcess.ContinueResult> {
  public static enum ContinueResult implements Process.Result {
    INSTANCE
  }

  private final Scanner scanner;
  private final String prompt;

  public ContinueProcess(Scanner scanner) {
    this(scanner, "Press Enter to continue...");
  }

  public ContinueProcess(Scanner scanner, String prompt) {
    this.scanner = scanner;
    this.prompt = prompt;
  }

  @Override
  public ContinueResult run() {
    System.out.println(prompt);
    scanner.nextLine();
    return ContinueResult.INSTANCE;
  }
}
