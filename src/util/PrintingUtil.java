package src.util;

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
		for(int i = 0; i < padding; i++){
			newMsg.append(" ");
		}
		return newMsg.toString();
	}

	public static String printTwoColTable(String[] leftCol, String[] rightCol, String leftTitle, String rightTitle){
		StringBuilder table = new StringBuilder();
		table.append(printWithPadding(leftTitle));
		table.append("|");
		table.append(rightTitle);
		table.append("\n");
		table.append("----------------------------------------\n");

		int numRows = Math.max(leftCol.length, rightCol.length);
		for(int i = 0; i < numRows; i++){
			String left = i < leftCol.length ? leftCol[i] : "";
			String right = i < rightCol.length ? rightCol[i] : "";
			table.append(left);
			table.append(printWithPadding(left));
			table.append("|");
			table.append(right);
			table.append("\n");
		}
		return table.toString();
	}

}