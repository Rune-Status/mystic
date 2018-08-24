package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendString;

public class Donator extends Dialogue {

	public Donator(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9167:
			end();
			player.start(new DonatorShopSelect(player));
			return true;
		case 9168:
			player.send(new SendString("https://www.exigence.org/store/", 12000));
			end();
			return true;
		case 9169:
			player.send(
					new SendString("http://forum.exigence.org/index.php?/topic/4-gold-member-benefits/#entry6", 12000));
			end();
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player,
				new String[] { "Open Stores", "Purchase Member Points", "Member Ranks Information" });
	}
}