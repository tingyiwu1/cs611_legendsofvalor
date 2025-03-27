package src.util;

public enum ItemType {
	WEAPON("Weapon"), 
	BIG_WEAPON("Two Handed Weapon"),
	SPELL("Spell"),
	POTION("Potion"),
	CONSUMABLE("Consumable"),
	HELMET("Helmet"),
	CHEST("Chestplate"),
	LEGS("Leggings"),
	BOOTS("Boots");

	private final String displayName;

	ItemType(String displayName) {
        this.displayName = displayName;
    }

	@Override
	public String toString(){
		return displayName;
	}

}
