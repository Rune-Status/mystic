package org.mystic.game.model.content.dialogue.impl.teleport;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.magic.MagicSkill;
import org.mystic.game.model.entity.player.Player;

public class GodWarsWizard extends Dialogue {

	public GodWarsWizard(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9167:
			getPlayer().getMagic().teleport(2886, 5349, 2, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		case 9168:
			getPlayer().getMagic().teleport(2918, 5273, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		case 9169:
			getPlayer().getMagic().teleport(2882, 5310, 2, MagicSkill.TeleportTypes.SPELL_BOOK);
			break;
		}
		return false;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Zamorak Boss", "Saradomin Boss", "Main Area" });
	}
}