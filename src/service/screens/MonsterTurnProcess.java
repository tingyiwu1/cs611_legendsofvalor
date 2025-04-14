package src.service.screens;

import java.util.List;
import java.util.Scanner;

import src.service.entities.Player;
import src.service.entities.heroes.Hero;
import src.service.entities.monsters.Monster;
import src.service.entities.monsters.MonsterTeam;
import src.service.game.TurnKeeper;
import src.service.game.battle.Battle;
import src.service.game.board.GameBoard;
import src.service.game.enemyControl.EnemyController;
import src.service.game.enemyControl.MonsterAction;
import src.service.screens.ScreenInterfaces.Process;
import src.util.PrintingUtil;
import src.util.StatsTracker;

public class MonsterTurnProcess extends Process<ScreenResult<Void>> {

  private final GameBoard currGameBoard;
  private final MonsterTeam monsterTeam;
  private final TurnKeeper turnKeeper;
  private final Player player;
  private final MapDisplay mapDisplay;

  public MonsterTurnProcess(Scanner scanner, GameBoard gameBoard, MonsterTeam monsterTeam, TurnKeeper turnKeeper,
      Player player) {
    super(scanner);
    this.currGameBoard = gameBoard;
    this.monsterTeam = monsterTeam;
    this.turnKeeper = turnKeeper;
    this.player = player;
    this.mapDisplay = new MapDisplay(gameBoard, turnKeeper);
  }

  @Override
  public ScreenResult<Void> run() {
    assert turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.MONSTER : "Not monster's turn";
    while (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.MONSTER) {
      StatsTracker.addToStats("Screens Visited", 1);
      PrintingUtil.clearScreen();
      display();
      if (new ContinueProcess(scanner, true).run().isQuit()) {
        return ScreenResult.quit();
      }
      List<Monster> monsterList = monsterTeam.getMonsters();

      MonsterAction monsterAction = EnemyController.makeCurrentEnemyMove(turnKeeper, currGameBoard, monsterList);

      if (monsterAction.getTargetHero().isPresent()) {
        Battle battle = new Battle(player, monsterTeam, monsterAction.getMonster(), turnKeeper,
            monsterAction.getTargetHero().get());
        BattleProcess battleProcess = new BattleProcess(scanner, battle, turnKeeper);
        ScreenResult<Void> battleResult = battleProcess.run();
        currGameBoard.resetBattleInitializer();
        for (Hero hero : player.getParty()) {
          if (hero.getCurrentHealth() <= 0) {
            hero.respawn();
          }
        }
        // TODO: handle battle result
        if (battleResult.isQuit()) {
          return ScreenResult.quit();
        }
      }
    }
    return ScreenResult.success(null);
  }

  private void display() {
    mapDisplay.display();
    System.out.println("Enemy's turn!");
  }

}
