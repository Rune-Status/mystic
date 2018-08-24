package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class ChangePasswordDialogue extends Dialogue {
	private final String password;

	public ChangePasswordDialogue(Player player, String password) {
		this.player = player;
		this.password = password;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.setPassword(password.toLowerCase());
			DialogueManager.sendStatement(player, new String[] { "Your password will now be:", "'" + password + "'" });
			end();
			return true;
		case 9158:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendStatement(player, new String[] { "Your new password will be:", "'" + password + "'",
					"Are you sure you want to make this change?" });
			next += 1;
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "Yes.", "No." });
		}
	}
}
