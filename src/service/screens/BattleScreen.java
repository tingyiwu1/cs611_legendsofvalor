package src.service.screens;

import java.util.ArrayList;
import java.util.Scanner;

import src.service.entities.attributes.AttackOption;
import src.service.entities.heroes.Hero;
import src.service.entities.items.Item;
import src.service.entities.monsters.Monster;
import src.service.game.battle.Battle;
import src.util.PrintingUtil;
import src.util.PrintColor;
import src.util.TextColor;

public class BattleScreen implements Screen, InputInterface {

	private Battle currBattle;
	private Scanner scanny;
	private Character lastInput;

	public BattleScreen(Battle battle, Scanner scanny){
		//constructor
		this.currBattle = battle;
		this.scanny = scanny;	
		this.lastInput = null;
	}

	@Override
	public void displayAndProgress() {
		PrintingUtil.clearScreen();
		System.out.println("This is the Battle Screen!");
		System.out.println("-----------------------------");
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
		System.out.println(options.size());
		for(int i = 0; i < options.size(); i++){
			AttackOption currOption = options.get(i);
			Item currItem = currOption.getSourceItem();

			InputInterface.DisplayInputOption("Attack with " + currItem.getName(), Integer.toString(i + 1), TextColor.BLUE);
		}
		this.displayQuit();
		Character input = this.scanny.next().charAt(0);
		this.lastInput = input;
		return input;
	}

	public void displayBattle(){
		// System.out.println("Hero health: " + this.currBattle.hero.getCurrentHealth() 
		// 									+ " / " + this.currBattle.hero.getMaxHealth());
		// System.out.println("Monster health: " + this.currBattle.monster.getCurrentHealth() 
		// 									+ " / " + this.currBattle.monster.getMaxHealth());

		// TODO here we gao! display more relevant infromation about the battle

		Hero hero = this.currBattle.hero;
		Monster monster = this.currBattle.monster;

		String heroName = "🧙 " + hero.getName();
		String monsterName = "🐉 " + monster.getName();

		String heroHP = getHealthBar(hero.getCurrentHealth(), hero.getMaxHealth(), 20);
    	String monsterHP = getHealthBar(monster.getCurrentHealth(), monster.getMaxHealth(), 20);

		System.out.println(PrintingUtil.printWithPadding(heroName) + monsterName);
		System.out.println(PrintingUtil.printWithPadding("HP: " + heroHP) + "HP: " + monsterHP);

		
	}

	public String getHealthBar(int current, int max, int barLength) {
		int filledLength = (int) ((double) current / max * barLength);
		StringBuilder bar = new StringBuilder();
		bar.append("[");
		for (int i = 0; i < barLength; i++) {
			if (i < filledLength) bar.append("█"); // You can also use '='
			else bar.append(" ");
		}
		bar.append("] ");
		bar.append(current).append("/").append(max);
		return bar.toString();
	}

	@Override
	public Character getLastInput() {
		return this.lastInput;
	}

	@Override
	public void displayPauseAndProgress(String message) {
		// TODO Auto-generated method stub
		PrintingUtil.clearScreen();
		System.out.println("-----------------------------");
		this.displayStatuses(this.currBattle.getStatusList(), this.currBattle.getStatusColors());
		this.displayBattle();
		System.out.println("-----------------------------");
		PrintColor.yellow(message);
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println();
		InputInterface.DisplayInputOption("Input Any Character to Continue", "", TextColor.BLUE);
		this.displayQuit();
		Character input = this.scanny.next().charAt(0);
		this.lastInput = input;
		if(input != 'q'){
			this.lastInput = ' ';
		}
		// this.lastInput = ' ';

	}
	
	
}
