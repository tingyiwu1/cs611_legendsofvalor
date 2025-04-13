package src.service.screens;

import java.util.Scanner;

import src.service.screens.GameProcess.GameResult;
import src.service.screens.ScreenInterfaces.Process;
import src.util.PrintingUtil;
import src.util.StatsTracker;

public class MainProcess extends Process<MainProcess.MainProcessResult> {
  // Singleton instance
  public static enum MainProcessResult implements Process.Result {
    INSTANCE
  }

  public MainProcess() {
    super(new Scanner(System.in));
  }

  @Override
  public MainProcessResult run() {
    GameProcess gameProcess = new GameProcess(scanner);
    GameResult gameResult = gameProcess.run();

    if (gameResult == GameResult.QUIT) {
      System.out.println("Game quit");
      return MainProcessResult.INSTANCE;
    } else if (gameResult == GameResult.WIN) {
      System.out.println("You have won the game!");
    } else if (gameResult == GameResult.LOSE) {
      System.out.println("You have lost the game.");
    }

    scanner.close();
    PrintingUtil.clearScreen();
    System.out.println("...");
    System.out.println("With a gasp, you wake up. ");
    System.out.println("\"What kind of dream was that!\" you think. ");
    System.out.println("...");

    System.out.println("Thanks for playing!");
    System.out.println("");

    StatsTracker.printStatsMap();

    return MainProcessResult.INSTANCE;
  }

}
