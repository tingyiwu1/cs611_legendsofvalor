package src.service.entities.monsters;

import java.util.ArrayList;

import src.service.entities.attributes.Position;
import src.service.game.battle.BattleMonsterFactory;

public class MonsterTeam {
	private ArrayList<Monster> monsters;
	private int teamSize;


	public MonsterTeam(int teamSize) {
		this.teamSize = teamSize;
		this.monsters = new ArrayList<>(teamSize);
	}
	public ArrayList<Monster> getMonsters() {
		return monsters;
	}

	public void addMonster(Monster monster) {
		if (monsters.size() < teamSize) {
			monsters.add(monster);
		} else {
			System.out.println("Team is full. Cannot add more monsters.");
		}
	}

	public void addGenericMonster() {
		Monster monster = BattleMonsterFactory.generateRandomMonster(BattleMonsterFactory.monsterType.NORMAL, 1);
		if (monsters.size() < teamSize) {
			monsters.add(monster);
		} else {
			System.out.println("Team is full. Cannot add more monsters.");
		}
	}	
	public void addGenericMonster(Position pos) {
		Monster monster = BattleMonsterFactory.generateRandomMonster(BattleMonsterFactory.monsterType.NORMAL, 1);
		monster.setPosition(pos);
		if (monsters.size() < teamSize) {
			monsters.add(monster);
		} else {
			System.out.println("Team is full. Cannot add more monsters.");
		}
	}

}
