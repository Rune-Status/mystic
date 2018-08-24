package org.mystic.game.model.content;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.Misc;

public class ClueCasket {

	// TODO: Figure out what we want as medium reward and low
	// Medium is only 1 in 4 chance to land on the table and will have things like
	// gilded etc

	public static final int CASKET_ID = 2714;

	private static Item[] rare_rewards = {

			new Item(10330), new Item(10332), new Item(10334), new Item(10336), new Item(10338), new Item(10340),
			new Item(10342), new Item(10344), new Item(10346), new Item(10348), new Item(10350), new Item(10352),
			new Item(2581), new Item(2577)

	};

	private static Item[] medium_rewards = {

			new Item(10452), new Item(10454), new Item(10456), new Item(10446), new Item(10448), new Item(10450),
			new Item(2599), new Item(2601), new Item(2603), new Item(2605), new Item(2607), new Item(2609),
			new Item(2611), new Item(2613), new Item(3474), new Item(3475), new Item(7319), new Item(7321),
			new Item(7323), new Item(7325), new Item(7327), new Item(10400), new Item(10402), new Item(10404),
			new Item(10406), new Item(10408), new Item(10410), new Item(10412), new Item(10414), new Item(10416),
			new Item(10418), new Item(10420), new Item(10422), new Item(10424), new Item(10426), new Item(10428),
			new Item(10430), new Item(10432), new Item(10434), new Item(10436), new Item(10438), new Item(7370),
			new Item(7372), new Item(7374), new Item(7376), new Item(7378), new Item(7380), new Item(7382),
			new Item(7384), new Item(2577), new Item(2579), new Item(13107), new Item(13109), new Item(13111),
			new Item(13113), new Item(13115), new Item(13354)

	};

	private static Item[] low_rewards = {

			new Item(2595), new Item(2591), new Item(2593), new Item(3473), new Item(2597), new Item(7390),
			new Item(7392), new Item(7394), new Item(7396), new Item(7386), new Item(7388), new Item(2583),
			new Item(2585), new Item(2587), new Item(2589), new Item(7362), new Item(7364), new Item(7366),
			new Item(7368), new Item(10392), new Item(10396), new Item(10392), new Item(10394), new Item(10398),
			new Item(2633), new Item(2635), new Item(2637), new Item(2631), new Item(10458), new Item(10460),
			new Item(10462), new Item(10464), new Item(10466), new Item(10468), new Item(10452), new Item(10454),
			new Item(10456)

	};

	public static void open(Player player, int itemId, int slot) {
		if (player.getInventory().playerHasItem(new Item(itemId, 1))) {
			if (itemId == CASKET_ID) {
				int rare_chance = Misc.random(100);
				int medium_chance = Misc.random(4);
				int randomized_index = Misc.random(low_rewards.length - 1);
				Item reward = low_rewards[randomized_index];
				if (rare_chance == 1) {
					randomized_index = Misc.random(rare_rewards.length - 1);
					reward = rare_rewards[randomized_index];
				} else if (medium_chance == 1) {
					randomized_index = Misc.random(medium_rewards.length - 1);
					reward = medium_rewards[randomized_index];
				}
				player.getInventory().clear(slot);
				player.getInventory().getItems()[slot] = reward;
				player.getInventory().update();
				player.send(new SendMessage("You open the chest."));
			}
		}
	}

}