/**
 * The InfoTextReader class provides utility methods for reading and parsing text files
 * to create lists of game entities such as Monsters and MarketItems. These methods
 * are designed to process specific file formats and convert the data into objects
 * used within the game.

 */
package src.service.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import src.service.entities.items.Armor;
import src.service.entities.items.Item;
import src.service.entities.items.Potion;
import src.service.entities.items.Spell;
import src.service.entities.items.Weapon;
import src.service.entities.monsters.Monster;
import src.service.game.market.MarketItem;
import src.util.ItemType;

public class InfoTextReader {
	// # filepath:
	// /Users/charlesli/Documents/schoolCS/cs611/HeroesAndMonsters/src/service/entities/infoTexts/MarketItemList.txt

	public static ArrayList<Monster> readMonsterTextFile(String filepath, int monsterLevel) {
		ArrayList<Monster> resultingList = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			String line;

			reader.readLine();

			while ((line = reader.readLine()) != null) {
				// Normalize spacing/tabs → split by whitespace
				String[] parts = line.trim().split(",");
				if (parts.length < 5)
					continue; // skip malformed lines

				// Integer maxHealth, Integer level, String name, Integer strength, Integer
				// magicStrength, Integer defense, Integer dodge
				// xp, gold, description
				// monster item dmg, name, description
				int maxHealth = Integer.parseInt(parts[1]);
				int level = monsterLevel;
				String name = parts[0];
				int strength = Integer.parseInt(parts[3]);
				int magicStrength = Integer.parseInt(parts[4]);
				int defense = Integer.parseInt(parts[5]);
				int dodge = Integer.parseInt(parts[6]);
				int xp = Integer.parseInt(parts[7]);
				int gold = Integer.parseInt(parts[8]);
				String description = parts[9];
				int monsterItemDamage = Integer.parseInt(parts[10]);
				String monsterItemName = parts[11];
				String monsterItemDescription = parts[12];

				Item monsterItem = new Weapon(monsterItemDamage, monsterItemName, monsterItemDescription);
				Monster monster = new Monster(maxHealth, level, name, strength, magicStrength, defense, dodge, xp, gold,
						description, monsterItem);

				resultingList.add(monster);

			}

		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
		}

		return resultingList;

	}

	public static ArrayList<MarketItem> readItemTextFile(String filepath) {
		ArrayList<MarketItem> resultingList = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			String line;

			// Skip the header
			reader.readLine();

			// name, cost, description, damage, level requirement, item type,
			// bonus str, bonus def, bonus mp, bonus dodge
			// maxUses

			while ((line = reader.readLine()) != null) {
				// Normalize spacing/tabs → split by whitespace
				String[] parts = line.trim().split(",");
				if (parts.length < 5)
					continue; // skip malformed lines

				String name = parts[0];
				int cost = Integer.parseInt(parts[1]);
				String description = parts[2];
				int damage = Integer.parseInt(parts[3]);
				int levelRequirement = Integer.parseInt(parts[4]);
				String itemType = parts[5];
				int bonusStr = parts.length > 6 ? Integer.parseInt(parts[6]) : 0;
				int bonusDef = parts.length > 7 ? Integer.parseInt(parts[7]) : 0;
				int bonusMp = parts.length > 8 ? Integer.parseInt(parts[8]) : 0;
				int bonusDodge = parts.length > 9 ? Integer.parseInt(parts[9]) : 0;
				int maxUses = parts.length > 10 ? Integer.parseInt(parts[10]) : 0;
				int range = parts.length > 11 ? Integer.parseInt(parts[11]) : 0;
				ItemType type;
				try {
					type = ItemType.valueOf(itemType.toUpperCase().replace(" ", "_"));
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid item type: " + itemType);
					continue; // Skip this line if the item type is invalid
				}
				Item item;
				switch (type) {
					case WEAPON:
						item = new Weapon(damage, name, description, bonusStr, bonusMp, bonusDef, bonusDodge, range);
						break;
					case SPELL:
						item = new Spell(damage, name, description, levelRequirement, maxUses, range);
						break;
					case BIG_WEAPON:
						item = new Weapon(damage, name, description, bonusStr, bonusMp, bonusDef, bonusDodge, range);
						break;
					case HELMET:
					case CHEST:
					case LEGS:
					case BOOTS:
						item = new Armor(name, description, type, bonusStr, bonusMp, bonusDef, bonusDodge);
						break;
					case POTION:
						item = new Potion(damage, name, description, 0, type, bonusDodge, maxUses);

						break;
					default:
						System.out.println("Unhandled item type: " + type);
						continue;
				}

				MarketItem mItem = new MarketItem(item, cost);
				if (name.equals("Small Health Potion") || name.equals("Large Health Potion")) {
					for (int i = 0; i < 20; i++) {
						resultingList.add(mItem);
					}
				}

				resultingList.add(mItem);

			}
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
		}

		return resultingList;
	}
}
