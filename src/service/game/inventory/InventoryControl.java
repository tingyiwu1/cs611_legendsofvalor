package src.service.game.inventory;

public interface InventoryControl {
	public Boolean isMoveValid(Integer itemSlot, Integer itemIndex);
	public Boolean makeMove(Integer itemSlot, Integer itemIndex);
	public Boolean processMove(Integer itemSlot, Integer itemIndex);
}
