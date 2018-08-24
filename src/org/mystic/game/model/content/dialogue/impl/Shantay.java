package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueConstants;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;

public class Shantay extends Dialogue {

	public Shantay(Player player) {
		this.player = player;
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
			DialogueManager.sendNpcChat(player, 836, Emotion.HAPPY_TALK,
					"Would you like to buy a shantay pass for 50gp?");
			next++;
			break;
		case 1:
			DialogueManager.sendOption(player, "Yes please", "No thanks.");
			break;
		case 2:
			DialogueManager.sendPlayerChat(player, Emotion.CALM, "No thanks.");
			end();
			break;
		case 3:
			final Item gold = new Item(995, 50);
			final Item pass = new Item(1854, 1);
			if (!player.getInventory().hasItemAmount(new Item(gold))) {
				DialogueManager.sendStatement(player, "You do not have enough gold.");
				end();
			} else if (!player.getInventory().hasSpaceOnRemove(new Item(gold), new Item(pass))) {
				DialogueManager.sendStatement(player, "You do not have enough inventory space.");
				end();
			} else {
				player.getInventory().remove(new Item(gold), false);
				player.getInventory().add(new Item(pass), true);
				DialogueManager.sendStatement(player, "You purchase a shantay pass for 50 gold.");
				end();
			}
			break;
		}

	}

}
