/**
 * The Inventory class represents the inventory system for a hero in the game.
 * It provides functionality to manage and interact with items, including equipping,
 * unequipping, and using items such as potions. The class also implements status
 * display functionality to provide feedback to the user about actions performed
 * within the inventory.
 * 
 * 
 * Key Methods:
 * - isMoveValid(Integer itemSlot, Integer itemIndex): Validates the item slot and index.
 * - makeMove(Integer itemSlot, Integer itemIndex): Validates and processes an inventory action.
 * - processMove(Integer itemSlot, Integer itemIndex): Executes the inventory action, such as
 *   equipping or using an item.
 * - clearStatuses(): Clears all status messages and their colors.
 * - addStatus(String status, TextColor color): Adds a status message with a specific color.
 * - removeStatus(int index): Removes a status message and its color by index.
 * - getStatusList(): Retrieves the list of status messages.
 * - getStatusColors(): Retrieves the list of status message colors.
 */
package src.service.game.inventory;

import src.service.game.StatusDisplay;
import src.util.StatsTracker;
import src.util.TextColor;

import java.util.ArrayList;

import src.service.entities.Player;
import src.service.entities.heroes.Hero;
import src.service.entities.items.Potion;

public class Inventory implements InventoryControl, StatusDisplay {

	private Hero activeHero;
	private ArrayList<String> statuses;
	private ArrayList<TextColor> statusColors;

	public Inventory(Player player, Hero activeHero) {
		this.activeHero = activeHero;
		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
	}

	public Inventory(Player player, int activeHero) {
		this.activeHero = player.getParty()[activeHero];
		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
	}

	@Override
	public Boolean isMoveValid(Integer itemSlot, Integer itemIndex) {
		if (itemSlot < 0 || itemSlot > 6) {
			this.addStatus("Invalid Item Slot, Please Try Again", TextColor.RED);
			return false;
		} else if (itemIndex < -1 || itemIndex > activeHero.getItemsList().size()) {
			this.addStatus("Invalid Item Index, Please Try Again", TextColor.RED);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Boolean makeMove(Integer itemSlot, Integer itemIndex) {
		if (isMoveValid(itemSlot, itemIndex)) {
			return processMove(itemSlot, itemIndex);
		} else {
			return false;
		}
	}

	@Override
	public Boolean processMove(Integer itemSlot, Integer itemIndex) {
		if (itemSlot == 6) {
			try {
				Potion p = (Potion) activeHero.getItemsList().get(itemIndex);
				if (p.use(this.activeHero)) {
					this.addStatus("consumed potion!", TextColor.YELLOW);
					return true;
				}
				this.addStatus("No more uses of potion remaining! Can you take this?", TextColor.BLUE);
				return false;
			} catch (Exception e) {
				throw new IllegalArgumentException("erm no");
			}
		}

		Boolean result = activeHero.equipItem(itemSlot, itemIndex);
		if (result) {
			if (itemIndex == -1) {
				this.addStatus("Unequipped item slot " + itemSlot, TextColor.CYAN);
				return result;
			}
			StatsTracker.addToStats("Number of items equipped", 1);
			this.addStatus("Updated item slot to " + this.activeHero.getItemsList().get(itemIndex).getName(), TextColor.CYAN);
		} else {
			this.addStatus("Failed to update item slot", TextColor.RED);
		}
		// this.addStatus("Updated item slot to " +
		// this.activeHero.getItemsList().get(itemSlot).getName(), TextColor.CYAN);
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
