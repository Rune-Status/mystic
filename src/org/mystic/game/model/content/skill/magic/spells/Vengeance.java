package org.mystic.game.model.content.skill.magic.spells;

import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.Hit.HitPriority;
import org.mystic.game.model.content.combat.Hit.HitTypes;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.magic.Spell;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class Vengeance extends Spell {

	public static final int DELAY = 30000;

	public static final double RECOIL = 0.55D;

	public static void recoil(Player player, Hit hit) {
		if (hit.getDamage() <= 0) {
			return;
		}
		int recoil = (int) (hit.getDamage() * 0.55D);
		hit.getAttacker().hit(new Hit(player, recoil, HitPriority.LOW_PRIORITY, HitTypes.DEFLECT));
		player.getUpdateFlags().sendForceMessage("Taste Vengeance!");
		player.getMagic().deactivateVengeance();
	}

	@Override
	public boolean execute(Player player) {
		if (player.getSkill().getLevels()[Skills.DEFENCE] >= 40) {
			if (player.getDueling().isDueling()) {
				if (player.getDueling().getRuleToggle() != null) {
					if (player.getDueling().getRuleToggle()[4]) {
						player.getClient()
								.queueOutgoingPacket(new SendMessage("Magic has been disabled during this duel."));
						return false;
					}
				}
			}
			if (System.currentTimeMillis() - player.getMagic().getLastVengeance() < 30000L) {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You can only cast this spell once every 30 seconds."));
				return false;
			}
			player.getUpdateFlags().sendAnimation(4410, 0);
			player.getUpdateFlags().sendGraphic(Graphic.highGraphic(726, 0));
			player.getMagic().activateVengeance();
		} else {
			player.send(new SendMessage("You need a defence level of at least 40 to cast this spell."));
		}
		return true;
	}

	@Override
	public double getExperience() {
		return 112.0D;
	}

	@Override
	public int getLevel() {
		return 94;
	}

	@Override
	public String getName() {
		return "Vengeance";
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(9075, 4), new Item(557, 10), new Item(560, 2) };
	}
}
