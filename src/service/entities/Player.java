package src.service.entities;
import src.service.entities.heroes.Hero;
import src.service.entities.monsters.Monster;
import src.service.entities.heroes.BattleWinnerHandler;



public class Player implements BattleWinnerHandler{

	public enum Difficulty{
		EASY(1),
		MEDIUM(2),
		HARD(3);

		private final int value;

		Difficulty(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public static Difficulty currDifficulty = Difficulty.EASY;

	Hero[] party;

	public Player(){
		//constructor

		this.party = new Hero[1];
		this.party[0] = new Hero();
	}

	public Player(Difficulty diff, int numHeros){
		Player.currDifficulty = diff;

		if(diff == Difficulty.EASY){
			Monster.setLevelBoon(3);
		} else if(diff == Difficulty.MEDIUM){
			Monster.setLevelBoon(6);
		} else {
			Monster.setLevelBoon(12);
		}

		this.party = new Hero[numHeros];

		Hero warrior = new Hero(150, 1, "The Warrior", 50, 30, 45, 10);
		Hero mage = new Hero(90, 1, "The Mage", 30, 75, 35, 10);
		Hero assassin = new Hero(120, 1, "The Assassin", 50, 50, 40, 50);

		for (int i = 0; i < numHeros; i++) {
			if (i == 0) {
				this.party[i] = warrior;
			} else if (i == 1) {
				this.party[i] = mage;
			} else if (i == 2) {
				this.party[i] = assassin;
			}
		}
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

	public int getMonsterLevel(){
		double avg_level = 0;
		for(Hero h : party){
			avg_level += h.getLevel();
		}
		return (int) (avg_level / 3.0) - 1;
	}

	


}
