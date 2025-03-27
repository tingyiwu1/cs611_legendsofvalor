package src.util;

import java.util.ArrayList;

public class PrintingUtil{
	public static void clearScreen() {  
		System.out.print("\033[H\033[2J");  
		System.out.flush();  
	}  
	public static String printWithPadding(String message){
		StringBuilder newMsg = new StringBuilder(message);
		int padding = 40 - message.length();
		for(int i = 0; i < padding; i++){
			newMsg.append(" ");
		}
		return newMsg.toString();
	}

	public static String printWithPadding(String message, int padding){
		StringBuilder newMsg = new StringBuilder(message);
		for(int i = 0; i < padding - message.length(); i++){
			newMsg.append(" ");
		}
		return newMsg.toString();
	}

	public static String printTwoColTable(String[] leftCol, String[] rightCol, String leftTitle, String rightTitle){
		int padding = 35;
		StringBuilder table = new StringBuilder();
		table.append(printWithPadding(leftTitle,padding));
		table.append("|");
		table.append(rightTitle);
		table.append("\n");
		for(int i = 0; i < padding; i++){
			table.append("--");
		}
		table.append("\n");

		int numRows = Math.max(leftCol.length, rightCol.length);
		for(int i = 0; i < numRows; i++){
			String left = i < leftCol.length ? leftCol[i] : "";
			String right = i < rightCol.length ? rightCol[i] : "";
			table.append(printWithPadding(left, padding));
			table.append("|");
			table.append(right);
			table.append("\n");
		}
		table.append("\n");
		for(int i = 0; i < padding; i++){
			table.append("--");
		}
		return table.toString();
	}

	public static String printAnyColTable(ArrayList<String[]> cols, ArrayList<String> titles){
		int padding = 35;
		StringBuilder table = new StringBuilder();
		for(int i = 0; i < cols.size(); i++){
			table.append(printWithPadding(titles.get(i),padding));
			table.append("|");
		}
		table.append("\n");
		for(int i = 0; i < (padding + 2) * cols.size(); i++){
			table.append("-");
		}
		table.append("\n");

		int numRows = 0;
		for(String[] col : cols){
			numRows = Math.max(numRows, col.length);
		}
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < cols.size(); j++){
				String[] col = cols.get(j);
				String val = i < col.length ? col[i] : "";
				table.append(printWithPadding(val, padding));
				table.append("|");
			}
			table.append("\n");
		}
		for(int i = 0; i < (padding + 2) * cols.size(); i++){
			table.append("-");
		}
		return table.toString();
	}

}