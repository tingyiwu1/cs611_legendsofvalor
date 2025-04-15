package src.service.process;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import src.service.entities.heroes.Hero;
import src.service.entities.items.Potion;
import src.service.game.GameContext;
import src.service.game.inventory.Inventory;
import src.util.PrintColor;
import src.util.PrintItemTable;
import src.util.PrintingUtil;
import src.util.TextColor;

/**
 * Handles inventory management for a hero. Run from GameProcess.
 * 
 * Displays the hero's stats and inventory, as well as the available potions.
 * Allows the user to select an item to equip or swap, as well as standard
 * handling of going back and quitting.
 */
public class InventoryProcess extends Process<ScreenResult<Void>> {
  private final Hero activeHero;
  private final Inventory currentInventory;
  private final Potion[] availablePotions;

  public InventoryProcess(Scanner scanner, GameContext gameContext) {
    super(scanner);
    this.activeHero = gameContext.gameBoard.getCurrentHero();
    this.currentInventory = new Inventory(gameContext.player, activeHero);
    this.availablePotions = activeHero.getPotionsList();
  }

  @Override
  public ScreenResult<Void> run() {
    // don't actually need this label, but helps for clarity
    slotSelect: while (true) {
      char input = getSlotSelectProcess().runLoop(() -> {
        PrintingUtil.clearScreen();
        display();
      }, () -> currentInventory.addStatus("Invalid input. Please try again.", TextColor.RED));
      if (input == 'q') {
        return ScreenResult.quit();
      } else if (input == 'b') {
        return ScreenResult.goBack();
      } else if (input == 'p') {
        assert availablePotions.length > 0 : "No potions available";
        ScreenResult<Void> potionResult = new PotionProcess(scanner, activeHero, currentInventory).run();
        if (potionResult.isQuit()) {
          return ScreenResult.quit();
        } else if (potionResult.isGoBack()) {
          continue slotSelect; // don't actually need this label, but helps for clarity
        } else {
          currentInventory.addStatus("Successfully used potion!", TextColor.YELLOW);
          PrintingUtil.clearScreen();
          display();
          new ContinueProcess(scanner).run();
          return ScreenResult.success(null);
        }
      } else {
        int slot = Character.getNumericValue(input);
        assert slot >= 0 && slot <= 5 : "Invalid slot number";
        String itemInput = getItemSelectProcess().runLoop(() -> {
          PrintingUtil.clearScreen();
          display();
        }, () -> currentInventory.addStatus("Invalid input. Please try again.", TextColor.RED));

        if (itemInput.equals("q")) {
          return ScreenResult.quit();
        } else if (itemInput.equals("b")) {
          continue slotSelect; // don't actually need this label, but helps for clarity
        } else {
          int itemIndex = Integer.parseInt(itemInput) - 1;
          assert currentInventory.isMoveValid(slot, itemIndex) : "Invalid item index";
          if (currentInventory.makeMove(slot, itemIndex)) {
            currentInventory.addStatus("Successfully moved item!", TextColor.YELLOW);
            PrintingUtil.clearScreen();
            display();
            new ContinueProcess(scanner).run();
            return ScreenResult.success(null);
          } else {
            currentInventory.addStatus("Failed to move item!", TextColor.RED);
            continue slotSelect; // don't actually need this label, but helps for clarity
          }
        }
      }
    }
  }

  private InputProcess<Character> getSlotSelectProcess() {
    ArrayList<InputProcess.Option<Character>> options = new ArrayList<>();

    options.add(new InputProcess.Option<>("0-5", "Select slot", TextColor.BLUE, (input) -> {
      if (input.matches("[0-5]")) {
        return Optional.of(input.charAt(0));
      }
      return Optional.empty();
    }));

    if (availablePotions.length > 0) {
      options.add(new InputProcess.Option<>("p", "Use Potion", TextColor.BLUE, 'p'));
    }

    options.add(new InputProcess.Option<>("b", "Go Back", TextColor.CYAN, 'b'));

    options.add(new InputProcess.Option<>("q", "Quit", TextColor.RED, 'q'));

    return new InputProcess<>(this.scanner, options, "Select an equipment slot to manage:");
  }

  private InputProcess<String> getItemSelectProcess() {
    int size = this.activeHero.getItemsList().size();

    ArrayList<InputProcess.Option<String>> options = new ArrayList<>();

    options.add(new InputProcess.Option<>("0", "Unequip Item", TextColor.BLUE, "0"));
    options.add(new InputProcess.Option<>("1-" + size, "Select item", TextColor.BLUE, (input) -> {
      try {
        int itemIndex = Integer.parseInt(input);
        if (itemIndex < 1 || itemIndex > size) {
          return Optional.empty();
        }
        return Optional.of(String.valueOf(itemIndex));
      } catch (NumberFormatException e) {
        return Optional.empty();
      }
    }));

    options.add(new InputProcess.Option<>("b", "Go Back", TextColor.CYAN, "b"));

    options.add(new InputProcess.Option<>("q", "Quit", TextColor.RED, "q"));

    return new InputProcess<>(this.scanner, options, "Select an item to equip or swap, or 0 to unequip current item:");
  }

  private void display() {
    /*
     * Printing the header of the inventory screen
     */
    PrintingUtil.clearScreen();
    System.out.println("This is the Inventory Screen!");
    System.out.println("Managing Hero: " + this.activeHero.getName());
    System.out.println("-----------------------------");
    String[] statuses = this.currentInventory.getStatusList();
    TextColor[] colors = this.currentInventory.getStatusColors();
    for (int i = 0; i < statuses.length; i++) {
      PrintColor.printWithColor(statuses[i], colors[i]);
    }
    this.currentInventory.clearStatuses();
    System.out.println("-----------------------------");

    /*
     * Printing the table
     */
    System.out.println(Hero.getHeroDisplay(this.activeHero));
    PrintItemTable.printInventoryTable(this.activeHero.getItemsList(), this.activeHero.getEquippedItems());
    PrintItemTable.printItemTable(this.activeHero.getItemsList());
  }

}
