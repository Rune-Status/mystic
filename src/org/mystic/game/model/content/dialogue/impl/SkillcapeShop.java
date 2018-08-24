package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.player.Player;

public class SkillcapeShop extends Dialogue {

	public SkillcapeShop(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.getShopping().open(19);
			return true;
		case 9158:
			player.getShopping().open(20);
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		if (next == 0) {
			DialogueManager.sendOption(player, new String[] { "Skillcape Shop (1)", "Skillcape Shop (2)" });
		}
	}
}
