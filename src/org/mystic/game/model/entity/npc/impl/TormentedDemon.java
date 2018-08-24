package org.mystic.game.model.entity.npc.impl;

import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;

public class TormentedDemon extends Npc {

	public static void spawn() {
		new TormentedDemon(new Location(2603, 5718));
		new TormentedDemon(new Location(2612, 5727));
		new TormentedDemon(new Location(2603, 5736));
		new TormentedDemon(new Location(2594, 5727));
	}

	private int meleeDamage = 0;

	private int magicDamage = 0;

	private int rangedDamage = 0;

	public TormentedDemon(Location loc) {
		super(8349, true, loc);
	}

	@Override
	public void doPostHitProcessing(Hit hit) {
		switch (hit.getType()) {
		case MELEE:
			meleeDamage += (hit.getDamage() == 0 ? 2 : hit.getDamage());
			break;
		case MAGIC:
			magicDamage += (hit.getDamage() == 0 ? 2 : hit.getDamage());
			break;
		case RANGED:
			rangedDamage += (hit.getDamage() == 0 ? 2 : hit.getDamage());
			break;
		default:
			return;
		}
		int trans = 0;
		if ((meleeDamage > 125) && (meleeDamage >= magicDamage) && (meleeDamage >= rangedDamage))
			trans = 8349;
		else if ((magicDamage > 125) && (magicDamage >= meleeDamage) && (magicDamage >= rangedDamage))
			trans = 8350;
		else if ((rangedDamage > 125) && (rangedDamage >= meleeDamage) && (rangedDamage >= magicDamage)) {
			trans = 8351;
		}
		if (trans > 0) {
			transform(trans);
		}
	}

	@Override
	public int getAffectedDamage(Hit hit) {
		switch (hit.getType()) {
		case MAGIC:
			if (getId() == 8350) {
				return 0;
			}
			break;
		case MELEE:
			if (getId() == 8349) {
				if ((hit.getAttacker() != null) && (!hit.getAttacker().isNpc())) {
					Player player = org.mystic.game.World.getPlayers()[hit.getAttacker().getIndex()];
					if ((player != null) && (player.getMelee().isVeracEffectActive())) {
						return hit.getDamage();
					}
				}
				return 0;
			}
			break;
		case RANGED:
			if (getId() == 8351) {
				return 0;
			}
			break;
		default:
			return hit.getDamage();
		}
		return hit.getDamage();
	}
}