package org.mystic.game.model.content.combat.special.effects;

import org.mystic.cache.map.Region;
import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Directions.NormalDirection;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class DragonSpearEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		if (!e.isNpc()) {
			Player p2 = org.mystic.game.World.getPlayers()[e.getIndex()];
			if (p2 == null) {
				return;
			}
			if (!e.isSpeared()) {
				e.stun(3);
			}
			walk(p, p2);
			p.getClient().queueOutgoingPacket(new SendMessage("You stun your opponent."));
		}
	}

	private void walk(Player player, Player victim) {
		NormalDirection dir = player.getLocation().direction(victim.getLocation());
		if (dir == null) {
			return;
		}
		boolean pushNorth = dir.equals(NormalDirection.NORTH);
		boolean pushSouth = dir.equals(NormalDirection.SOUTH);
		boolean pushEast = dir.equals(NormalDirection.EAST);
		boolean pushWest = dir.equals(NormalDirection.WEST);
		int victimX = victim.getLocation().getX(), victimY = victim.getLocation().getY();
		if (pushNorth && !Region.getRegion(victim.getLocation().getX(), victim.getLocation().getY())
				.blockedNorth(victim.getLocation().getX(), victim.getLocation().getY(), victim.getLocation().getZ())) {
			victimY++;
		} else if (pushSouth && !Region.getRegion(victim.getLocation().getX(), victim.getLocation().getY())
				.blockedSouth(victim.getLocation().getX(), victim.getLocation().getY(), victim.getLocation().getZ())) {
			victimY--;
		} else if (pushEast && !Region.getRegion(victim.getLocation().getX(), victim.getLocation().getY())
				.blockedEast(victim.getLocation().getX(), victim.getLocation().getY(), victim.getLocation().getZ())) {
			victimX++;
		} else if (pushWest && !Region.getRegion(victim.getLocation().getX(), victim.getLocation().getY())
				.blockedWest(victim.getLocation().getX(), victim.getLocation().getY(), victim.getLocation().getZ())) {
			victimX--;
		}
		victim.getCombat().reset();
		victim.getMovementHandler().walkTo2(victimX, victimY);
	}
}
