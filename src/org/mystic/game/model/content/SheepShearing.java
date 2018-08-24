package org.mystic.game.model.content;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.ShearSheepTask;
import org.mystic.utility.Misc;

/**
 * Handles the shearing of sheep for wool
 */
public class SheepShearing {

	/**
	 * The wool item
	 */
	private static Item WOOL = new Item(1737);

	/**
	 * Starts the shearing of the sheep
	 * 
	 * @param player
	 * @param mob
	 */
	public static void start(Player player, Npc mob) {
		if (!player.getInventory().playerHasItem(new Item(1735))) {
			player.send(new SendMessage("You need a pair of sheers to do that"));
			return;
		}
		if (!player.getInventory().hasSpaceFor(WOOL)) {
			player.send(new SendMessage("You don't have enough inventory space to do this."));
			return;
		}
		int chance = Misc.randomNumber(100);
		if (!player.isDoingCape()) {
			TaskQueue.queue(new ShearSheepTask(player, 6));
		}
		if (chance > 60) {
			mob.getUpdateFlags().sendForceMessage("BAA!");
			player.getInventory().add(WOOL);
		} else {
			player.send(new SendMessage("The sheep manages to quickly escape from within your reach!"));
		}
		return;
	}
}
