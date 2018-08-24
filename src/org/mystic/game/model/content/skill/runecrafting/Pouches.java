package org.mystic.game.model.content.skill.runecrafting;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

/**
 * Runecrafting Pouches
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class Pouches {

	/**
	 * Handles the clicking and interactions with the runecrafting pouches
	 * 
	 * @param player
	 * @param id
	 * @param option
	 * @return
	 */
	public static boolean clickPouch(Player player, int id, int option) {
		if ((id >= 5509) && (id <= 5512)) {
			if (option == 1) {
				int index = id - 5509;
				int ess = (index + 1) * (player.isGoldMember() ? 4 : 3);
				if (player.getPouches()[index] == ess) {
					player.getClient().queueOutgoingPacket(new SendMessage("Your pouch is already full."));
					return true;
				}
				if ((index > 0) && (player.getMaxLevels()[20] < index * 25)) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You need a runecrafting level of " + index * 25 + " to use this pouch."));
					return true;
				}
				int amount = ess - player.getPouches()[index];
				int invAmount = player.getInventory().getItemAmount(7936);
				if (invAmount == 0) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You do not have any essence to fill your pouch with."));
					return true;
				}
				if (amount > invAmount) {
					amount = invAmount;
				}
				player.getInventory().remove(7936, amount, true);
				player.getPouches()[index] = ((byte) (player.getPouches()[index] + amount));
				if (player.getPouches()[index] == ess) {
					player.getClient().queueOutgoingPacket(new SendMessage("You fill your pouch."));
				}
			} else if (option == 2) {
				int index = id - 5509;
				if (player.getPouches()[index] == 0) {
					player.getClient().queueOutgoingPacket(new SendMessage("Your pouch is empty."));
					return true;
				}
				int add = player.getInventory().add(7936, player.getPouches()[index]);
				player.getPouches()[index] = ((byte) (player.getPouches()[index] - add));
				if (player.getPouches()[index] == 0) {
					player.getClient().queueOutgoingPacket(new SendMessage("You empty your pouch."));
				}
			} else if (option == 3) {
				int index = id - 5509;
				if (player.getPouches()[index] == 0) {
					player.getClient().queueOutgoingPacket(new SendMessage("Your pouch is empty."));
					return true;
				}
				player.getClient().queueOutgoingPacket(
						new SendMessage("There is " + player.getPouches()[index] + " essence is your pouch."));
			}
			return true;
		}
		return false;
	}

}