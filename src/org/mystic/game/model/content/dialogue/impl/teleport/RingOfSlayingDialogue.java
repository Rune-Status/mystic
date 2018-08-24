package org.mystic.game.model.content.dialogue.impl.teleport;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.magic.MagicSkill;
import org.mystic.game.model.entity.player.Player;

public class RingOfSlayingDialogue extends Dialogue {

	public RingOfSlayingDialogue(Player player, boolean operate, int itemId) {
		this.player = player;

	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9178:
			getPlayer().getMagic().teleport(3086, 3489, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		case 9179:
			getPlayer().getMagic().teleport(3093, 3244, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		case 9180:
			getPlayer().getMagic().teleport(2909, 3151, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		case 9181:
			getPlayer().getMagic().teleport(3356, 3268, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		}
		return false;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Vannaka", "Slayer Tower", "Gold Member Slayer Dungeon",
				"Ancient Cavern Slayer Dungeon" });
	}
}