package src.util;

public enum TextColor {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    BLACK("\u001B[30m"),
    ORANGE("\u001B[38;5;214m"),
    RESET("\u001B[0m");

    private final String code;

    TextColor(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

}
