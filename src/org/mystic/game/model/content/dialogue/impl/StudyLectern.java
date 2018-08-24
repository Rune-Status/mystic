package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.magic.MagicSkill.SpellBookTypes;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class StudyLectern extends Dialogue {

	public StudyLectern(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9178:
			player.getMagic().setSpellBookType(SpellBookTypes.ANCIENT);
			player.getMagic().setMagicBook(12855);
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			DialogueManager.sendStatement(player, "Your mind fills with new knowledge.");
			break;
		case 9179:
			player.getMagic().setSpellBookType(SpellBookTypes.MODERN);
			player.getMagic().setMagicBook(1151);
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			DialogueManager.sendStatement(player, "Your mind fills with new knowledge.");
			break;
		case 9180:
			player.getMagic().setSpellBookType(SpellBookTypes.LUNAR);
			player.getMagic().setMagicBook(29999);
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			DialogueManager.sendStatement(player, "Your mind fills with new knowledge.");
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
		DialogueManager.sendOption(player,
				new String[] { "Ancient Spellbook", "Modern Spellbook", "Lunar Spellbook", "Exit" });
	}
}