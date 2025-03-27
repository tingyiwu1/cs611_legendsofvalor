package src.service.game.market;

import java.util.ArrayList;

import src.service.entities.Player;
import src.service.entities.heroes.Hero;
import src.service.game.PlayerControl;
import src.service.game.StatusDisplay;
import src.util.TextColor;

public class Market implements PlayerControl, StatusDisplay{

	private Hero activeHero;
	private ArrayList<String> statuses;
	private ArrayList<TextColor> statusColors;
	private Character lastInput;

	private ArrayList<MarketItem> marketOfferings;

	public Market(Player player, int activeHero){
		this.activeHero = player.getParty()[activeHero];
		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
		this.lastInput = ' ';
		this.marketOfferings = ItemFactory.generateRandomMarketItems(this.activeHero.getLevel() + 2);
	// int damage, String name, String description, Integer levelRequirement, Integer maxUses
		// this.marketOfferings.add(new MarketItem(new Spell
		// 	(4, "Debug Dart", "A short darting dart for debugging", 0, 10),
		// 	 30));
		// this.marketOfferings.add(new MarketItem(new Weapon
		// 	(40, "Yapping Club", "A club with a head that screams on it"), 20));
		// this.marketOfferings.add(new MarketItem(new Armor
		// 	("Debugging Helm of Darkness", "A dark helmet", ItemType.HELMET), 10));
		// this.marketOfferings.add(new MarketItem(new Armor
		// 	("Debugging Chestplate of Darkness", "A dark Chestplate", ItemType.CHEST), 10));
		// this.marketOfferings.add(new MarketItem(new Armor
		// 	("Debugging Leggings of Darkness", "A dark helmet", ItemType.LEGS), 10));
		// this.marketOfferings.add(new MarketItem(new Armor
		// 	("Debugging Boots of Darkness", "A dark helmet", ItemType.BOOTS), 10));
	}
	public Market(Player player, int activeHero, ArrayList<MarketItem> marketOfferings){
		this.activeHero = player.getParty()[activeHero];
		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
		this.lastInput = ' ';
		this.marketOfferings = marketOfferings;
	}

	@Override
	public Boolean isMoveValid(Character inputtedMove) {
		int inputInt;
		try{
			inputInt = Integer.parseInt(inputtedMove.toString());
			if(inputInt < 0 || inputInt >= marketOfferings.size()){
				this.addStatus("Invalid Index, please try again.", TextColor.RED);
				return false;
			}
		} catch (NumberFormatException e){
			this.addStatus("Invalid Move, please try again.", TextColor.RED);
			return false;
		}
		return activeHero.canAfford(marketOfferings.get(inputInt).getPrice());

	}

	public ArrayList<MarketItem> getMarketOfferings(){
		return this.marketOfferings;
	}


	@Override
	public Boolean makeMove(Character inputtedMove) {
		this.lastInput = Character.toLowerCase(inputtedMove);
		this.clearStatuses();
		if(!isMoveValid(lastInput)){
			this.addStatus("Invalid Move(do you have enough gold?), please try again.", TextColor.RED);
			return false;
		} 

		return processMove(inputtedMove);
	}


	@Override
	public Boolean processMove(Character inputtedMove) {
		int inputInt = Integer.parseInt(inputtedMove.toString());

		MarketItem buyingItem = this.marketOfferings.get(inputInt);

		this.activeHero.spendGold(buyingItem.getPrice());
		this.activeHero.addItem(buyingItem.getItem());

		this.marketOfferings.remove(inputInt);
		return null;
	}




	@Override
	public void clearStatuses() {
		this.statuses.clear();
		this.statusColors.clear();
	}

	@Override
	public void addStatus(String status, TextColor color) {
		this.statuses.add(status);
		this.statusColors.add(color);
	}

	@Override
	public void removeStatus(int index) {
		this.statuses.remove(index);
		this.statusColors.remove(index);
		
	}

	@Override
	public String[] getStatusList() {
		return this.statuses.toArray(new String[0]);
	}

	@Override
	public TextColor[] getStatusColors() {
		return this.statusColors.toArray(new TextColor[0]);
	}



	
}
