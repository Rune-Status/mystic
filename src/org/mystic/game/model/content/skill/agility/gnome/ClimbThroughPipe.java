package org.mystic.game.model.content.skill.agility.gnome;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.agility.Agility;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.impl.ForceMovementTask;

public class ClimbThroughPipe extends ForceMovementTask {

	private final int walk;

	private final boolean run;

	private final double exp;

	private final Location to;

	private final boolean gnome;

	public ClimbThroughPipe(Player player, Location to, Location start, double exp, boolean gnome) {
		super(player, to, ControllerManager.DEFAULT_CONTROLLER);
		this.walk = player.getAnimations().getWalkEmote();
		this.run = player.getRunEnergy().isRunning();
		this.exp = exp;
		this.to = to;
		this.gnome = gnome;
		if (!stopped()) {
			player.teleport(start);
			player.getClient().queueOutgoingPacket(new SendMessage("You squeeze in.."));
			player.setAppearanceUpdateRequired(true);
			player.getAnimations().setWalkEmote(844);
			player.getRunEnergy().setRunning(false);
		}
		player.setTakeDamage(false);
	}

	@Override
	public void onDestination() {
		player.getAnimations().setWalkEmote(walk);
		player.getRunEnergy().setRunning(run);
		player.setAppearanceUpdateRequired(true);
		player.getSkill().addExperience(Skills.AGILITY, exp);
		player.teleport(new Location(to.getX(), to.getY()));
		player.getClient().queueOutgoingPacket(new SendMessage("..and make it safely to the other side"));
		player.setTakeDamage(true);
		if (gnome) {
			Agility.finish(player);
		}
		this.stop();
	}
}
