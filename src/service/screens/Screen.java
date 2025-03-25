package src.service.screens;

import src.util.PrintColor;
import src.util.TextColor;

public interface Screen {
    void displayAndProgress();

	default void displayStatuses(String[] statuses, TextColor[] colors){
		for(int i = 0; i < statuses.length; i++){
			PrintColor.printWithColor(statuses[i], colors[i]);
		}
	}

	Character getLastInput();

	void displayPauseAndProgress(String message);

	default void displayQuit(){
		System.out.println();
		InputInterface.DisplayInputOption("Quit", "Q", src.util.TextColor.RED);
	}
}

