package org.mystic.game.model.content.combat;

import org.mystic.game.model.entity.Entity;

public class Hit {

	public enum HitTypes {
		NONE, MELEE, RANGED, MAGIC, POISON, DISEASE, DEFLECT;
	}

	/**
	 * Holds the hit priority types.
	 */
	public enum HitPriority {

		/**
		 * Low priority means that when the next loop is called that checks the hit
		 * queue, if the hit is not picked out, it is never displayed, used for hits
		 * such as Ring of Recoil.
		 */
		LOW_PRIORITY,

		/**
		 * High priority means that the hit will wait in the queue until it's displayed,
		 * used for hits such as special attacks.
		 */
		HIGH_PRIORITY;

	}

	/**
	 * The hit's priority.
	 */
	private HitPriority hitPriority;

	public HitPriority getHitPriority() {
		return hitPriority;
	}

	private int damage;

	private HitTypes type;

	private Entity attacker;

	/**
	 * The delay for the hit.
	 */
	private int delay;

	private final boolean success;

	public Hit(Entity attacker, int damage, HitPriority hitPriority, HitTypes type, int delay) {
		this.attacker = attacker;
		this.damage = damage;
		this.type = type;
		this.delay = delay;
		this.hitPriority = hitPriority;
		success = (damage > 0);
	}

	public Hit(int damage) {
		this(null, damage, HitPriority.HIGH_PRIORITY, HitTypes.NONE, 0);
	}

	public Hit(int damage, HitTypes type) {
		this(null, damage, HitPriority.HIGH_PRIORITY, type, 0);
	}

	public Hit(Entity attacker, int damage, HitTypes type) {
		this(attacker, damage, HitPriority.HIGH_PRIORITY, type, 0);
	}

	public Hit(Entity attacker, int damage, HitPriority priority, HitTypes type) {
		this(attacker, damage, priority, type, 0);
	}

	/**
	 * Creates a standard hit.
	 * 
	 * @param damage
	 *            The damage dealt by the hit.
	 */
	public Hit(int damage, int delay) {
		this(null, damage, HitPriority.HIGH_PRIORITY, HitTypes.NONE, delay);
	}

	/**
	 * Creates a standard hit.
	 * 
	 * @param damage
	 *            The damage dealt by the hit.
	 */
	public Hit(int damage, HitPriority hitPriority, HitTypes type) {
		this(null, damage, hitPriority, type, 0);
	}

	public int getCombatHitType() {
		switch (type) {
		case MELEE:
			return 0;
		case RANGED:
			return 1;
		case MAGIC:
			return 2;
		case DEFLECT:
			return 3;
		case POISON:
			return -1;
		case NONE:
		case DISEASE:
			return 0;
		}
		return 0;
	}

	public int getHitType() {
		switch (type) {
		case MELEE:
		case RANGED:
		case MAGIC:
			if (damage > 0) {
				return 1;
			}
			return 0;
		case POISON:
			return 2;
		case DISEASE:
			return 3;
		default:
			break;
		}
		return 1;
	}

	public int getDamage() {
		return damage;
	}

	public HitTypes getType() {
		return type;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setType(HitTypes type) {
		this.type = type;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	public Entity getAttacker() {
		return attacker;
	}
}