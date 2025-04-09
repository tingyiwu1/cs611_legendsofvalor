package src.service.game;

import src.service.entities.Player;
import src.service.entities.monsters.MonsterTeam;

public class TurnKeeper {

	private enum CurrentTurn {
		PLAYER,
		MONSTER
	}

	private Player player;
	private MonsterTeam monsterTeam;
	private CurrentTurn currentTurn;
	private int turnCount;
	private int teamTurnCount;

	public TurnKeeper(Player player, MonsterTeam monsterTeam) {
		this.player = player;
		this.monsterTeam = monsterTeam;
		this.turnCount = 0;
		this.teamTurnCount = 0;
		this.currentTurn = CurrentTurn.PLAYER; // Player starts first
	}

	public CurrentTurn getCurrentTurn() {
		return currentTurn;
	}
	public int getTurnCount() {
		return turnCount;
	}

	public int getPlayerTeamTurnCount() {
		if(currentTurn == CurrentTurn.PLAYER) {
			return teamTurnCount;
		} else {
			throw new IllegalStateException("It's not the player's turn.");
		}
	}

	public int getMonsterTeamTurnCount() {
		if(currentTurn == CurrentTurn.MONSTER) {
			return teamTurnCount;
		} else {
			throw new IllegalStateException("It's not the MONSTER's turn.");
		}
	}

	/**
	 * Progresses the turn to the next player or monster.
	 * @Return Returns a boolean if the turn changed
	 */
	public boolean progressTurn() {
		System.out.println("PROGRESSING TURN");
		System.out.println("Current Turn: " + currentTurn);
		System.out.println("Team Turn Count: " + teamTurnCount);
		if(currentTurn == CurrentTurn.PLAYER) {
			teamTurnCount++;
			if(teamTurnCount >= player.getParty().length) {
				currentTurn = CurrentTurn.MONSTER;
				teamTurnCount = 0;
				return true;
			}
		} else {
			teamTurnCount++;
			if(teamTurnCount >= monsterTeam.getMonsters().size()) {
				currentTurn = CurrentTurn.PLAYER;
				teamTurnCount = 0;
				// Increment the turn count for the total turns taken
				turnCount++;
				return true;
			}
		}


		return false;
	}

	public void resetTurn() {
		this.turnCount = 0;
		this.teamTurnCount = 0;
		this.currentTurn = CurrentTurn.PLAYER; // Reset to player turn
	}



}
