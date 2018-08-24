package org.mystic.game.model.content.skill.woodcutting;

import java.util.HashMap;
import java.util.Map;

public enum Tree {

	NORMAL_TREE(new int[] { 1276, 1278, 1316, 1315, 1318 }, 1, 1511, 1342, 22, 25.0D),

	DEAD_TREE(new int[] { 1286 }, 1, 1511, 1342, 22, 25.0D),

	OAK_TREE(new int[] { 1281 }, 15, 1521, 1356, 30, 37.5D),

	WILLOW_TREE(new int[] { 1308, 5551, 5552, 5553 }, 30, 1519, 7399, 38, 67.5D),

	MAPLE_TREE(new int[] { 1307, 4674 }, 45, 1517, 1343, 45, 100.0D),

	YEW_TREE(new int[] { 1309 }, 60, 1515, 7402, 50, 175.0D),

	MAGIC_TREE(new int[] { 1306 }, 75, 1513, 7401, 55, 250.0D);

	private static Map<Integer, Tree> trees = new HashMap<Integer, Tree>();

	static {
		for (Tree i : values()) {
			int[] arrayOfInt;
			int m = (arrayOfInt = i.getObjects()).length;
			for (int k = 0; k < m; k++) {
				trees.put(arrayOfInt[k], i);
			}
		}
	}

	public static Tree forId(int id) {
		return trees.get(Integer.valueOf(id));
	}

	public static boolean isTree(int objectId) {
		for (Tree data : values()) {
			for (int i : data.getObjects()) {
				if (i == objectId) {
					return true;
				}
			}
		}
		return false;
	}

	private final int levelRequired, reward, replacementId, respawnTimer;

	private final double experience;

	private final int[] objects;

	private Tree(final int[] objects, final int level, final int reward, final int replacement, final int respawnTimer,
			final double experience) {
		this.objects = objects;
		this.levelRequired = level;
		this.reward = reward;
		this.replacementId = replacement;
		this.respawnTimer = respawnTimer;
		this.experience = experience;
	}

	public final double getExperience() {
		return experience;
	}

	public final int[] getObjects() {
		return objects;
	}

	public final int getLevelRequired() {
		return levelRequired;
	}

	public final int getReplacement() {
		return replacementId;
	}

	public final int getRespawnTimer() {
		return respawnTimer;
	}

	public final int getReward() {
		return reward;
	}

}