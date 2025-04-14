package src.service.entities.monsters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import src.service.entities.attributes.Position;
import src.service.game.battle.BattleMonsterFactory;

public class MonsterTeam {
	private final HashSet<Monster> monsters;
	private final Position[] spawnPositions;

	public MonsterTeam(Position[] spawnPositions) {
		this.monsters = new HashSet<>();
		this.spawnPositions = spawnPositions;
	}

	// order: top to bottom, left to right
	public List<Monster> getMonsters() {
		ArrayList<Monster> monsterList = new ArrayList<>(monsters);
		monsterList.sort((m1, m2) -> {
			Position pos1 = m1.getPosition();
			Position pos2 = m2.getPosition();

			if (pos1.getY() != pos2.getY()) {
				return Integer.compare(pos1.getY(), pos2.getY());
			} else {
				return Integer.compare(pos1.getX(), pos2.getX());
			}
		});
		return Collections.unmodifiableList(monsterList);
	}

	public void spawnMonsters(int level) {
		for (Position pos : spawnPositions) {
			Monster monster = BattleMonsterFactory.generateRandomMonster(BattleMonsterFactory.MonsterType.NORMAL, level);
			monster.setPosition(pos);
			monsters.add(monster);
		}
	}

	public void removeMonster(Monster monster) {
		monsters.remove(monster);
	}

	public void addMonster(Monster monster) {
		monsters.add(monster);
	}

	public void addGenericMonster() {
		Monster monster = BattleMonsterFactory.generateRandomMonster(BattleMonsterFactory.MonsterType.NORMAL, 1);
		monsters.add(monster);
	}

	public void addGenericMonster(Position pos) {
		Monster monster = BattleMonsterFactory.generateRandomMonster(BattleMonsterFactory.MonsterType.NORMAL, 1);
		monster.setPosition(pos);
		monsters.add(monster);
	}

}
