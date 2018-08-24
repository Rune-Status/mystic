package org.mystic.game.model.content.combat.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.CombatConstants;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.formula.MagicFormulas;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.RunOnceTask;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.GraphicTask;
import org.mystic.game.task.impl.HitTask;
import org.mystic.utility.Misc;

public class Magic {

	private final Entity entity;

	private Attack attack = new Attack(4, 5);

	private Animation animation = null;

	private Graphic start = null;

	private Graphic end = null;

	private Projectile projectile = null;

	private byte pDelay = 0;

	private boolean multi = false;

	public Magic(Entity entity) {
		this.entity = entity;
	}

	private double accuracy;

	private double defence;

	private boolean accurate;

	public void execute(Entity attacking) {
		if (attack == null) {
			return;
		}
		entity.getCombat().updateTimers(attack.getAttackDelay());
		if (animation != null) {
			entity.getUpdateFlags().sendAnimation(animation);
		}
		if (start != null) {
			entity.getUpdateFlags().sendGraphic(start);
		}
		if (projectile != null) {
			final int lockon = attacking.isNpc() ? attacking.getIndex() + 1 : -attacking.getIndex() - 1;
			final int offsetX = ((entity.getLocation().getY() - attacking.getLocation().getY()) * -1) - 2;
			final int offsetY = ((entity.getLocation().getX() - attacking.getLocation().getX()) * -1) - 3;
			if (pDelay > 0) {
				TaskQueue.queue(new RunOnceTask(entity, pDelay) {
					@Override
					public void onStop() {
						World.sendProjectile(projectile, CombatConstants.getOffsetProjectileLocation(entity), lockon,
								(byte) offsetX, (byte) offsetY);
					}
				});
			} else {
				World.sendProjectile(projectile, CombatConstants.getOffsetProjectileLocation(entity), lockon,
						(byte) offsetX, (byte) offsetY);
			}
		}
		finish(attacking);
	}

	public void finish(Entity attacking) {
		if (entity.getCombat().getAttacking().isNpc() || entity.isNpc()) {
			accurate = Misc.random(100) <= entity.getAccuracy(attacking, CombatTypes.MAGIC);
		} else {
			accuracy = MagicFormulas.getAttack(entity.getPlayer());
			defence = MagicFormulas.getDefence(entity.getPlayer().getCombat().getAttacking().getPlayer());
			if (accuracy == 0) {
				accurate = false;
			} else {
				if (Misc.randomNumber((int) accuracy) > Misc.randomNumber((int) defence)) {
					accurate = true;
				} else {
					accurate = false;
				}
			}
		}
		final int damage = entity.getCorrectedDamage(Misc.random(entity.getMaxHit(CombatTypes.MAGIC)));
		Graphic end = null;
		if (!entity.isNpc()) {
			Player player = (Player) entity;
			if (accurate && this.end != null && entity.getCombat().getAttacking().isFrozen()
					&& player.getMagic().getSpellCasting().getCurrentSpellId() == 12891) {
				end = new Graphic(1677, 0, 50);
			} else if (!accurate) {
				end = new Graphic(85, 0, true);
			} else if (this.end != null) {
				end = this.end;
			}
		} else if ((accurate || entity.isNpc()) && (this.end != null)) {
			end = this.end;
		} else if (!accurate && !entity.isNpc()) {
			end = new Graphic(85, 0, true);
		}
		final Hit hit = new Hit(entity, (accurate || entity.isNpc()) || (entity.isIgnoreHitSuccess()) ? damage : -1,
				Hit.HitTypes.MAGIC);
		entity.onAttack(attacking, hit.getDamage(), CombatTypes.MAGIC, accurate || entity.isNpc());
		entity.setLastDamageDealt(hit.getDamage());
		entity.setLastHitSuccess((accurate || entity.isNpc()) || (entity.isIgnoreHitSuccess()));
		if (hit.getDamage() > -1) {
			TaskQueue.queue(new HitTask(attack.getHitDelay(), false, hit, attacking));
		}
		if (!accurate) {
			attacking.retaliate(entity);
		}
		if (entity.isNpc() && end != null) {
			if (end.getId() == 1) {
				TaskQueue.queue(new GraphicTask(attack.getHitDelay(), true, end, entity));
			} else {
				TaskQueue.queue(new GraphicTask(attack.getHitDelay(), false, end, attacking));
			}
		} else if (end != null) {
			TaskQueue.queue(new GraphicTask(attack.getHitDelay(), false, end, attacking));
		}
		attacking.getCombat().setInCombat(entity);
	}

	public Attack getAttack() {
		return attack;
	}

	public byte getpDelay() {
		return pDelay;
	}

	public boolean isMulti() {
		return multi;
	}

	public void setAttack(Attack attack, Animation animation, Graphic start, Graphic end, Projectile projectile) {
		this.attack = attack;
		this.animation = animation;
		this.start = start;
		this.end = end;
		this.projectile = projectile;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public void setpDelay(byte pDelay) {
		this.pDelay = pDelay;
	}
}
