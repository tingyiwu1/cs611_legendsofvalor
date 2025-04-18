package src.service.process;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import src.service.entities.heroes.Hero;
import src.service.game.market.Market;
import src.util.PrintColor;
import src.util.PrintItemTable;
import src.util.PrintingUtil;
import src.util.StatsTracker;
import src.util.TextColor;

/**
 * Handles a hero's interaction with the market. Run from GameProcess.
 * 
 * Displays the hero's stats and inventory, as well as the market offerings.
 * Allows the user to select an item to purchase, as well as standard handling
 * of going back and quitting.
 */
public class MarketProcess extends Process<ScreenResult<Void>> {

  // private final GameBoard currentBoard;
  private final Hero activeHero;
  private final Market market;

  public MarketProcess(Scanner scanner, Hero activeHero, Market market) {
    super(scanner);
    this.activeHero = activeHero;
    this.market = market;
  }

  @Override
  public ScreenResult<Void> run() {
    StatsTracker.addToStats("Visited Market", 1);

    itemSelect: while (true) {
      InputProcess<String> inputProcess = getItemSelectProcess();
      InputResult<String> inputResult = InputResult.invalid();

      while (inputResult.isInvalid()) {
        PrintingUtil.clearScreen();
        display();
        inputResult = inputProcess.run();
        if (inputResult.isInvalid()) {
          market.addStatus("Invalid input. Please try again.", TextColor.RED);
        }
      }
      String input = inputResult.getResult();
      if (input.equals("q")) {
        return ScreenResult.quit();
      } else if (input.equals("b")) {
        return ScreenResult.goBack();
      } else {
        int itemIndex = Integer.parseInt(input);
        if (market.makeMove(itemIndex)) {
          market.addStatus("Purchased item successfully!", TextColor.GREEN);
          market.addStatus("You have " + activeHero.getGold() + " gold left.", TextColor.YELLOW);
          PrintingUtil.clearScreen();
          display();
          new ContinueProcess(scanner).run();
          return ScreenResult.success(null);
        } else {
          continue itemSelect;
        }
      }
    }
  }

  private InputProcess<String> getItemSelectProcess() {
    ArrayList<InputProcess.Option<String>> options = new ArrayList<>();

    int max = market.getMarketOfferings().size() - 1;
    if (max >= 0) {
      options.add(new InputProcess.Option<>("0-" + max, "Select item", TextColor.BLUE, (input) -> {
        try {
          int itemIndex = Integer.parseInt(input);
          if (itemIndex < 0 || itemIndex > max) {
            return Optional.empty();
          }
          return Optional.of(String.valueOf(itemIndex));
        } catch (NumberFormatException e) {
          return Optional.empty();
        }
      }));
    }

    options.add(new InputProcess.Option<>("b", "Go Back", TextColor.CYAN, "b"));

    options.add(new InputProcess.Option<>("q", "Quit", TextColor.RED, "q"));

    return new InputProcess<>(this.scanner, options, "Select an item to purchase:");
  }

  private void display() {
    /**
     * Print header
     */
    PrintingUtil.clearScreen();
    System.out.println("This is the Market Screen!");
    PrintColor.green("Shopping Hero:\n");
    System.out.println(Hero.getShortHeroDisplay(this.activeHero));
    System.out.println("-----------------------------");
    String[] statuses = market.getStatusList();
    TextColor[] colors = market.getStatusColors();
    for (int i = 0; i < statuses.length; i++) {
      PrintColor.printWithColor(statuses[i], colors[i]);
    }
    market.clearStatuses();
    System.out.println("-----------------------------");

    /**
     * Print hero's inventory
     */
    PrintColor.green("Your Inventory:\n");
    PrintItemTable.printInventoryTable(this.activeHero.getItemsList(), this.activeHero.getEquippedItems());
    PrintItemTable.printItemTable(this.activeHero.getItemsList());

    /**
     * Print market options
     */
    PrintColor.green("Market Items:\n");
    PrintItemTable.printMarketTable(market.getMarketOfferings());

  }

}
