package org.mystic.game.model.content.skill.smithing;

import java.util.HashMap;

import org.mystic.game.model.entity.item.Item;

public enum Bar {

	BRONZE_BAR(new Item[] { new Item(436), new Item(438) }, new Item(2349), 1, 8.5D),

	BLURITE_BAR(new Item[] { new Item(668) }, new Item(9467), 8, 10.0D),

	IRON_BAR(new Item[] { new Item(440) }, new Item(2351), 15, 15.5D),

	SILVER_BAR(new Item[] { new Item(442) }, new Item(2355), 20, 20.0D),

	STEEL_BAR(new Item[] { new Item(440), new Item(453, 2) }, new Item(2353), 30, 25.5D),

	GOLD_BAR(new Item[] { new Item(444) }, new Item(2357), 40, 35.5D),

	PERFECT_GOLD_BAR(new Item[] { new Item(446) }, new Item(2365), 40, 35.5D),

	MITHRIL_BAR(new Item[] { new Item(447), new Item(453, 4) }, new Item(2359), 50, 50.0D),

	ADAMANITE_BAR(new Item[] { new Item(449), new Item(453, 6) }, new Item(2361), 70, 75.5D),

	RUNITE_BAR(new Item[] { new Item(451), new Item(453, 8) }, new Item(2363), 85, 95.0D);

	private static HashMap<Integer, Bar> smelting = new HashMap<Integer, Bar>();

	static {
		for (Bar ores : values()) {
			smelting.put(Integer.valueOf(ores.getResult().getId()), ores);
		}
	}

	private final Item[] requiredOres;
	private final Item result;
	private final int levelRequired;

	private final double exp;

	private Bar(final Item[] requiredOres, final Item result, final int levelRequired, final double exp) {
		this.requiredOres = requiredOres;
		this.result = result;
		this.levelRequired = levelRequired;
		this.exp = exp;
	}

	public double getExp() {
		return exp;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public Item[] getRequiredOres() {
		return requiredOres;
	}

	public Item getResult() {
		return result;
	}

}