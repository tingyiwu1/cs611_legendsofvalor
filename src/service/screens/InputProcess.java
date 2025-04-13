package src.service.screens;

import java.util.List;
import java.util.Scanner;

import src.service.screens.ScreenInterfaces.Process;
import src.util.PrintColor;
import src.util.TextColor;

public class InputProcess<T> implements Process<InputResult<T>> {
  public static class Option<T> {
    private final String actionDescription;
    private final String charInput;
    private final TextColor colorCode;
    private final T result;

    public Option(String charInput, String actionDescription, TextColor colorCode, T result) {
      this.actionDescription = actionDescription;
      this.charInput = charInput;
      this.colorCode = colorCode;
      this.result = result;
    }

  }

  private final Scanner scanner;
  private final List<Option<T>> options;

  public InputProcess(Scanner scanner, List<Option<T>> options) {
    this.scanner = scanner;
    this.options = options;
  }

  private void displayInputOption(Option<T> option) {
    PrintColor.printWithColor("[" + option.charInput + "] " + option.actionDescription, option.colorCode);
  }

  @Override
  public InputResult<T> run() {
    for (Option<T> option : options) {
      displayInputOption(option);
    }
    String input = scanner.next().trim();
    for (Option<T> option : options) {
      if (option.charInput.equalsIgnoreCase(input)) {
        return InputResult.of(option.result);
      }
    }
    return InputResult.invalid();
  }

}
