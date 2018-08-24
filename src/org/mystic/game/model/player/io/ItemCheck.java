package org.mystic.game.model.player.io;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;

public class ItemCheck {

	public static Item check(Player player, Item item) {
		try {
			if (item == null) {
				return null;
			}
			if (item.getId() == 2513) {
				item.setId(3140);
				return item;
			}
			if (item.getId() == 4212) {
				item.setId(4214);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

}
