package org.mystic.game.model.entity.following.impl;

import org.mystic.game.GameConstants;
import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.following.Following;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.pathfinding.RS317PathFinder;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class PlayerFollowing extends Following {
	private final Player player;

	public PlayerFollowing(Player player) {
		super(player);
		this.player = player;
	}

	@Override
	public void findPath(Location location) {
		if (type == Following.FollowType.COMBAT) {
			if (player.getCombat().getCombatType() == CombatTypes.MELEE)
				RS317PathFinder.findRoute(player, location.getX(), location.getY(), false, 0, 0);
			else
				RS317PathFinder.findRoute(player, location.getX(), location.getY(), true, 16, 16);
		} else
			RS317PathFinder.findRoute(player, location.getX(), location.getY(), true, 16, 16);
	}

	@Override
	public void onCannotReach() {
		reset();
		if (type == Following.FollowType.COMBAT) {
			player.getCombat().reset();
			return;
		}
		player.getClient().queueOutgoingPacket(new SendMessage("I can't reach that!"));
	}

	@Override
	public boolean pause() {
		if (type == Following.FollowType.COMBAT) {
			if (GameConstants.withinBlock(following.getLocation().getX(), following.getLocation().getY(),
					following.getSize(), player.getLocation().getX(), player.getLocation().getY())) {
				return false;
			}

			if (following.isNpc()) {
				CombatTypes c = player.getCombat().getCombatType();

				if ((c == CombatTypes.MAGIC) || (c == CombatTypes.RANGED)) {
					Npc mob = org.mystic.game.World.getNpcs()[following.getIndex()];

					if (!mob.withinMobWalkDistance(player)) {
						return false;
					}
				}
			}

			if ((!player.getLocation().equals(following.getLocation()))
					&& (player.getCombat().withinDistanceForAttack(player.getCombat().getCombatType(), true))) {
				return true;
			}

		}

		return false;
	}
}
