package src.service.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

import src.service.entities.heroes.Hero;
import src.service.entities.items.Potion;
import src.service.game.inventory.Inventory;
import src.util.PrintColor;
import src.util.PrintItemTable;
import src.util.PrintingUtil;
import src.util.TextColor;;;

/**
 * Handles potion selection and usage for a hero. Run from InventoryProcess.
 * 
 * Displays available potions and allows the user to select one to use, as well
 * as standard handling of going back and quitting
 */
public class PotionProcess extends Process<ScreenResult<Void>> {
  private final Hero activeHero;
  private final Inventory currentInventory;
  private final Potion[] availablePotions;

  public PotionProcess(Scanner scanner, Hero activeHero, Inventory inventory) {
    super(scanner);
    this.activeHero = activeHero;
    this.currentInventory = inventory;
    this.availablePotions = activeHero.getPotionsList();
  }

  @Override
  public ScreenResult<Void> run() {
    // don't actually need this label, but helps for clarity
    potionSelect: while (true) {
      char input = getPotionSelectProcess().runLoop(() -> {
        display();
      }, () -> currentInventory.addStatus("Invalid input. Please try again.", TextColor.RED));

      if (input == 'q') {
        return ScreenResult.quit();
      } else if (input == 'b') {
        return ScreenResult.goBack();
      } else {
        int potionIndex = Character.getNumericValue(input) - 1;
        assert potionIndex >= 0 && potionIndex < availablePotions.length : "Invalid potion index";
        Potion potion = availablePotions[potionIndex];
        int selectedPotionIndex = activeHero.getItemsList().indexOf(potion);
        assert selectedPotionIndex != -1 : "Potion not found in inventory";
        assert currentInventory.isMoveValid(6, selectedPotionIndex) : "Invalid potion index";
        if (currentInventory.makeMove(6, selectedPotionIndex)) {
          currentInventory.addStatus("Successfully used potion!", TextColor.YELLOW);
          return ScreenResult.success(null);
        } else {
          currentInventory.addStatus("Failed to use potion!", TextColor.RED);
          continue potionSelect; // don't actually need this label, but helps for clarity
        }
      }
    }

  }

  private InputProcess<Character> getPotionSelectProcess() {
    int size = availablePotions.length;

    ArrayList<InputProcess.Option<Character>> options = new ArrayList<>();

    options.add(new InputProcess.Option<>("1-" + size, "Select potion", TextColor.BLUE, (input) -> {
      if (input.matches("[1-" + size + "]")) {
        return Optional.of(input.charAt(0));
      }
      return Optional.empty();
    }));
    options.add(new InputProcess.Option<>("b", "Go Back", TextColor.CYAN, 'b'));

    options.add(new InputProcess.Option<>("q", "Quit", TextColor.RED, 'q'));

    return new InputProcess<>(this.scanner, options, "Select a potion to use:");
  }

  private void display() {
    /*
     * Printing the header of the inventory screen
     */
    PrintingUtil.clearScreen();
    System.out.println("This is the Potion Screen!");
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
    PrintItemTable.printItemTable(new ArrayList<>(Arrays.asList(availablePotions)));
  }
}
