package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.player.Player;

public class Nastroth extends Dialogue {
	public Nastroth(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9167:
			DialogueManager.sendNpcChat(player, 6540, Emotion.ANGRY_1, "The wilderness rewards store is coming soon!");
			// player.getShopping().open(95);
			return true;
		case 9168:
			player.getEarningPotential().trade();
			return true;
		case 9169:
			DialogueManager.sendNpcChat(player, 6540, Emotion.ANGRY_1, "Ring imbueing is coming soon!");
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player,
				new String[] { "Wilderness Rewards", "Exchange Ancient Artifacts", "Imbue rings" });
	}
}
