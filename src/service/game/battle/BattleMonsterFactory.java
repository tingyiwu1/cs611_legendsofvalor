/**
 * The BattleMonsterFactory class is responsible for generating random monsters
 * for battles in the game. It supports generating both normal and boss monsters
 * based on the specified type and level.
 */
package src.service.game.battle;

import java.util.ArrayList;
import java.util.Random;

import src.service.entities.InfoTextReader;
import src.service.entities.monsters.Monster;

public class BattleMonsterFactory {
	public static final String FILEPATH = "src/service/entities/infoTexts/BasicMonsterList.txt";
	public static final String BOSSFILEPATH = "src/service/entities/infoTexts/BossMonsterList.txt";

	public static enum monsterType {
		NORMAL,
		BOSS;
	}

	public static Monster generateRandomMonster(monsterType type, int monsterLevel) {
		Monster randomMonster = null;
		Random rng = new Random();
		ArrayList<Monster> allItems;

		// Read all items from the MarketItemList.txt file
		if (type == monsterType.BOSS) {
			allItems = InfoTextReader.readMonsterTextFile(BOSSFILEPATH, monsterLevel);
		} else {
			allItems = InfoTextReader.readMonsterTextFile(FILEPATH, monsterLevel);
		}

		// Check if allItems is null or empty
		if (allItems == null || allItems.isEmpty()) {
			throw new IllegalStateException("Monster list is null or empty. Cannot generate a random monster.");
		}

		// Randomly select numItems from the list
		int randomIndex = rng.nextInt(allItems.size());
		randomMonster = allItems.get(randomIndex);

		return randomMonster;
	}

}
