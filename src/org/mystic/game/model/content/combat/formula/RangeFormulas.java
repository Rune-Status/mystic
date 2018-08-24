package org.mystic.game.model.content.combat.formula;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.prayer.PrayerBook;
import org.mystic.game.model.content.skill.prayer.PrayerBook.PrayerBookType;
import org.mystic.game.model.definition.RangedWeaponDefinition;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.ItemCheck;
import org.mystic.game.model.entity.player.Player;

/**
 * Mystic - Combat Formulas (Ranged)
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class RangeFormulas {

	public static final double[][] RANGED_PRAYER_MODIFIERS = { { 3.0, 0.05 }, { 11.0, 0.1 }, { 19.0, 0.15 } };

	public static int calculateRangeAttack(Player player) {
		if ((player != null) && (ItemCheck.hasDFireShield(player)) && (player.getMagic().isDFireShieldEffect())) {
			return 100;
		}
		int level = player.getSkill().getLevels()[Skills.RANGED];
		if (player.getSpecialAttack().isInitialized()) {
			level *= getRangedSpecialAccuracyModifier(player);
		}
		if (ItemCheck.wearingFullVoidRanged(player)) {
			level += player.getSkill().getLevelForExperience(Skills.RANGED,
					player.getSkill().getExperience()[Skills.RANGED]) * 0.45;
		}
		if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT) {
			if (player.getPrayer().active(3)) {
				level *= 1.05;
			} else if (player.getPrayer().active(11)) {
				level *= 1.10;
			} else if (player.getPrayer().active(19)) {
				level *= 1.15;
			}
		}
		return level + (player.getBonuses()[4] * 3);
	}

	public static int calculateRangeDefence(Player player) {
		int level = player.getSkill().getLevels()[Skills.DEFENCE];
		if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT) {
			if (player.getPrayer().active(0)) {
				level += player.getSkill().getLevelForExperience(1, player.getSkill().getExperience()[1]) * 0.05;
			} else if (player.getPrayer().active(5)) {
				level += player.getSkill().getLevelForExperience(1, player.getSkill().getExperience()[1]) * 0.10;
			} else if (player.getPrayer().active(13)) {
				level += player.getSkill().getLevelForExperience(1, player.getSkill().getExperience()[1]) * 0.15;
			} else if (player.getPrayer().active(24)) {
				level += player.getSkill().getLevelForExperience(1, player.getSkill().getExperience()[1]) * 0.20;
			} else if (player.getPrayer().active(25)) {
				level += player.getSkill().getLevelForExperience(1, player.getSkill().getExperience()[1]) * 0.25;
			}
		}
		return level + player.getBonuses()[9] + (player.getBonuses()[9] / 2);
	}

	public static int getEffectiveRangedStrength(Player player) {
		Item weapon = player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT];
		if ((weapon == null) || (weapon.getRangedDefinition() == null)) {
			return 0;
		}
		int strength = player.getBonuses()[12];
		if ((weapon.getRangedDefinition().getType() == RangedWeaponDefinition.RangedTypes.THROWN)
				|| (weapon.getRangedDefinition().getArrows() == null)
				|| (weapon.getRangedDefinition().getArrows().length == 0)) {
			strength = weapon.getRangedStrengthBonus();
		} else {
			Item ammo = player.getEquipment().getItems()[EquipmentConstants.AMMO_SLOT];
			if (ammo != null) {
				strength = ammo.getRangedStrengthBonus();
			}
		}
		return strength;
	}

	public static int getMaxHit(Player player) {
		double prayer_bonus = 1.0;
		int style_bonus = 0;
		switch (player.getEquipment().getAttackStyle()) {
		case ACCURATE:
			style_bonus = 3;
			break;
		case AGGRESSIVE:
			style_bonus = 2;
			break;
		case DEFENSIVE:
			style_bonus = 1;
			break;
		default:
			break;
		}
		if (player.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
			for (int i = 0; i < RANGED_PRAYER_MODIFIERS.length; i++) {
				if (player.getPrayer().active((int) RANGED_PRAYER_MODIFIERS[i][0])) {
					prayer_bonus += RANGED_PRAYER_MODIFIERS[i][1];
					break;
				}
			}
		}
		int strength = player.getSkill().getLevels()[Skills.RANGED];
		double calculation = (int) (strength * prayer_bonus + style_bonus);
		int ranged_strength = getEffectiveRangedStrength(player);
		Entity defending = player.getCombat().getAttacking();
		if ((defending != null) && (!defending.isNpc())) {
			Player p = org.mystic.game.World.getPlayers()[defending.getIndex()];
			if ((p != null) && (p.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.CURSES)
					&& (player.getPrayer().active(2))) {
				ranged_strength = (int) (ranged_strength
						* (defending.hasAttackedConsecutively(player, 25) ? 0.8 : 0.9));
			}
		}
		double base = 5.0 + (calculation + 8.0) * (ranged_strength + 64) / 64.0;
		if (player.getSpecialAttack().isInitialized()) {
			base = (int) (base * getRangedSpecialModifier(player));
		}
		Item helmet = player.getEquipment().getItems()[0];
		if ((helmet != null) && (helmet.getId() == 15492) && (player.getCombat().getAttacking().isNpc())
				&& (player.getSlayer().hasTask())) {
			Npc npc = org.mystic.game.World.getNpcs()[player.getCombat().getAttacking().getIndex()];
			if ((npc != null) && player.getSlayer().getTask() == npc.getId()) {
				base += base * 0.125;
			}
		}
		if (ItemCheck.wearingFullVoidRanged(player)) {
			base += base * 0.10;
		}
		return (int) base / 10;
	}

	public static double getRangedSpecialAccuracyModifier(Player player) {
		Item weapon = player.getEquipment().getItems()[3];
		Item arrow = player.getEquipment().getItems()[13];
		if (weapon == null) {
			return 1.0;
		}
		switch (weapon.getId()) {
		case 11235:
			if (arrow.getId() == 11212) {
				return 1.5;
			}
			return 1.3;
		case 13883:
		case 15241:
			return 1.2;
		case 20088:
		case 9185:
			if (arrow.getId() == 9243)
				return 1.25;
			if (arrow.getId() == 9244)
				return 1.85;
			if (arrow.getId() == 9245) {
				return 1.25;
			}
			return 1.0;
		}
		return 1.0;
	}

	public static double getRangedSpecialModifier(Player player) {
		Item weapon = player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT];
		Item arrow = player.getEquipment().getItems()[EquipmentConstants.AMMO_SLOT];
		if (weapon == null || arrow == null) {
			return 1.0;
		}
		switch (weapon.getId()) {
		case 11235:
			if (arrow.getId() == 11212) {
				return 1.50;
			}
			return 1.30;
		case 13883:
		case 15241:
			return 1.20;
		case 20088:
		case 9185:
			if (arrow.getId() == 9243)
				return 1.15;
			if (arrow.getId() == 9244)
				return 1.45;
			if (arrow.getId() == 9245) {
				return 1.15;
			}
			return 1.0;
		}
		return 1.0;
	}

}