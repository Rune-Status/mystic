package org.mystic.game.model.content.minigames.barrows;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class BarrowsTunnelDialogue extends Dialogue {

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.teleport(new Location(3552, 9690));
			getPlayer().getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		case 9158:
			getPlayer().getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		}
		return false;
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendStatement(getPlayer(),
					new String[] { "You've found a hidden tunnel, do you want to enter?" });
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), new String[] { "Yes, I'm fearless!", "No way, that looks scary!" });
			end();
			break;
		}
	}
}
