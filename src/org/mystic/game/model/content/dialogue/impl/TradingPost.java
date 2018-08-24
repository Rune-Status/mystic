package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class TradingPost extends Dialogue {

	public int buttonId;

	public TradingPost(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9167:
			end();
			player.start(new ShopExchangeDialogue(player));
			break;
		case 9168:
			player.getDialogue().end();
			player.start(new Banker(player));
			break;
		case 9169:
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		}
		return false;

	}

	@Override
	public void execute() {
		if (IronMan.isIronMan(player)) {
			end();
			player.send(new SendRemoveInterfaces());
			player.send(new SendMessage("You are an Iron Man. You stand alone"));
		} else {
			DialogueManager.sendOption(player, new String[] { "Player Owned Shops", "Banking Options", "Exit" });
		}
	}
}
