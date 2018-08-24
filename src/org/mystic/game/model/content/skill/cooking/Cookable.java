package org.mystic.game.model.content.skill.cooking;

import java.util.HashMap;
import java.util.Map;

public enum Cookable {

	RAW_SHRIMP(317, 1, 34, 315, 323, 30.0D),

	SARDINE(327, 1, 38, 325, 369, 40.0D),

	ANCHOVIES(321, 1, 34, 319, 323, 30.0D),

	HERRING(345, 5, 41, 347, 357, 50.0D),

	MACKEREL(353, 10, 45, 355, 353, 60.0D),

	TROUT(335, 15, 50, 333, 343, 70.0D),

	COD(341, 18, 52, 339, 343, 75.0D),

	PIKE(349, 20, 53, 351, 343, 80.0D),

	SALMON(331, 25, 58, 329, 343, 90.0D),

	SLIMY_EEL(3379, 28, 58, 3381, 3383, 95.0D),

	TUNA(359, 30, 65, 361, 367, 100.0D),

	KARAMBWAN(3142, 30, 200, 3144, 3148, 190.0D),

	RAINBOW_FISH(10138, 35, 60, 10136, 10140, 110.0D),

	CAVE_EEL(5001, 38, 40, 4003, 5002, 115.0D),

	LOBSTER(377, 40, 74, 379, 381, 120.0D),

	BASS(363, 43, 80, 365, 367, 130.0D),

	SWORDFISH(371, 45, 86, 373, 375, 140.0D),

	LAVA_EEL(2148, 53, 53, 2149, 3383, 30.0D),

	MONKFISH(7944, 62, 92, 7946, 7948, 150.0D),

	SHARK(383, 80, 99, 385, 387, 210.0D),

	SEA_TURTLE(395, 82, 150, 397, 399, 212.0D),

	CAVEFISH(15264, 88, 150, 15266, 15268, 214.0D),

	MANTA_RAY(389, 91, 150, 391, 393, 216.0D),

	ANGLERFISH(15270, 93, 150, 15272, 15274, 225.0D);

	private static final Map<Integer, Cookable> food = new HashMap<Integer, Cookable>();

	static {
		for (Cookable data : values()) {
			food.put(Integer.valueOf(data.getFoodId()), data);
		}
	}

	public static Cookable forId(int id) {
		return food.get(Integer.valueOf(id));
	}

	private final int foodId, levelRequired, noBurnLevel, replacement, burnt;

	private final double experience;

	private Cookable(final int food, final int level, final int noBurn, final int replacement, final int burnt,
			double exp) {
		this.foodId = food;
		this.levelRequired = level;
		this.noBurnLevel = noBurn;
		this.experience = exp;
		this.replacement = replacement;
		this.burnt = burnt;
	}

	public final int getBurnt() {
		return burnt;
	}

	public final double getExperience() {
		return experience;
	}

	public final int getFoodId() {
		return foodId;
	}

	public final int getLevelRequired() {
		return levelRequired;
	}

	public final int getNoBurnLevel() {
		return noBurnLevel;
	}

	public int getReplacement() {
		return replacement;
	}

}