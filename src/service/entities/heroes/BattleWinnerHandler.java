package src.service.entities.heroes;

public interface BattleWinnerHandler {
	public void earnsXP(Hero hero, int xp);
	public boolean willLevelUp(Hero hero, int xp);
	public void earnsGold(Hero hero, int earnedGold);



}
