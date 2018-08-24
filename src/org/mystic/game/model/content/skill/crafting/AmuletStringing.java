package org.mystic.game.model.content.skill.crafting;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class AmuletStringing {

	public enum AmuletData {

		GOLD(1673, 1692),

		SAPPHIRE(1675, 1694),

		EMERALD(1677, 1696),

		RUBY(1679, 1698),

		DIAMOND(1681, 1700),

		DRAGONSTONE(1683, 1702),

		ONYX(6579, 6581);

		private final int amuletId, product;

		private AmuletData(final int amuletId, final int product) {
			this.amuletId = amuletId;
			this.product = product;
		}

		public int getAmuletId() {
			return amuletId;
		}

		public int getProduct() {
			return product;
		}
	}

	public static void stringAmulet(Player player, int itemUsed, int usedWith) {
		int amuletId = (itemUsed == 1759 ? usedWith : itemUsed);
		for (AmuletData a : AmuletData.values()) {
			if (amuletId == a.getAmuletId()) {

				// Removes the items
				player.getInventory().remove(1759, 1);
				player.getInventory().remove(amuletId, 1);

				// Adds the item
				player.getInventory().add(a.getProduct(), 1);

				// Gives experience
				player.getSkill().addExperience(Skills.CRAFTING, 30);

				// Gets the name of item
				String name = GameDefinitionLoader.getItemDef(a.getProduct()).getName();

				// Send the message
				player.send(new SendMessage("You have have strung " + Misc.getAOrAn(name) + " " + name + "."));
			}
		}
	}

}
