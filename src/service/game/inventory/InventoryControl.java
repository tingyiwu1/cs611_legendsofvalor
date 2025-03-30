/**
 * The InventoryControl interface defines the contract for managing and 
 * validating inventory-related actions in a game. It provides methods 
 * to check the validity of a move, execute a move, and process a move 
 * within the inventory system.

 */
package src.service.game.inventory;



public interface InventoryControl {
	public Boolean isMoveValid(Integer itemSlot, Integer itemIndex);
	public Boolean makeMove(Integer itemSlot, Integer itemIndex);
	public Boolean processMove(Integer itemSlot, Integer itemIndex);
}
