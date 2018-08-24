package org.mystic.game.model.content.combat;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.Hit.HitTypes;
import org.mystic.game.model.content.combat.formula.MagicFormulas;
import org.mystic.game.model.content.combat.formula.MeleeFormulas;
import org.mystic.game.model.content.combat.formula.RangeFormulas;
import org.mystic.game.model.content.combat.impl.PoisonWeapons;
import org.mystic.game.model.content.combat.impl.RingOfRecoil;
import org.mystic.game.model.content.combat.special.SpecialAttackHandler;
import org.mystic.game.model.content.minigames.pestcontrol.PestControlGame;
import org.mystic.game.model.content.skill.magic.MagicEffects;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.content.skill.magic.spells.Vengeance;
import org.mystic.game.model.content.skill.melee.BarrowsSpecials;
import org.mystic.game.model.content.skill.ranged.BoltSpecials;
import org.mystic.game.model.content.skill.slayer.Slayer;
import org.mystic.game.model.content.skill.summoning.FamiliarMob;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.NpcCombatFormulas;
import org.mystic.game.model.entity.npc.NpcConstants;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.ItemCheck;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.PlayerDeathTask;

public class PlayerCombatInterface implements CombatInterface {

	private final Player player;

	public PlayerCombatInterface(Player player) {
		this.player = player;
	}

	@Override
	public void afterCombatProcess(Entity entity) {
		if (player.getSpecialAttack().isInitialized() && player.getCombat().getCombatType() != CombatTypes.MAGIC) {
			SpecialAttackHandler.executeSpecialEffect(player, entity);
			player.getSpecialAttack().afterSpecial();
		}
		if (!player.getMagic().isDFireShieldEffect()) {
			player.getMagic().getSpellCasting().resetOnAttack();
		}
		player.getMelee().afterCombat();
		player.updateCombatType();
	}

	@Override
	public boolean canAttack() {
		if (!player.getController().canUseCombatType(player, player.getCombat().getCombatType())) {
			return false;
		}
		final Entity attacking = player.getCombat().getAttacking();
		final CombatTypes type = player.getCombat().getCombatType();
		if (attacking.getIndex() <= -1) {
			return false;
		}
		if (player.getSpecialAttack().isInitialized() && player.getCombat().getCombatType() != CombatTypes.MAGIC
				&& (!player.getController().canUseSpecialAttack(player)
						|| !SpecialAttackHandler.hasSpecialAmount(player))) {
			player.getSpecialAttack().toggleSpecial();
			player.getCombat().setAttackTimer(2);
			return false;
		}
		if (type == CombatTypes.MAGIC && !player.getMagic().getSpellCasting().canCast()) {
			return false;
		}
		if (type == CombatTypes.RANGED && !player.getRanged().canUseRanged()) {
			return false;
		}
		if (type != CombatTypes.MAGIC && attacking.isNpc()) {
			final Npc mob = World.getNpcs()[attacking.getIndex()];
			if (mob.getId() == 912 || mob.getId() == 913 || mob.getId() == 914) {
				if (player.getCombat().getCombatType() != CombatTypes.MAGIC) {
					player.getPlayer().send(new SendMessage("You may only use magic on this NPC."));
					return false;
				}
			}
		}
		if (type == CombatTypes.MELEE && attacking.isNpc()) {
			final Npc mob = World.getNpcs()[attacking.getIndex()];
			if (mob != null) {
				for (int i : NpcConstants.FLYING_MOBS) {
					if (mob.getId() == i) {
						player.getClient().queueOutgoingPacket(new SendMessage("You can't reach that!"));
						return false;
					}
				}
			}
		}
		if (!player.inMultiArea() || !attacking.inMultiArea()) {
			if (player.getCombat().inCombat()
					&& player.getCombat().getLastAttackedBy() != player.getCombat().getAttacking()) {
				player.getClient().queueOutgoingPacket(new SendMessage("You are already under attack."));
				return false;
			}
			if (attacking.getCombat().inCombat() && attacking.getCombat().getLastAttackedBy() != player
					&& !player.getSummoning().isFamiliar(attacking.getCombat().getLastAttackedBy())) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("This " + (player.getCombat().getAttacking().isNpc() ? "monster" : "player")
								+ " is already under attack."));
				return false;
			}
		}
		if (!attacking.isNpc()) {
			final Player other = World.getPlayers()[attacking.getIndex()];
			if (other != null && !player.getController().canAttackPlayer(player, other)) {
				return false;
			}
		} else {
			final Npc mob = World.getNpcs()[attacking.getIndex()];
			if (mob != null) {
				if (mob instanceof FamiliarMob) {
					if (!mob.inWilderness()) {
						return false;
					}
					if (mob.getOwner().equals(player)) {
						player.getClient().queueOutgoingPacket(new SendMessage("You can't attack your own familiar!"));
						return false;
					}
				}
				if (!player.getController().canAttackNPC()) {
					player.getClient().queueOutgoingPacket(new SendMessage("You can't attack NPCs here."));
					return false;
				}
				if (!Slayer.canAttackMob(player, mob)) {
					return false;
				}
				boolean familiar = mob instanceof FamiliarMob;
				if (!familiar) {
					if (!mob.getDefinition().isAttackable()
							|| (mob.getOwner() != null && !mob.getOwner().equals(player))) {
						player.getClient().queueOutgoingPacket(new SendMessage("You can't attack this NPC."));
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void checkForDeath() {
		if (player.getLevels()[3] <= 0 && !player.isDead()) {
			TaskQueue.queue(new PlayerDeathTask(player));
			player.getAchievements().incr(player, "Die 10 times");
			player.getAchievements().incr(player, "Die 50 times");
		}
	}

	@Override
	public int getAccuracy(Entity paramEntity, CombatTypes paramCombatTypes) {
		return NpcCombatFormulas.getAccuracy(player, paramEntity, paramCombatTypes);
	}

	@Override
	public int getCorrectedDamage(int damage) {
		final Item weapon = player.getEquipment().getItems()[3];
		final Item ammo = player.getEquipment().getItems()[13];
		if (player.getCombat().getCombatType() == CombatTypes.RANGED) {
			if (player.getSpecialAttack().isInitialized()) {
				if ((weapon != null) && (weapon.getId() == 11235)) {
					if (ammo != null) {
						if (ammo.getId() == 11212 || ammo.getId() == 11227 || ammo.getId() == 0
								|| ammo.getId() == 11228) {
							if (damage < 8) {
								return 8;
							}
						} else if (damage < 5) {
							return 5;
						}
					}
				}
			}
		}
		if (player.getCombat().getCombatType() == CombatTypes.MAGIC && ItemCheck.hasDFireShield(player)
				&& player.getMagic().isDFireShieldEffect() && damage < 15) {
			return 15;
		}
		return damage;
	}

	@Override
	public int getMaxHit(CombatTypes type) {
		switch (type) {
		case MAGIC:
			return MagicFormulas.getMaxHit(player);
		case MELEE:
			return MeleeFormulas.getMaxHit(player, player.getCombat().getAttacking());
		case RANGED:
			return RangeFormulas.getMaxHit(player);
		}
		return MeleeFormulas.getMaxHit(player, player.getCombat().getAttacking());
	}

	@Override
	public void hit(Hit hit) {
		if (!player.canTakeDamage() || player.isImmuneToHit()
				|| player.getMagic().isTeleporting() && !player.getController().isSafe(player)) {
			return;
		}
		if (player.isDead()) {
			return;
		}
		if (hit.getAttacker() != null) {
			if (hit.getAttacker().isNpc()) {
				Npc mob = World.getNpcs()[hit.getAttacker().getIndex()];
				if (mob != null && NpcConstants.isDragon(mob)) {
					if (ItemCheck.isWearingAntiDFireShield(player) && (hit.getType() == Hit.HitTypes.MAGIC)) {
						if (ItemCheck.hasDFireShield(player)) {
							player.getMagic().incrDragonFireShieldCharges();
						}
						if (player.hasFireImmunity()) {
							player.getClient()
									.queueOutgoingPacket(new SendMessage("You resist all of the dragonfire."));
							hit.setDamage(0);
						} else {
							player.getClient().queueOutgoingPacket(
									new SendMessage("You manage to resist some of the dragonfire."));
							hit.setDamage((int) (hit.getDamage() * 0.2));
						}
					} else if (hit.getType() == Hit.HitTypes.MAGIC && player.hasSuperFireImmunity()) {
						player.getClient().queueOutgoingPacket(new SendMessage("You resist all of the dragonfire."));
						hit.setDamage(0);
					} else if (hit.getType() == Hit.HitTypes.MAGIC && player.hasFireImmunity()) {
						player.getClient()
								.queueOutgoingPacket(new SendMessage("You manage to resist some of the dragonfire."));
						hit.setDamage((int) (hit.getDamage() * 0.2));
					} else if ((hit.getType() == Hit.HitTypes.MAGIC)) {
						if (hit.getDamage() > 0) {
							player.getClient()
									.queueOutgoingPacket(new SendMessage("You are horribly burnt by the dragonfire."));
						}
					}
				}
			} else {
				Player p = World.getPlayers()[hit.getAttacker().getIndex()];
				if (p != null && !player.getController().canAttackPlayer(p, player)
						|| !player.getController().canAttackPlayer(player, p)) {
					return;
				}
			}
		}
		hit.setDamage(player.getPrayer().getDamage(hit));
		hit.setDamage(player.getEquipment().getEffectedDamage(hit.getDamage()));
		if (hit.getType() != Hit.HitTypes.POISON && hit.getType() != Hit.HitTypes.NONE) {
			player.getDegrading().degradeEquipment(player);
		}
		if (hit.getDamage() > player.getLevels()[3]) {
			player.getLevels()[3] = 0;
		} else {
			player.getLevels()[3] = (short) (player.getLevels()[3] - hit.getDamage());
		}
		if (!player.getUpdateFlags().isHitUpdate()) {
			player.getUpdateFlags().sendHit(hit.getDamage(), hit.getHitType(), hit.getCombatHitType());
		} else {
			player.getUpdateFlags().sendHit2(hit.getDamage(), hit.getHitType(), hit.getCombatHitType());
		}
		if (player.getTrade().trading()) {
			player.getTrade().end(false);
		} else if (player.getDueling().isScreen()) {
			player.getDueling().decline();
		}
		checkForDeath();
		if (player.getLevels()[3] > 0 && !player.isDead()) {
			if (player.getEquipment().isWearingItem(11090)) {
				if (player.getSkill().getLevels()[3] > 0
						&& player.getSkill().getLevels()[3] <= player.getMaxLevels()[3] * 0.20) {
					player.send(new SendMessage("Your phoenix necklace heals you, but is destoryed in the process."));
					player.setAppearanceUpdateRequired(true);
					player.getEquipment().getItems()[EquipmentConstants.NECKLACE_SLOT] = null;
					player.getEquipment().update(EquipmentConstants.NECKLACE_SLOT);
					player.getLevels()[3] += (player.getMaxLevels()[3] * 0.30);
				}
			}
			if (player.getEquipment().isWearingItem(2570) && !player.inDuelArena()) {
				if (player.getSkill().getLevels()[3] > 0
						&& player.getSkill().getLevels()[3] <= player.getMaxLevels()[3] * 0.10) {
					player.setAppearanceUpdateRequired(true);
					player.getEquipment().getItems()[EquipmentConstants.RING_SLOT] = null;
					player.getEquipment().update(EquipmentConstants.RING_SLOT);
					player.getMagic().teleport(3085, 3491, 0, TeleportTypes.SPELL_BOOK);
					player.send(new SendMessage("Your ring of life saves you and is destroyed in the process."));
					hit.getAttacker().getCombat().reset();
				}
			}
		}
		if (hit.getAttacker() != null) {
			RingOfRecoil.doRecoil(player, hit.getAttacker(), hit.getDamage());
			if (player.isRetaliate() && player.getCombat().getAttacking() == null
					&& !player.getMovementHandler().moving()) {
				player.getCombat().setAttack(hit.getAttacker());
			}
			player.getCombat().getDamageTracker().addDamage(hit.getAttacker(), hit.getDamage());
			hit.getAttacker().onHit(player, hit);
			if (hit.getType() != HitTypes.NONE && hit.getType() != HitTypes.POISON) {
				if (hit.getDamage() >= 4 && player.getMagic().isVengeanceActive() && !player.isDead()) {
					Vengeance.recoil(player, hit);
				}
			}
		}
		player.getSkill().update(3);
		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
	}

	@Override
	public boolean isIgnoreHitSuccess() {
		if (player.getCombat().getCombatType() == CombatTypes.RANGED && player.getSpecialAttack().isInitialized()) {
			Item weapon = player.getEquipment().getItems()[3];
			if (weapon != null && weapon.getId() == 11235) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onAttack(Entity attack, int hit, CombatTypes type, boolean success) {
		if (success || type == CombatTypes.MAGIC) {
			player.getSkill().addCombatExperience(type, hit);
		}
		if (!attack.isNpc()) {
			Player p = World.getPlayers()[attack.getIndex()];
			if (p != null) {
				player.getSkulling().checkForSkulling(player, p);
			}
		}
		switch (type) {
		case MAGIC:
			if (success) {
				MagicEffects.doMagicEffects(player, attack, player.getMagic().getSpellCasting().getCurrentSpellId());
			}
			player.getMagic().getSpellCasting().removeRunesForAttack();
			break;
		case MELEE:
			break;
		case RANGED:
			player.getRanged().removeArrowsOnAttack();
			if (player.getRanged().isOnyxEffectActive()) {
				player.getRanged().doOnyxEffect(hit);
			}
			break;
		}
	}

	@Override
	public void onCombatProcess(Entity entity) {
		if (player.getSpecialAttack().isInitialized() && player.getCombat().getCombatType() != CombatTypes.MAGIC) {
			SpecialAttackHandler.handleSpecialAttack(player);
			if (player.getCombat().getCombatType() == CombatTypes.RANGED) {
				player.getRanged().doActionsForDarkBow(entity);
			}
		} else if (player.getCombat().getCombatType() == CombatTypes.RANGED) {
			BoltSpecials.checkForBoltSpecial(player, entity);
			BarrowsSpecials.checkForBarrowsSpecial(player);
			player.getRanged().doActionsForDarkBow(entity);
		} else {
			if (player.getMagic().isDFireShieldEffect()) {
				player.getMagic().decrDragonFireShieldCharges();
			}
			BarrowsSpecials.checkForBarrowsSpecial(player);
		}
		if (player.getCombat().getCombatType() == CombatTypes.MAGIC) {
			player.getMagic().getSpellCasting().appendMultiSpell(player);
			player.getAchievements().incr(player, "Cast 1,000 spells");
			player.getAchievements().incr(player, "Cast 10,000 spells");
		}
		PoisonWeapons.checkForPoison(player, entity);
		player.getDegrading().degradeWeapon(player);
	}

	@Override
	public void onHit(Entity entity, Hit hit) {
		if (player.getAttributes().get(PestControlGame.PEST_GAME_KEY) != null) {
			player.getAttributes().set(PestControlGame.PEST_DAMAGE_KEY,
					player.getAttributes().get(PestControlGame.PEST_DAMAGE_KEY) != null
							? player.getAttributes().getInt(PestControlGame.PEST_DAMAGE_KEY) + hit.getDamage()
							: hit.getDamage());
		}
		if (player.getCombat().getCombatType() == CombatTypes.RANGED) {
			player.getRanged().dropArrowAfterHit();
			player.getRanged().doActionsForChinchompa(entity);
		}
		if (hit.getType() != Hit.HitTypes.POISON && hit.getType() != Hit.HitTypes.NONE) {
			player.getPrayer().doEffectOnHit(entity, hit);
		}
		if (player.getMelee().isGuthanEffectActive())
			BarrowsSpecials.doGuthanEffect(player, entity, hit);
		else if (player.getMelee().isToragEffectActive())
			BarrowsSpecials.doToragEffect(player, entity);
		else if (player.getRanged().isKarilEffectActive())
			BarrowsSpecials.doKarilEffect(player, entity);
		else if (player.getMagic().isAhrimEffectActive()) {
			BarrowsSpecials.doAhrimEffect(player, entity, hit.getDamage());
		}
		if (entity.isNpc() && entity.isDead()) {
			player.getCombat().setAttackTimer(0);
			player.getCombat().resetCombatTimer();
		}
		if (player.getMagic().isDFireShieldEffect()) {
			player.getMagic().reset();
			player.getMagic().getSpellCasting().updateMagicAttack();
			player.updateCombatType();
		}
	}

	@Override
	public void updateCombatType() {
		CombatTypes type;
		if (player.getMagic().getSpellCasting().isCastingSpell()) {
			type = CombatTypes.MAGIC;
		} else {
			type = EquipmentConstants.getCombatTypeForWeapon(player);
		}
		player.getCombat().setCombatType(type);
		switch (type) {
		case MELEE:
			player.getEquipment().updateMeleeDataForCombat();
			break;
		case RANGED:
			player.getEquipment().updateRangedDataForCombat();
			break;
		default:
			break;
		}
	}

}