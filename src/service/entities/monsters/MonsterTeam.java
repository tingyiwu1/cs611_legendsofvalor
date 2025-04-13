package src.service.entities.monsters;

import java.util.ArrayList;

import src.service.entities.attributes.Position;
import src.service.game.battle.BattleMonsterFactory;

public class MonsterTeam {
	private ArrayList<Monster> monsters;

	public MonsterTeam() {
		this.monsters = new ArrayList<>();
	}

	public ArrayList<Monster> getMonsters() {
		return monsters;
	}

	public void addMonster(Monster monster) {
		monsters.add(monster);
	}

	public void addGenericMonster() {
		Monster monster = BattleMonsterFactory.generateRandomMonster(BattleMonsterFactory.monsterType.NORMAL, 1);
		monsters.add(monster);
	}

	public void addGenericMonster(Position pos) {
		Monster monster = BattleMonsterFactory.generateRandomMonster(BattleMonsterFactory.monsterType.NORMAL, 1);
		monster.setPosition(pos);
		monsters.add(monster);
	}

}
