package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueConstants;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendEnterString;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

/**
 * Dialogue for the Player Shop Exchange
 * 
 * @author Daniel
 *
 */
public class ShopExchangeDialogue2 extends Dialogue {

	public ShopExchangeDialogue2(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int button) {
		switch (button) {

		/* Opens players shop */
		case DialogueConstants.OPTIONS_3_1:
			player.getShopping().open(player);
			break;

		/* Setting motto */
		case DialogueConstants.OPTIONS_3_2:
			player.setEnterXInterfaceId(55776);
			player.getClient().queueOutgoingPacket(new SendEnterString());
			break;

		/* Setting Color */
		case DialogueConstants.OPTIONS_3_3:
			player.start(new OptionDialogue("Red", p -> {
				player.setShopColor("@red@");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Red", "", "");
			}, "Blue", p -> {
				player.setShopColor("@blu@");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Blue", "", "");
			}, "Green", p -> {
				player.setShopColor("@gre@");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Green", "", "");
			}, "Cyan", p -> {
				player.setShopColor("@cya@");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Cyan", "", "");
			}, "Default", p -> {
				player.setShopColor("</col>");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Default	", "", "");
			}));
			break;

		}

		return false;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendOption(player, "Edit shop", "Edit shop motto", "Edit shop color");
			break;
		}
	}

}
