package src.util;

public class PrintColor {
    // ANSI escape codes for text colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Print in red
    public static void red(String message) {
        System.out.print(RED + message + RESET);
    }

    // Print in green
    public static void green(String message) {
        System.out.print(GREEN + message + RESET);
    }

    // Print in yellow
    public static void yellow(String message) {
        System.out.print(YELLOW + message + RESET);
    }

    // Print in blue
    public static void blue(String message) {
        System.out.print(BLUE + message + RESET);
    }

    // Generic print with color
    public static void printWithColor(String message, TextColor color) {
		System.out.println(color.code() + message + TextColor.RESET.code());
	}
}