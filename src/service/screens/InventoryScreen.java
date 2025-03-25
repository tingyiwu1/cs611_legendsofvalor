	package src.service.screens;

	import java.util.ArrayList;
	import java.util.Scanner;

	import src.service.entities.Player;
	import src.service.entities.heroes.Hero;
	import src.service.entities.items.Item;
	import src.service.game.inventory.Inventory;
	import src.util.PrintColor;
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

				int heroIndex = Integer.parseInt(input);
				if (this.checkInnerQuery(heroIndex)) {
					heroIndex -= 1;
					this.currentInventory = new Inventory(this.player, heroIndex);
					this.activeHero = party[heroIndex];
					this.displayInnerQuery();
				} else {
					PrintColor.red("Invalid selection!");
				}
			}

		}

		@Override
		public Boolean checkInnerQuery(Integer input) {
			if(input >= 1 || input <= this.party.length){
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
			PrintingUtil.clearScreen();
			System.out.println("This is the Inventory Screen!");
			System.out.println("Managing Hero: " + this.activeHero.getName());
			System.out.println("-----------------------------");
			this.displayStatuses(this.currentInventory.getStatusList(), this.currentInventory.getStatusColors());
			this.currentInventory.clearStatuses();
			System.out.println("-----------------------------");
			// Hero activeHero = currentHero;
			this.displayCurrentEquipment();
			System.out.println("-----------------------------");
			ArrayList<Item> items = this.activeHero.getItemsList();
			for(int i = 0; i < items.size(); i++){
				Item item = items.get(i);
				InputInterface.DisplayInputOption(item.getName(), Integer.toString(i), TextColor.BLUE);
			}
			InputInterface.DisplayInputOption("Return to Hero Select", "B", TextColor.BLUE);
			this.displayQuit();
			int[] chosenSwap = this.InventoryInput();
			if(chosenSwap != null){
				this.currentInventory.makeMove(chosenSwap[0], chosenSwap[1]);
			}
		}

		public void displayCurrentEquipment(){
			int[] equipment = this.activeHero.getEquippedItems();
			ArrayList<Item> items = this.activeHero.getItemsList();
			for(int i = 0; i < equipment.length; i++){
				if(equipment[i] != -1){
					System.out.println("Slot " + i + ": " + items.get(equipment[i]).getName());
				} else {
					System.out.println("Slot " + i + ": Empty");
				}
			}
		}

		public int[] InventoryInput(){
			System.out.println("Enter the item slot and item index you wish to equip.");
			System.out.println("Enter 'b' to go back.");

			System.out.print("Item Slot: ");
			String slotInput = this.scanny.next();

			if (slotInput.equalsIgnoreCase("b")) {
				this.lastInput = 'b';
				return null;
			}
			int itemSlot;
			try {
				itemSlot = Integer.parseInt(slotInput);
			} catch (NumberFormatException e) {
				PrintColor.red("Invalid input for item slot. Please enter a number or 'b' to go back.");
				return null;
			}

			System.out.print("Item Index: ");
			String indexInput = this.scanny.next();

			if (indexInput.equalsIgnoreCase("b")) {
				this.lastInput = 'b';
				return null;
			}

			int itemIndex;
			try {
				itemIndex = Integer.parseInt(indexInput);
			} catch (NumberFormatException e) {
				PrintColor.red("Invalid input for item index. Please enter a number or 'b' to go back.");
				return null;
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
