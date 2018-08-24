package org.mystic.game.model.content;

import org.mystic.game.World;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.utility.Misc;

public class CrystalChest {

	private static final Item[] TABLE = {

			new Item(1079, 1), new Item(1093, 1), new Item(1127, 1), new Item(1201, 1), new Item(445, 200),
			new Item(441, 400), new Item(448, 150), new Item(450, 100), new Item(454, 100), new Item(452, 25),
			new Item(1516, 200), new Item(1520, 400), new Item(1514, 100), new Item(1305, 1), new Item(1149, 1),
			new Item(537, 25), new Item(1249, 1), new Item(1377, 1), new Item(3204, 1), new Item(4131, 1),

	};

	public static void click(Player player) {
		if (!player.getInventory().hasItemId(989)) {
			DialogueManager.sendStatement(player, "The chest is locked shut.");
			return;
		}
		if (player.getSkill().locked()) {
			return;
		}
		if (player.getInventory().getFreeSlots() < 4) {
			player.send(new SendMessage("Please clear up at least 3 free slots before opening the chest."));
		} else {
			player.getInventory().remove(989);
			player.getUpdateFlags().sendAnimation(881, 0);
			player.getInventory().addOrCreateGroundItem(995, Misc.randomNumber(80000), true);
			if (Misc.randomNumber(612) == 1) {
				player.getInventory().addOrCreateGroundItem(6571, 1, true);
				World.sendGlobalMessage(
						"@dre@" + player.getUsername() + " has recieved an onyx from the crystal chest!", true);
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " received an uncut onyx from the crystal chest.");
				PlayerLogs.log("SERVER", "" + player.getUsername() + " received an uncut onyx from the crystal.");
			} else {
				player.getInventory().addOrCreateGroundItem(1631, 1, true);
			}
			if (Misc.randomNumber(5000) == 1) {
				player.getInventory().addOrCreateGroundItem(1050, 1, true);
				World.sendGlobalMessage(
						"@dre@" + player.getUsername() + " has recieved a Santa hat from the crystal chest!", true);
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " received a Santa hat from the crystal chest.");
				PlayerLogs.log("SERVER", "" + player.getUsername() + " received a Santa hat from the crystal.");
			}
			for (int i = 0; i < 2; i++) {
				Item loot = new Item(TABLE[org.mystic.utility.Misc.randomNumber(TABLE.length)]);
				player.getInventory().addOrCreateGroundItem(loot.getId(), loot.getAmount(), true);
			}
			player.getSkill().lock(3);
			player.getClient().queueOutgoingPacket(new SendMessage("You use your key and unlock the chest."));
		}
	}
}
