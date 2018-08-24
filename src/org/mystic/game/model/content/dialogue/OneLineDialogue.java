package org.mystic.game.model.content.dialogue;

import java.util.HashMap;

import org.mystic.game.model.entity.player.Player;

public class OneLineDialogue {

	private static final HashMap<Integer, String> ids = new HashMap<Integer, String>();

	static {
		ids.put(1785, "Looking for a team-cape? I've got a good supply.");
		ids.put(462, "Welcome to the Mages' guild!");
		ids.put(4707, "Hello novice, take a look through my magical wares!");
		ids.put(6970, "Hello there, take a look through my familiars!");
		ids.put(747, "Hi there, I can offer you various rewards in exchange for PK Points.");
		ids.put(1861, "I sell various Ranged Weapons and Equipment.");
		ids.put(7950, "Hello soldier, I sell melee equipment around here, take a look.");
		ids.put(162, "Come on now! Don't stop! Keep going!");
		ids.put(4290, "Do not stop now. Keep working!");
	}

	public static boolean doOneLineChat(Player player, int id) {
		if (ids.containsKey(id)) {
			DialogueManager.sendNpcChat(player, id, Emotion.HAPPY_TALK, ids.get(id));
			return true;
		}
		return false;
	}

}
