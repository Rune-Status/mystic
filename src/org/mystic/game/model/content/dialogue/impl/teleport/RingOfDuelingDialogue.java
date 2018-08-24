package org.mystic.game.model.content.dialogue.impl.teleport;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.magic.MagicSkill;
import org.mystic.game.model.entity.player.Player;

public class RingOfDuelingDialogue extends Dialogue {

	public RingOfDuelingDialogue(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9167:
			getPlayer().getMagic().teleport(3356, 3268, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		case 9168:
			getPlayer().getMagic().teleport(2442, 3090, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		case 9169:
			getPlayer().getMagic().teleport(2659, 2661, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		}
		return false;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Duel Arena", "Castle Wars", "Pest Control" });
	}
}
