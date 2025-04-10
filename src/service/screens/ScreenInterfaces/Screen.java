
/**
 * The Screen interface defines the structure for screen components in the application.
 * It provides methods for displaying content, handling user input, and managing screen transitions.
 */
package src.service.screens.ScreenInterfaces;

import src.util.PrintColor;
import src.util.TextColor;

public interface Screen {
	void displayAndProgress();

	default void displayStatuses(String[] statuses, TextColor[] colors) {
		for (int i = 0; i < statuses.length; i++) {
			PrintColor.printWithColor(statuses[i], colors[i]);
		}
	}

	Character getLastInput();

	void displayPauseAndProgress(String message);

	default void displayQuit() {
		System.out.println();
		InputInterface.DisplayInputOption("Quit", "Q", src.util.TextColor.RED);
	}
}
