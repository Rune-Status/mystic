package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueConstants;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.utility.Misc;

public class WildernessExpert extends Dialogue {

	public WildernessExpert(Player player) {
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
			DialogueManager.sendNpcChat(player, 1273, Emotion.HAPPY_TALK,
					"Hello traveler, I can open your wilderness caskets.", "Do you have any?");
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
			if (player.getInventory().playerHasItem(new Item(2728, 1))) {
				player.getInventory().remove(2728, 1);
				player.getInventory().add(player.isGoldMember() ? new Item(995, 2000000 + Misc.random(3500000))
						: new Item(995, 1000000 + Misc.random(2000000)));
				player.send(new SendMessage("@dre@The Wilderness Soul reveals you found a Tier 1 Casket."));
				player.getInventory().update();
				player.getDialogue().end();
				player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			} else {
				if (player.getInventory().playerHasItem(new Item(2730, 1))) {
					player.getInventory().remove(2730, 1);
					player.getInventory().add(player.isGoldMember() ? new Item(995, 3000000 + Misc.random(4500000))
							: new Item(995, 2000000 + Misc.random(3000000)));
					player.send(new SendMessage("@dre@The Wilderness Soul reveals you found a Tier 2 Casket."));
					player.getInventory().update();
					player.getDialogue().end();
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
				} else {
					if (player.getInventory().playerHasItem(new Item(2732, 1))) {
						player.getInventory().remove(2732, 1);
						player.getInventory().add(player.isGoldMember() ? new Item(995, 4000000 + Misc.random(5500000))
								: new Item(995, 3000000 + Misc.random(5500000)));
						player.send(new SendMessage("@dre@The Wilderness Soul reveals you found a Tier 3 Casket."));
						player.getInventory().update();
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
	}
}