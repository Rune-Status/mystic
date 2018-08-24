package org.mystic.game.model.content.skill.crafting;

import java.util.HashMap;
import java.util.Map;

public enum Gem {

	OPAL(1625, 1609, 891, 1, 4, true),

	JADE(1627, 1611, 891, 13, 7, true),

	REDTOPAZ(1629, 1613, 892, 16, 10, true),

	SAPPHIRE(1623, 1607, 888, 1, 13, false),

	EMERALD(1621, 1605, 889, 27, 19, false),

	RUBY(1619, 1603, 887, 34, 35, false),

	DIAMOND(1617, 1601, 890, 43, 70, false),

	DRAGONSTONE(1631, 1615, 890, 55, 95, false),

	ONYX(6571, 6573, 2717, 67, 300, false);

	private final int uncutGem, cutGem, animation, requiredLevel, experience;
	private final boolean isSemiPrecious;

	private Gem(int uncutID, int cutID, int animation, int levelReq, int XP, boolean semiPrecious) {
		this.uncutGem = uncutID;
		this.cutGem = cutID;
		this.animation = animation;
		this.requiredLevel = levelReq;
		this.experience = XP;
		this.isSemiPrecious = semiPrecious;
	}

	public short getAnimation() {
		return (short) animation;
	}

	public int getCutGem() {
		return cutGem;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

	public int getUncutGem() {
		return uncutGem;
	}

	public int getExperience() {
		return experience;
	}

	public boolean isSemiPrecious() {
		return isSemiPrecious;
	}

	private static Map<Integer, Gem> gems = new HashMap<Integer, Gem>();

	static {
		for (Gem gem : Gem.values()) {
			gems.put(gem.uncutGem, gem);
		}
	}

	public static Gem forId(int gemId) {
		return gems.get(gemId);
	}

}