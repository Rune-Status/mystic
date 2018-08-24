package org.mystic.game.model.content.skill.crafting;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.entity.item.Item;

public enum Spinnable {

	BOWSTRING(new Item(1779), new Item(1777), 15.0D, 10),

	WOOL(new Item(1737), new Item(1759), 2.5D, 1),

	ROPE(new Item(10814), new Item(954), 25.0D, 30),

	MAGIC_STRING(new Item(6051), new Item(6038), 30.0D, 19),

	YEW_STRING(new Item(6049), new Item(9438), 15.0D, 10),

	SINEW_STRING(new Item(9436), new Item(9438), 15.0D, 10);

	private final Item item, outcome;

	private final double experience;

	private final int requiredLevel;

	public final static Map<Integer, Spinnable> spins = new HashMap<Integer, Spinnable>();

	static {
		for (Spinnable spinnable : values()) {
			spins.put(Integer.valueOf(spinnable.getItem().getId()), spinnable);
		}
	}

	public static Spinnable forId(int id) {
		return spins.get(Integer.valueOf(id));
	}

	private Spinnable(final Item item, final Item outcome, final double experience, final int requiredLevel) {
		this.item = item;
		this.outcome = outcome;
		this.experience = experience;
		this.requiredLevel = requiredLevel;
	}

	public final double getExperience() {
		return experience;
	}

	public final Item getItem() {
		return item;
	}

	public final Item getOutcome() {
		return outcome;
	}

	public final int getRequiredLevel() {
		return requiredLevel;
	}

}