package org.mystic.game.model.entity.npc;

import org.mystic.game.model.content.minigames.castlewars.CastleWarsConstants;
import org.mystic.utility.Misc;

public class RandomNpcChatting {

	private static double time = CastleWarsConstants.LOBBY_TIME / 60;

	private static String[] gnome_chats = { "Come on! Keep it moving!", "My granny moves faster than you!",
			"Move move move!" };

	private static String[] warrior_chats = { "Those things which cannot be seen, perceive them.",
			"Do nothing which is of no use.", "Think not dishonestly.", "The Way in training is.",
			"Gain and loss between you must distinguish.", "Trifles pay attention even to.",
			"Way of the warrior this is.", "Judgment and understanding for everything develop you must.",
			"Ways of all professions know you.", "Acquainted with every art become." };

	private static String[] bandos_chats = { "For the glory of Bandos!", "Break their bones!", "Split their skulls!",
			"We feast on the bones of our enemies tonight!", "Crush them underfoot!", "All glory to Bandos!",
			"Death to our enemies!", "GRRRAAAAAR!" };

	/**
	 * Handles sending a random forced message from a mob
	 * 
	 * @param mob
	 *            The mob sending the message
	 */
	public static void handleRandomMobChatting(Npc mob) {
		if (mob.getId() == 1526) {
			if (Misc.randomNumber(30) == 1) {
				mob.getUpdateFlags().sendForceMessage("Time till next game starts: " + Math.round(time) + " minutes.");
			}
		} else if (mob.getId() == 162) {
			if (Misc.randomNumber(50) == 1) {
				mob.getUpdateFlags().sendForceMessage(gnome_chats[Misc.random(gnome_chats.length - 1)]);
			}
		} else if (mob.getId() == 4290) {
			if (Misc.randomNumber(50) == 1) {
				mob.getUpdateFlags().sendForceMessage(warrior_chats[Misc.random(warrior_chats.length - 1)]);
			}
		} else if (mob.getId() == 6260) {
			if (Misc.randomNumber(70) == 1) {
				mob.getUpdateFlags().sendForceMessage(bandos_chats[Misc.random(bandos_chats.length - 1)]);
			}
		}
	}

}