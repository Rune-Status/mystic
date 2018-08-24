package org.mystic.game.model.content.skill.melee;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.ItemCheck;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.GraphicTask;
import org.mystic.utility.Misc;

public class BarrowsSpecials {

	public static final Graphic GUTHAN_SPECIAL_GRAPHIC = Graphic.highGraphic(398, 0);

	public static final Graphic TORAG_SPECIAL_GRAPHIC = Graphic.highGraphic(399, 0);

	public static final Graphic KARIL_SPECIAL_GRAPHIC = Graphic.highGraphic(401, 0);

	public static final Graphic AHRIM_SPECIAL_GRAPHIC = Graphic.highGraphic(400, 0);

	public static void checkForBarrowsSpecial(Player player) {
		if (Misc.randomNumber(100) > 25) {
			return;
		}
		if (player.getSpecialAttack().isInitialized()) {
			return;
		}
		if (ItemCheck.wearingFullBarrows(player, "Dharok")) {
			player.getSpecialAttack().toggleSpecial();
		} else if (ItemCheck.wearingFullBarrows(player, "Guthan")) {
			player.getMelee().setGuthanEffectActive(true);
		} else if (ItemCheck.wearingFullBarrows(player, "Torag")) {
			player.getMelee().setToragEffectActive(true);
		} else if ((player.getCombat().getCombatType() == CombatTypes.MAGIC)
				&& (ItemCheck.wearingFullBarrows(player, "Ahrim"))) {
			player.getMagic().setAhrimEffectActive(true);
		} else if (ItemCheck.wearingFullBarrows(player, "Verac")) {
			player.getMelee().setVeracEffectActive(true);
		} else if (ItemCheck.wearingFullBarrows(player, "Karil")) {
			player.getRanged().setKarilEffectActive(true);
		}
	}

	public static void doAhrimEffect(Player player, Entity attack, int damage) {
		TaskQueue.queue(new GraphicTask(4, false, AHRIM_SPECIAL_GRAPHIC, attack));
		player.getMagic().setAhrimEffectActive(false);
		if ((damage > 0) && (Misc.randomNumber(4) == 0)) {
			int newLvl = attack.getLevels()[2] - 5;
			attack.getLevels()[2] = ((byte) (newLvl > 0 ? newLvl : 0));
			if (!attack.isNpc()) {
				Player p2 = org.mystic.game.World.getPlayers()[attack.getIndex()];
				if (p2 == null) {
					return;
				}
				p2.getSkill().update(2);
			}
			player.getClient().queueOutgoingPacket(new SendMessage("You drain some of your opponent's strength."));
		}
	}

	public static void doGuthanEffect(Player player, Entity attack, Hit hit) {
		int newLvl = player.getLevels()[3] + hit.getDamage();
		int maxLvl = player.getMaxLevels()[3];
		player.getLevels()[3] = ((short) (newLvl > maxLvl ? maxLvl : newLvl));
		player.getSkill().update(3);
		attack.getUpdateFlags().sendGraphic(GUTHAN_SPECIAL_GRAPHIC);
		player.getMelee().setGuthanEffectActive(false);
		player.getClient().queueOutgoingPacket(new SendMessage("You absorb some of your opponent's hitpoints."));
	}

	public static void doKarilEffect(Player player, Entity attack) {
		attack.getUpdateFlags().sendGraphic(KARIL_SPECIAL_GRAPHIC);
		player.getRanged().setKarilEffectActive(false);
		int newLvl = attack.getLevels()[6] - 5;
		attack.getLevels()[6] = ((byte) (newLvl > 0 ? newLvl : 0));
		if (!attack.isNpc()) {
			Player p2 = org.mystic.game.World.getPlayers()[attack.getIndex()];
			if (p2 == null) {
				return;
			}
			p2.getSkill().update(6);
		}
		player.getClient().queueOutgoingPacket(new SendMessage("You drain some of your opponent's magic."));
	}

	public static void doToragEffect(Player player, Entity attack) {
		attack.getUpdateFlags().sendGraphic(TORAG_SPECIAL_GRAPHIC);
		player.getMelee().setToragEffectActive(false);
		if (!attack.isNpc()) {
			Player p = org.mystic.game.World.getPlayers()[attack.getIndex()];
			if (p == null) {
				return;
			}
			p.getRunEnergy().deduct(0.2D);
			p.getRunEnergy().update();
			player.getClient().queueOutgoingPacket(new SendMessage("You have drained some of your opponent's energy."));
		}
	}

}