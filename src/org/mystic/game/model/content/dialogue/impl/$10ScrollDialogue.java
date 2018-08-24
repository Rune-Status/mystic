package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueConstants;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class $10ScrollDialogue extends Dialogue {

	public $10ScrollDialogue(Player player) {
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
			DialogueManager.sendNpcChat(player, 590, Emotion.HAPPY_TALK,
					"Activating this scroll will grant you 100 Member Points.", "Are you sure you want to do this?");
			next++;
			break;
		case 1:
			DialogueManager.sendOption(player, "Yes", "No");
			break;
		case 2:
			DialogueManager.sendPlayerChat(player, Emotion.CALM, "No thanks");
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		case 3:
			if (player.getInventory().playerHasItem(new Item(6758, 1))) {
				player.getInventory().remove(6758, 1);
				player.addDonatorPoints(100);
				QuestTab.update(player);
				player.send(new SendMessage("Your account has been credited 100 Member Points."));
				player.getDialogue().end();
				player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			} else {
				player.getDialogue().end();
				player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			}
			break;
		}
	}
}