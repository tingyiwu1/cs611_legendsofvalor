package src.service.game;
import src.service.game.board.GameBoard;
import src.service.game.market.MarketItem;
import src.service.screens.ScreenContext;
import src.service.screens.ScreenInterfaces.Screen;
import src.service.entities.Player;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

import src.service.game.MainGame;
import src.service.screens.MapScreen;
import src.service.screens.MarketScreen;
import src.service.game.battle.Battle;
import src.service.game.battle.BattleMonsterFactory;
import src.service.game.battle.BattleMonsterFactory.monsterType;
import src.service.screens.BattleScreen;
import src.service.screens.IntroScreen;
import src.service.screens.InventoryScreen;
import src.service.entities.monsters.Monster;
import src.util.PieceType;
import src.util.PrintingUtil;
import src.util.ScreenState;
import src.util.StatsTracker;

public class MainGame {

	private Random rand = new Random();

	GameBoard currentBoard;
	Battle currBattle;
	Player currentPlayer;
	ScreenState currentScreen;
	ScreenState previousScreen;
	Boolean continueToGame;
	HashMap<Integer, ArrayList<MarketItem>> marketHash;

	public MainGame(){
		this.currentBoard = new GameBoard(8, 0.3, 0.2);
		this.currentPlayer = new Player();
		this.currentScreen = ScreenState.MAP;
		this.currBattle = null;
		this.previousScreen = null;
		this.marketHash = new HashMap<>();
		this.continueToGame = true;
	}

	public void playGame(){
		System.out.println("Welcome to the game!!");

		Scanner scanny = new Scanner(System.in);

		ScreenContext myScreen = new ScreenContext(null);
		IntroScreen initScreen = new IntroScreen(scanny);
		myScreen.setScreen(initScreen);

		this.currentPlayer = initScreen.initializePlayer();
		if(initScreen.getLastInput() == 'q'){
			continueToGame = false;
		}

		if(continueToGame){
			myScreen.displayScreen();
			if(myScreen.getLastInput() == 'q'){
				continueToGame = false;
			}
		}


		
		myScreen.setScreen(new MapScreen(currentBoard, scanny));

		while(continueToGame){
			StatsTracker.addToStats("Screens Visited", 1);

			Character lastInput = myScreen.getLastInput();
			if(lastInput == 'q'){
				break;
			}

			//opening up the market
			if(lastInput == 'm' && this.currentScreen == ScreenState.MAP){
				if(this.currentBoard.characterAtMarket()){
					StatsTracker.addToStats("Visited Market", 1);
					
					this.previousScreen = this.currentScreen;
					this.currentScreen = ScreenState.MARKET;

					int marketIndex = this.currentBoard.getCurrentMarketIndex();
        			ArrayList<MarketItem> savedItems = this.marketHash.getOrDefault(marketIndex, null);

					if(savedItems == null){
						myScreen.setScreen(new MarketScreen(this.currentPlayer, scanny));
					} else{
						myScreen.setScreen(new MarketScreen(this.currentPlayer, scanny, savedItems));
					}	
				}
			}
			if(this.currentScreen == ScreenState.MARKET && lastInput == 'b'){
				
				int marketIndex = this.currentBoard.getCurrentMarketIndex();
				Screen current = myScreen.getScreen();
				if (current instanceof MarketScreen) {
					MarketScreen marketScreen = (MarketScreen) current;
					if(marketScreen.getCurrentMarket() != null){
						// Save back into hash
						ArrayList<MarketItem> updatedItems = marketScreen.getCurrentMarket().getMarketOfferings();
						this.marketHash.put(marketIndex, updatedItems);
					}
				}


				this.currentScreen = this.previousScreen;
				myScreen.setScreen(new MapScreen(this.currentBoard, scanny));
				this.previousScreen = null;
			}

			//opening up the inventory
			if(lastInput == 'i'){
				this.previousScreen = this.currentScreen;

				this.currentScreen = ScreenState.INVENTORY;
				myScreen.setScreen(new InventoryScreen(this.currentPlayer, scanny));
			}

			// if we are in the inventory screen and we want to go back to the previous screen
			if(this.currentScreen == ScreenState.INVENTORY && lastInput == 'b'){
				if(this.currBattle == null){
					this.currentScreen = this.previousScreen;
					myScreen.setScreen(new MapScreen(this.currentBoard, scanny));
					this.previousScreen = null;
				} else {
					this.currentScreen = this.previousScreen;
					myScreen.setScreen(new BattleScreen(currBattle, scanny));
					this.previousScreen = null;
				}
			}

			/*
			 * Battle is over, level up time
			 */

			if(this.currBattle != null && this.currBattle.isBattleOver()){
				myScreen.getScreen().displayPauseAndProgress("Battle is over!");
				// if(this.currBattle.getDidLevelUp()){
				// 	System.out.println("TODO: PROCESS LEVEL UP!");
				// 	this.currentPlayer.getFirstHero().earnGold(100);
				// } else
				if(this.currBattle.isGameOver()){
					System.out.println("TODO: WE LOST");
					break;
				}
				
				if(this.currentBoard.getCurrentPiece().getPieceType() == PieceType.BOSS){
					int[] charLocation = this.currentBoard.getCharacterLocation();
					System.out.println("Boss defeated at (" + charLocation[0] + "," + charLocation[1] + ") — clearing and spawning new boss.");
					StatsTracker.addToStats("Defeated Bosses", 1);
					this.currentBoard.setPieceAt(charLocation[0], charLocation[1], PieceType.EMPTY);
					this.currentBoard.setNewBoss();
				}

				lastInput = myScreen.getLastInput();
				if(lastInput == 'q'){
					break;
				}

				this.currentScreen = ScreenState.MAP;
				myScreen.setScreen(new MapScreen(this.currentBoard, scanny));
				this.currBattle = null;
			}

			if(this.currentScreen == ScreenState.MAP && 
			(this.currentBoard.isMoveValid(lastInput) || this.currentBoard.isAtBoss()) && 
			lastInput != null && (lastInput == 'w' || lastInput == 'a' || lastInput == 's' || lastInput == 'd')){
					// random chance to fight an enemy
					
					int randomNum = rand.nextInt(100);

					// if this is a boss piece, immediately go to battle
					if(this.currentBoard.getCurrentPiece().getPieceType() == PieceType.BOSS){
						myScreen.getScreen().displayPauseAndProgress("You encountered BOSS enemy!");
						StatsTracker.addToStats("Encountered Bosses", 1);
						lastInput = myScreen.getLastInput();
						if(lastInput == 'q'){
							break;
						}

						System.out.println("Entering battle!");
						this.currentScreen = ScreenState.BATTLE;
						this.currBattle = new Battle(currentPlayer, BattleMonsterFactory.generateRandomMonster(monsterType.BOSS, this.currentPlayer.getMonsterLevel()));
						myScreen.setScreen(new BattleScreen(this.currBattle, scanny));
					// otherwise, go to battle with a 50% chance. TODO
					} else if(randomNum < 50){
						myScreen.getScreen().displayPauseAndProgress("You encountered an enemy!");
						StatsTracker.addToStats("Encountered Enemies", 1);
						lastInput = myScreen.getLastInput();
						if(lastInput == 'q'){
							break;
						}

						System.out.println("Entering battle!");
						this.currentScreen = ScreenState.BATTLE;
						this.currBattle = new Battle(currentPlayer, BattleMonsterFactory.generateRandomMonster(monsterType.NORMAL, this.currentPlayer.getMonsterLevel()));
						myScreen.setScreen(new BattleScreen(this.currBattle, scanny));
					}
				}
			myScreen.displayScreen();

			lastInput = myScreen.getLastInput();
			if(lastInput == 'q'){
				break;
			}
		}


		scanny.close();
		PrintingUtil.clearScreen();
		System.out.println("...");
		System.out.println("With a gasp, you wake up. ");
		System.out.println("\"What kind of dream was that!\" you think. ");
		System.out.println("...");

		System.out.println("Thanks for playing!");
		System.out.println("");

		StatsTracker.printStatsMap();
		
		// something.printStats();
	}
	
}
