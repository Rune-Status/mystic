package org.mystic.game.model.content.skill.crafting;

import java.util.HashMap;
import java.util.Map;

public enum Glass {

	MOLTEN_GLASS(1775, 1, 20.0D, 1781),

	BEER_GLASS(1919, 1, 17.5D, 1775),

	CANDLE_LANTERN(4527, 4, 19.0D, 1775),

	OIL_LAMP(4522, 12, 25.0D, 1775),

	VIAL(229, 33, 35.0D, 1775),

	FISHBOWL(6667, 42, 42.5D, 1775),

	UNPOWDERED_ORD(567, 46, 52.5D, 1775),

	LANTERN_LENS(4542, 49, 55.0D, 1775),

	LIGHT_ORB(10973, 87, 70.0D, 1775);

	private final short rewardId, levelRequired;

	private final double experience;

	private final int materialId;

	public static final Map<Integer, Glass> glass = new HashMap<Integer, Glass>();

	static {
		for (Glass glassType : values()) {
			glass.put(Integer.valueOf(glassType.getRewardId()), glassType);
		}
	}

	public static Glass forReward(int id) {
		return glass.get(Integer.valueOf(id));
	}

	private Glass(final int rewardId, final int levelRequired, final double experience, final int materialId) {
		this.rewardId = ((short) rewardId);
		this.levelRequired = ((short) levelRequired);
		this.experience = experience;
		this.materialId = materialId;
	}

	public final double getExperience() {
		return experience;
	}

	public final int getMaterialId() {
		return materialId;
	}

	public final int getRequiredLevel() {
		return levelRequired;
	}

	public final int getRewardId() {
		return rewardId;
	}

}