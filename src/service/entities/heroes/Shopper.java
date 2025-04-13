/**
 * The Shopper interface defines the behavior of an entity that can manage gold
 * for purchasing and earning purposes. It provides methods to retrieve the
 * current gold amount, spend gold, earn gold, and check if a purchase can be
 * afforded.
 */
package src.service.entities.heroes;

public interface Shopper {
	public Integer getGold();

	public void spendGold(int amount);

	public void earnGold(int amount);

	public boolean canAfford(int cost);
}
