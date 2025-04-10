
/**
 * The BattleScreen class represents the user interface for a battle in the game.
 * It implements the Screen and InputInterface interfaces to handle displaying
 * battle information and processing user input during the battle.
 */
package src.service.screens;

import java.util.ArrayList;
import java.util.Scanner;

import src.service.entities.attributes.AttackOption;
import src.service.entities.heroes.Hero;
import src.service.entities.items.Item;
import src.service.entities.monsters.Monster;
import src.service.game.battle.Battle;
import src.service.screens.ScreenInterfaces.InputInterface;
import src.service.screens.ScreenInterfaces.Screen;
import src.util.PrintingUtil;
import src.util.ItemType;
import src.util.PrintColor;
import src.util.TextColor;

public class BattleScreen implements Screen, InputInterface {

	private Battle currBattle;
	private Scanner scanny;
	private Character lastInput;

	public BattleScreen(Battle battle, Scanner scanny) {
		// constructor
		this.currBattle = battle;
		this.scanny = scanny;
		this.lastInput = null;
	}

	@Override
	public void displayAndProgress() {
		PrintingUtil.clearScreen();
		System.out.println("This is the Battle Screen!");
		System.out.println("----------------------------------------------------------");
		System.out.println(Hero.getShortHeroDisplay(this.currBattle.getHero()));
		System.out.println("----------------------------------------------------------");
		this.displayBattle();
		System.out.println("-----------------------------");

		this.displayStatuses(this.currBattle.getStatusList(), this.currBattle.getStatusColors());
		System.out.println("-----------------------------");
		Character inputtedOption = this.DisplayInputs();
		this.currBattle.makeMove(inputtedOption);
	}

	@Override
	public Character DisplayInputs() {
		// TODO Auto-generated method stub
		// TODO: Automatically iterate over all of hero.attackList and display them,
		// correlate that to the correct choice of attack

		ArrayList<AttackOption> options = this.currBattle.hero.getAttacksList();
		for (int i = 0; i < options.size(); i++) {
			AttackOption currOption = options.get(i);
			Item currItem = currOption.getSourceItem();
			if (currItem.getItemType() == ItemType.POTION && currItem.getRemainingUses() == 0) {
				continue;
			} else {
				String displayOption = PrintingUtil.printWithPadding("Attack with " + currItem.getName())
						+ PrintingUtil.printWithPadding("Damage: " + currOption.getDamage(), 12);
				if (currItem.getItemType() == ItemType.CONSUMABLE || currItem.getItemType() == ItemType.SPELL
						|| currItem.getItemType() == ItemType.POTION) {
					displayOption += PrintingUtil
							.printWithPadding("Uses: " + currItem.getRemainingUses() + " / " + currItem.getMaxUses(), 13);
				} else {
					displayOption += PrintingUtil.printWithPadding("", 13);
				}

				InputInterface.DisplayInputOption(displayOption, "(" + Integer.toString(i + 1) + ")", "| ", TextColor.BLUE);
			}

		}

		InputInterface.DisplayInputOption("Access Inventory", "I", src.util.TextColor.CYAN);
		InputInterface.DisplayInputOption("Switch to Next Hero", "S", src.util.TextColor.CYAN);

		this.displayQuit();
		Character input = this.scanny.next().charAt(0);
		this.lastInput = Character.toLowerCase(input);
		return input;
	}

	public void displayBattle() {
		Hero hero = this.currBattle.hero;
		Monster monster = this.currBattle.monster;

		String heroName = "🧙 " + hero.getName();
		String monsterName = "🐉 " + monster.getName();

		String heroHP = getHealthBar(hero.getCurrentHealth(), hero.getMaxHealth(), 20);
		String monsterHP = getHealthBar(monster.getCurrentHealth(), monster.getMaxHealth(), 20);

		System.out.println(PrintingUtil.printWithPadding(heroName, 40) + monsterName);
		System.out.println(PrintingUtil.printWithPadding("HP: " + heroHP, 40) + "HP: " + monsterHP);

		System.out.println();

		// Display Hero and Monster Stats in the same row
		System.out.println(PrintingUtil.printWithPadding("Strength: " + hero.getStrength(), 40)
				+ "Strength: " + monster.getStrength());
		System.out.println(PrintingUtil.printWithPadding("Magic Strength: " + hero.getMagicStrength(), 40)
				+ "Magic Strength: " + monster.getMagicStrength());
		System.out.println(PrintingUtil.printWithPadding("Defense: " + hero.getDefense(), 40)
				+ "Defense: " + monster.getDefense());
		System.out.println(PrintingUtil.printWithPadding("Dodge: " + hero.getDodge(), 40)
				+ "Dodge: " + monster.getDodge());

		// Display Monster Description and Weapon
		System.out.println();
		System.out.println(PrintingUtil.printWithPadding(monster.getDescription() + " | Level: " + monster.getLevel(), 40));
		System.out.println(PrintingUtil.printWithPadding(monster.getMonsterWeapon().getName()
				+ " (Damage: " + monster.getMonsterWeapon().getDamage() + ")", 40));
		System.out.println();
		// this.displayBasicBattleInfo();
	}

	private void displayBasicBattleInfo() {
		System.out.println("----------------------------------------------------------");
		System.out.println("Dodge chance calculation:");
		System.out.println(" -  (Total dodge / 20) + (Random crit) Percent chance");
		System.out.println("Damage calculation: ");
		System.out.println(" -  (Base Str + Weapon Damage - Enemy Def) + (Random crit) Total Damage");
		System.out.println("Spell Damage calculation: ");
		System.out.println(" -  (Base MP + Spell dmg - Enemy Def) + (Random crit) Total Damage");
	}

	@Override
	public Character getLastInput() {
		return this.lastInput;
	}

	@Override
	public void displayPauseAndProgress(String message) {
		// TODO Auto-generated method stub
		PrintingUtil.clearScreen();
		System.out.println("----------------------------------------------------------");
		System.out.println(Hero.getShortHeroDisplay(this.currBattle.getHero()));
		System.out.println("----------------------------------------------------------");
		this.displayBattle();
		System.out.println("-----------------------------");
		this.displayStatuses(this.currBattle.getStatusList(), this.currBattle.getStatusColors());
		System.out.println("-----------------------------");
		PrintColor.yellow(message);
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println();
		InputInterface.DisplayInputOption("Input Any Character to Continue", "", TextColor.BLUE);
		this.displayQuit();
		Character input = this.scanny.next().charAt(0);
		this.lastInput = input;
		if (input != 'q') {
			this.lastInput = ' ';
		}
	}

	public String getHealthBar(int current, int max, int barLength) {
		int filledLength = (int) ((double) current / max * barLength);
		StringBuilder bar = new StringBuilder();
		bar.append("[");
		for (int i = 0; i < barLength; i++) {
			if (i < filledLength)
				bar.append("█"); // You can also use '='
			else
				bar.append(" ");
		}
		bar.append("] ");
		bar.append(current).append("/").append(max);
		return bar.toString();
	}

}
