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
		ArrayList<MarketItem> allItems = InfoTextReader.readTextFile(FILEPATH);

		// Randomly select numItems from the list
		for (int i = 0; i < numItems && !allItems.isEmpty(); i++) {
			int randomIndex = rng.nextInt(allItems.size());
			randomList.add(allItems.remove(randomIndex));
		}

		return randomList;
	}
}
