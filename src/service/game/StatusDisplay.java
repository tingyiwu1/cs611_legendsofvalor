/**
 * The StatusDisplay interface defines methods for managing and displaying
 * status messages with associated text colors in a game.
 */
package src.service.game;

import src.util.TextColor;

public interface StatusDisplay {
	void addStatus(String status, TextColor color);
	void removeStatus(int index);
	void clearStatuses();
	String[] getStatusList();
	TextColor[] getStatusColors();
}
