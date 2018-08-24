package org.mystic.game.model.content.quest.impl.disater;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.content.quest.QuestConstants;
import org.mystic.game.model.entity.player.Player;

public class GypsyDialogue extends Dialogue {

	public GypsyDialogue(final Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			if (player.getQuesting().isQuestCompleted(QuestConstants.RECIPE_FOR_DISASTER)) {
				DialogueManager.sendNpcChat(player, 3385, Emotion.HAPPY_TALK,
						new String[] { "You have already completed this mini-quest." });
				end();
			} else {
				DialogueManager.sendNpcChat(player, 3385, Emotion.CALM,
						new String[] { "Enter the portal next to me to begin", "the Recipe for Disaster mini-quest." });
				end();
			}
			break;
		}
	}

	@Override
	public boolean clickButton(int id) {
		return false;
	}
}
