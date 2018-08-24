package org.mystic.game.model.content.dialogue.impl.teleport;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.magic.MagicSkill;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class GloryDialogue extends Dialogue {

	private int itemId;

	private boolean operate = false;

	public GloryDialogue(Player player, boolean operate, int itemId) {
		this.player = player;
		this.operate = operate;
		this.itemId = itemId;
	}

	@Override
	public boolean clickButton(int id) {
		if (!player.getPlayer().getMagic().canTeleport(TeleportTypes.GLORY)) {
			end();
			return false;
		}
		switch (id) {
		case 9178:
			useGlory(player, new Location(3086, 3489, 0), itemId, operate);
			end();
			break;
		case 9179:
			useGlory(player, new Location(3093, 3244, 0), itemId, operate);
			end();
			break;
		case 9180:
			useGlory(player, new Location(2909, 3151, 0), itemId, operate);
			end();
			break;
		case 9181:
			useGlory(player, new Location(3097, 3499, 0), itemId, operate);
			end();
			player.getClient()
					.queueOutgoingPacket(new SendMessage("@blu@The Wilderness lever is on the wall infront of you."));
			break;
		}
		return false;
	}

	public static void useGlory(Player player, Location location, int itemId, boolean operate) {
		if (player.getPlayer().getMagic().canTeleport(TeleportTypes.GLORY)) {
			if (operate == true) {
				if (itemId - 2 != 1702) {
					player.getEquipment().getItems()[2].setId(itemId - 2);
					player.getEquipment().update();
				}
			}
			if (operate == false) {
				if (itemId - 2 != 1702) {
					player.getInventory().remove(itemId);
					player.getInventory().add(itemId - 2, 1);
				}
			}
			player.getClient().queueOutgoingPacket(
					new SendMessage("You use up a charge from your " + Item.getDefinition(itemId).getName() + "."));
			player.getPlayer().getMagic().teleport(location.getX(), location.getY(), location.getZ(),
					MagicSkill.TeleportTypes.GLORY);
		}
		player.send(new SendRemoveInterfaces());
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Edgeville", "Draynor", "Karamja", "Wilderness Lever" });
	}
}