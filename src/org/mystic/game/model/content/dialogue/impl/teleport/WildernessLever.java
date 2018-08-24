package org.mystic.game.model.content.dialogue.impl.teleport;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class WildernessLever extends Dialogue {

	public WildernessLever(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9190:
			getPlayer().getMagic().teleport(3153, 3923, 0, TeleportTypes.SPELL_BOOK);
			player.getDialogue().end();
			break;
		case 9191:
			getPlayer().getMagic().teleport(3158, 3670, 0, TeleportTypes.SPELL_BOOK);
			player.getDialogue().end();
			break;
		case 9192:
			getPlayer().send(new SendMessage("You may find green dragons if you proceed west."));
			getPlayer().getMagic().teleport(3361, 3687, 0, TeleportTypes.SPELL_BOOK);
			player.getDialogue().end();
			break;
		case 9193:
			getPlayer().getMagic().teleport(2958, 3615, 0, TeleportTypes.SPELL_BOOK);
			player.getDialogue().end();
			break;
		case 9194:
			getPlayer().getMagic().teleport(2974, 3868, 0, TeleportTypes.SPELL_BOOK);
			player.getDialogue().end();
			break;
		}
		return false;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player,
				new String[] { "Deserted Keep", "Graveyard", "East Dragons", "West Dragons", "Level 44 Obelisks" });
	}
}