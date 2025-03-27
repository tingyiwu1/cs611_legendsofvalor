package src.service.screens;

import java.util.ArrayList;
import java.util.Scanner;

import src.service.entities.Player;
import src.service.entities.heroes.Hero;
import src.service.game.market.Market;
import src.service.game.market.MarketItem;
import src.util.PrintColor;
import src.util.PrintItemTable;
import src.util.PrintingUtil;
import src.util.TextColor;

public class MarketScreen implements Screen, InputInterface, InnerInput  {
	private Market currentMarket;
	private Player player;
	private Scanner scanny;
	private Character lastInput;
	private Hero[] party;
	private Hero activeHero;

	private ArrayList<MarketItem> marketItemList;

	public MarketScreen(Player player, Scanner scanny){
		this.player = player;
		this.scanny = scanny;
		this.lastInput = ' ';
		this.party = player.getParty();
		this.marketItemList = null;
	}

	public MarketScreen(Player player, Scanner scanny, ArrayList<MarketItem> marketItemList){
		this.player = player;
		this.scanny = scanny;
		this.lastInput = ' ';
		this.party = player.getParty();
		this.marketItemList = marketItemList;
	}

	public Market getCurrentMarket(){
		return this.currentMarket;
	}

	// this.displayStatuses(this.currentInventory.getStatusList(), this.currentInventory.getStatusColors());

	@Override
	public void displayInnerQuery() {
		/**
		 * Print header
		 */
		PrintingUtil.clearScreen();
		System.out.println("This is the Market Screen!");
		System.out.println("Shopping Hero: ");
		System.out.println(Hero.getShortHeroDisplay(this.activeHero));
		System.out.println("-----------------------------");
		this.displayStatuses(this.currentMarket.getStatusList(), this.currentMarket.getStatusColors());
		this.currentMarket.clearStatuses();
		System.out.println("-----------------------------");

		/**
		 * Print market options
		 */

		PrintItemTable.printMarketTable(this.currentMarket.getMarketOfferings());

		/**
		 * Print input options
		 */

		PrintColor.blue("Select an Item to purchase! (0-" + (this.currentMarket.getMarketOfferings().size() - 1)+ ")");
		System.out.println("");
		InputInterface.DisplayInputOption("Return to Hero Select", "B", TextColor.BLUE);
		this.displayQuit();
		Character chosenPurchase = this.MarketInput();
		if(chosenPurchase != null){
			this.currentMarket.makeMove(chosenPurchase);
		}
		InputInterface.DisplayInputOption("Return to Hero Select", "B", TextColor.BLUE);

	}

	public Character MarketInput(){
		System.out.println();
		System.out.println("Purchase Item:");
		String slotInput = this.scanny.next();

		if (slotInput.equalsIgnoreCase("b")) {
			this.lastInput = 'b';
			return null;
		} else if(slotInput.equals("q")){
			this.lastInput = 'q';
			return null;
		}
		try {
			int itemSlot = Integer.parseInt(slotInput);
		} catch (NumberFormatException e) {
			PrintColor.red("Invalid input for item slot. Please enter a number or 'b' to go back.");
			return null;
		}
		return slotInput.charAt(0);
	}

	@Override
	public Boolean checkInnerQuery(Integer input) {
		if(input >= 1 && input <= this.party.length){
			return true;
		} 
		return false;
	}

	@Override
	public Character DisplayInputs() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'DisplayInputs'");
	}

	@Override
	public void displayAndProgress() {
		if(activeHero != null){
			this.displayInnerQuery();
		} else {
			PrintingUtil.clearScreen();
			PrintColor.green("Select which Hero will shop:\n");
			System.out.println("-----------------------------");

			for (int i = 0; i < this.party.length; i++) {
				InputInterface.DisplayInputOption(this.party[i].getName(), Integer.toString(i + 1), TextColor.BLUE);
			}
			InputInterface.DisplayInputOption("Return to Previous Screen", "B", TextColor.CYAN);
			this.displayQuit();

			String input = this.scanny.next();
			if (input.equals("q")) {
				this.lastInput = 'q';
				return;
			}else if(input.equals("b")){
				this.lastInput = 'b';
				return;
			}

			try{

				int heroIndex = Integer.parseInt(input);
				if (this.checkInnerQuery(heroIndex)) {
					heroIndex -= 1;
					if(this.marketItemList == null){
						this.currentMarket = new Market(this.player, heroIndex);
					} else {
						this.currentMarket = new Market(this.player, heroIndex, this.marketItemList);
					}
					
					this.activeHero = party[heroIndex];
					this.displayInnerQuery();
				} else {
					PrintColor.red("Invalid selection!");
				}
			} catch (Exception e) {
				PrintColor.red("Invalid input! Please enter a number corresponding to a hero.");
			}

		}
	}

	@Override
	public Character getLastInput() {
		return this.lastInput;
	}


	@Override
	public void displayPauseAndProgress(String message) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'displayPauseAndProgress'");
	}

	
}
