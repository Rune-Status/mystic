package org.mystic.game.model.entity.npc;

import java.util.HashMap;
import java.util.Map;

public enum NpcFollowDistance {

	BOSSES(new int[] { 8133, 6247, 6248, 6252, 6250, 6204, 6203, 6208, 6265, 6263, 6261, 6260, 6225, 6227, 6223, 6222 },
			40);

	private static Map<Integer, NpcFollowDistance> mobs;

	static {
		mobs = new HashMap<Integer, NpcFollowDistance>();

		for (NpcFollowDistance def : values()) {
			for (int k = 0; k < def.mobId.length; k++) {
				mobs.put(def.mobId[k], def);
			}
		}
	}

	private static NpcFollowDistance forId(int id) {
		return mobs.get(Integer.valueOf(id));
	}

	public static int getDistance(int mobId) {
		NpcFollowDistance def = forId(mobId);
		return def == null ? 8 : def.getDistance();
	}

	private int[] mobId;

	private int distance;

	private NpcFollowDistance(int[] mobId, int distance) {
		this.mobId = mobId;
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public int[] getMobId() {
		return mobId;
	}
}
