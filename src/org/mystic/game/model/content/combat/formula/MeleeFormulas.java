package org.mystic.game.model.content.combat.formula;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.prayer.PrayerBook.PrayerBookType;
import org.mystic.game.model.content.skill.prayer.PrayerConstants;
import org.mystic.game.model.content.skill.prayer.impl.CursesPrayerBook;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.NpcConstants;
import org.mystic.game.model.entity.player.Equipment;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.ItemCheck;
import org.mystic.game.model.entity.player.Player;

/**
 * Mystic - Combat Formulas (Melee)
 * 
 * @author Vali - http://www.rune-server.org/members/Valli
 *
 */
public class MeleeFormulas {

	public static double MODIFIER = 0.14;

	public static int getAttackRoll(Player player) {
		int level = player.getSkill().getLevels()[Skills.ATTACK];
		int attack = getAttackPrayerModifier(player, level);
		int bonus = player.getBonuses()[getHighestMeleeAttackBonus(player)];
		int modifier = (int) Math.round(bonus * MODIFIER) + level / 2;
		double specialbonus = getSpecialAccuracy(player);
		int stylebonus = 0;
		int roll = (attack + bonus) + modifier;
		if (player.getEquipment().getAttackStyle() == Equipment.AttackStyles.ACCURATE) {
			stylebonus = 3;
		} else if (player.getEquipment().getAttackStyle() == Equipment.AttackStyles.AGGRESSIVE) {
			stylebonus = 2;
		} else if (player.getEquipment().getAttackStyle() == Equipment.AttackStyles.CONTROLLED) {
			stylebonus = 1;
		}
		roll *= (1 + (stylebonus) / 64);
		if (ItemCheck.wearingFullBarrows(player, "Dharok")) {
			roll *= 1.50;
		}
		return roll * (int) Math.round(specialbonus);
	}

	public static int getDefenceRoll(Player player) {
		int level = player.getSkill().getLevels()[Skills.DEFENCE];
		int defence = getDefencePrayerModifier(player, level);
		int bonus = player.getBonuses()[getHighestDefenceBonus(player)];
		int stylebonus = 0;
		if (player.getEquipment().getAttackStyle() == Equipment.AttackStyles.DEFENSIVE) {
			stylebonus = 3;
		}
		int roll = (defence + bonus);
		roll *= (1 + (stylebonus) / 64);
		if (ItemCheck.wearingFullBarrows(player, "Verac")) {
			roll *= 0.75;
		}
		return roll;
	}

	public static int getAttackPrayerModifier(Player player, int level) {
		if (player.getPrayer().getPrayerBookType() == PrayerBookType.CURSES) {
			if (player.getPrayer().active(CursesPrayerBook.SAP_WARRIOR)) {
				level *= 1.05;
			} else if (player.getPrayer().active(CursesPrayerBook.LEECH_DEFENCE)) {
				level *= 1.06;
			} else if (player.getPrayer().active(CursesPrayerBook.TURMOIL)) {
				level *= 1.20;
			}
		} else if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT) {
			if (player.getPrayer().active(PrayerConstants.BURST_OF_STRENGTH)) {
				level *= 1.05;
			} else if (player.getPrayer().active(PrayerConstants.SUPERHUMAN_STRENGTH)) {
				level *= 1.10;
			} else if (player.getPrayer().active(PrayerConstants.ULTIMATE_STRENGTH)) {
				level *= 1.15;
			} else if (player.getPrayer().active(PrayerConstants.CHIVALRY)) {
				level *= 1.15;
			} else if (player.getPrayer().active(PrayerConstants.PIETY)) {
				level *= 1.20;
			}
		}
		return level;
	}

	public static int getDefencePrayerModifier(Player player, int defence) {
		if (player.getPrayer().getPrayerBookType() == PrayerBookType.CURSES) {
			if (player.getPrayer().active(CursesPrayerBook.SAP_WARRIOR)) {
				defence *= 1.05;
			} else if (player.getPrayer().active(CursesPrayerBook.LEECH_DEFENCE)) {
				defence *= 1.06;
			} else if (player.getPrayer().active(CursesPrayerBook.TURMOIL)) {
				defence *= 1.15;
			}
		} else {
			if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT) {
				if (player.getPrayer().active(PrayerConstants.THICK_SKIN)) {
					defence *= 0.5;
				} else if (player.getPrayer().active(PrayerConstants.ROCK_SKIN)) {
					defence *= 0.10;
				} else if (player.getPrayer().active(PrayerConstants.STEEL_SKIN)) {
					defence *= 1.15;
				} else if (player.getPrayer().active(PrayerConstants.CHIVALRY)) {
					defence *= 1.20;
				} else if (player.getPrayer().active(PrayerConstants.PIETY)) {
					defence *= 1.25;
				}
			}
		}
		return defence;
	}

	public static int getHighestDefenceBonus(Player player) {
		if (player.getBonuses()[5] > player.getBonuses()[6] && player.getBonuses()[5] > player.getBonuses()[7]) {
			return 5;
		}
		if (player.getBonuses()[6] > player.getBonuses()[5] && player.getBonuses()[6] > player.getBonuses()[7]) {
			return 6;
		}
		return player.getBonuses()[7] <= player.getBonuses()[5] || player.getBonuses()[7] <= player.getBonuses()[6] ? 5
				: 7;
	}

	private static int getHighestMeleeAttackBonus(Player player) {
		if (player.getBonuses()[EquipmentConstants.ATTACK_STAB] > player.getBonuses()[EquipmentConstants.ATTACK_SLASH]
				&& player.getBonuses()[EquipmentConstants.ATTACK_STAB] > player
						.getBonuses()[EquipmentConstants.ATTACK_CRUSH]) {
			return EquipmentConstants.ATTACK_STAB;
		}
		if (player.getBonuses()[EquipmentConstants.ATTACK_SLASH] > player.getBonuses()[EquipmentConstants.ATTACK_STAB]
				&& player.getBonuses()[EquipmentConstants.ATTACK_SLASH] > player
						.getBonuses()[EquipmentConstants.ATTACK_CRUSH]) {
			return 1;
		}
		return player.getBonuses()[2] <= player.getBonuses()[EquipmentConstants.ATTACK_SLASH] || player
				.getBonuses()[EquipmentConstants.ATTACK_CRUSH] <= player.getBonuses()[EquipmentConstants.ATTACK_STAB]
						? EquipmentConstants.ATTACK_STAB
						: 2;
	}

	public static double getPrayerStrengthModifier(Player player, int strength) {
		if (player.getPrayer().getPrayerBookType() == PrayerBookType.CURSES) {
			if (player.getPrayer().active(CursesPrayerBook.SAP_WARRIOR)) {
				strength *= 1.05;
			} else if (player.getPrayer().active(CursesPrayerBook.LEECH_STRENGTH)) {
				strength *= 1.08;
			} else if (player.getPrayer().active(CursesPrayerBook.TURMOIL)) {
				strength *= 1.23;
			}
		} else if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT) {
			if (player.getPrayer().active(PrayerConstants.BURST_OF_STRENGTH)) {
				strength *= 1.05;
			} else if (player.getPrayer().active(PrayerConstants.SUPERHUMAN_STRENGTH)) {
				strength *= 1.10;
			} else if (player.getPrayer().active(PrayerConstants.ULTIMATE_STRENGTH)) {
				strength *= 1.15;
			} else if (player.getPrayer().active(PrayerConstants.CHIVALRY)) {
				strength *= 1.18;
			} else if (player.getPrayer().active(PrayerConstants.PIETY)) {
				strength *= 1.23;
			}
		}
		return strength;
	}

	public static double getSpecialStrengthModifier(Player player) {
		Item weapon = player.getEquipment().getItems()[3];
		if (weapon == null || !player.getSpecialAttack().isInitialized()) {
			return 1.0;
		}
		switch (weapon.getId()) {
		case 14484:
			return 1.25;
		case 4153:
			return 1.0;
		case 5698:
		case 5680:
		case 1231:
			return 1.10;
		case 1215:
			return 1.10;
		case 3204:
			return 1.15;
		case 1305:
			return 1.15;
		case 1434:
			return 1.35;
		case 11694:
			return 1.38;
		case 11696:
			return 1.25;
		case 11698:
			return 1.15;
		case 11700:
			return 1.15;
		}
		return 1.0;
	}

	public static double getSpecialAccuracy(Player player) {
		Item weapon = player.getEquipment().getItems()[3];
		if (weapon == null || !player.getSpecialAttack().isInitialized()) {
			return 1.0;
		}
		switch (weapon.getId()) {

		}
		return 1.0;
	}

	public static int getMaxHit(Player player, Entity defending) {
		double strength = getPrayerStrengthModifier(player, player.getSkill().getLevels()[Skills.STRENGTH]);
		double specialBonus = getSpecialStrengthModifier(player);
		double strengthBonus = player.getBonuses()[EquipmentConstants.STRENGTH];
		double base = (13 + strength + (strengthBonus / 8) + ((strength * strengthBonus) / 64)) / 10;
		if (player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT] != null) {
			switch (player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT].getId()) {
			case 4718:
			case 4886:
			case 4887:
			case 4888:
			case 4889:
				if (ItemCheck.wearingFullBarrows(player, "Dharok")) {
					int maximumHitpoints = player.getSkill().getLevelForExperience(Skills.HITPOINTS,
							player.getSkill().getExperience()[Skills.HITPOINTS]);
					int currentHitpoints = player.getSkill().getLevels()[Skills.HITPOINTS];
					double dharokEffect = ((maximumHitpoints - currentHitpoints) * 0.01) + 1;
					base *= dharokEffect;
				}
			}
		}
		Item helm = player.getEquipment().getItems()[EquipmentConstants.HELM_SLOT];
		if (defending != null) {
			if (((helm != null) && (helm.getId() == 8921)) || ((helm != null) && (helm.getId() == 15492))
					|| ((helm != null) && (helm.getId() == 13263) && (defending.isNpc())
							&& (player.getSlayer().hasTask()))) {
				Npc m = org.mystic.game.World.getNpcs()[defending.getIndex()];
				if ((m != null) && player.getSlayer().getTask() == m.getId()) {
					base += 0.125;
				}
			}
			if ((ItemCheck.isUsingBalmung(player)) && (defending.isNpc())) {
				Npc m = org.mystic.game.World.getNpcs()[defending.getIndex()];
				if ((m != null) && (NpcConstants.isDagannothKing(m))) {
					base += 0.25;
				}
			}
		}
		base = (base * specialBonus);
		if (ItemCheck.hasBNeckAndObbyMaulCombo(player) || ItemCheck.wearingFullVoidMelee(player)) {
			base = (base * 1.25);
		}
		return (int) Math.round(base);
	}

}