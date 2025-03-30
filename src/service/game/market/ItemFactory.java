/**
 * The ItemFactory class is responsible for generating a list of random market items
 * from a predefined list of items stored in a text file. It provides functionality
 * to read the items from the file and randomly select a specified number of items
 * to create a subset.
 *
 */
package src.service.game.market;



import src.service.entities.InfoTextReader;

import java.util.ArrayList;
import java.util.Random;

public class ItemFactory {
	public static final String FILEPATH = "src/service/entities/infoTexts/MarketItemList.txt";

	public static ArrayList<MarketItem> generateRandomMarketItems(int numItems){
		ArrayList<MarketItem> randomList = new ArrayList<>();
		Random rng = new Random();

		// Read all items from the MarketItemList.txt file
		ArrayList<MarketItem> allItems = InfoTextReader.readItemTextFile(FILEPATH);

		// Randomly select numItems from the list
		for (int i = 0; i < numItems && !allItems.isEmpty(); i++) {
			int randomIndex = rng.nextInt(allItems.size());
			randomList.add(allItems.remove(randomIndex));
		}

		return randomList;
	}
}
