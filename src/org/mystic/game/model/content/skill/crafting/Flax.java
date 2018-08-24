package org.mystic.game.model.content.skill.crafting;

import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class Flax {

	public static void pickFlax(final Player player, final int x, final int y) {
		if (player.getInventory().getFreeSlots() < 1) {
			player.send(new SendMessage("Not enough space in your inventory."));
			return;
		}
		if (player.getSkill().locked()) {
			return;
		} else {
			player.getSkill().lock(1);
			player.getInventory().addItems(new Item(1779, 1));
			player.getUpdateFlags().sendAnimation((new Animation(827)));
			player.send(new SendMessage("You pick some flax."));
		}
	}

}
