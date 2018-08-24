package org.mystic.game.model.content.dialogue.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mystic.game.World;
import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueConstants;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendEnterString;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendString;

/**
 * Dialogue for the Player Shop Exchange
 * 
 * @author Daniel
 *
 */
public class ShopExchangeDialogue extends Dialogue {

	public ShopExchangeDialogue(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int button) {
		switch (button) {

		/* Opens players shop */
		case DialogueConstants.OPTIONS_3_1:
			player.getShopping().open(player);
			break;

		/* Setting motto */
		case DialogueConstants.OPTIONS_3_2:
			player.setEnterXInterfaceId(55776);
			player.getClient().queueOutgoingPacket(new SendEnterString());
			break;

		/* Setting Color */
		case DialogueConstants.OPTIONS_3_3:
			player.start(new OptionDialogue("Red", p -> {
				player.setShopColor("@red@");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Red", "", "");
			}, "Blue", p -> {
				player.setShopColor("@blu@");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Blue", "", "");
			}, "Green", p -> {
				player.setShopColor("@gre@");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Green", "", "");
			}, "Cyan", p -> {
				player.setShopColor("@cya@");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Cyan", "", "");
			}, "Default", p -> {
				player.setShopColor("</col>");
				player.send(new SendRemoveInterfaces());
				DialogueManager.sendInformationBox(player, "Player Owned Shops Exchange",
						"You have successfully changed your shop color.", "It's now Default	", "", "");
			}));
			break;

		/* Shop collecting */
		case DialogueConstants.OPTIONS_5_5:
			if (player.getShopCollection() == 0) {
				DialogueManager.sendStatement(player, "You do not have any coins to collect!");
				return true;
			}
			if (player.getInventory().getFreeSlots() == 0) {
				DialogueManager.sendStatement(player, "Please free up some space first.");
				return true;
			}
			if (player.getInventory().hasItemId(995)) {
				DialogueManager.sendStatement(player, "Please remove all coins from your inventory.");
				return true;
			}
			player.getInventory().add(new Item(995, (int) player.getShopCollection()));
			player.setShopCollection(0);
			return true;

		/* Searching player */
		case DialogueConstants.OPTIONS_5_3:
			player.setEnterXInterfaceId(55777);
			player.getClient().queueOutgoingPacket(new SendEnterString());
			return true;

		/* Searching item */
		case DialogueConstants.OPTIONS_5_4:
			player.setEnterXInterfaceId(55778);
			player.getClient().queueOutgoingPacket(new SendEnterString());
			return true;

		/* Editing */
		case DialogueConstants.OPTIONS_5_2:
			DialogueManager.sendOption(player, "Edit shop", "Edit shop motto", "Edit shop color");
			return true;

		/* Show all active shops */
		case DialogueConstants.OPTIONS_5_1:
			player.getClient().queueOutgoingPacket(new SendInterface(53500));
			List<Player> available = Arrays.stream(World.getPlayers())
					.filter(p -> p != null && p.isActive() && p.getPlayerShop().hasAnyItems())
					.collect(Collectors.toList());
			for (int i = 53516; i < 53716; i++) {
				Player p = null;
				if (i - 53516 < available.size()) {
					p = available.get(i - 53516);
					String color = "";
					if (p.getShopColor() == null) {
						color = "</col>";
					} else {
						color = p.getShopColor();
					}
					player.getClient().queueOutgoingPacket(new SendString(p.getUsername(), i));
					if (player.getShopMotto() != null) {
						player.getClient().queueOutgoingPacket(new SendString(color + p.getShopMotto(), i + 200));
					} else {
						player.getClient()
								.queueOutgoingPacket(new SendString(color + "No shop description set.", i + 200));
					}
				} else {
					player.getClient().queueOutgoingPacket(new SendString("", i));
					player.getClient().queueOutgoingPacket(new SendString("", i + 200));
				}
			}
			return true;
		}

		return false;
	}

	@Override
	public void execute() {
		if (IronMan.isIronMan(player)) {
			end();
			player.send(new SendRemoveInterfaces());
			player.send(new SendMessage("You are an Iron Man. You stand alone"));
		}
		switch (next) {
		case 0:
			DialogueManager.sendOption(player, "View all shops", "Edit your Shop", "Search for player's shop",
					"Search for specific item", "Collect coins");
			break;
		}
	}

}
