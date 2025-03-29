package src.service.screens;

import java.util.ArrayList;
import java.util.Scanner;

import src.service.entities.Player;
import src.service.entities.heroes.Hero;
import src.service.entities.items.Item;
import src.service.game.inventory.Inventory;
import src.util.PrintColor;
import src.util.PrintItemTable;
import src.util.PrintingUtil;
import src.util.TextColor;

public class InventoryScreen implements Screen, InputInterface, InnerInput {

	private Inventory currentInventory;
	private Player player;
	private Scanner scanny;
	private Character lastInput;
	private Hero[] party;
	private Hero activeHero;

	public InventoryScreen(Player player, Scanner scanny) {
		this.player = player;
		this.scanny = scanny;
		this.lastInput = ' ';
		this.party = player.getParty();
		//constructor
	}

	@Override
	public void displayAndProgress(){
		if(activeHero != null){
			this.displayInnerQuery();
		} else {
			
			PrintingUtil.clearScreen();
			PrintColor.green("Select the inventory you wish to manage:");
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

			int heroIndex;
			try {
				heroIndex = Integer.parseInt(input);
				if (this.checkInnerQuery(heroIndex)) {
					heroIndex -= 1;
					this.currentInventory = new Inventory(this.player, heroIndex);
					this.activeHero = party[heroIndex];
					this.displayInnerQuery();
				} else {
					PrintColor.red("Invalid selection!");
				}
			} catch (NumberFormatException e) {
				PrintColor.red("Invalid input! Please enter a number corresponding to a hero.");
			}
		}

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
	public void displayInnerQuery() {
		/*
			* Printing the header of the inventory screen
			*/
		PrintingUtil.clearScreen();
		System.out.println("This is the Inventory Screen!");
		System.out.println("Managing Hero: " + this.activeHero.getName());
		System.out.println("-----------------------------");
		this.displayStatuses(this.currentInventory.getStatusList(), this.currentInventory.getStatusColors());
		this.currentInventory.clearStatuses();
		System.out.println("-----------------------------");
		
		/*
		* Printing the table
		*/

		System.out.println(Hero.getHeroDisplay(this.activeHero));
		PrintItemTable.printInventoryTable(this.activeHero.getItemsList(), this.activeHero.getEquippedItems());
		PrintItemTable.printItemTable(this.activeHero.getItemsList());
		
		/*
			* Printing the input options
			*/
		System.out.println("Main Hand attack determined by equipment in Main hand");
		PrintColor.blue("Select an equipment slot on the top: (0-5)");
		System.out.println("");
		PrintColor.blue("Select an item from the inventory on the right: (1-" + (this.getInventoryList().length-1) + ")");
		System.out.println("");
		InputInterface.DisplayInputOption("Unequip Item", "0", TextColor.BLUE);
		InputInterface.DisplayInputOption("Consume Item", "6", TextColor.BLUE);
		InputInterface.DisplayInputOption("Return to Hero Select", "B", TextColor.BLUE);
		this.displayQuit();
		int[] chosenSwap = this.InventoryInput();
		if(chosenSwap != null){
			this.currentInventory.makeMove(chosenSwap[0], chosenSwap[1]);
		}
		InputInterface.DisplayInputOption("Return to Hero Select", "B", TextColor.BLUE);

	}

	public String[] getCurrentEquipmentList(){
		int[] equipment = this.activeHero.getEquippedItems();
		ArrayList<Item> items = this.activeHero.getItemsList();
		String[] slots = {"Main Hand", "Off Hand", "Helmet", "Chest", "Legs", "Boots"};
		String[] slotStrings = new String[6];
		for(int i = 0; i < equipment.length + 1; i++){
			if(equipment[i] != -1){
				slotStrings[i] = "(" + i + ")" + slots[i] + ": " + items.get(equipment[i]).getName();
			} else {
				slotStrings[i] = "(" + i + ")" + slots[i] + ": Empty";
			}
		}

		return slotStrings;
	}
	public String[] getInventoryList(){
		ArrayList<Item> items = this.activeHero.getItemsList();
		ArrayList<String> rightCol = new ArrayList<>();
		Integer display_idx;
		for(int i = 0; i < items.size(); i++){
			display_idx = i + 1;
			Item item = items.get(i);

			rightCol.add("(" + display_idx + ")" + item.getName());
		}
		rightCol.add("(0)Unequip Item");

		return rightCol.toArray(new String[rightCol.size()]);
	}

	public String[] getInventoryDamageStrings(){
		ArrayList<Item> items = this.activeHero.getItemsList();
		ArrayList<String> col = new ArrayList<>();
		for(int i = 0; i < items.size(); i++){
			Item item = items.get(i);
			col.add(item.getDamage().toString());
		}

		return col.toArray(new String[col.size()]);
	}

	public int[] InventoryInput(){
		System.out.println();

		System.out.print("Item Slot(Or 6 to consume Potion): ");
		String slotInput = this.scanny.next();

		if (slotInput.equalsIgnoreCase("b")) {
			this.lastInput = 'b';
			return null;
		} else if(slotInput.equals("q")){
			this.lastInput = 'q';
			return null;
		}
		int itemSlot;
		try {
			itemSlot = Integer.parseInt(slotInput);
		} catch (NumberFormatException e) {
			PrintColor.red("Invalid input for item slot. Please enter a number or 'b' to go back.");
			return null;
		}

		System.out.print("Item Index(Or 0 to unequip): ");
		String indexInput = this.scanny.next();

		if (indexInput.equalsIgnoreCase("b")) {
			this.lastInput = 'b';
			return null;
		} else if(slotInput.equals("q")){
			this.lastInput = 'q';
			return null;
		}

		int itemIndex;
		try {
			itemIndex = Integer.parseInt(indexInput);
		} catch (NumberFormatException e) {
			PrintColor.red("Invalid input for item index. Please enter a number or 'b' to go back.");
			return null;
		}
		// System.out.println("ITEM INDEX: " + itemIndex);
		if(itemIndex == 0){
			itemIndex = -1;
		} else {
			itemIndex -= 1;
		}
		return new int[] { itemSlot, itemIndex };
	}

	@Override
	public void displayPauseAndProgress(String message) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'displayPauseAndProgress'");
	}

	@Override
	public Character getLastInput() {
		return this.lastInput;
	}


	
	
}
