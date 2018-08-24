package org.mystic.game.model.content.combat;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;

public class CombatConstants {

	public static final long CLEAR_DAMAGE_HISTORY = 60000L;

	public static final byte MELEE_DISTANCE = 1;

	public static final byte RANGED_DISTANCE = 9;

	public static final byte MAGIC_DISTANCE = 11;

	public static final byte HIT_TYPE_BLOCK = 0;

	public static final byte HIT_TYPE_HIT = 1;

	public static final byte HIT_TYPE_POISON = 2;

	public static final byte HIT_TYPE_DISEASE = 3;

	public static final short DEFAULT_ATTACK_ANIMATION = 422;

	public static final short DEFAULT_BLOCK_ANIMATION = 424;

	public static final byte DEFAULT_ATTACK_SPEED = 5;

	public static int getDistanceForCombatType(CombatTypes type) {
		switch (type) {
		case RANGED:
			return 7;
		case MAGIC:
			return 10;
		case MELEE:
			return 1;
		}
		return 1;
	}

	public static final Location getOffsetProjectileLocation(Entity e) {
		int offset = e.getSize() / 3;
		Location p = e.getLocation();
		return new Location(p.getX() + offset, p.getY() + offset, p.getZ());
	}
}
