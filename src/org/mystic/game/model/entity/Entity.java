package org.mystic.game.model.entity;

import java.util.LinkedList;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Combat;
import org.mystic.game.model.content.combat.CombatInterface;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.Hit.HitTypes;
import org.mystic.game.model.content.minigames.pestcontrol.PestControlGame;
import org.mystic.game.model.entity.following.Following;
import org.mystic.game.model.entity.movement.MovementHandler;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.RegenerateSkillTask;

/**
 * Represents a single combatable entity
 * 
 * @author Michael Sasse
 * 
 */
public abstract class Entity implements CombatInterface {

	/**
	 * The primary hit to display this update cycle.
	 */
	private Hit primaryHit;

	/**
	 * The secondary hit to display this update cycle.
	 */
	private Hit secondaryHit;

	/**
	 * Sets the primary hit.
	 * 
	 * @param hit
	 *            The primary hit.
	 */
	public void setPrimaryHit(Hit hit) {
		this.primaryHit = hit;
	}

	/**
	 * Sets the secondary hit.
	 * 
	 * @param hit
	 *            The secondary hit.
	 */
	public void setSecondaryHit(Hit hit) {
		this.secondaryHit = hit;
	}

	/**
	 * Gets the primary hit.
	 * 
	 * @return The primary hit.
	 */
	public Hit getPrimaryHit() {
		return primaryHit;
	}

	/**
	 * Gets the secondary hit.
	 * 
	 * @return The secondary hit.
	 */
	public Hit getSecondaryHit() {
		return secondaryHit;
	}

	/**
	 * Resets the primary and secondary hits.
	 */
	public void resetHits() {
		primaryHit = null;
		secondaryHit = null;
	}

	/**
	 * The player index mod
	 */
	public static final int PLAYER_INDEX_MOD = 32768;

	/**
	 * The index of the entity
	 */
	private short index = -1;

	/**
	 * The size of the entity
	 */
	private byte size = 1;

	/**
	 * The last damage dealt by the entity
	 */
	private byte lastDamageDealt = 0;

	/**
	 * The entities max levels
	 */
	private short[] maxLevels = new short[25];

	/**
	 * The entities current levels
	 */
	private short[] levels = new short[25];

	/**
	 * The entities current bonuses
	 */
	private short[] bonuses = new short[13];

	/**
	 * the last attacked entity
	 */
	private Entity lastAttacked;
	private int consecutiveAttacks;
	private double rangedWeaknessMod;
	private double magicWeaknessMod;
	private double meleeWeaknessMod;

	/**
	 * A list of tasks
	 */
	private final LinkedList<Task> tasks = new LinkedList<Task>();

	/**
	 * The entity is an npc
	 */
	private boolean npc = true;

	/**
	 * The entity is active
	 */
	private boolean active = false;

	/**
	 * The entity is dead
	 */
	private boolean dead = false;

	/**
	 * The entity can take damage
	 */
	private boolean takeDamage = true;

	/**
	 * The entity can retaliate
	 */
	private boolean retaliate = true;

	/**
	 * The last hit success
	 */
	private boolean lastHitSuccess = false;

	/**
	 * The entity is poisoned
	 */
	private boolean poisoned = false;

	/**
	 * The current poison damage
	 */
	private int poisonDamage = 0;

	/**
	 * The teleblock time
	 */
	private int teleblockTime;

	/**
	 * The freeze delay
	 */
	private long freeze;

	/**
	 * The spear delay
	 */
	private long spear;

	/**
	 * The immunity from being frozen
	 */
	private long iceImmunity;

	/**
	 * The immunity from being poisoned again
	 */
	private long poisonImmunity;

	/**
	 * The immunity from being hit
	 */
	private long hitImmunity = 0L;

	/**
	 * The immunity from fire
	 */
	private long fireImmunity = 0;

	public enum AttackType {
		STAB, SLASH, CRUSH
	}

	private AttackType attackType = AttackType.CRUSH;

	public AttackType getAttackType() {
		return attackType;
	}

	public void setAttackType(AttackType attackType) {
		this.attackType = attackType;
	}

	/**
	 * the location of the entity
	 */
	private final Location location = new Location(0, 0, 0);

	/**
	 * Constructs an update flags instance
	 */
	private final UpdateFlags updateFlags = new UpdateFlags();

	/**
	 * Constructs a new combat instance
	 */
	private final Combat combat = new Combat(this);

	/**
	 * Constructs a new attributes instance
	 */
	private final Attributes attributes = new Attributes();

	/**
	 * Adds a bonus to the bonuses
	 * 
	 * @param id
	 *            The bonus id
	 * @param add
	 *            The amount to add to the bonus
	 */
	public void addBonus(int id, int add) {
		int tmp5_4 = id;
		short[] tmp5_1 = bonuses;
		tmp5_1[tmp5_4] = ((short) (tmp5_1[tmp5_4] + add));
	}

	/**
	 * Gets if the entity can take damage
	 * 
	 * @return
	 */
	public boolean canTakeDamage() {
		return takeDamage;
	}

	/**
	 * Cures poison
	 * 
	 * @param immunity
	 */
	public void curePoison(int immunity) {
		poisoned = false;
		poisonDamage = 0;
		if (immunity > 0) {
			poisonImmunity = (World.getCycles() + immunity);
		}
	}

	/**
	 * Provides fire immunity for a set amount of seconds
	 * 
	 * @param delayInSeconds
	 *            The delay of the immunity
	 */
	public void doFireImmunity(int delayInSeconds) {
		fireImmunity = System.currentTimeMillis() + (delayInSeconds * 1000);
	}

	@Override
	public boolean equals(Object other) {
		if ((other instanceof Entity)) {
			Entity e = (Entity) other;
			return (e.getIndex() == index) && (e.isNpc() == npc);
		}

		return false;
	}

	/**
	 * Forces an entity to face a direction
	 * 
	 * @param entity
	 */
	public void face(Entity entity) {
		if (entity == null) {
			return;
		}
		if (!entity.isNpc()) {
			updateFlags.faceEntity(entity.getIndex() + 32768);
		} else {
			updateFlags.faceEntity(entity.getIndex());
		}
	}

	/**
	 * Freezes an entity for a certain time
	 * 
	 * @param time
	 *            The time to freeze the entity
	 * @param immunity
	 *            The time the entity is immune to being frozen again
	 */
	public void freeze(double time, int immunity) {
		if ((isFrozen()) || (isImmuneToIce())) {
			return;
		}
		freeze = (long) (System.currentTimeMillis() + (time * 1000));
		iceImmunity = (freeze + 5000);
	}

	/**
	 * Gets the entities attributes
	 * 
	 * @return
	 */
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * Gets the bonuses for the entity
	 * 
	 * @return
	 */
	public short[] getBonuses() {
		return bonuses;
	}

	/**
	 * Gets the combat instance
	 * 
	 * @return
	 */
	public Combat getCombat() {
		return combat;
	}

	/**
	 * Gets the current following instance
	 * 
	 * @return
	 */
	public abstract Following getFollowing();

	/**
	 * Gets the entities index
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the last damage dealt
	 * 
	 * @return
	 */
	public int getLastDamageDealt() {
		return lastDamageDealt;
	}

	/**
	 * Gets the entities levels
	 * 
	 * @return
	 */
	public short[] getLevels() {
		return levels;
	}

	/**
	 * Gets the entities location
	 * 
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	public double getMagicWeaknessMod() {
		return magicWeaknessMod;
	}

	/**
	 * Gets the entities max levels
	 * 
	 * @return
	 */
	public short[] getMaxLevels() {
		return maxLevels;
	}

	public double getMeleeWeaknessMod() {
		return meleeWeaknessMod;
	}

	public Npc getMob() {
		return !npc ? null : World.getNpcs()[index];
	}

	/**
	 * Get the current movement handler instance
	 * 
	 * @return
	 */
	public abstract MovementHandler getMovementHandler();

	public Player getPlayer() {
		return npc ? null : World.getPlayers()[index];
	}

	/**
	 * Gets the poison damage
	 * 
	 * @return
	 */
	public int getPoisonDamage() {
		return poisonDamage;
	}

	public double getRangedWeaknessMod() {
		return rangedWeaknessMod;
	}

	/**
	 * Gets the size of the entity
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets a list of tasks
	 * 
	 * @return
	 */
	public LinkedList<Task> getTasks() {
		return tasks;
	}

	/**
	 * Gets the current teleblock time
	 * 
	 * @return
	 */
	public int getTeleblockTime() {
		return teleblockTime;
	}

	/**
	 * Gets the entities update flags
	 * 
	 * @return
	 */
	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

	/**
	 * Gets the wilderness level the entity is in
	 * 
	 * @return
	 */
	public int getWildernessLevel() {
		int y = location.getY();
		int level = -1;
		int modY = y > 6400 ? y - 6400 : y;
		level = (((modY - 3520) / 8) + 1);
		return level;
	}

	/**
	 * Gets the entities x coordinate
	 * 
	 * @return
	 */
	public int getX() {
		return location.getX();
	}

	/**
	 * Gets the entities y coordinate
	 * 
	 * @return
	 */
	public int getY() {
		return location.getY();
	}

	/**
	 * Gets the entities z coordinate
	 * 
	 * @return
	 */
	public int getZ() {
		return location.getZ();
	}

	/**
	 * The entity has attacked consecutively
	 * 
	 * @param check
	 * @param req
	 * @return
	 */
	public boolean hasAttackedConsecutively(Entity check, int req) {
		return (lastAttacked != null) && (lastAttacked.equals(check)) && (consecutiveAttacks >= req);
	}

	/**
	 * The entity has fire immunity
	 * 
	 * @return
	 */
	public boolean hasFireImmunity() {
		return fireImmunity > System.currentTimeMillis()
				|| (getAttributes().get("fire_resist") != null && (Boolean) getAttributes().get("fire_resist"))
				|| (getAttributes().get("super_fire_resist") != null
						&& (Boolean) getAttributes().get("super_fire_resist"));
	}

	/**
	 * The entity has super fire immunity
	 * 
	 * @return
	 */
	public boolean hasSuperFireImmunity() {
		return (getAttributes().get("super_fire_resist") != null && (Boolean) getAttributes().get("super_fire_resist"));
	}

	/**
	 * Gets if an entity is in an area
	 * 
	 * @param bottomLeft
	 *            The bottom left corner
	 * @param topRight
	 *            The top right corner
	 * @param heightSupport
	 *            Should the height be checked
	 * @return
	 */
	public boolean inArea(Location bottomLeft, Location topRight, boolean heightSupport) {
		if ((heightSupport) && (location.getZ() != bottomLeft.getZ())) {
			return false;
		}
		return (location.getX() >= bottomLeft.getX()) && (location.getX() <= topRight.getX())
				&& (location.getY() >= bottomLeft.getY()) && (location.getY() <= topRight.getY());
	}

	public boolean inBork() {
		int x = location.getX();
		int y = location.getY();
		return ((x > 3095 && x < 3116 && y > 5526 && y < 5548));
	}

	/**
	 * The entity is in duel arena
	 * 
	 * @return
	 */
	public boolean inDuelArena() {
		return (location.getX() >= 3325) && (location.getX() <= 3396) && (location.getY() >= 3199)
				&& (location.getY() <= 3289);
	}

	/**
	 * The entity is in godwars
	 * 
	 * @return
	 */
	public boolean inGodwars() {
		return inArea(new Location(2816, 5243, 2), new Location(2960, 5400, 2), false);
	}

	/**
	 * The entity is in a multi area
	 * 
	 * @return
	 */
	public boolean inMultiArea() {
		if (attributes.get(PestControlGame.PEST_GAME_KEY) != null) {
			return true;
		}
		if (inGodwars()) {
			return true;
		}
		if (inBork()) {
			return true;
		}
		int x = location.getX();
		int y = location.getY();
		int z = location.getZ();
		if (inArea(new Location(2686, 2690, 0), new Location(2825, 2816, 0), false)) {
			return true;
		}
		return x >= 2333 && y >= 3687 && x <= 2362 && y <= 3717 || x >= 3306 && y >= 9364 && x <= 3332 && y <= 9392
				|| ((x >= 2956) && (y >= 3714) && (x <= 3006) && (y <= 3750))
				|| ((x <= 2049) && (y <= 5251) && (x >= 1980) && (y >= 5178))
				|| ((x >= 2893) && (y >= 4430) && (x <= 2934) && (y <= 4471))
				|| ((x >= 1761) && (y >= 5337) && (x <= 1780) && (y <= 5370))
				|| ((x >= 3029) && (x <= 3374) && (y >= 3759) && (y <= 3903))
				|| ((x >= 2250) && (x <= 2280) && (y >= 4670) && (y <= 4720))
				|| ((x >= 3198) && (x <= 3380) && (y >= 3904) && (y <= 3970))
				|| ((x >= 3191) && (x <= 3326) && (y >= 3510) && (y <= 3759))
				|| ((x >= 2987) && (x <= 3006) && (y >= 3912) && (y <= 3930))
				|| ((x >= 2245) && (x <= 2295) && (y >= 4675) && (y <= 4720))
				|| ((x >= 3006) && (x <= 3071) && (y >= 3602) && (y <= 3710))
				|| ((x >= 3134) && (x <= 3192) && (y >= 3525) && (y <= 3646))
				|| ((z > 0) && (x >= 3460) && (x <= 3545) && (y >= 3150) && (y <= 3267))
				|| ((x >= 2369) && (x <= 2425) && (y >= 5058) && (y <= 5122))
				|| ((x >= 3241) && (y >= 9353) && (x <= 3256) && (y <= 9378))
				|| ((x >= 2914) && (y >= 4359) && (x <= 2972) && (y <= 4412));
	}

	/**
	 * The entity is in the wilderness
	 * 
	 * @return
	 */
	public boolean inWilderness() {
		int x = location.getX();
		int y = location.getY();
		return ((x > 2944 && x < 3392 && y > 3519 && y < 3966 || x > 2941 && x < 3392 && y > 9918 && y < 10366));
	}

	public static boolean inWilderness(int x, int y) {
		return ((x >= 2944) && (x < 3392) && (y >= 3520) && (y < 4026))
				|| ((y > 9919) && (y < 10009) && (x > 3065) && (x < 3151));
	}

	/**
	 * Gets if the entity is active
	 * 
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Gets if the entity is dead
	 * 
	 * @return
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * The entity is immune
	 * 
	 * @return
	 */
	public boolean isFrozen() {
		return freeze > System.currentTimeMillis();
	}

	/**
	 * Gets if the entity is immune to a hit
	 * 
	 * @return
	 */
	public boolean isImmuneToHit() {
		return System.currentTimeMillis() < hitImmunity;
	}

	/**
	 * Gets if the entity is immune to ice
	 * 
	 * @return
	 */
	public boolean isImmuneToIce() {
		return iceImmunity > System.currentTimeMillis();
	}

	/**
	 * Gets if the entity is an npc
	 * 
	 * @return
	 */
	public boolean isNpc() {
		return npc;
	}

	/**
	 * The entity is poisoned
	 * 
	 * @return
	 */
	public boolean isPoisoned() {
		return poisoned;
	}

	/**
	 * Gets if the entity can retaliate
	 * 
	 * @return
	 */
	public boolean isRetaliate() {
		return retaliate;
	}

	/**
	 * The entity is immune
	 * 
	 * @return
	 */
	public boolean isSpeared() {
		return spear > System.currentTimeMillis();
	}

	/**
	 * The entity is teleblocked
	 * 
	 * @return
	 */
	public boolean isTeleblocked() {
		return teleblockTime > 0;
	}

	/**
	 * poisons an entity
	 * 
	 * @param start
	 */
	public void poison(int start) {
		if ((poisoned) || (World.getCycles() < poisonImmunity)) {
			return;
		}
		poisoned = true;
		poisonDamage = start;
		TaskQueue.queue(new Task(this, 30) {
			int count = 0;

			@Override
			public void execute() {
				if ((!poisoned) || (poisonDamage <= 0)) {
					stop();
					return;
				}
				hit(new Hit(poisonDamage, HitTypes.POISON));
				if (++count == 4) {
					poisonDamage -= 1;
					count = 0;
					if (poisonDamage == 0) {
						stop();
					}
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	/**
	 * The entities individual process
	 * 
	 * @throws Exception
	 */
	public abstract void process() throws Exception;

	/**
	 * Resets the entitys updates
	 */
	public abstract void reset();

	/**
	 * Resets the entities combat stats
	 */
	public void resetCombatStats() {
		Player p = null;
		if (!npc) {
			p = World.getPlayers()[index];
		}

		for (int i = 0; i <= 7; i++) {
			levels[i] = maxLevels[i];
			if (!npc)
				p.getSkill().update(i);
		}
	}

	/**
	 * Resets the entities levels
	 */
	public void resetLevels() {
		for (int i = 0; i < 25; i++) {
			levels[i] = maxLevels[i];
		}

		if (!npc) {
			Player p = World.getPlayers()[index];

			if (p != null) {
				p.getSkill().update();
			}
		}
	}

	public void resetSpear() {
		spear = 0;
	}

	public abstract void retaliate(Entity attacked);

	/**
	 * Sets if the entity is active
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Sets the bonuses for the entity
	 * 
	 * @param bonuses
	 */
	public void setBonuses(int[] bonuses) {
		for (int i = 0; i < bonuses.length; i++)
			this.bonuses[i] = ((short) bonuses[i]);
	}

	/**
	 * Sets the entity dead
	 * 
	 * @param dead
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public int setFreeze(int value) {
		return (int) (freeze = value);
	}

	/**
	 * Sets the hit immunity in seconds
	 * 
	 * @param delay
	 *            The seconds the entity is immune to hits
	 */
	public void setHitImmunityDelay(int delay) {
		hitImmunity = (System.currentTimeMillis() + delay * 1000);
	}

	/**
	 * Sets the entities index
	 * 
	 * @param index
	 *            The index to place the npc
	 */
	public void setIndex(int index) {
		this.index = ((short) index);
	}

	/**
	 * Sets the last damage dealt
	 * 
	 * @param lastDamageDealt
	 */
	public void setLastDamageDealt(int lastDamageDealt) {
		this.lastDamageDealt = ((byte) lastDamageDealt);
	}

	/**
	 * Sets the last hit successful
	 * 
	 * @param lastHitSuccess
	 */
	public void setLastHitSuccess(boolean lastHitSuccess) {
		this.lastHitSuccess = lastHitSuccess;
	}

	/**
	 * Sets the entities levels
	 * 
	 * @param levels
	 */
	public void setLevels(int[] levels) {
		for (int i = 0; i < levels.length; i++)
			this.levels[i] = ((short) levels[i]);
	}

	public void setMagicWeaknessMod(double magicWeaknessMod) {
		this.magicWeaknessMod = magicWeaknessMod;
	}

	/**
	 * Sets the entities max levels
	 * 
	 * @param maxLevels
	 */
	public void setMaxLevels(int[] maxLevels) {
		for (int i = 0; i < maxLevels.length; i++)
			this.maxLevels[i] = ((short) maxLevels[i]);
	}

	public void setMeleeWeaknessMod(double meleeWeaknessMod) {
		this.meleeWeaknessMod = meleeWeaknessMod;
	}

	/**
	 * Sets if the entity is an npc
	 * 
	 * @param npc
	 */
	public void setNpc(boolean npc) {
		this.npc = npc;
	}

	/**
	 * Sets the poison damage
	 * 
	 * @param poisonDamage
	 */
	public void setPoisonDamage(int poisonDamage) {
		this.poisonDamage = poisonDamage;
	}

	public void setRangedWeaknessMod(double rangedWeaknessMod) {
		this.rangedWeaknessMod = rangedWeaknessMod;
	}

	/**
	 * Sets if the entity can retalite
	 * 
	 * @param retaliate
	 */
	public void setRetaliate(boolean retaliate) {
		this.retaliate = retaliate;
	}

	/**
	 * Sets the size of the entity
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		this.size = ((byte) size);
	}

	/**
	 * Sets if the entity can take damage
	 * 
	 * @param takeDamage
	 */
	public void setTakeDamage(boolean takeDamage) {
		this.takeDamage = takeDamage;
	}

	/**
	 * Sets the teleblock time
	 * 
	 * @param teleblockTime
	 *            The time to teleblock the entity
	 */
	public void setTeleblockTime(int teleblockTime) {
		this.teleblockTime = teleblockTime;
	}

	/**
	 * Starts a regeneration task
	 */
	public void startRegeneration() {
		TaskQueue.queue(new RegenerateSkillTask(this, 75));
	}

	public void stun(double time) {
		spear = (long) (System.currentTimeMillis() + (time * 1000));
	}

	/**
	 * Teleblocks an entity for a certain amount of time
	 * 
	 * @param time
	 *            The time to teleblock the entity
	 */
	public void teleblock(int time) {
		if (isTeleblocked()) {
			return;
		}
		teleblockTime = time;
		tickTeleblock();
	}

	/**
	 * Ticks the teleblock
	 */
	public void tickTeleblock() {
		TaskQueue.queue(new Task(this, 1) {
			@Override
			public void execute() {
				if (--teleblockTime <= 0) {
					teleblockTime = 0;
					stop();
				}
			}

			@Override
			public void onStop() {
			}

		});
	}

	/**
	 * Unfreezes an entity
	 */
	public void unfreeze() {
		freeze = 0L;
	}

	/**
	 * Gets if the last hit was successful
	 * 
	 * @return
	 */
	public boolean wasLastHitSuccess() {
		return lastHitSuccess;
	}

}