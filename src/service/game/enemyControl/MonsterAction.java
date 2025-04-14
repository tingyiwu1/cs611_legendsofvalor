package src.service.game.enemyControl;

import java.util.Optional;

import src.service.entities.attributes.Position;
import src.service.entities.heroes.Hero;
import src.service.entities.monsters.Monster;

public class MonsterAction {
  private final Monster monster;
  private final Position newPosition;
  private final Hero targetHero;

  private MonsterAction(Monster monster, Position newPosition, Hero targetHero) {
    this.monster = monster;
    this.newPosition = newPosition;
    this.targetHero = targetHero;
  }

  public static MonsterAction move(Monster monster, Position newPosition) {
    return new MonsterAction(monster, newPosition, null);
  }

  public static MonsterAction attack(Monster monster, Hero targetHero) {
    return new MonsterAction(monster, null, targetHero);
  }

  public static MonsterAction doNothing(Monster monster) {
    return new MonsterAction(monster, null, null);
  }

  public Optional<Position> getNewPosition() {
    return Optional.ofNullable(newPosition);
  }

  public Optional<Hero> getTargetHero() {
    return Optional.ofNullable(targetHero);
  }

  public Monster getMonster() {
    return monster;
  }
}
