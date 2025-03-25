package src.service.game;
import src.service.game.board.GameBoard;
import src.service.game.inventory.Inventory;
import src.service.screens.ScreenContext;
import src.service.entities.Player;

import java.util.Scanner;

import src.service.game.MainGame;
import src.service.screens.MapScreen;
import src.service.game.battle.Battle;
import src.service.screens.BattleScreen;
import src.service.screens.InventoryScreen;
import src.service.entities.monsters.Monster;
import src.util.ScreenState;

public class MainGame {

	GameBoard currentBoard;
	Battle currBattle;
	Player currentPlayer;
	ScreenState currentScreen;
	ScreenState previousScreen;

	public MainGame(){
		this.currentBoard = new GameBoard(8, 0.1, 0.2);
		this.currentPlayer = new Player();
		this.currentScreen = ScreenState.MAP;
		this.currBattle = null;
		this.previousScreen = null;
	}

	public void playGame(){
		System.out.println("Welcome to the game!!");

		Scanner scanny = new Scanner(System.in);

		ScreenContext myScreen = new ScreenContext(new MapScreen(currentBoard, scanny));

		myScreen.displayScreen();

		while(true){

			Character lastInput = myScreen.getLastInput();

			//opening up the inventory
			if(lastInput == 'i'){
				this.previousScreen = this.currentScreen;

				this.currentScreen = ScreenState.INVENTORY;
				myScreen.setScreen(new InventoryScreen(this.currentPlayer, scanny));
			}

			// if we are in the inventory screen and we want to go back to the previous screen
			if(this.currentScreen == ScreenState.INVENTORY && lastInput == 'b'){
				this.currentScreen = this.previousScreen;
				myScreen.setScreen(new MapScreen(this.currentBoard, scanny));
				this.previousScreen = null;
			}

			if(this.currBattle != null && this.currBattle.isBattleOver()){
				myScreen.getScreen().displayPauseAndProgress("Battle is over!");
				lastInput = myScreen.getLastInput();
				if(lastInput == 'q'){
					break;
				}

				this.currentScreen = ScreenState.MAP;
				myScreen.setScreen(new MapScreen(this.currentBoard, scanny));
				this.currBattle = null;
			}

			// if last input was movement, check if we need to enter battle

			if(this.currentScreen == ScreenState.MAP && this.currentBoard.isMoveValid(lastInput)
				 && lastInput != null && (lastInput == 'w' || lastInput == 'a' 
				|| lastInput == 's' || lastInput == 'd')){

					myScreen.getScreen().displayPauseAndProgress("You encountered an enemy!");
					lastInput = myScreen.getLastInput();
					if(lastInput == 'q'){
						break;
					}

					System.out.println("Entering battle!");
					this.currentScreen = ScreenState.BATTLE;
					this.currBattle = new Battle(currentPlayer, new Monster());
					myScreen.setScreen(new BattleScreen(this.currBattle, scanny));
				}
			// if(this.currentScreen == ScreenState.BATTLE && this.currBattle.isBattleOver()){
				

			// 	this.currentScreen = ScreenState.MAP;
			// 	myScreen.setScreen(new MapScreen(this.currentBoard, scanny));
			// } 

			myScreen.displayScreen();


			lastInput = myScreen.getLastInput();
			if(lastInput == 'q'){
				break;
			}
		
		}


		scanny.close();
		System.out.println("Thanks for playing!");
		// something.printStats();
	}
	
}
