package org.mystic.game.model.entity.following.impl;

import org.mystic.game.GameConstants;
import org.mystic.game.model.content.combat.Combat;
import org.mystic.game.model.definition.NpcCombatDefinition;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.following.Following;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.pathfinding.SimplePathWalker;
import org.mystic.utility.Misc;

/**
 * Handles mob following
 * 
 * @author Michael Sasse
 * 
 */
public class MobFollowing extends Following {

	public static final String NEXT_DIR_KEY = "nextfollowdir";

	private final Npc mob;

	public MobFollowing(Npc mob) {
		super(mob);
		this.mob = mob;
	}

	@Override
	public void findPath(Location location) {
		if ((mob.isLockFollow()) && (Misc.getManhattanDistance(mob.getLocation(), mob.getOwner().getLocation()) > 10)) {
			onCannotReach();
		} else if (type == FollowType.DEFAULT && GameConstants.withinBlock(following.getX(), following.getY(),
				following.getSize(), mob.getX(), mob.getY(), mob.getSize())) {
			if (mob.getAttributes().get("nextfollowdir") == null) {
				Location l = GameConstants.getClearAdjacentLocation(following.getLocation(), mob.getSize());
				if (l != null) {
					SimplePathWalker.walkToNextTile(mob, l);
					if (mob.getMovementHandler().getPrimaryDirection() != -1)
						mob.getAttributes().set("nextfollowdir",
								Integer.valueOf(mob.getMovementHandler().getPrimaryDirection()));
				}
			} else {
				int dir = mob.getAttributes().getInt("nextfollowdir");
				SimplePathWalker.walkToNextTile(mob, new Location(mob.getX() + GameConstants.DIR[dir][0],
						mob.getY() + GameConstants.DIR[dir][1], mob.getZ()));
			}
		} else {
			if (mob.getAttributes().get("nextfollowdir") != null) {
				mob.getAttributes().remove("nextfollowdir");
			}
			SimplePathWalker.walkToNextTile(mob, location);
		}
	}

	@Override
	public void onCannotReach() {
		if (mob.isLockFollow()) {
			Location l = GameConstants.getClearAdjacentLocation(mob.getOwner().getLocation(), mob.getSize());
			if (l != null) {
				mob.teleport(l);
			}
			setFollow(mob.getOwner(), Following.FollowType.DEFAULT);
		} else if (mob.getOwner() != null) {
			mob.remove();
		} else {
			reset();
			if (type == Following.FollowType.COMBAT) {
				mob.getCombat().reset();
			}
		}
	}

	@Override
	public boolean outOfRange() {
		if (ignoreDistance) {
			return false;
		}
		if ((!mob.getCombat().inCombat()) && (mob.isWalkToHome())) {
			return true;
		}
		return !isWithinDistance();
	}

	@Override
	public boolean pause() {
		if (GameConstants.withinBlock(following.getX(), following.getY(), following.getSize(), mob.getX(), mob.getY(),
				mob.getSize())) {
			return false;
		}
		if (this.type == Following.FollowType.COMBAT) {
			if (mob.getCombatDefinition() != null) {
				NpcCombatDefinition.CombatTypes type = mob.getCombatDefinition().getCombatType();

				if (type == NpcCombatDefinition.CombatTypes.MELEE)
					return mob.getCombat().withinDistanceForAttack(Combat.CombatTypes.MELEE, false);
				if (type == NpcCombatDefinition.CombatTypes.RANGED) {
					return mob.getCombat().withinDistanceForAttack(Combat.CombatTypes.RANGED, false);
				}

				return mob.getCombat().withinDistanceForAttack(Combat.CombatTypes.MAGIC, false);
			} else {
				return mob.getCombat().withinDistanceForAttack(mob.getCombat().getCombatType(), false);
			}
		}
		int x = entity.getLocation().getX();
		int y = entity.getLocation().getY();
		int x2 = following.getLocation().getX();
		int y2 = following.getLocation().getY();
		Location[] a = GameConstants.getBorder(x, y, entity.getSize());
		Location[] b = GameConstants.getBorder(x2, y2, following.getSize());
		for (Location i : a) {
			for (Location k : b) {
				if ((Math.abs(i.getX() - k.getX()) < 2) && (Math.abs(i.getY() - k.getY()) < 2)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void process() {
		if (following != null) {
			if (mob.isNoFollow()) {
				reset();
			} else {
				follow();
			}
		}
	}

}