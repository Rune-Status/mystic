package org.mystic.game.task.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.skill.SkillManager;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;

public class RegenerateSkillTask extends Task {

	public static final String EXRTA_HP_REGEN_TASK = "extrahpregentask";

	private final Entity entity;

	private SkillManager skill = null;

	public RegenerateSkillTask(Entity entity, int delay) {
		super(entity, delay, false, StackType.NEVER_STACK, BreakType.NEVER, TaskIdentifier.SKILL_RESTORE);
		if (entity == null) {
			stop();
		}
		if (!entity.isNpc()) {
			Player p = World.getPlayers()[entity.getIndex()];
			if (p != null) {
				skill = p.getSkill();
			}
		}
		this.entity = entity;
	}

	@Override
	public void execute() {
		if (entity == null) {
			stop();
			return;
		}
		if (!entity.isNpc()) {
			final Player p = World.getPlayers()[entity.getIndex()];
			if (p != null) {
				if (p.getAttributes().get(EXRTA_HP_REGEN_TASK) == null) {
					Item gl = p.getEquipment().getItems()[EquipmentConstants.GLOVES_SLOT];
					if (gl != null && gl.getId() == 11133) {
						p.getAttributes().set(EXRTA_HP_REGEN_TASK, (byte) 0);
						Task t = new Task(p, 25) {

							@Override
							public void execute() {
								Item gl = p.getEquipment().getItems()[EquipmentConstants.GLOVES_SLOT];
								if (gl == null || gl != null && gl.getId() != 11133) {
									p.getAttributes().remove(EXRTA_HP_REGEN_TASK);
									stop();
									return;
								}
								if (p.getLevels()[3] < p.getMaxLevels()[3]) {
									p.getLevels()[3] += 1;
									p.getSkill().update(3);
								}
							}

							@Override
							public void onStop() {
							}

						};
						TaskQueue.queue(t);
					}
				}
			}
		}
		for (int i = 0; i < (!entity.isNpc() ? Skills.SKILL_COUNT : 7); i++) {
			if (i > 7 && entity.isNpc()) {
				break;
			}
			if (i == Skills.PRAYER || i == Skills.HITPOINTS
					&& entity.getLevels()[Skills.HITPOINTS] > entity.getMaxLevels()[Skills.HITPOINTS]) {
				continue;
			}
			int lvl = entity.getLevels()[i];
			int max = entity.getMaxLevels()[i];
			if (lvl != max) {
				entity.getLevels()[i] += (lvl < max ? 1 : -1);
				if (skill != null) {
					skill.update(i);
				}
			}
		}
	}

	@Override
	public void onStop() {

	}
}
