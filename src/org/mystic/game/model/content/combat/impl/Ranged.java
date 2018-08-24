package org.mystic.game.model.content.combat.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.formula.RangeFormulas;
import org.mystic.game.model.content.sound.CombatSounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.GraphicTask;
import org.mystic.game.task.impl.HitTask;
import org.mystic.utility.Misc;

public class Ranged {

	private final Entity entity;

	private Attack attack = null;

	private Animation animation = null;

	private Graphic start = null;

	private Graphic end = null;

	private Projectile projectile = null;

	private int pOffset = 0;
	private byte gOffset = 0;

	public Ranged(Entity entity) {
		this.entity = entity;
	}

	public void execute(Entity attacking) {
		if ((attack == null) || (attacking == null) || (attacking.isDead())) {
			return;
		}
		boolean success = false;
		if (!entity.getCombat().getAttacking().isNpc() && !entity.isNpc()) {
			if (Misc.randomNumber(
					RangeFormulas.calculateRangeDefence((Player) entity.getCombat().getAttacking())) < Misc
							.randomNumber(RangeFormulas.calculateRangeAttack((Player) entity))) {
				success = true;
			}
		} else {
			success = entity.getAccuracy(attacking, CombatTypes.RANGED) >= Misc.random(100);
		}
		int damage = entity.getCorrectedDamage(Misc.random(entity.getMaxHit(CombatTypes.RANGED)));
		final Hit hit = new Hit(entity, (success) || (entity.isIgnoreHitSuccess()) ? damage : 0, Hit.HitTypes.RANGED);
		entity.setLastDamageDealt(hit.getDamage());
		entity.setLastHitSuccess((success) || (entity.isIgnoreHitSuccess()));
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
		if (start != null) {
			executeStartGraphic();
		}
		if (projectile != null) {
			executeProjectile(attacking);
		}
		TaskQueue.queue(new HitTask(attack.getHitDelay(), false, hit, attacking));
		if (end != null) {
			TaskQueue.queue(new GraphicTask(attack.getHitDelay(), false, end, attacking));
		}
		attacking.getCombat().setInCombat(entity);
		entity.onAttack(attacking, hit.getDamage(), CombatTypes.RANGED, success);
	}

	public void executeProjectile(Entity target) {
		final int lockon = target.isNpc() ? target.getIndex() + 1 : -target.getIndex() - 1;
		final byte offsetX = (byte) ((entity.getLocation().getY() - target.getLocation().getY()) * -1);
		final byte offsetY = (byte) ((entity.getLocation().getX() - target.getLocation().getX()) * -1);
		if (pOffset > 0) {
			final Projectile p = new Projectile(projectile);
			TaskQueue.queue(new Task(pOffset) {
				@Override
				public void execute() {
					World.sendProjectile(p, entity.getLocation(), lockon, offsetX, offsetY);
					stop();
				}

				@Override
				public void onStop() {
				}
			});
		} else {
			World.sendProjectile(projectile, entity.getLocation(), lockon, offsetX, offsetY);
		}
	}

	public void executeStartGraphic() {
		if (gOffset > 0) {
			final Graphic g = new Graphic(start);
			TaskQueue.queue(new Task(gOffset) {
				@Override
				public void execute() {
					entity.getUpdateFlags().sendGraphic(g);
					stop();
				}

				@Override
				public void onStop() {
				}
			});
		} else {
			entity.getUpdateFlags().sendGraphic(start);
		}
	}

	public Animation getAnimation() {
		return animation;
	}

	public Attack getAttack() {
		return attack;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public int getProjectileOffset() {
		return pOffset;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void setAttack(Attack attack) {
		this.attack = attack;
	}

	public void setAttack(Attack attack, Animation animation, Graphic start, Graphic end, Projectile projectile) {
		this.attack = attack;
		this.animation = animation;
		this.start = start;
		this.end = end;
		this.projectile = projectile;
	}

	public void setEnd(Graphic end) {
		this.end = end;
	}

	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	public void setProjectileOffset(int pOffset) {
		this.pOffset = pOffset;
	}

	public void setStart(Graphic start) {
		this.start = start;
	}

	public void setStartGfxOffset(byte gOffset) {
		this.gOffset = gOffset;
	}

}