package org.mystic.game.model.content.minigames.pestcontrol;

import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class Splatter extends Pest {

	public Splatter(Location location, PestControlGame game) {
		super(game, PestControlConstants.SPLATTERS[Misc.randomNumber(PestControlConstants.SPLATTERS.length)], location);
	}

	@Override
	public void onDeath() {
		if (Misc.getManhattanDistance(getGame().getVoidKnight().getLocation(), getLocation()) <= 2) {
			getGame().getVoidKnight().hit(new Hit(3 + Misc.randomNumber(20)));
		}
		for (Player k : getGame().getPlayers()) {
			if (Misc.getManhattanDistance(k.getLocation(), getLocation()) <= 2) {
				k.hit(new Hit(4 + Misc.randomNumber(13)));
			}
		}
	}

	@Override
	public void tick() {
		if (Misc.getManhattanDistance(getGame().getVoidKnight().getLocation(), getLocation()) <= 2) {
			getLevels()[3] = 0;
			checkForDeath();
		}
	}

}
