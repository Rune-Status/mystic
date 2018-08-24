package org.mystic.game.model.entity.npc.impl;

import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;

public class CorporealBeast extends Npc {

	private Npc[] darkEnergyCores = null;

	public CorporealBeast() {
		super(8133, true, new Location(2946, 4386));
	}

	public boolean areCoresDead() {
		if (darkEnergyCores == null) {
			return true;
		}
		for (Npc mob : darkEnergyCores) {
			if (!mob.isDead()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void doPostHitProcessing(Hit hit) {
		if ((getCombatants().size() != 0) && (getLevels()[3] != 0) && (getLevels()[3] <= 150) && (areCoresDead()))
			darkEnergyCores = DarkEnergyCore.spawn();
	}

	@Override
	public void onDeath() {
		darkEnergyCores = null;
	}

	public void spawn() {
		new CorporealBeast();
	}
}
