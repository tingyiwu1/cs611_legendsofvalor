package src.util;

import java.util.HashMap;

public class StatsTracker {
	public static HashMap<String, Integer> statsMap = new HashMap<>();

	public static void addToStats(String str, Integer num) {
		statsMap.put(str, statsMap.getOrDefault(str, 0) + num);
	}

	public static void printStatsMap() {
		for (String key : statsMap.keySet()) {
			System.out.println(key + ": " + statsMap.get(key));
		}
	}
}
