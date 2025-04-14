package src.service.game.market;

import java.util.ArrayList;
import java.util.HashMap;

import src.service.entities.heroes.Hero;

public class MarketFactory {
  private class MarketEntry {
    private final int level;
    public final ArrayList<MarketItem> marketOfferings;

    public MarketEntry(Hero hero) {
      this.level = hero.getLevel();
      this.marketOfferings = ItemFactory.generateRandomMarketItems(level + 2);
    }
  }

  private final HashMap<Hero, MarketEntry> heroMarketMap = new HashMap<>();

  public Market getMarket(Hero hero) {
    MarketEntry entry = heroMarketMap.get(hero);
    if (entry == null || entry.level != hero.getLevel() || entry.marketOfferings.isEmpty()) {
      entry = new MarketEntry(hero);
      heroMarketMap.put(hero, entry);
    }
    return new Market(hero, entry.marketOfferings);
  }
}
