package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.impl.Ranged;
import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;

public class DarkBowSpecialAttack implements Special {

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}

	@Override
	public int getSpecialAmountRequired() {
		return 60;
	}

	@Override
	public void handleAttack(Player player) {
		final Ranged r = player.getCombat().getRanged();
		final Item ammo = player.getEquipment().getItems()[13];
		if (ammo != null) {
			if ((ammo.getId() == 11212) || (ammo.getId() == 11227) || (ammo.getId() == 0) || (ammo.getId() == 11228)) {
				r.setProjectile(new Projectile(1099));
				r.setEnd(new Graphic(1100, 0, true));
			} else {
				r.setProjectile(new Projectile(1101));
				r.setEnd(new Graphic(1103, 0, true));
			}
		}
		r.setProjectileOffset(1);
	}
}
