package src.service.entities;
import src.service.entities.heroes.Hero;
import src.service.entities.heroes.BattleWinnerHandler;

public class Player implements BattleWinnerHandler{

	Hero[] party;

	public Player(){
		//constructor

		this.party = new Hero[1];
		this.party[0] = new Hero();
	}

	public Hero[] getParty(){
		return this.party;
	}

	public Hero getFirstHero(){
		return this.party[0];
	}

	@Override
	public void earnsXP(Hero hero, int xp) {
		hero.gainExperience(xp);
	}

	@Override
	public boolean willLevelUp(Hero hero, int xp) {
		return hero.willLevelUp(xp);
	}

	@Override
	public void earnsGold(Hero hero, int amt) {
		hero.earnGold(amt);
	}

	


}
