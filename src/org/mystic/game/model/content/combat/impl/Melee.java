package org.mystic.game.model.content.combat.impl;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.formula.FormulaData;
import org.mystic.game.model.content.combat.formula.MeleeFormulas;
import org.mystic.game.model.content.sound.CombatSounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.HitTask;
import org.mystic.utility.Misc;

public class Melee {

	private final Entity entity;

	private Attack attack = new Attack(1, 5);

	private Animation animation = new Animation(422, 0);

	private int nextDamage = -1;

	private double accuracy;
	private double defence;
	private double chance;
	private boolean accurate;

	public Melee(Entity entity) {
		this.entity = entity;
	}

	public void execute(Entity attacking) {
		if (attack == null) {
			return;
		}
		if (entity.getCombat().getAttacking().isNpc() || entity.isNpc()) {
			accurate = entity.getAccuracy(attacking, CombatTypes.MELEE) >= Misc.random(100);
		} else {
			accuracy = MeleeFormulas.getAttackRoll(entity.getPlayer());
			defence = MeleeFormulas.getDefenceRoll(entity.getPlayer().getCombat().getAttacking().getPlayer());
			chance = FormulaData.getChance(accuracy, defence);
			accurate = FormulaData.isAccurateHit(chance);
		}
		int damage = (entity.getCorrectedDamage(Misc.random(entity.getMaxHit(CombatTypes.MELEE))));
		if (nextDamage != -1) {
			damage = nextDamage;
		}
		final Hit hit = new Hit(entity, (accurate) || (entity.isIgnoreHitSuccess()) ? damage : 0, Hit.HitTypes.MELEE);
		entity.setLastDamageDealt(!accurate ? 0 : hit.getDamage());
		entity.setLastHitSuccess((accurate) || (entity.isIgnoreHitSuccess()));
		entity.onAttack(attacking, hit.getDamage(), CombatTypes.MELEE, accurate);
		entity.getCombat().updateTimers(attack.getAttackDelay());
		if (animation != null) {
			entity.getUpdateFlags().sendAnimation(animation);
			if (!entity.isNpc()) {
				if (!entity.getPlayer().getSpecialAttack().isInitialized()) {
					CombatSounds.executeAttack(entity.getPlayer(),
							entity.getPlayer().getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT]);
				}
			}
		}
		finish(attacking, hit);
	}

	public void finish(Entity attacking, Hit hit) {
		attacking.getCombat().setInCombat(entity);
		TaskQueue.queue(new HitTask(attack.getHitDelay(), false, hit, attacking));
	}

	public Animation getAnimation() {
		return animation;
	}

	public Attack getAttack() {
		return attack;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void setAttack(Attack attack, Animation animation) {
		this.attack = attack;
		this.animation = animation;
	}

	public void setNextDamage(int nextDamage) {
		this.nextDamage = nextDamage;
	}

}