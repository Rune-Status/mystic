package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueConstants;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.mysql.Donation;

public class DonationClaimer extends Dialogue {

	public DonationClaimer(Player player) {
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
			DialogueManager.sendNpcChat(player, 590, Emotion.CALM, "Hello, I'm the Donation rewards manager here",
					"at Mystic. Would you like me to check", "for any unclaimed rewards you may have?");
			next++;
			break;
		case 1:
			DialogueManager.sendOption(player, "Yes please", "No thanks.");
			break;
		case 2:
			DialogueManager.sendPlayerChat(player, Emotion.SAD, "No thanks.");
			end();
			break;
		case 3:
			if (player.getInventory().getFreeSlots() != 28) {
				player.send(new SendRemoveInterfaces());
				player.send(new SendMessage("Please clear your inventory before checking for unclaimed donations."));
			} else {
				player.send(new SendRemoveInterfaces());
				new Thread(new Donation(player)).start();
				player.send(new SendMessage(
						"@dre@Please wait one moment while your account is checked for unclaimed items."));
			}
			end();
			break;
		}

	}

}