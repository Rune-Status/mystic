package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueConstants;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;

public class Lanthus extends Dialogue {

	public Lanthus(Player player, Npc mob) {
		this.player = player;
		this.mob = mob;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case DialogueConstants.OPTIONS_2_1:
			next = 3;
			execute();
			return true;
		case DialogueConstants.OPTIONS_2_2:
			next = 2;
			execute();
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, 1526, Emotion.HAPPY_TALK,
					"Hi, I sell rewards in exchange for castle-wars tickets.", "Would you like to view my store?");
			next++;
			break;
		case 1:
			DialogueManager.sendOption(player, "Yes please", "No thanks");
			break;
		case 2:
			DialogueManager.sendPlayerChat(player, Emotion.CALM, "No thanks");
			end();
			break;
		case 3:
			end();
			player.getShopping().open(52);
			break;
		}
	}
}