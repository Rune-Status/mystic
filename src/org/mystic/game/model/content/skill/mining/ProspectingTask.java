package org.mystic.game.model.content.skill.mining;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.mining.Mining.Ore;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

public class ProspectingTask extends Task {

	private final Player player;

	private final Ore data;

	public ProspectingTask(Player entity, GameObject object) {
		super(entity, 3, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = entity;
		this.data = Ore.get(object.getId());
		player.getClient().queueOutgoingPacket(new SendMessage("You prospect the rock for ore.."));
	}

	@Override
	public void execute() {
		DialogueManager.sendStatement(player, "This rock contains " + data.getName().toLowerCase() + ".");
		stop();
	}

	@Override
	public void onStop() {

	}
}
