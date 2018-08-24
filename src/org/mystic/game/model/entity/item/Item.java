package org.mystic.game.model.entity.item;

import org.mystic.game.model.definition.EquipmentDefinition;
import org.mystic.game.model.definition.ItemBonusDefinition;
import org.mystic.game.model.definition.ItemDefinition;
import org.mystic.game.model.definition.RangedStrengthDefinition;
import org.mystic.game.model.definition.RangedWeaponDefinition;
import org.mystic.game.model.definition.SpecialAttackDefinition;
import org.mystic.game.model.definition.WeaponDefinition;
import org.mystic.utility.GameDefinitionLoader;

public class Item {

	public static ItemDefinition getDefinition(int id) {
		return GameDefinitionLoader.getItemDef(id);
	}

	public static EquipmentDefinition getEquipmentDefinition(int id) {
		return GameDefinitionLoader.getEquipmentDefinition(id);
	}

	public static short[] getItemBonuses(int id) {
		ItemBonusDefinition def = GameDefinitionLoader.getItemBonusDefinition(id);
		if (def != null) {
			return def.getBonuses();
		}
		return null;
	}

	public static RangedWeaponDefinition getRangedDefinition(int id) {
		return GameDefinitionLoader.getRangedWeaponDefinition(id);
	}

	public static int getRangedStrengthBonus(int id) {
		RangedStrengthDefinition def = GameDefinitionLoader.getRangedStrengthDefinition(id);
		return def == null ? 0 : def.getBonus();
	}

	public static SpecialAttackDefinition getSpecialDefinition(int id) {
		return GameDefinitionLoader.getSpecialDefinition(id);
	}

	public static WeaponDefinition getWeaponDefinition(int id) {
		if ((id < 0) || (id > 20144)) {
			return null;
		}

		return GameDefinitionLoader.getWeaponDefinition(id);
	}

	private short id;

	private int amount;

	public Item() {
	}

	public Item(int id) {
		this.id = ((short) id);
		amount = 1;
	}

	public Item(int id, int amount) {
		this.id = ((short) id);
		this.amount = amount;
	}

	public Item(Item item) {
		id = ((short) item.getId());
		amount = item.getAmount();
	}

	public void add(int amount) {
		this.amount += amount;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof Item)) {
			Item item = (Item) obj;
			return item.getId() == id;
		}
		return false;
	}

	public int getAmount() {
		return amount;
	}

	public ItemDefinition getDefinition() {
		return GameDefinitionLoader.getItemDef(id);
	}

	public EquipmentDefinition getEquipmentDefinition() {
		return GameDefinitionLoader.getEquipmentDefinition(id);
	}

	public int getId() {
		return id;
	}

	public short[] getItemBonuses() {
		ItemBonusDefinition def = GameDefinitionLoader.getItemBonusDefinition(id);
		if (def != null) {
			return def.getBonuses();
		}
		return null;
	}

	public byte[][] getItemRequirements() {
		return GameDefinitionLoader.getItemRequirements(id);
	}

	public RangedWeaponDefinition getRangedDefinition() {
		return GameDefinitionLoader.getRangedWeaponDefinition(id);
	}

	public int getRangedStrengthBonus() {
		RangedStrengthDefinition def = GameDefinitionLoader.getRangedStrengthDefinition(id);
		return def == null ? 0 : def.getBonus();
	}

	public SpecialAttackDefinition getSpecialDefinition() {
		return GameDefinitionLoader.getSpecialDefinition(id);
	}

	public WeaponDefinition getWeaponDefinition() {
		return GameDefinitionLoader.getWeaponDefinition(id);
	}

	public void note() {
		int noteId = getDefinition().getNoteId();

		if (noteId == -1) {
			return;
		}

		id = ((short) noteId);
	}

	public void remove(int amount) {
		this.amount -= amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setId(int id) {
		this.id = ((short) id);
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", amount=" + amount + "]";
	}

	public void unNote() {
		int noteId = getDefinition().getNoteId();

		if (noteId == -1) {
			return;
		}

		id = ((short) noteId);
	}
}
