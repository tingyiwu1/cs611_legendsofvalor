package src.service.process.display;

import src.service.entities.heroes.Hero;
import src.service.entities.monsters.Monster;
import src.service.game.battle.Battle;
import src.util.PrintColor;
import src.util.PrintingUtil;
import src.util.TextColor;

public class BattleDisplay implements Display {
  private final Battle battle;

  public BattleDisplay(Battle battle) {
    this.battle = battle;
  }

  @Override
  public void display() {
    System.out.println("This is the Battle Screen!");
    System.out.println("----------------------------------------------------------");
    System.out.println(Hero.getShortHeroDisplay(this.battle.getHero()));
    System.out.println("----------------------------------------------------------");
    this.displayBattle();
    System.out.println("-----------------------------");

    String[] statuses = this.battle.getStatusList();
    TextColor[] colors = this.battle.getStatusColors();
    for (int i = 0; i < statuses.length; i++) {
      PrintColor.printWithColor(statuses[i], colors[i]);
    }
    System.out.println("-----------------------------");
  }

  private void displayBattle() {
    Hero hero = this.battle.hero;
    Monster monster = this.battle.monster;

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

  private String getHealthBar(int current, int max, int barLength) {
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
