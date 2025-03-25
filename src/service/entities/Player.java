package src.service.entities;
import src.service.entities.heroes.Hero;

public class Player {

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
}
