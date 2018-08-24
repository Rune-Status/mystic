package org.mystic.game.model.content.skill.agility.gnome;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.task.RunOnceTask;

public class ClimbOverTask extends RunOnceTask {

	private final Player player;

	private final Location location;

	private final double exp;

	public ClimbOverTask(Player player, int delay, Location location, int animation, double exp) {
		super(player, delay);
		this.player = player;
		this.location = location;
		this.exp = exp;
		player.getUpdateFlags().sendAnimation(animation, 0);
		player.setTakeDamage(false);
	}

	@Override
	public void onStop() {
		player.teleport(new Location(location));
		player.getSkill().addExperience(Skills.AGILITY, exp);
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
		player.setTakeDamage(true);
	}
}
