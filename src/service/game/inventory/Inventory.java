package src.service.game.inventory;

import src.service.game.StatusDisplay;
import src.util.TextColor;

import java.util.ArrayList;

import src.service.entities.Player;
import src.service.entities.heroes.Hero;

public class Inventory implements InventoryControl, StatusDisplay {

	private Hero activeHero;
	private ArrayList<String> statuses;
	private ArrayList<TextColor> statusColors;

	public Inventory(Player player, int activeHero){
		this.activeHero = player.getParty()[activeHero];
		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
	}

	
	@Override
	public Boolean isMoveValid(Integer itemSlot, Integer itemIndex) {
		if(itemSlot < 0 || itemSlot > 5){
			this.addStatus("Invalid Item Slot, Please Try Again", TextColor.RED);
			return false;
		} else if(itemIndex < 0 || itemIndex > activeHero.getItemsList().size()){
			this.addStatus("Invalid Item Index, Please Try Again", TextColor.RED);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Boolean makeMove(Integer itemSlot, Integer itemIndex) {
		if(isMoveValid(itemSlot, itemIndex)){
			return processMove(itemSlot, itemIndex);
		} else {
			return false;
		}
	}

	@Override
	public Boolean processMove(Integer itemSlot, Integer itemIndex) {
		Boolean result = activeHero.equipItem(itemSlot, itemIndex);
		if(result){
			this.addStatus("Updated item slot to " + this.activeHero.getItemsList().get(itemIndex).getName(), TextColor.CYAN);
		} else {
			this.addStatus("Failed to update item slot", TextColor.RED);
		}
		// this.addStatus("Updated item slot to " + this.activeHero.getItemsList().get(itemSlot).getName(), TextColor.CYAN);
		return result;
	}
	
	@Override
	public void clearStatuses() {
		// TODO Auto-generated method stub
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
