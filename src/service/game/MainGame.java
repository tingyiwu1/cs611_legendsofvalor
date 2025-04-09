/**
 * The MainGame class serves as the primary state machine for the game, managing
 * the flow of gameplay, transitions between different screens, and interactions
 * between the player and various game components. It handles the core game loop,
 * player actions, and game state transitions.
 *
 */
package src.service.game;
import src.service.game.board.GameBoard;
import src.service.game.enemyControl.EnemyController;
import src.service.game.market.MarketItem;
import src.service.screens.ScreenContext;
import src.service.screens.ScreenInterfaces.Screen;
import src.service.entities.Player;
import src.service.entities.Player.Difficulty;
import src.service.entities.attributes.Position;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

import src.service.game.MainGame;
import src.service.game.TurnKeeper.CurrentTurn;
import src.service.screens.MapScreen;
import src.service.screens.MarketScreen;
import src.service.game.battle.Battle;
import src.service.screens.BattleScreen;
import src.service.screens.InventoryScreen;
import src.service.entities.monsters.MonsterTeam;
import src.util.PieceType;
import src.util.PrintingUtil;
import src.util.ScreenState;
import src.util.StatsTracker;

/**
 * The primary state machine for how the game is played out
 */

public class MainGame {

	// private Random rand = new Random();

	GameBoard currentBoard;
	Battle currBattle;
	Player currentPlayer;
	ScreenState currentScreen;
	ScreenState previousScreen;
	Boolean continueToGame;
	HashMap<Integer, ArrayList<MarketItem>> marketHash;
	MonsterTeam monsterTeam;
	TurnKeeper turnKeeper;

	public MainGame(){
		this.currentPlayer = new Player(Difficulty.EASY, 3);
		this.monsterTeam = new MonsterTeam();

		this.monsterTeam.addGenericMonster(new Position(0, 0));
		this.monsterTeam.addGenericMonster(new Position(0, 3));
		this.monsterTeam.addGenericMonster(new Position(0, 6));

		this.turnKeeper = new TurnKeeper(this.currentPlayer, this.monsterTeam);

		this.currentBoard = new GameBoard(8, 0.3, 0.2, this.currentPlayer, this.monsterTeam, this.turnKeeper);
		this.currBattle = null;
		
		this.marketHash = new HashMap<>();
		this.continueToGame = true;

		this.currentScreen = ScreenState.MAP;
		this.previousScreen = null;


	}

	public void playGame(){
		System.out.println("Welcome to the game!!");

		Scanner scanny = new Scanner(System.in);

		ScreenContext myScreen = new ScreenContext(null, this.turnKeeper);

		// TODO DEBUGGING COMMENTED OUT
		// IntroScreen initScreen = new IntroScreen(scanny);
		// myScreen.setScreen(initScreen);

		// this.currentPlayer = initScreen.initializePlayer();
		// if(initScreen.getLastInput() == 'q'){
		// 	continueToGame = false;
		// }

		// if(continueToGame){
		// 	myScreen.displayScreen();
		// 	if(myScreen.getLastInput() == 'q'){
		// 		continueToGame = false;
		// 	}
		// }


		
		myScreen.setScreen(new MapScreen(currentBoard, scanny, turnKeeper));

		while(continueToGame){
			if(this.turnKeeper.getCurrentTurn() == CurrentTurn.PLAYER){
				System.out.println("====== HERO TURN ======");
				if(processHeroTurn(myScreen, scanny)){
					break;
				}
			} else {
				if(processEnemyTurn(myScreen, scanny)){
					break;
				}
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

	public Boolean processEnemyTurn(ScreenContext myScreen, Scanner scanny){
		/*
		 * TODO:
		 * Process the enemy turn
		 * do the stuff here :):):):) ok 
		 */
		Boolean monsterWins = false;
		myScreen.getScreen().displayPauseAndProgress("Enemy Makes a move!");

		if(EnemyController.makeCurrentEnemyMove(turnKeeper, currentBoard, monsterTeam)){
			// did enter battle?
			System.out.println("uh oh");
		} else {
			// did not enter battle
			// don't need to show another pause screen
			
		}
		

		

		return monsterWins;
	}
	


	public Boolean processHeroTurn(ScreenContext myScreen, Scanner scanny){
		StatsTracker.addToStats("Screens Visited", 1);

		Character lastInput = myScreen.getLastInput();
		if(lastInput == 'q'){
			return true;
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
			myScreen.setScreen(new MapScreen(this.currentBoard, scanny, this.turnKeeper));
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
				myScreen.setScreen(new MapScreen(this.currentBoard, scanny, this.turnKeeper));
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
				return true;
			}
			
			if(this.currentBoard.getCurrentPiece().getPieceType() == PieceType.BOSS){
				int[] charLocation = this.currentBoard.getCurrHeroLocation().getPositionXY();
				System.out.println("Boss defeated at (" + charLocation[0] + "," + charLocation[1] + ") — clearing and spawning new boss.");
				StatsTracker.addToStats("Defeated Bosses", 1);
				this.currentBoard.setPieceAt(charLocation[0], charLocation[1], PieceType.EMPTY);
				// this.currentBoard.setNewBoss();
			}

			lastInput = myScreen.getLastInput();
			if(lastInput == 'q'){
				return true;
			}

			this.currentScreen = ScreenState.MAP;
			myScreen.setScreen(new MapScreen(this.currentBoard, scanny, this.turnKeeper));
			this.currBattle = null;
		}


		/*
		 * TODO:
		 * Change this to be a BattleFrame perhaps?
		 * Change the battling system :(
		 */

		if(this.currentScreen == ScreenState.MAP && this.currentBoard.getEnteredBattle()){

			/*
			 * TODO: Make this smoother
			 */
			// myScreen.getScreen().displayPauseAndProgress("You attacked an enemy!");
			StatsTracker.addToStats("Encountered Enemies", 1);

			lastInput = myScreen.getLastInput();
			this.currentScreen = ScreenState.BATTLE;
			this.currBattle = new Battle(this.currentPlayer, this.currentBoard.getMonsterTarget(), this.turnKeeper, this.currentBoard.getAttackOption());
			myScreen.setScreen(new BattleScreen(this.currBattle, scanny));
			myScreen.getScreen().displayPauseAndProgress("You attacked an enemy with: " + this.currentBoard.getAttackOption().getSourceItem().getName());
			
			this.currBattle.heroBattleCycle();

			myScreen.getScreen().displayPauseAndProgress("You attacked an enemy with: " + this.currentBoard.getAttackOption().getSourceItem().getName());

			this.currentBoard.resetBattleInitializer();
			/*
			 * TODO: PROCESS GAME END
			 * TODO: PROCESS LEVEL UP
			 * etc etc etc.
			 */
			if(this.currBattle.isGameOver()){
				System.out.println("TODO: WE LOST");
				return true;
			}

			this.currentScreen = ScreenState.MAP;
			myScreen.setScreen(new MapScreen(this.currentBoard, scanny, this.turnKeeper));
			this.currBattle = null;
		
		}
		myScreen.displayScreen();

		lastInput = myScreen.getLastInput();
		if(lastInput == 'q'){
			return true;
		}

		return false;
	}
}
