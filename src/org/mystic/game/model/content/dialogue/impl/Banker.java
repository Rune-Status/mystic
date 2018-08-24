package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.bank.BankSecurity;
import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class Banker extends Dialogue {

	public Banker(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9178:
			if (!player.getBankPinAttributes().hasBankPin()) {
				BankSecurity.init(player, false);
			} else {
				DialogueManager.sendStatement(player, "You already have a pin set.");
			}
			player.getDialogue().end();
			break;
		case 9179:
			if (!player.getBankPinAttributes().hasBankPin()) {
				DialogueManager.sendStatement(player, "You do not have a PIN to remove.");
			} else if (player.getBankPinAttributes().hasBankPin()
					&& !player.getBankPinAttributes().hasEnteredBankPin()) {
				DialogueManager.sendStatement(player, "You must enter and confirm your pin before you can remove it.");
			} else {
				BankSecurity.deletePin(player);
			}
			break;
		case 9180:
			player.getDialogue().end();
			player.getBank().openBankNoBusy();
			break;
		case 9181:
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		}
		return false;

	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Set Bank PIN", "Delete PIN", "Open bank account", "Exit" });
	}
}