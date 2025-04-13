package src.service.screens.ScreenInterfaces;

import java.util.Scanner;

/**
 * Represents a process which, when run, interacts with the user and returns a
 * result.
 */
public abstract class Process<T extends Process.Result> {
  public static interface Result {
  }

  protected final Scanner scanner;

  protected Process(Scanner scanner) {
    this.scanner = scanner;
  }

  public abstract T run();
}
