package org.mystic.game.model.entity.npc.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.prayer.PrayerBook.PrayerBookType;
import org.mystic.game.model.content.skill.prayer.PrayerConstants;
import org.mystic.game.model.content.skill.prayer.impl.CursesPrayerBook;
import org.mystic.game.model.definition.NpcCombatDefinition.Magic;
import org.mystic.game.model.definition.NpcCombatDefinition.Melee;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class SeaTrollQueen extends Npc {

	private static Projectile getProjectile() {
		return new Projectile(109, 1, 40, 70, 43, 31, 16);
	}

	public SeaTrollQueen() {
		super(3847, false, new Location(2342, 3702));
	}

	@Override
	public int getRespawnTime() {
		return 60;
	}

	@Override
	public void updateCombatType() {
		CombatTypes type = CombatTypes.MELEE;
		if (getCombat().getAttacking() != null) {
			if (!getCombat().getAttacking().isNpc()) {
				Player player = (Player) getCombat().getAttacking();
				if (!getCombat().withinDistanceForAttack(CombatTypes.MELEE, true)) {
					if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT
							? player.getPrayer().active(PrayerConstants.PROTECT_FROM_MAGIC)
							: player.getPrayer().active(CursesPrayerBook.DEFLECT_MAGIC)) {
						if (player.getSkill().getLevels()[Skills.PRAYER] > 0) {
							World.sendProjectile(getProjectile(), player, this);
							int modifier = player.getSkill().getLevels()[Skills.PRAYER] - 20 > 0 ? 20
									: player.getSkill().getLevels()[Skills.PRAYER];
							player.getPrayer().drain(modifier);
							type = CombatTypes.RANGED;
							getCombat().setAttackTimer(6);
						} else {
							type = CombatTypes.MAGIC;
						}
					} else {
						type = CombatTypes.MAGIC;
					}
				} else {
					if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT
							? player.getPrayer().active(PrayerConstants.PROTECT_FROM_MAGIC)
							: player.getPrayer().active(CursesPrayerBook.DEFLECT_MAGIC)) {
						type = CombatTypes.MELEE;
					} else if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT
							? player.getPrayer().active(PrayerConstants.PROTECT_FROM_MELEE)
							: player.getPrayer().active(CursesPrayerBook.DEFLECT_MELEE)) {
						type = CombatTypes.MAGIC;
					} else {
						type = Misc.randomNumber(10) < 5 ? CombatTypes.MELEE : CombatTypes.MAGIC;
					}
				}
			}
			getCombat().setCombatType(type);
			getCombat().setBlockAnimation(getCombatDefinition().getBlock());
			switch (getCombat().getCombatType()) {
			case MAGIC:
				byte combatIndex = (byte) Misc.randomNumber(getCombatDefinition().getMagic().length);
				Magic magic = getCombatDefinition().getMagic()[combatIndex];
				getCombat().getMagic().setAttack(magic.getAttack(), magic.getAnimation(), magic.getStart(),
						magic.getEnd(), magic.getProjectile());
				break;
			case MELEE:
				combatIndex = (byte) Misc.randomNumber(getCombatDefinition().getMelee().length);
				Melee melee = getCombatDefinition().getMelee()[combatIndex];
				getCombat().getMelee().setAttack(melee.getAttack(), melee.getAnimation());
				break;
			default:
				break;

			}
		}
	}

}
