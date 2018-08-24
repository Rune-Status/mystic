package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.impl.Ranged;
import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.player.Player;

public class MagicShortbowSpecialAttack implements Special {

	public static final int MAGIC_SHORTBOW_PROJECTILE_ID = 256;

	public static final int DOUBLE_SHOOT_ANIMATION_ID = 1074;

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}

	@Override
	public int getSpecialAmountRequired() {
		return 55;
	}

	@Override
	public void handleAttack(Player player) {
		SoundPlayer.play(player, Sounds.MAGIC_BOW_SPEC);
		final Ranged r = player.getCombat().getRanged();
		r.setStart(new Graphic(256, 5, true));
		r.setAnimation(new Animation(1074, 0));
		r.setProjectile(new Projectile(249));
		r.setStartGfxOffset((byte) 1);
		r.getProjectile().setDelay(35);
		r.execute(player.getCombat().getAttacking());
		r.setStartGfxOffset((byte) 0);
		r.setProjectileOffset(0);
		r.setProjectile(new Projectile(249));
	}
}
