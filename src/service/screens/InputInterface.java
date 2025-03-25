package src.service.screens;
import src.util.PrintColor;
import src.util.TextColor;

public interface InputInterface {
    Character DisplayInputs();

	static void DisplayInputOption(String questionText, String charInput, TextColor colorCode) {
		PrintColor.printWithColor(questionText + ": " + charInput, colorCode);
	}
}

