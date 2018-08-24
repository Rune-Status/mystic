package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.content.skill.magic.SpellBookTeleporting;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class HomeTeleport extends Dialogue {

	public int buttonId;

	public HomeTeleport(Player player, int buttonId) {
		this.player = player;
		this.buttonId = buttonId;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			if (player.inWilderness() && player.getCombat().inCombat()) {
				if (player.getInventory().playerHasItem(new Item(563, 1))) {
					SpellBookTeleporting.teleport(player, buttonId);
				} else {
					DialogueManager.sendStatement(player, "You can't use the home teleport while in combat",
							"without a law rune or teleport tablet.");
					player.send(new SendMessage(
							"You can't use the home teleport while in combat without a law rune or teleport tablet."));
				}
			} else {
				player.getMagic().teleport(3087, 3490, 0, TeleportTypes.SPELL_BOOK);
			}
			player.getDialogue().end();
			break;

		case 9158:
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		}
		return false;

	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Home", "Exit" });
	}
}