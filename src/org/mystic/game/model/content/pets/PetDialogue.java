package org.mystic.game.model.content.pets;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;

public class PetDialogue extends Dialogue {
	private final int id;

	public PetDialogue(int id) {
		this.id = id;
	}

	@Override
	public boolean clickButton(int id) {
		return false;
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			String[] chat = Pets.getPetChat(id);

			if (chat == null) {
				end();
				return;
			}

			DialogueManager.sendNpcChat(getPlayer(), id, Emotion.HAPPY_TALK,
					new String[] { chat[org.mystic.utility.Misc.randomNumber(chat.length)] });
			end();
		}
	}
}
