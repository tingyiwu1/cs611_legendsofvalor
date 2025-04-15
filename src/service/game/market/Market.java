
/**
 * The Market class represents a marketplace where a player can purchase items for their hero.
 * It implements the PlayerControl and StatusDisplay interfaces to handle player interactions
 * and display status messages during the market operations.
 *
 */
package src.service.game.market;

import java.util.ArrayList;

import src.service.entities.Player;
import src.service.entities.heroes.Hero;
import src.service.game.StatusDisplay;
import src.util.StatsTracker;
import src.util.TextColor;

public class Market implements MarketControl, StatusDisplay {

	private Hero activeHero;
	private ArrayList<String> statuses;
	private ArrayList<TextColor> statusColors;

	private ArrayList<MarketItem> marketOfferings;

	public Market(Hero activeHero, ArrayList<MarketItem> marketOfferings) {
		this.activeHero = activeHero;
		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
		this.marketOfferings = marketOfferings;
	}

	public Market(Player player, int activeHero) {
		this.activeHero = player.getParty()[activeHero];
		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
		this.marketOfferings = ItemFactory.generateRandomMarketItems(this.activeHero.getLevel() + 2);
	}

	public Market(Player player, int activeHero, ArrayList<MarketItem> marketOfferings) {
		this.activeHero = player.getParty()[activeHero];
		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
		this.marketOfferings = marketOfferings;
	}

	@Override
	public boolean isMoveValid(int itemIndex) {
		try {
			if (itemIndex < 0 || itemIndex >= marketOfferings.size()) {
				this.addStatus("Invalid Index, please try again.", TextColor.RED);
				return false;
			}
		} catch (NumberFormatException e) {
			this.addStatus("Invalid Move, please try again.", TextColor.RED);
			return false;
		}
		return activeHero.canAfford(marketOfferings.get(itemIndex).getPrice());

	}

	public ArrayList<MarketItem> getMarketOfferings() {
		return this.marketOfferings;
	}

	@Override
	public boolean makeMove(int itemIndex) {
		this.clearStatuses();
		if (!isMoveValid(itemIndex)) {
			this.addStatus("Invalid Move(do you have enough gold?), please try again.", TextColor.RED);
			return false;
		}

		return processMove(itemIndex);
	}

	@Override
	public boolean processMove(int itemIndex) {
		MarketItem buyingItem = this.marketOfferings.get(itemIndex);

		this.activeHero.spendGold(buyingItem.getPrice());
		StatsTracker.addToStats("Gold Spent", buyingItem.getPrice());
		this.activeHero.addItem(buyingItem.getItem());
		StatsTracker.addToStats("Items purchased", 1);

		this.marketOfferings.remove(itemIndex);
		return true;
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
