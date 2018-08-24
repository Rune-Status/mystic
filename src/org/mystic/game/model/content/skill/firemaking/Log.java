package org.mystic.game.model.content.skill.firemaking;

import java.util.HashMap;

public enum Log {

	NORMAL_LOG(1511, 1, 40.0D),

	ACHEY_LOG(2862, 1, 40.0D),

	OAK_LOG(1521, 15, 60.0D),

	WILLOW_LOG(1519, 30, 90.0D),

	TEAK_LOG(6333, 35, 105.0D),

	ARCTIC_PINE_LOG(10810, 42, 125.0D),

	MAPLE_LOG(1517, 45, 135.0D),

	MOHOGANY_LOG(6332, 50, 157.5D),

	EUCALYPTUS_LOG(12581, 58, 193.5D),

	YEW_LOG(1515, 60, 202.5D),

	MAGIC_LOG(1513, 75, 303.80000000000001D);

	private static final HashMap<Integer, Log> logs = new HashMap<Integer, Log>();

	static {
		for (Log data : values()) {
			logs.put(Integer.valueOf(data.logId), data);
		}
	}

	public static Log getLogById(int id) {
		return logs.get(Integer.valueOf(id));
	}

	private final int logId, levelRequired;

	private final double experience;

	private Log(final int logId, final int levelRequired, final double exp) {
		this.logId = logId;
		this.levelRequired = levelRequired;
		this.experience = exp;
	}

	public final double getExperience() {
		return experience;
	}

	public final int getLevelRequired() {
		return levelRequired;
	}

	public final int getLogId() {
		return logId;
	}

}