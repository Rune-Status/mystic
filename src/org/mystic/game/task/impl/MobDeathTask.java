package org.mystic.game.task.impl;

import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.Walking;
import org.mystic.game.model.entity.npc.drops.NPCDrops;
import org.mystic.game.model.entity.npc.impl.KalphiteQueen;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;

public class MobDeathTask extends Task {

	public static final int DEATH_DELAY = 2;

	public static final int DEFAULT_DISSAPEAR_DELAY = 5;

	private final Npc mob;

	public MobDeathTask(final Npc mob) {
		super(mob, mob.getRespawnTime() + 6, false, Task.StackType.STACK, Task.BreakType.NEVER,
				TaskIdentifier.CURRENT_ACTION);
		this.mob = mob;
		mob.setDead(true);
		mob.getUpdateFlags().faceEntity(65535);
		mob.getCombat().reset();
		Task death = new Task(mob, 2, false, Task.StackType.STACK, Task.BreakType.NEVER,
				TaskIdentifier.CURRENT_ACTION) {
			@Override
			public void execute() {
				mob.getUpdateFlags().sendAnimation(mob.getDeathAnimation());
				stop();
			}

			@Override
			public void onStop() {
			}
		};
		Task dissapear = new Task(mob, 5, false, Task.StackType.STACK, Task.BreakType.NEVER,
				TaskIdentifier.CURRENT_ACTION) {
			@Override
			public void execute() {
				final Entity killer = mob.getCombat().getDamageTracker().getKiller();
				if (killer != null && !killer.isNpc()) {
					NPCDrops.dropItems((Player) killer, mob);
					NPCDrops.checkMob((Player) killer, mob);
				}
				mob.onDeath();
				mob.setVisible(false);
				Walking.setNpcOnTile(mob, false);
				mob.getUpdateFlags().setUpdateRequired(true);
				mob.curePoison(0);
				mob.unTransform();
				stop();
			}

			@Override
			public void onStop() {
			}
		};
		TaskQueue.queue(death);
		TaskQueue.queue(dissapear);
	}

	@Override
	public void execute() {
		if (mob.shouldRespawn()) {
			mob.setVisible(true);
			mob.getLocation().setAs(mob.getNextSpawnLocation());
			mob.getUpdateFlags().setUpdateRequired(true);
			mob.getCombat().forRespawn();
			Walking.setNpcOnTile(mob, true);
			mob.resetLevels();
			mob.unfreeze();
			mob.getCombat().getDamageTracker().clear();
			if ((mob instanceof KalphiteQueen)) {
				((KalphiteQueen) mob).transform();
			}
		} else {
			mob.remove();
		}
		stop();
	}

	@Override
	public void onStop() {

	}
}