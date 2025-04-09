/**
 * The MapScreen class represents a screen in the game that displays the game board
 * and allows the player to interact with it by providing inputs. It implements the
 * Screen and InputInterface interfaces to handle screen display and user input.
 */
package src.service.screens;

import java.util.ArrayList;
import java.util.Scanner;

import src.service.entities.Entity;
import src.service.entities.Entity.EntityType;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Position;
import src.service.game.TurnKeeper;
import src.service.game.board.GameBoard;
import src.service.screens.ScreenInterfaces.InputInterface;
import src.service.screens.ScreenInterfaces.Screen;
import src.util.PieceType;
import src.util.PrintColor;
import src.util.PrintingUtil;



public class MapScreen implements Screen, InputInterface {

	private GameBoard currGameBoard;
	private int gameSize;
	private Scanner scanny;
	private Character lastInput;
	private TurnKeeper turnKeeper;


	public MapScreen(GameBoard gameBoard, Scanner scanny, TurnKeeper turnKeeper) {
		this.scanny = scanny;
		this.currGameBoard = gameBoard;
		this.gameSize = gameBoard.getSize();
		this.lastInput = ' ';
		this.turnKeeper = turnKeeper;
		
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

		ArrayList<Entity> entityList = currGameBoard.getEntityList();
		ArrayList<Position> entityPositions = new ArrayList<Position>();
		for (Entity entity : entityList) {
			entityPositions.add(entity.getPosition());
		}
		// rows
		for (int r = 0; r < gameSize; r++) {
			for (int i = 0; i < gameSize; i++) {
				System.out.print("+-------");
			}
			System.out.println("+");

			for (int inner = 0; inner < 3; inner++) {
				for (int c = 0; c < gameSize; c++) {
					PieceType currentPieceType = currGameBoard.getPieceAt(r, c).getPieceType();
					System.out.print("|");
					if (currentPieceType == PieceType.WALL) {
						System.out.print(" XXXXX ");
					} else if (inner == 0 && entityPositions.contains(new Position(r, c))) {
						// Check if a hero is at this position and print " H " in the center row
						boolean hasHero = false;
						boolean hasMonster = false;
						boolean isActiveHero = false;
						for (int idx = 0; idx < entityList.size(); idx++) {
							Entity entity = entityList.get(idx);
							if (entity.getPosition().equals(new Position(r, c))) {
								if (entity.getType() == EntityType.HERO) {
									hasHero = true;
									if(turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER){
										if (idx == turnKeeper.getPlayerTeamTurnCount()) {
											isActiveHero = true;
										}
									}
								} else if (entity.getType() == EntityType.MONSTER) {
									hasMonster = true;
								}
							}
						}
						// TODO: IS THERE A BETTER WAY TO DISPLAY WHICH HERO IS ACTIVE?
						if(isActiveHero){
							if (hasHero && hasMonster) {
								PrintColor.blue("AH  ");
								PrintColor.yellow(" M ");
							} else if (hasHero) {
								PrintColor.blue("AH     ");
							} else if (hasMonster) {
								PrintColor.yellow("     M ");
							} else {
								System.out.print("       ");
							}
						} else {
							if (hasHero && hasMonster) {
								PrintColor.red(" H  ");
								PrintColor.yellow(" M ");
							} else if (hasHero) {
								PrintColor.red(" H     ");
							} else if (hasMonster) {
								PrintColor.yellow("     M ");
							} else {
								System.out.print("       ");
							}
						}
						
					} else if (currentPieceType == PieceType.HERO_NEXUS && inner == 1) {
						PrintColor.blue(" NEXUS ");
					} else if (currentPieceType == PieceType.MONSTER_NEXUS && inner == 1) {
						PrintColor.red(" NEXUS ");
					} else if(currentPieceType == PieceType.BUSH && inner == 1){
						System.out.print("  ~B~  ");
					}else if(currentPieceType == PieceType.CAVE && inner == 1){
						System.out.print("  [C]  ");
					}else if(currentPieceType == PieceType.KOULOU && inner == 1){
						System.out.print("  _K_  ");
						
					}else if(currentPieceType == PieceType.OBSTACLE && inner == 1){
						System.out.print("  xxx  ");
						
					}
					else if (currentPieceType == PieceType.MARKET && inner == 2) {
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
		System.out.println("H = Hero, M = Monster, NEXUS = Nexus, X = Wall");
		if(turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER){
			System.out.println("Current Hero: " + currGameBoard.getEntityList().get(turnKeeper.getPlayerTeamTurnCount()).getName());
		} else {
			/*
			 * Handle enemy turn progression!
			 */
			System.out.println("TODO: Handle Enemy Turn");
		}
		
	}

	


	@Override
	public Character DisplayInputs(){
		System.out.println("These are the inputs!");


		// TODO: make this list of options more pretty ==> stack attacks on the right?
		InputInterface.DisplayInputOption("Move Hero North", "W", src.util.TextColor.BLUE);
		InputInterface.DisplayInputOption("Move Hero East", "D", src.util.TextColor.BLUE);
		InputInterface.DisplayInputOption("Move Hero West", "A", src.util.TextColor.BLUE);
		InputInterface.DisplayInputOption("Move Hero South", "S", src.util.TextColor.BLUE);
		InputInterface.DisplayInputOption("Access Inventory", "I", src.util.TextColor.CYAN);
		InputInterface.DisplayInputOption("Pass the Turn", "P", src.util.TextColor.CYAN);

		// if(this.currGameBoard.characterAtMarket()){
		// 	InputInterface.DisplayInputOption("Access Nexus Market", "M", src.util.TextColor.CYAN);
		// }

		ArrayList<AttackOption> heroAttackList = this.currGameBoard.currHeroAttackList();
		if(heroAttackList.size() > 0){
			for(int i = 0; i < heroAttackList.size(); i++){
				AttackOption currAttackOption = heroAttackList.get(i);
				InputInterface.DisplayInputOption("Attack with" + currAttackOption.getSourceItem().getName(), "A" + (i+1), src.util.TextColor.RED);
			}
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
