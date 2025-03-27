package src.service.entities.heroes;

public interface Shopper {
	public Integer getGold();
	public void spendGold(int amount);
	public void earnGold(int amount);
	public boolean canAfford(int cost);
}
