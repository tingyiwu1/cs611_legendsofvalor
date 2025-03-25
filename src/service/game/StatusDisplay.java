package src.service.game;

import src.util.TextColor;

public interface StatusDisplay {
	void addStatus(String status, TextColor color);
	void removeStatus(int index);
	void clearStatuses();
	String[] getStatusList();
	TextColor[] getStatusColors();
}
