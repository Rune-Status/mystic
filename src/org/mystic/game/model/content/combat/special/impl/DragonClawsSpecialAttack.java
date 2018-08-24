package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.impl.Attack;
import org.mystic.game.model.content.combat.impl.Melee;
import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;

public class DragonClawsSpecialAttack implements Special {

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}

	@Override
	public int getSpecialAmountRequired() {
		return 50;
	}

	@Override
	public void handleAttack(Player player) {
		Melee melee = player.getCombat().getMelee();
		Entity attacking = player.getCombat().getAttacking();
		melee.setAnimation(new Animation(10961));
		player.getUpdateFlags().sendGraphic(new Graphic(1950, 0, false));
		melee.execute(attacking);
		int damage = player.getLastDamageDealt();
		if (damage == 0) {
			melee.execute(attacking);
			int d2 = player.getLastDamageDealt();
			melee.setAttack(new Attack(2, melee.getAttack().getAttackDelay()), new Animation(10961));
			if (d2 == 0) {
				melee.execute(attacking);
			} else {
				melee.setNextDamage(d2 / 2);
				melee.execute(attacking);
			}
		} else {
			melee.setNextDamage(damage / 2);
			melee.execute(attacking);
			melee.setAttack(new Attack(2, melee.getAttack().getAttackDelay()), new Animation(10961));
			int n = player.getLastDamageDealt();
			melee.setNextDamage(n / 2);
			melee.execute(attacking);
			melee.setNextDamage(n - player.getLastDamageDealt());
		}
	}
}
