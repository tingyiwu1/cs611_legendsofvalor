package src.service.screens;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import src.service.entities.Player;
import src.service.entities.heroes.Hero;
import src.service.game.inventory.Inventory;
import src.service.screens.ScreenInterfaces.Process;
import src.util.PrintColor;
import src.util.PrintItemTable;
import src.util.PrintingUtil;
import src.util.TextColor;

public class InventoryProcess extends Process<ScreenResult<Void>> {
  private final Hero activeHero;
  private final Inventory currentInventory;

  public InventoryProcess(Scanner scanner, Player player, Hero activeHero) {
    super(scanner);
    this.activeHero = activeHero;
    this.currentInventory = new Inventory(player, activeHero);
  }

  @Override
  public ScreenResult<Void> run() {
    // don't actually need this label, but helps for clarity
    slotSelect: while (true) {
      InputProcess<Character> inputProcess = getSlotSelectProcess();
      InputResult<Character> inputResult = InputResult.invalid();
      while (inputResult.isInvalid()) {
        PrintingUtil.clearScreen();
        displayInfo();
        inputResult = inputProcess.run();
        if (inputResult.isInvalid()) {
          currentInventory.addStatus("Invalid input. Please try again.", TextColor.RED);
        }
      }

      char input = inputResult.getResult();
      if (input == 'q') {
        return ScreenResult.quit();
      } else if (input == 'b') {
        return ScreenResult.goBack();
      } else {
        int slot = Character.getNumericValue(input);
        // TODO: remove handling for selecting slot 6 to consume potion
        assert slot >= 0 && slot <= 5 : "Invalid slot number";
        InputProcess<Character> itemSelectProcess = getItemSelectProcess();
        InputResult<Character> itemInputResult = InputResult.invalid();
        while (itemInputResult.isInvalid()) {
          PrintingUtil.clearScreen();
          displayInfo();
          itemInputResult = itemSelectProcess.run();
          if (itemInputResult.isInvalid()) {
            currentInventory.addStatus("Invalid input. Please try again.", TextColor.RED);
          }
        }
        char itemInput = itemInputResult.getResult();

        if (itemInput == 'q') {
          return ScreenResult.quit();
        } else if (itemInput == 'b') {
          continue slotSelect; // don't actually need this label, but helps for clarity
        } else {
          int itemIndex = Character.getNumericValue(itemInput) - 1;
          assert currentInventory.isMoveValid(slot, itemIndex) : "Invalid item index";
          if (currentInventory.makeMove(slot, itemIndex)) {
            currentInventory.addStatus("Successfully moved item!", TextColor.YELLOW);
            PrintingUtil.clearScreen();
            displayInfo();
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

    options.add(new InputProcess.Option<>("b", "Go Back", TextColor.CYAN, 'b'));

    options.add(new InputProcess.Option<>("q", "Quit", TextColor.RED, 'q'));

    return new InputProcess<>(this.scanner, options, "Select an equipment slot to manage:");
  }

  private InputProcess<Character> getItemSelectProcess() {
    int size = this.activeHero.getItemsList().size();

    ArrayList<InputProcess.Option<Character>> options = new ArrayList<>();

    options.add(new InputProcess.Option<>("0", "Unequip Item", TextColor.BLUE, '0'));
    options.add(new InputProcess.Option<>("1-" + size, "Select item", TextColor.BLUE, (input) -> {
      if (input.matches("[1-" + size + "]")) {
        return Optional.of(input.charAt(0));
      }
      return Optional.empty();
    }));

    options.add(new InputProcess.Option<>("b", "Go Back", TextColor.CYAN, 'b'));

    options.add(new InputProcess.Option<>("q", "Quit", TextColor.RED, 'q'));

    return new InputProcess<>(this.scanner, options, "Select an item to equip or swap, or 0 to unequip current item:");
  }

  public void displayInfo() {
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
