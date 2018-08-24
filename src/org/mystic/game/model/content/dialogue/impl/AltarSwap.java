package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.prayer.PrayerBook;
import org.mystic.game.model.content.skill.prayer.PrayerBook.PrayerBookType;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class AltarSwap extends Dialogue {

	public AltarSwap(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.send(new SendMessage("@dre@You switch to the ancient curses prayer book."));
			PrayerBook.setBook(player, PrayerBookType.CURSES);
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;

		case 9158:
			player.send(new SendMessage("@dre@You switch to the default prayer book."));
			PrayerBook.setBook(player, PrayerBookType.DEFAULT);
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		}
		return false;
	}

	@Override
	public void execute() {
		if (next == 0) {
			DialogueManager.sendOption(player, new String[] { "Ancient Curses Prayer Book", "Default Prayer Book" });
		}
	}
}