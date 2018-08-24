package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.utility.Misc;

public class WildernessCloak extends Dialogue {

	public WildernessCloak(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9167:
			player.getDialogue().end();
			player.getMagic().teleport(2540 + Misc.randomNumber(2), 4716 + Misc.randomNumber(3), 0,
					TeleportTypes.SPELL_BOOK);
			break;
		case 9168:
			player.getDialogue().end();
			player.getMagic().teleport(2662, 3305, 0, TeleportTypes.SPELL_BOOK);
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
		DialogueManager.sendOption(player, new String[] { "Mage Bank", "Ardougne", "Exit" });
	}
}