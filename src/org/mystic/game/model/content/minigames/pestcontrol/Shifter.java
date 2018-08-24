package org.mystic.game.model.content.minigames.pestcontrol;

import org.mystic.game.GameConstants;
import org.mystic.game.model.entity.Location;
import org.mystic.utility.Misc;

public class Shifter extends Pest {

	private byte delay = 0;

	public Shifter(Location location, PestControlGame game) {
		super(game, PestControlConstants.SHIFTERS[Misc.randomNumber(PestControlConstants.SHIFTERS.length)], location);
	}

	@Override
	public void tick() {
		if (++delay == 7) {
			if (Misc.getManhattanDistance(getLocation(), getGame().getVoidKnight().getLocation()) > 2) {
				if (!isMovedLastCycle() && getCombat().getAttackTimer() == 0) {
					if (getCombat().getAttacking() != null) {
						if (getCombat().getAttacking().equals(getGame().getVoidKnight())) {
							Location l = GameConstants.getClearAdjacentLocation(getGame().getVoidKnight().getLocation(),
									getSize(), getGame().getVirtualRegion());

							if (l != null) {
								teleport(l);
							}
						} else {
							getCombat().setAttack(getGame().getVoidKnight());
						}
					} else {
						getCombat().setAttack(getGame().getVoidKnight());
					}
				}
			}

			delay = 0;
		}
	}
}
