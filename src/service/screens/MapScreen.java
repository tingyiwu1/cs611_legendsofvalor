package src.service.screens;

import java.util.Scanner;

import src.service.game.board.GameBoard;
import src.util.PieceType;
import src.util.PrintColor;
import src.util.TextColor;
import src.util.PrintingUtil;



public class MapScreen implements Screen, InputInterface {

	private GameBoard currGameBoard;
	private int gameSize;
	private Scanner scanny;
	private Character lastInput;


	public MapScreen(GameBoard gameBoard, Scanner scanny){
		this.scanny = scanny;
		this.currGameBoard = gameBoard;
		this.gameSize = gameBoard.getSize();
		this.lastInput = ' ';
		
	}

	@Override 
	public void displayAndProgress(){
		PrintingUtil.clearScreen();
		System.out.println("This is the Map Screen!");
		this.displayMap();
		Character inputtedOption = this.DisplayInputs();
		this.currGameBoard.makeMove(inputtedOption);
	}

	// need to display the game board
	public void displayMap() {
		// rows
		for(int r = 0; r < gameSize; r++){
			for(int i = 0; i < gameSize; i++){
				System.out.print("+-------");
			}
			System.out.println("+");
	
			for (int inner = 0; inner < 3; inner++) {
				for (int c = 0; c < gameSize; c++) {
					PieceType currentPieceType = currGameBoard.getPieceAt(r, c).getPieceType();
					int[] heroLocation = currGameBoard.getCharacterLocation();
					System.out.print("|");
					if(currentPieceType == PieceType.WALL){
						System.out.print(" XXXXX ");
					} else if(heroLocation[0] == r && heroLocation[1] == c && inner == 0){
						PrintColor.red(" H     ");
					} else if(currentPieceType == PieceType.BOSS && inner == 1){
						PrintColor.red(" BOSS! ");
					} else if(currentPieceType == PieceType.MARKET && inner == 2){
						PrintColor.yellow("     M ");
					} else {
						System.out.print("       ");
					}
				}
				System.out.println("|");
			}
		}
		for (int col = 0; col < gameSize; col++) {
			System.out.print("+-------");
		}
		System.out.println("+");
	}

	@Override
	public Character DisplayInputs(){
		System.out.println("These are the inputs!");

		InputInterface.DisplayInputOption("Move Hero North", "W", src.util.TextColor.BLUE);
		InputInterface.DisplayInputOption("Move Hero East", "D", src.util.TextColor.BLUE);
		InputInterface.DisplayInputOption("Move Hero West", "A", src.util.TextColor.BLUE);
		InputInterface.DisplayInputOption("Move Hero South", "S", src.util.TextColor.BLUE);
		InputInterface.DisplayInputOption("Access Inventory", "I", src.util.TextColor.CYAN);

		if(this.currGameBoard.characterAtMarket()){
			InputInterface.DisplayInputOption("Access Market", "M", src.util.TextColor.CYAN);
		}

		this.displayQuit();

		Character input = this.scanny.next().charAt(0);
		this.lastInput = input;
		
		// scanny.close();

		return input;
		

		// return null;
	}

	@Override
	public Character getLastInput(){
		return this.lastInput;
	}

	@Override
	public void displayPauseAndProgress(String message){
		PrintingUtil.clearScreen();
		System.out.println("This is the Map Screen!");
		this.displayMap();

		//you encountered an enemy!
		PrintColor.yellow(message);
		System.out.println();
		InputInterface.DisplayInputOption("Input Any Character to Continue", "", src.util.TextColor.BLUE);
		// no input validation for now
		this.displayQuit();
		Character input = this.scanny.next().charAt(0);
		this.lastInput = input;
		if(input != 'q'){
			this.lastInput = ' ';
		}


	}
}
