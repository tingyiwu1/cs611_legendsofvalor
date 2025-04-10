/**
 * The Equippable interface represents items that can be equipped by a character
 * in the game. It provides methods to retrieve various bonus attributes that
 * the item grants when equipped.
 */
package src.service.entities.items;

public interface Equippable {

	public Integer getBonusStrength();

	public Integer getBonusMagicStrength();

	public Integer getBonusDefense();

	public Integer getBonusDodge();

}
