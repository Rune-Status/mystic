package org.mystic.game.model.content;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class MysteryBox {

	public static final int BOX_ID = 6199;

	private static final int[] COMMON_TABLE = {

			15486, 6585, 13870, 13873, 13876, 13858, 13861, 13864, 13867, 13902, 13896, 13884, 13890, 13905, 13887,
			13893, 13899, 11872, 1377, 15486, 11732, 11846, 11848, 11850, 11852, 11854, 11856, 6737, 6731, 6733, 6735,
			4151, 10551, 6570, 11872, 11874, 11128, 15486, 15441, 15442, 15443, 15444 };

	private static final int[] RARE_TABLE = {

			11694, 11698, 11700, 11696, 11724, 11726, 11728, 11718, 11720, 11722, 13736, 13738, 21790, 21787, 21793,
			20083, 11283

	};

	private static final int[] VERY_RARE_TABLE = {

			13740, 13742, 14484, 1038, 1040, 1042, 1044, 1046, 1048, 1053, 1055, 1057, 1050, 1037, 10330, 10332, 10334,
			10336, 10338, 10340, 10342, 10344, 10346, 10348, 10350, 10352

	};

	public static void open(Player player, int itemId) {
		int chance = Misc.random(100);
		int item = 0;
		int amount = 0;
		player.getInventory().remove(6199);
		if (chance >= 90) {
			// 10% chance
			item = VERY_RARE_TABLE[(int) (Math.random() * VERY_RARE_TABLE.length)];
			amount = GameDefinitionLoader.getItemDef(item).isStackable() ? Misc.random(50) : 1;
			player.getInventory().add(item, amount);
			String message = "@dre@" + player.getUsername() + " has just received a very rare item from a Mystery Box!";
			World.sendGlobalMessage(message, true);
		} else if (chance > 65) {
			// 25% chance
			item = RARE_TABLE[(int) (Math.random() * RARE_TABLE.length)];
			amount = GameDefinitionLoader.getItemDef(item).isStackable() ? Misc.random(50) : 1;
			player.getInventory().add(item, amount);
			String message = "@dre@" + player.getUsername() + " has just received a rare item from a Mystery Box!";
			World.sendGlobalMessage(message, true);
		} else {
			// 65% chance
			item = COMMON_TABLE[(int) (Math.random() * COMMON_TABLE.length)];
			amount = GameDefinitionLoader.getItemDef(item).isStackable() ? Misc.random(50) : 1;
			player.getInventory().add(item, amount);
			return;
		}
	}
}