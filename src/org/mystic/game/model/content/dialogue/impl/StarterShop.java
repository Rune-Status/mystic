package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.player.Player;

public class StarterShop extends Dialogue {

	public StarterShop(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9178:
			player.getDialogue().end();
			player.getShopping().open(7);
			break;
		case 9179:
			player.getDialogue().end();
			player.getShopping().open(8);
			break;
		case 9180:
			player.getDialogue().end();
			player.getShopping().open(9);
			break;
		case 9181:
			player.getDialogue().end();
			player.getShopping().open(27);
			break;
		}
		return false;

	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Melee Gear", "Range Gear", "Magic Gear", "Supplies" });
	}
}