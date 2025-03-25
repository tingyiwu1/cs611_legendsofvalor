package src.service.game.battle;

import java.util.ArrayList;

import src.service.entities.Player;
import src.service.entities.attributes.AttackOption;
import src.service.entities.heroes.Hero;
import src.service.entities.items.Item;
import src.service.entities.monsters.Monster;
import src.service.game.PlayerControl;
import src.service.game.StatusDisplay;
import src.util.TextColor;

public class Battle implements PlayerControl, StatusDisplay {

	public Player player;
	public Monster monster;
	public Hero hero;
	public Character lastInput;

	private ArrayList<AttackOption> heroAttacks;
	private AttackOption monsterAttack;

	public Boolean isBattleOver = false;

	private ArrayList<String> statuses;
	private ArrayList<TextColor> statusColors;

	public Battle(Player player, Monster monster){
		this.monster = monster;
		this.player = player;
		this.hero = player.getFirstHero();

		this.heroAttacks = hero.getAttacksList();
		this.monsterAttack = monster.mainHandAttack();

		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
	}

	public Boolean battleCycle(){
		
		this.addStatus("Hero health: " + hero.getCurrentHealth(), TextColor.BLACK);
		this.addStatus("Monster health: " + monster.getCurrentHealth(), TextColor.BLACK);
		return true;
		
	}

	public Boolean isBattleOver(){
		return this.isBattleOver;
	}

	private Boolean heroAttack(int idx){
		AttackOption chosenAttackOption = this.heroAttacks.get(idx);

		// System.out.println("Hero: " + chosenAttackOption.getDescription());
		this.addStatus("Hero: "+ chosenAttackOption.getDescription(), TextColor.CYAN);
		monster.takeDamage(chosenAttackOption.getDamage());
		Item heroItem = chosenAttackOption.getSourceItem();
		// System.out.println("The hero's " + monsterItem.getName() + " does " + chosenAttackOption.getDamage() + " damage!");
		this.addStatus("The hero's " + heroItem.getName() + " does " + chosenAttackOption.getDamage(), TextColor.CYAN);
		if(monster.getCurrentHealth() <= 0){
			this.isBattleOver = true;
			return true;
		}
		return false;	
	}

	private Boolean monsterAttack(){

		// System.out.println("Monster: " + this.monsterAttack.getDescription());
		this.addStatus("Monster: " + this.monsterAttack.getDescription(), TextColor.PURPLE);
		hero.takeDamage(this.monsterAttack.getDamage());
		Item monsterItem = this.monsterAttack.getSourceItem();
		// System.out.println("The monster's " + monsterItem.getName() + " does " + this.monsterAttack.getDamage() + " damage!");
		this.addStatus("The monster's " + monsterItem.getName() + " does " + this.monsterAttack.getDamage(), TextColor.PURPLE);
		if(hero.getCurrentHealth() <= 0){
			this.isBattleOver = true;
			return true;
		}
		return false;
	}



	@Override
	public Boolean isMoveValid(Character inputtedMove) {
		// TODO: Make this a status update instead of a print statement
		int inputInt;

		try{
			inputInt = Integer.parseInt(inputtedMove.toString());
		} catch (NumberFormatException e){
			this.addStatus("Invalid Move, please try again.", TextColor.RED);
			return false;
		}

		if(inputInt >= 1 && inputInt <= this.heroAttacks.size()){
			return true;
		}
		return false;
	}

	@Override
	public Boolean makeMove(Character inputtedMove) {
		this.lastInput = Character.toLowerCase(inputtedMove);
		this.clearStatuses();

		if(!isMoveValid(lastInput)){
			// System.out.println("INVALID MOVE");
			this.addStatus("Invalid Move, please try again.", TextColor.RED);
			return false;
		} 

		processMove(inputtedMove);
		return null;
		// throw new UnsupportedOperationException("Unimplemented method 'makeMove'");
	}

	@Override
	public Boolean processMove(Character inputtedMove) {

		int inputInt = Integer.parseInt(inputtedMove.toString());

		this.addStatus("Processing attack: " + inputInt, TextColor.CYAN);

		if(this.heroAttack(inputInt - 1)){
			this.addStatus("The hero has defeated the monster!", TextColor.YELLOW);
			return null;
		}
		if(monsterAttack()){
			this.addStatus("The monster has defeated the hero :(", TextColor.YELLOW);
			return null;
		}
		this.battleCycle();
		return null;
	}

	public ArrayList<AttackOption> getHeroAttacks(){
		return this.heroAttacks;
	}

	@Override
	public void clearStatuses() {
		// TODO Auto-generated method stub
		this.statuses.clear();
		this.statusColors.clear();
	}

	@Override
	public void addStatus(String status, TextColor color) {
		this.statuses.add(status);
		this.statusColors.add(color);
	}

	@Override
	public void removeStatus(int index) {
		this.statuses.remove(index);
		this.statusColors.remove(index);
		
	}

	@Override
	public String[] getStatusList() {
		return this.statuses.toArray(new String[0]);
	}

	@Override
	public TextColor[] getStatusColors() {
		return this.statusColors.toArray(new TextColor[0]);
	}


}
