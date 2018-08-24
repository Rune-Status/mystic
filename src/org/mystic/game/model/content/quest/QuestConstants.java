package org.mystic.game.model.content.quest;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.content.quest.impl.disater.RecipeForDisaster;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class QuestConstants {

	public static final Quest RECIPE_FOR_DISASTER = new RecipeForDisaster();

	public static final Quest[] QUESTS_BY_ID = { RECIPE_FOR_DISASTER };

	public static final int[] QUEST_BUTTONS = { 49228, 2161, 28184 };

	public static final int[] QUEST_LINE_IDS = { 12772, 673, 7352 };

	public static final short TOTAL_QUESTS = (short) QUESTS_BY_ID.length;

	private static final Map<String, Quest> questsByName = new HashMap<String, Quest>();

	public static boolean hasCompletedAllQuests(Player player) {
		for (Quest i : QUESTS_BY_ID) {
			if (!player.getQuesting().isQuestCompleted(i)) {
				return false;
			}
		}
		return true;
	}

	static {
		for (Quest i : QUESTS_BY_ID) {
			questsByName.put(i.getName(), i);
		}
	}

	public static Quest getQuestForName(String name) {
		return questsByName.get(name);
	}

	public static boolean clickObject(Player player, int id) {
		switch (id) {
		case 12355:
			if (player.getQuesting().isQuestCompleted(RECIPE_FOR_DISASTER)) {
				player.getClient().queueOutgoingPacket(new SendMessage("You have already completed this mini-quest."));
			} else {
				RECIPE_FOR_DISASTER.init(player);
			}
			return true;
		}
		return false;
	}
}
