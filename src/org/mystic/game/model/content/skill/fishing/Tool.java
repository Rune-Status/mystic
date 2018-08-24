package org.mystic.game.model.content.skill.fishing;

import java.util.HashMap;
import java.util.Map;

public enum Tool {

	SMALL_NET(303, 621, new short[] { 317, 3150, 321, 5004, 7994 }),

	BIG_NET(305, 621, new short[] { 353, 341, 363 }),

	CRAYFISH_CAGE(13431, 619, new short[] { 13435 }),

	FISHING_ROD(307, 622, new short[] { 327, 345, 349, 3379, 5001, 2148 }),

	FLYFISHING_ROD(309, 622, new short[] { 335, 331 }),

	KARAMBWAN_POT(3157, -1, new short[] { 3142 }),

	HARPOON(311, 618, new short[] { 359, 371 }),

	LOBSTER_POT(301, 619, new short[] { 377 });

	private static final Map<Integer, Tool> tools = new HashMap<Integer, Tool>();

	static {
		for (Tool tool : values()) {
			tools.put(Integer.valueOf(tool.getToolId()), tool);
		}
	}

	public static Tool forId(int id) {
		return tools.get(Integer.valueOf(id));
	}

	private final int toolId, animation;

	private final short[] outcomes;

	private Tool(final int toolId, final int animation, final short[] outcomes) {
		this.toolId = toolId;
		this.outcomes = outcomes;
		this.animation = animation;
	}

	public final int getAnimationId() {
		return animation;
	}

	public final short[] getOutcomes() {
		return outcomes;
	}

	public final int getToolId() {
		return toolId;
	}

}