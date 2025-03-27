package src.service.game.battle;

import java.util.ArrayList;

import src.service.entities.Player;
import src.service.entities.attributes.AttackOption;
import src.service.entities.heroes.Hero;
import src.service.entities.items.Item;
import src.service.entities.monsters.Monster;
import src.service.game.PlayerControl;
import src.service.game.StatusDisplay;
import src.util.PrintColor;
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

	private Boolean gameOver;
	private Boolean didLevelUp;
	private Boolean isBossBattle;

	public Battle(Player player, Monster monster){
		this.monster = monster;
		this.player = player;
		this.hero = player.getFirstHero();

		this.heroAttacks = hero.getAttacksList();
		this.monsterAttack = monster.mainHandAttack();

		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
		this.gameOver = false;
		this.didLevelUp = false;
		this.isBossBattle = false;
	}
	public Battle(Player player, Boolean isBoss){
		this.monster = new Monster();
		this.player = player;
		this.hero = player.getFirstHero();

		this.heroAttacks = hero.getAttacksList();
		this.monsterAttack = monster.mainHandAttack();

		this.statuses = new ArrayList<String>();
		this.statusColors = new ArrayList<TextColor>();
		this.gameOver = false;
		this.didLevelUp = false;
		this.isBossBattle = true;
	}

	

	public Boolean battleCycle(){
		
		this.addStatus("Hero health: " + hero.getCurrentHealth(), TextColor.WHITE);
		this.addStatus("Monster health: " + monster.getCurrentHealth(), TextColor.WHITE);
		return true;
		
	}

	public Boolean isBattleOver(){
		return this.isBattleOver;
	}
	public boolean isBossBattle(){
		return this.isBossBattle;
	}

	private Boolean heroAttack(int idx){
		AttackOption chosenAttackOption = this.heroAttacks.get(idx);

		// System.out.println("Hero: " + chosenAttackOption.getDescription());
		this.addStatus("Hero: "+ chosenAttackOption.getDescription(), TextColor.CYAN);
		Item heroItem = chosenAttackOption.getSourceItem();
		if(heroItem.use()){
			this.addStatus("The hero's " + heroItem.getName() + " does " + chosenAttackOption.getDamage() + "damage!", TextColor.CYAN);
			monster.takeDamage(chosenAttackOption.getDamage());
		} else {
			this.addStatus("The hero's " + heroItem.getName() + " Fails to do anything! Can you use this item?", TextColor.CYAN);
		}
		if(monster.getCurrentHealth() <= 0){
			this.isBattleOver = true;
			this.processHeroWin();
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
		this.addStatus("The monster's " + monsterItem.getName() + " does " + this.monsterAttack.getDamage() + "damage!", TextColor.PURPLE);
		if(hero.getCurrentHealth() <= 0){
			this.isBattleOver = true;
			this.processMonsterWin();
			return true;
		}
		return false;
	}

	private void processHeroWin(){
		String name = this.hero.getName();
		this.addStatus("------------------------\n", TextColor.WHITE);
		this.addStatus( name + " has defeated the monster!", TextColor.GREEN);
		int rewardedXP = monster.getRewardXP();
		int rewardedGold = monster.getRewardGold();
		this.addStatus( name + " obtains " + rewardedGold + " Gold!", TextColor.GREEN);
		this.addStatus( name + " obtains " + rewardedXP	 + " XP!", TextColor.GREEN);
		if(this.hero.willLevelUp(rewardedXP)){
			
			this.processHeroLevelUp();
		}
		this.hero.earnGold(rewardedGold);
		this.hero.gainExperience(rewardedXP);
	}

	private void processHeroLevelUp(){
		String name = this.hero.getName();
		this.addStatus( name + " Levelled up!", TextColor.GREEN);
		this.didLevelUp = true;
		this.addStatus( name + " Earns 100 Gold for levelling up!", TextColor.GREEN);
		this.addStatus( name + " levels each of its status up by 5!", TextColor.GREEN);
		this.addStatus( name + " regains max spell usage!", TextColor.CYAN);

		this.addRainbowStatus( "... you fall deeper into your dream ... ");

	}

	private void processMonsterWin(){
		this.gameOver = true;
		this.addStatus( this.monster.getName() +" has defeated the Hero!", TextColor.RED);
	}

	public Boolean isGameOver(){
		return this.gameOver;
	}
	
	public Boolean getDidLevelUp(){
		return this.didLevelUp;
	}



	@Override
	public Boolean isMoveValid(Character inputtedMove) {
		if(inputtedMove.equals('i') || inputtedMove.equals('b')){
			return true;
		}

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
		// at the beginning of the turn, update your move list
		this.heroAttacks = hero.getAttacksList();
		this.lastInput = Character.toLowerCase(inputtedMove);
		this.clearStatuses();

		if(!isMoveValid(lastInput)){
			// System.out.println("INVALID MOVE");
			this.addStatus(Character.toString(inputtedMove), TextColor.YELLOW);
			this.addStatus("Invalid Move, please try again.", TextColor.RED);
			return false;
		} 

		processMove(inputtedMove);
		return null;
	}

	@Override
	public Boolean processMove(Character inputtedMove) {
		if(inputtedMove.equals('i') || inputtedMove.equals('b')){
			return null;
		}

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

	public Hero getHero(){
		return this.hero;
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

	public void addRainbowStatus(String status) {
		TextColor[] rainbowColors = {
			TextColor.RED, TextColor.ORANGE, TextColor.YELLOW, 
			TextColor.GREEN, TextColor.BLUE, TextColor.PURPLE
		};

		StringBuilder rainbowStatus = new StringBuilder();
		for (int i = 0; i < status.length(); i++) {
			TextColor color = rainbowColors[i % rainbowColors.length];
			rainbowStatus.append(PrintColor.textWithColor(String.valueOf(status.charAt(i)), color));
		}
		this.addStatus("", TextColor.BLACK);
		this.addStatus(rainbowStatus.toString(), TextColor.RESET);
		this.addStatus("", TextColor.BLACK);
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
