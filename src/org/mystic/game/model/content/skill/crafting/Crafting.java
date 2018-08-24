package org.mystic.game.model.content.skill.crafting;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendEnterXInterface;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.TaskQueue;

public class Crafting {

	public static boolean handleCraftingByButtons(Player player, int buttonId) {
		switch (buttonId) {
		case 6211:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case WHEEL_SPINNING:
					if (player.getAttributes().get("spinnable") != null) {
						TaskQueue.queue(new WheelSpinning(player, (short) 28,
								(Spinnable) player.getAttributes().get("spinnable")));
						player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
						player.getAttributes().remove("spinnable");
						player.getAttributes().remove("craftingType");
					}
					break;
				default:
					return true;
				}
				player.getAttributes().remove("craftingType");
			}
			break;
		case 10238:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case WHEEL_SPINNING:
					TaskQueue.queue(
							new WheelSpinning(player, (short) 5, (Spinnable) player.getAttributes().get("spinnable")));
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					player.getAttributes().remove("spinnable");
					break;
				default:
					return true;
				}
				player.getAttributes().remove("craftingType");
			}
			break;
		case 10239:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case WHEEL_SPINNING:
					TaskQueue.queue(
							new WheelSpinning(player, (short) 1, (Spinnable) player.getAttributes().get("spinnable")));
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					player.getAttributes().remove("spinnable");
					player.getAttributes().remove("craftingType");
					return true;
				default:
					player.getAttributes().remove("craftingType");
					return true;
				}
			} else {
				return false;
			}
		case 6212:
			player.getClient().queueOutgoingPacket(new SendEnterXInterface(8886, 0));
			break;
		case 34186:
			player.getClient().queueOutgoingPacket(new SendEnterXInterface(8890, 0));
			break;
		case 34190:
			player.getClient().queueOutgoingPacket(new SendEnterXInterface(8894, 0));
			return true;
		}
		return false;
	}
}
