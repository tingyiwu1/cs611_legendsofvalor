package src.service.screens;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

import src.service.screens.ScreenInterfaces.Process;
import src.util.PrintColor;
import src.util.TextColor;

public class InputProcess<T> implements Process<InputResult<T>> {
  public static class Option<T> {
    private final String actionDescription;
    private final String charInput;
    private final TextColor colorCode;
    private final Function<String, Optional<T>> parseInput;

    public Option(String charInput, String actionDescription, TextColor colorCode,
        T output) {
      this(charInput, actionDescription, colorCode,
          (input) -> input.equalsIgnoreCase(charInput) ? Optional.of(output) : Optional.empty());
    }

    public Option(String charInput, String actionDescription, TextColor colorCode,
        Function<String, Optional<T>> parseInput) {
      this.actionDescription = actionDescription;
      this.charInput = charInput;
      this.colorCode = colorCode;
      this.parseInput = parseInput;
    }
  }

  private final Scanner scanner;
  private final List<Option<T>> options;
  private final String prompt;

  public InputProcess(Scanner scanner, List<Option<T>> options, String prompt) {
    this.scanner = scanner;
    this.options = options;
    this.prompt = prompt;
  }

  private void displayInputOption(Option<T> option) {
    PrintColor.printWithColor("[" + option.charInput + "] " + option.actionDescription, option.colorCode);
  }

  @Override
  public InputResult<T> run() {
    System.out.println(prompt);
    for (Option<T> option : options) {
      displayInputOption(option);
    }
    String input = scanner.nextLine().trim();
    for (Option<T> option : options) {
      if (option.parseInput.apply(input).isPresent()) {
        return InputResult.of(option.parseInput.apply(input).get());
      }
    }
    return InputResult.invalid();
  }

}
