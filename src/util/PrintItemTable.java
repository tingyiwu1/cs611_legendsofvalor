package src.util;

import java.util.ArrayList;

import src.service.entities.items.Item;
import src.service.game.market.MarketItem;

public class PrintItemTable {

	public static String[] equipmentNames = new String[] { "Main Hand", "Off Hand", "Helmet", "Chestplate", "Leggings",
			"Boots" };

	public static void printItemTable(ArrayList<Item> items) {
		ArrayList<String[]> cols = new ArrayList<>();
		ArrayList<String> colTitles = new ArrayList<>();
		colTitles.add("Index");
		colTitles.add("Name");
		colTitles.add("Description");
		colTitles.add("Damage");
		colTitles.add("Item Type");
		colTitles.add("Remaining Uses");

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			String[] row = new String[6];
			row[0] = Integer.toString(i + 1);
			row[1] = item.getName();
			row[2] = item.getDescription();
			row[3] = Integer.toString(item.getDamage());
			row[4] = item.getItemType().toString();
			String remainingUsesString = Integer.toString(item.getRemainingUses()) + " / "
					+ Integer.toString(item.getMaxUses());
			row[5] = (item.getItemType() == ItemType.CONSUMABLE || item.getItemType() == ItemType.SPELL
					|| item.getItemType() == ItemType.POTION) ? remainingUsesString : "";
			cols.add(row);
		}
		constructDynamicTable(cols, colTitles);
	}

	public static void printInventoryTable(ArrayList<Item> items, int[] equipment) {
		ArrayList<String[]> cols = new ArrayList<>();
		ArrayList<String> colTitles = new ArrayList<>();
		colTitles.add("Index");
		colTitles.add("Slot");
		colTitles.add("Name");
		colTitles.add("Description");
		// colTitles.add("Damage");

		for (int i = 0; i < 6; i++) {
			int equipIdx = equipment[i];
			String[] row = new String[4];
			row[0] = Integer.toString(i);
			row[1] = equipmentNames[i];
			if (equipIdx == -1) {
				row[2] = "Empty";
				row[3] = "";
				cols.add(row);
				continue;
			}
			Item item = items.get(equipIdx);
			row[2] = item.getName();
			row[3] = item.getDescription();
			cols.add(row);
		}
		constructDynamicTable(cols, colTitles);
	}

	public static void printMarketTable(ArrayList<MarketItem> marketItems) {
		ArrayList<String[]> cols = new ArrayList<>();
		ArrayList<String> colTitles = new ArrayList<>();

		// Define column titles
		colTitles.add("Index");
		colTitles.add("Name");
		colTitles.add("Description");
		colTitles.add("Damage");
		colTitles.add("Type");
		colTitles.add("Price");

		// Populate rows with market item data
		for (int i = 0; i < marketItems.size(); i++) {
			Item item = marketItems.get(i).getItem();
			String[] row = new String[6];
			row[0] = Integer.toString(i);
			row[1] = item.getName();
			row[2] = item.getDescription();
			row[3] = Integer.toString(item.getDamage());
			row[4] = item.getItemType().toString();
			row[5] = marketItems.get(i).getPrice().toString();
			cols.add(row);
		}

		// Construct and print the table

		constructDynamicTable(cols, colTitles);
	}

	public static void constructDynamicTable(ArrayList<String[]> cols, ArrayList<String> colTitles) {
		int colCount = colTitles.size();
		int[] colWidths = new int[colCount];

		for (int i = 0; i < colCount; i++) {
			colWidths[i] = colTitles.get(i).length();
		}
		for (String[] row : cols) {
			for (int i = 0; i < colCount; i++) {
				if (row[i] != null && row[i].length() > colWidths[i]) {
					colWidths[i] = row[i].length();
				}
			}
		}
		printDivider(colWidths);
		System.out.print("| ");
		for (int i = 0; i < colCount; i++) {
			System.out.print(PrintingUtil.printWithPadding(colTitles.get(i), colWidths[i]));
			System.out.print(" | ");
		}
		System.out.println();
		printDivider(colWidths);
		for (String[] row : cols) {
			System.out.print("| ");
			for (int i = 0; i < colCount; i++) {
				String cell = row[i] == null ? "" : row[i];
				System.out.print(PrintingUtil.printWithPadding(cell, colWidths[i]));
				System.out.print(" | ");
			}
			System.out.println();
		}
		printDivider(colWidths);
	}

	// Helper to print row dividers
	private static void printDivider(int[] colWidths) {
		StringBuilder sb = new StringBuilder();
		sb.append("+");
		for (int width : colWidths) {
			for (int i = 0; i < width + 2; i++) {
				sb.append("-");
			}
			sb.append("+");
		}
		System.out.println(sb.toString());
	}
	// System.out.println(sb.toString());
}
