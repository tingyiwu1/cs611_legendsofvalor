package src.service.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import src.service.entities.items.Armor;
import src.service.entities.items.Item;
import src.service.entities.items.Spell;
import src.service.entities.items.Weapon;
import src.service.game.market.MarketItem;
import src.util.ItemType;

public class InfoTextReader {
	// # filepath: /Users/charlesli/Documents/schoolCS/cs611/HeroesAndMonsters/src/service/entities/infoTexts/MarketItemList.txt



	public static ArrayList<MarketItem> readTextFile(String filepath){
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
                if (parts.length < 5) continue; // skip malformed lines

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
						item = new Weapon(damage, name, description, bonusStr, bonusMp, bonusDef, bonusDodge);
						break;
					case SPELL:
						item = new Spell(damage, name, description, levelRequirement, maxUses);
						break;
					case BIG_WEAPON:
						item = new Weapon(damage, name, description, bonusStr, bonusMp, bonusDef, bonusDodge);
						break;
					case HELMET:
					case CHEST:
					case LEGS:
					case BOOTS:
						item = new Armor(name, description, type, bonusStr, bonusMp, bonusDef, bonusDodge);
						break;
					default:
						System.out.println("Unhandled item type: " + type);
						continue; 
				}
				MarketItem mItem = new MarketItem(item, cost);

				resultingList.add(mItem);


            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }


		return resultingList;
	}
}
