package org.mystic.game.model.content.combat.formula;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.magic.spells.Charge;
import org.mystic.game.model.content.skill.prayer.PrayerBook.PrayerBookType;
import org.mystic.game.model.content.skill.prayer.PrayerConstants;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;

public class MagicFormulas {

	// was 0.28
	public static double MODIFIER = 0.30;

	public static int getAttack(Player player) {
		int level = player.getSkill().getLevels()[Skills.MAGIC];
		int bonus = player.getBonuses()[EquipmentConstants.ATTACK_MAGIC];
		int modify = (int) Math.round(bonus * MODIFIER);
		int roll = getMagicLevelPrayerModifier(player, level) + bonus + modify;
		if (bonus < 0) {
			return 0;
		}
		return roll;
	}

	public static int getDefence(Player player) {
		int defence = player.getSkill().getLevels()[Skills.DEFENCE] * 30 / 100;
		int magic = player.getSkill().getLevels()[Skills.MAGIC] * 70 / 100;
		int bonus = player.getBonuses()[EquipmentConstants.DEFENCE_MAGIC];
		int roll = getMagicDefencePrayerModifier(player, defence) + magic + bonus;
		return roll;
	}

	public static int getMagicDefencePrayerModifier(Player player, int defence) {
		if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT) {
			if (player.getPrayer().active(PrayerConstants.THICK_SKIN)) {
				defence *= 1.05;
			} else if (player.getPrayer().active(PrayerConstants.ROCK_SKIN)) {
				defence *= 1.10;
			} else if (player.getPrayer().active(PrayerConstants.STEEL_SKIN)) {
				defence *= 1.15;
			} else if (player.getPrayer().active(PrayerConstants.CHIVALRY)) {
				defence *= 1.20;
			} else if (player.getPrayer().active(PrayerConstants.PIETY)) {
				defence *= 1.25;
			}
		}
		return defence;
	}

	public static int getMagicLevelPrayerModifier(Player player, int magic) {
		if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT) {
			if (player.getPrayer().active(PrayerConstants.MYSTIC_WILL)) {
				magic *= 1.05;
			} else if (player.getPrayer().active(PrayerConstants.MYSTIC_LORE)) {
				magic *= 1.10;
			} else if (player.getPrayer().active(PrayerConstants.MYSTIC_MIGHT)) {
				magic *= 1.15;
			}
		}
		return magic;
	}

	public static int getMaxHit(Player player) {
		int spellId = player.getMagic().getSpellCasting().getCurrentSpellId();
		if (spellId == -1) {
			return 0;
		}
		double damage = player.getMagic().getSpellCasting().getDefinition(spellId).getBaseMaxHit();
		double multiplier = 1;
		Item helm = player.getEquipment().getItems()[EquipmentConstants.HELM_SLOT];
		if ((helm != null) && (helm.getId() == 15492) && (player.getCombat().getAttacking().isNpc())
				&& (player.getSlayer().hasTask())) {
			Npc m = org.mystic.game.World.getNpcs()[player.getCombat().getAttacking().getIndex()];
			if ((m != null) && player.getSlayer().getTask() == m.getId()) {
				multiplier += 0.125D;
			}
		}
		if (player.getMagic().isDFireShieldEffect()) {
			return 23;
		}
		if ((spellId >= 1190) && (spellId <= 1192) && (Charge.isChargeActive(player))) {
			multiplier += 0.6D;
		}
		if (player.getSkill().getLevels()[6] > player.getSkill().getLevelForExperience(6,
				player.getSkill().getExperience()[6])
				&& player.getSkill().getLevelForExperience(6, player.getSkill().getExperience()[6]) >= 95) {
			multiplier += .03 * (player.getSkill().getLevels()[6] - 99);
		}
		if (player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT] != null) {
			switch (player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT].getId()) {
			// chaotic staff and staff of light 15% inc
			case 18355:
			case 15486:
				multiplier += 0.15;
				break;
			// ahrim staff 5% increase
			case 4710:
				multiplier += 0.05;
				break;
			}
		}
		if (player.getEquipment().getItems()[EquipmentConstants.NECKLACE_SLOT] != null) {
			switch (player.getEquipment().getItems()[EquipmentConstants.NECKLACE_SLOT].getId()) {
			// arcane stream necklace 10% increase
			case 18335:
				multiplier += 0.10;
				break;
			}
		}
		if (spellId > 0) {
			switch (spellId) {
			case 12037:
				damage += player.getSkill().getLevels()[6] / 10;
				break;
			}
		}
		damage *= multiplier;
		return (int) damage;
	}

}