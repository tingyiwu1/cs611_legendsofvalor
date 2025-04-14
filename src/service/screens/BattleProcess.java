package src.service.screens;

import java.util.Scanner;

import src.service.entities.heroes.Hero;
import src.service.entities.monsters.Monster;
import src.service.entities.monsters.MonsterTeam;
import src.service.game.TurnKeeper;
import src.service.game.battle.Battle;
import src.service.screens.ScreenInterfaces.Process;
import src.util.PrintColor;
import src.util.PrintingUtil;
import src.util.StatsTracker;

public class BattleProcess extends Process<ScreenResult<Void>> {

  private final Battle battle;
  private final TurnKeeper turnKeeper;
  private final BattleDisplay battleDisplay;

  public BattleProcess(Scanner scanner, Battle battle, TurnKeeper turnKeeper) {
    super(scanner);
    this.battle = battle;
    this.turnKeeper = turnKeeper;
    this.battleDisplay = new BattleDisplay(battle);
  }

  @Override
  public ScreenResult<Void> run() {
    StatsTracker.addToStats("Encountered Enemies", 1);
    PrintingUtil.clearScreen();
    display();
    if (new ContinueProcess(scanner, true).run().isQuit()) {
      return ScreenResult.quit();
    }
    if (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER) {
      battle.heroBattleCycle();
    } else {
      battle.monsterBattleCycle();
    }
    PrintingUtil.clearScreen();
    battleDisplay.display();
    if (new ContinueProcess(scanner, true).run().isQuit()) {
      return ScreenResult.quit();
    }
    return ScreenResult.success(null);
  }

  private void display() {
    battleDisplay.display();
    if (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER) {
      PrintColor.yellow("You attacked an enemy with: " + battle.getHeroChosenAttack().getSourceItem().getName());
    } else {
      PrintColor.yellow("The monster attacked you!");
    }
    System.out.println();
  }

}
