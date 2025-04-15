package src.service.process.display;

import java.util.ArrayList;

import src.service.entities.Entity;
import src.service.entities.Entity.EntityType;
import src.service.entities.attributes.Position;
import src.service.game.TurnKeeper;
import src.service.game.board.GameBoard;
import src.util.PieceType;
import src.util.PrintColor;

/**
 * Handles displaying the map of the game board. Used by HeroTurnProcess and
 * MonsterTurnProcess.
 */
public class MapDisplay implements Display {

  private final GameBoard currGameBoard;
  private final int gameSize;
  private final TurnKeeper turnKeeper;

  public MapDisplay(GameBoard gameBoard, TurnKeeper turnKeeper) {
    this.currGameBoard = gameBoard;
    this.gameSize = gameBoard.getSize();
    this.turnKeeper = turnKeeper;
  }

  @Override
  public void display() {
    System.out.println("This is the Map Screen!");
    ArrayList<Entity> entityList = currGameBoard.getEntityList();
    ArrayList<Position> entityPositions = new ArrayList<Position>();
    for (Entity entity : entityList) {
      entityPositions.add(entity.getPosition());
    }
    // rows
    for (int r = 0; r < gameSize; r++) {
      for (int i = 0; i < gameSize; i++) {
        System.out.print("+-------");
      }
      System.out.println("+");

      for (int inner = 0; inner < 3; inner++) {
        for (int c = 0; c < gameSize; c++) {
          PieceType currentPieceType = currGameBoard.getPieceAt(r, c).getPieceType();
          System.out.print("|");
          if (currentPieceType == PieceType.WALL) {
            System.out.print(" XXXXX ");
          } else if (inner == 0 && entityPositions.contains(new Position(r, c))) {
            // Check if a hero is at this position and print " H " in the center row
            boolean hasHero = false;
            int monsterCount = 0;
            boolean isActiveHero = false;
            for (int idx = 0; idx < entityList.size(); idx++) {
              Entity entity = entityList.get(idx);
              if (entity.getPosition().equals(new Position(r, c))) {
                if (entity.getType() == EntityType.HERO) {
                  hasHero = true;
                  if (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER) {
                    if (idx == turnKeeper.getPlayerTeamTurnCount()) {
                      isActiveHero = true;
                    }
                  }
                } else if (entity.getType() == EntityType.MONSTER) {
                  monsterCount++;
                }
              }
            }
            String monsterModifier = "" + (monsterCount == 1 ? " " : monsterCount > 9 ? "+" : monsterCount);
            if (isActiveHero) {
              if (hasHero && monsterCount > 0) {
                PrintColor.blue("AH  ");
                PrintColor.yellow(" M" + monsterModifier);
              } else if (hasHero) {
                PrintColor.blue("AH     ");
              } else if (monsterCount > 0) {
                PrintColor.yellow("     M" + monsterModifier);
              } else {
                System.out.print("       ");
              }
            } else {
              if (hasHero && monsterCount > 0) {
                PrintColor.red(" H  ");
                PrintColor.yellow(" M" + monsterModifier);
              } else if (hasHero) {
                PrintColor.red(" H     ");
              } else if (monsterCount > 0) {
                PrintColor.yellow("     M" + monsterModifier);
              } else {
                System.out.print("       ");
              }
            }

          } else if (currentPieceType == PieceType.HERO_NEXUS && inner == 1) {
            PrintColor.blue(" NEXUS ");
          } else if (currentPieceType == PieceType.MONSTER_NEXUS && inner == 1) {
            PrintColor.red(" NEXUS ");
          } else if (currentPieceType == PieceType.BUSH && inner == 1) {
            System.out.print("  ~B~  ");
          } else if (currentPieceType == PieceType.CAVE && inner == 1) {
            System.out.print("  [C]  ");
          } else if (currentPieceType == PieceType.KOULOU && inner == 1) {
            System.out.print("  _K_  ");

          } else if (currentPieceType == PieceType.OBSTACLE && inner == 1) {
            System.out.print("  xxx  ");

          } else if (currentPieceType == PieceType.MARKET && inner == 2) {
            PrintColor.yellow("     M ");
          } else {
            System.out.print("       ");
          }
        }
        System.out.println("|");
      }
    }
    for (int col = 0; col < gameSize; col++) {
      System.out.print("+-------");
    }
    System.out.println("+");
    System.out.println("H = Hero, M = Monster, NEXUS = Nexus, X = Wall");
  }

}
