/**
 * The IntroScreen class represents the introductory screen of a turn-based RPG game.
 * It implements the Screen interface and provides functionality for displaying the
 * game's introduction, initializing the player, and handling user input for game setup.
 */
package src.service.screens;

import java.util.Scanner;

import src.service.entities.Player;
import src.service.entities.Player.Difficulty;
import src.service.screens.ScreenInterfaces.InputInterface;
import src.service.screens.ScreenInterfaces.Screen;
import src.util.PrintColor;
import src.util.PrintingUtil;
import src.util.TextColor;

public class IntroScreen implements Screen {

	private Scanner scanny;
	private Character lastInput;

	public IntroScreen(Scanner scanny) {
		this.scanny = scanny;
	}

	public Player initializePlayer() {
		PrintingUtil.clearScreen();

		System.out.println("Welcome to the game! This is a RPG-type turn based fighting game.");
		System.out.println(
				"Your goal is to survive for as long as possible, while defeating bosses, obtaining new gear, and levelling up!");
		System.out.println(this.rainbowText("Live through your dream for as long as possible!"));
		System.out.println("Select a difficulty!");
		InputInterface.DisplayInputOption("Difficulty: Easy", "1", src.util.TextColor.RED);
		InputInterface.DisplayInputOption("Difficulty: Medium", "2", src.util.TextColor.RED);
		InputInterface.DisplayInputOption("Difficulty: Hard", "3", src.util.TextColor.RED);
		this.displayQuit();
		Character input = this.scanny.next().charAt(0);
		this.lastInput = input;
		Player.Difficulty diff = Difficulty.EASY;
		int numHeroes = 3;
		while (true) {
			switch (input) {
				case '1':
					diff = Difficulty.EASY;
					break;
				case '2':
					diff = Difficulty.MEDIUM;
					break;
				case '3':
					diff = Difficulty.HARD;
					break;
				case 'q':
					System.out.println("im here!!");
					this.lastInput = 'q';
					break;
				default:
					System.out.println("Invalid input. Please enter 1, 2, or 3.");
					input = this.scanny.next().charAt(0);
					continue;
			}
			break;
		}

		if (!lastInput.equals('q')) {
			System.out.println("Select the number of heroes in your party (1-3):");
			InputInterface.DisplayInputOption(PrintingUtil.printWithPadding("1 Hero: The Warrior", 50), "1",
					src.util.TextColor.GREEN);
			InputInterface.DisplayInputOption(PrintingUtil.printWithPadding("2 Heros: The Warrior / The Mage", 50), "2",
					src.util.TextColor.YELLOW);
			InputInterface.DisplayInputOption(
					PrintingUtil.printWithPadding("3 Heros: The Warrior / The Mage / The Assassin", 50), "3",
					src.util.TextColor.RED);
			this.displayQuit();
			while (true) {
				try {
					String inputStr = this.scanny.next();
					if (inputStr.equalsIgnoreCase("q")) {
						this.lastInput = 'q';
						break;
					}
					numHeroes = Integer.parseInt(inputStr);
					if (numHeroes >= 1 && numHeroes <= 3) {
						break;
					} else {
						System.out.println("Invalid input. Please enter a number between 1 and 3.");
					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter a valid number.");
				}
			}
		}

		return new Player(diff, numHeroes); // Assuming 3 heroes as default party size
	}

	public void DisplayIntroToGame() {
		PrintingUtil.clearScreen();
		System.out.println("After a long day of work and coding, you sink into bed, ready to sleep");
		System.out.println("...");
		System.out.println(this.rainbowText("... You close your eyes ... "));
		System.out.println("...");
		System.out.println(this.rainbowText(" ... And start having strange dreams ..."));
		System.out.println("...");
		System.out.println();
		InputInterface.DisplayInputOption("Input Any Character to Continue", "", src.util.TextColor.BLUE);
		// no input validation for now
		this.displayQuit();
		Character input = this.scanny.next().charAt(0);
		this.lastInput = input;
		if (input != 'q') {
			this.lastInput = ' ';
		}
	}

	@Override
	public void displayAndProgress() {
		DisplayIntroToGame();
	}

	@Override
	public Character getLastInput() {
		return this.lastInput;
	}

	@Override
	public void displayPauseAndProgress(String message) {
		throw new UnsupportedOperationException("Unimplemented method 'displayPauseAndProgress'");
	}

	public String rainbowText(String status) {
		TextColor[] rainbowColors = {
				TextColor.RED, TextColor.ORANGE, TextColor.YELLOW,
				TextColor.GREEN, TextColor.BLUE, TextColor.PURPLE
		};

		StringBuilder rainbowStatus = new StringBuilder();
		for (int i = 0; i < status.length(); i++) {
			TextColor color = rainbowColors[i % rainbowColors.length];
			rainbowStatus.append(PrintColor.textWithColor(String.valueOf(status.charAt(i)), color));
		}
		return rainbowStatus.toString();
	}

}
