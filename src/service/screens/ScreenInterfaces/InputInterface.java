

/**
 * The InputInterface defines a contract for displaying input options and handling user inputs
 * in the application. It provides methods for displaying input prompts with customizable
 * text and color formatting.
 */
package src.service.screens.ScreenInterfaces;
import src.util.PrintColor;
import src.util.TextColor;

public interface InputInterface {
    Character DisplayInputs();

	static void DisplayInputOption(String questionText, String charInput, TextColor colorCode) {
		PrintColor.printWithColor(questionText + ": " + charInput, colorCode);
	}

	static void DisplayInputOption(String questionText, String charInput, String divisor, TextColor colorCode) {
		PrintColor.printWithColor(questionText + divisor + " " + charInput, colorCode);
	}
}

