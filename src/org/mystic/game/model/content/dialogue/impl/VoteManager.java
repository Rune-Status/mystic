package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.player.Player;

public class VoteManager extends Dialogue {
	public VoteManager(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9167:
			player.getShopping().open(95);
			return true;
		case 9168:
			DialogueManager.sendNpcChat(player, 659, Emotion.HAPPY_TALK, "Voting will be coming very soon!");
			return true;
		case 9169:
			DialogueManager.sendNpcChat(player, 659, Emotion.HAPPY_TALK,
					"To claim an auth, simply type ::auth (auth code).", "You will then be granted a Vote book, which",
					"will grant you Vote points.");
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Can I view the vote rewards?",
				"I'd like to vote for Mystic.", "How do I claim an auth code?" });
	}
}
