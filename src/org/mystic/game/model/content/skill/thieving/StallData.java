package org.mystic.game.model.content.skill.thieving;

import java.util.HashMap;

public enum StallData {

	ARDY_CAKE_STALL(2561, 1, -1, 30D, new int[][] { StallTask.ARDY_ITEMS[0], StallTask.ARDY_ITEMS[1], }),

	ARDY_SILK_STALL(2560, 30, -1, 50D, new int[][] { StallTask.ARDY_ITEMS[2] }),

	ARDY_SILVER_STALL(2565, 50, -1, 70.0D, new int[][] { StallTask.ARDY_ITEMS[13] }),

	ARDY_SPICE_STALL(2564, 70, -1, 90D, new int[][] { StallTask.ARDY_ITEMS[14] }),

	ARDY_GEM_STALL(2562, 80, -1, 130.0D,
			new int[][] { StallTask.ARDY_ITEMS[9], StallTask.ARDY_ITEMS[10], StallTask.ARDY_ITEMS[11],
					StallTask.ARDY_ITEMS[12], StallTask.ARDY_ITEMS[9], StallTask.ARDY_ITEMS[10],
					StallTask.ARDY_ITEMS[11], StallTask.ARDY_ITEMS[12], StallTask.ARDY_ITEMS[15] }),

	KELDAGRIM_BAKER_STALL(6163, 1, -1, 15.0D,
			new int[][] { StallTask.THEIVING_ITEMS[0], StallTask.THEIVING_ITEMS[1], StallTask.THEIVING_ITEMS[2],
					StallTask.THEIVING_ITEMS[3], StallTask.THEIVING_ITEMS[4], StallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_CROSSBOW_STALL(17031, 10, -1, 30.0D,
			new int[][] { StallTask.THEIVING_ITEMS[1], StallTask.THEIVING_ITEMS[2], StallTask.THEIVING_ITEMS[3],
					StallTask.THEIVING_ITEMS[4], StallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_CRAFTING_STALL(6166, 25, -1, 40.0D, new int[][] { StallTask.THEIVING_ITEMS[2],
			StallTask.THEIVING_ITEMS[3], StallTask.THEIVING_ITEMS[4], StallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_CLOTHES_STALL(6165, 50, -1, 50.0D,
			new int[][] { StallTask.THEIVING_ITEMS[3], StallTask.THEIVING_ITEMS[4], StallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_SILVER_STALL(6164, 65, -1, 115.0D,
			new int[][] { StallTask.THEIVING_ITEMS[4], StallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_GEM_STALL(6162, 90, -1, 110.0D, new int[][] { StallTask.THEIVING_ITEMS[4], StallTask.THEIVING_ITEMS[5] }),

	SEED_STALL_LUMB(7053, 35, -1, 50.0D, new int[][] { { 5319, 1 }, { 5324, 1 }, { 5322, 1 }, { 5320, 1 }, { 5096, 1 },
			{ 5097, 1 }, { 5098, 1 }, { 5318, 1 }, { 5319, 1 }, { 6036, 1 } }),

	RELLEKKA_FISH(4277, 85, -1, 135.0D, new int[][] { { 383, 1 }, { 7944, 1 }, { 377, 1 }, { 7944, 1 }, { 15270, 1 },
			{ 377, 1 }, { 327, 1 }, { 327, 1 }, { 327, 1 } }),

	RELLEKKA_FUR(4278, 20, -1, 50.0D, new int[][] { { 6814, 1 } });

	private static HashMap<Integer, StallData> stalls = new HashMap<Integer, StallData>();

	static {
		for (StallData data : values()) {
			stalls.put(Integer.valueOf(data.getObjectId()), data);
		}
	}

	public static StallData getObjectById(int id) {
		return stalls.get(Integer.valueOf(id));
	}

	private final int objectId, levelRequired, replacementId;

	private final double experience;

	private final int[][] rewards;

	private StallData(final int id, final int level, final int replace, final double experience,
			final int[][] rewards) {
		this.objectId = id;
		this.levelRequired = level;
		this.replacementId = replace;
		this.experience = experience;
		this.rewards = rewards;
	}

	public final double getExperience() {
		return experience;
	}

	public final int getLevelRequired() {
		return levelRequired;
	}

	public final int getObjectId() {
		return objectId;
	}

	public final int getReplacementId() {
		return replacementId;
	}

	public final int[][] getRewards() {
		return rewards;
	}

}