package org.mystic.game.model.entity.npc.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class WallBeast extends Npc {

	private byte delay = 0;

	public WallBeast(Location location) {
		super(7823, true, location);
	}

	@Override
	public void process() {
		if (delay > 0) {
			delay = ((byte) (delay - 1));
			return;
		}
		for (Player p : World.getPlayers())
			if ((p != null) && (p.getLocation().getX() - getLocation().getX() == 0)
					&& (p.getLocation().getY() - getLocation().getY() == -1)) {
				getUpdateFlags().sendAnimation(new Animation(1802, 0));
				p.hit(new Hit(Misc.randomNumber(5) + 1));
				p.getUpdateFlags().sendAnimation((p.getCombat().getBlockAnimation()));
				delay = 5;
				return;
			}
	}
}
