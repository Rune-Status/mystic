package org.mystic.game.model.content.shopping;

import org.mystic.game.World;
import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInventoryInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendUpdateItems;

public class Shopping {

	public enum ShopType {
		DEFAULT, PLAYER;
	}

	public static final int SHOP_INTERFACE_ID = 3900;

	public static final int INVENTORY_INTERFACE_ID = 3823;

	private final Player player;

	private long shopId = -1L;

	private ShopType shopType = ShopType.DEFAULT;

	public Shopping(Player player) {
		this.player = player;
	}

	public void buy(int id, int amount, int slot) {
		if (shopId == -1L) {
			return;
		}
		Shop shop;
		if (shopType == ShopType.DEFAULT) {
			shop = Shop.getShops()[((int) shopId)];
		} else {
			Player p = World.getPlayerByName(shopId);
			if (p == null) {
				DialogueManager.sendStatement(p, new String[] { "The shop owner is no longer online." });
				return;
			}
			shop = p.getPlayerShop();
		}
		if (shop == null) {
			return;
		}
		shop.buy(player, slot, id, amount);
		player.getClient().queueOutgoingPacket(new SendUpdateItems(3823, player.getInventory().getItems()));
	}

	public void doShopPriceUpdate() {
		Item[] prices = new Item[48];
		Shop shop;
		if (shopId == -1) {
			return;
		}
		if (shopType == ShopType.DEFAULT) {
			shop = Shop.getShops()[(int) shopId];
			for (int i = 0; i < prices.length; i++) {
				if (shop.get(i) != null) {
					prices[i] = new Item(0, shop.getSellPrice(shop.get(i).getId()));
				}
			}
		} else {
			Player owner = World.getPlayerByName(shopId);
			if (owner == null) {
				return;
			}
			shop = owner.getPlayerShop();
			for (int i = 0; i < prices.length; i++) {
				if (shop.get(i) != null) {
					prices[i] = new Item(0, shop.getSellPrice(shop.get(i).getId()));
				}
			}
		}
		player.send(new SendUpdateItems(567, prices));
	}

	public long getShopId() {
		return shopId;
	}

	public void open(int id) {
		shopType = ShopType.DEFAULT;
		final Shop shop = Shop.getShops()[id];
		if (shop == null) {
			return;
		}
		shopId = id;
		if (!IronMan.isIronMan(player) && shopId == 67) {
			player.send(new SendRemoveInterfaces());
			player.send(new SendMessage("Only those who are worthy may access my shop."));
			return;
		}
		if (IronMan.isIronMan(player)) {
			if (shopId == 67 || shopId == 50 || shopId == 20 || shopId == 19 || shopId == 53 || shopId == 52
					|| shopId == 51 || shopId == 13 || shopId == 98 || shopId == 93 || shopId == 68 || shopId == 77) {
				player.send(new SendRemoveInterfaces());
			} else {
				player.send(new SendRemoveInterfaces());
				player.send(new SendMessage("As an Iron man you are not allowed to access this shop."));
				return;
			}
		}
		if (shopId == 99) {
			player.send(new SendString("                                                   Reward Points: "
					+ player.getEarningPotential().getPkp(), 3903));
		} else if (shopId == 89) {
			player.send(new SendString(
					"                                                   Reward Points: " + player.getPestPoints(),
					3903));
		} else {
			player.send(
					new SendString("Right-click on shop to buy item - Right-click on inventory to sell item.", 3903));
		}
		player.getClient().queueOutgoingPacket(new SendUpdateItems(3823, player.getInventory().getItems()));
		player.getClient().queueOutgoingPacket(new SendUpdateItems(3900, shop.getItems()));
		player.getClient().queueOutgoingPacket(new SendString(shop.getName(), 3901));
		player.getClient().queueOutgoingPacket(new SendInventoryInterface(3824, 3822));
		doShopPriceUpdate();
	}

	public void open(Player owner) {
		if (owner == null) {
			DialogueManager.sendStatement(player, new String[] { "Player not found." });
			return;
		}
		shopType = ShopType.PLAYER;
		Shop shop = owner.getPlayerShop();
		if (shop == null) {
			return;
		}
		shopId = owner.getUsernameToLong();
		player.getClient().queueOutgoingPacket(new SendUpdateItems(3823, player.getInventory().getItems()));
		player.getClient().queueOutgoingPacket(new SendUpdateItems(3900, shop.getItems()));
		player.getClient().queueOutgoingPacket(new SendString(shop.getName(), 3901));
		player.getClient().queueOutgoingPacket(new SendInventoryInterface(3824, 3822));
		doShopPriceUpdate();
	}

	public void reset() {
		shopId = -1L;
	}

	public void sell(int id, int amount, int slot) {
		if (shopId == -1L) {
			return;
		}
		Shop shop;
		if (shopType == ShopType.DEFAULT) {
			shop = Shop.getShops()[((int) shopId)];
		} else {
			Player p = World.getPlayerByName(shopId);
			if (p == null) {
				DialogueManager.sendStatement(p, new String[] { "The shop owner is no longer online." });
				return;
			}
			shop = p.getPlayerShop();
		}
		if (shop == null) {
			return;
		}
		if (shop.sell(player, id, amount)) {
			player.getClient().queueOutgoingPacket(new SendUpdateItems(3823, player.getInventory().getItems()));
		}
	}

	public void sendBuyPrice(int id) {
		if ((shopId == -1L) || (shopType == ShopType.PLAYER)) {
			return;
		}
		if (id == 995) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't sell coins to a shop."));
			return;
		}
		if (!Item.getDefinition(id).isTradable()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't sell this item."));
			return;
		}
		final Shop shop = Shop.getShops()[((int) shopId)];
		if ((shop != null) && (!shop.isGeneral()) && (!shop.isDefaultItem(id))) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't sell this item to this shop."));
			return;
		}
		int value = Shop.getShops()[((int) shopId)].getBuyPrice(id);
		final String itemName = Item.getDefinition(id).getName();
		String price = "" + value;
		String coin = value == 1 ? "coin" : "coins";
		if ((value > 1000) && (value < 1000000))
			price = value / 1000 + "k (" + value + ")";
		else if (value >= 1000000)
			price = value / 1000000 + "m (" + value / 1000 + "k)";
		else if (value == 1) {
			price = "one";
		}
		player.getClient()
				.queueOutgoingPacket(new SendMessage(itemName + ": shop will buy for " + price + " " + coin + "."));
	}

	public void sendSellPrice(int id) {
		if (shopId == -1L) {
			return;
		}
		int value;
		if (shopType == ShopType.PLAYER) {
			Player p = World.getPlayerByName(shopId);
			if (p == null) {
				DialogueManager.sendStatement(p, new String[] { "The shop owner is no longer online." });
				return;
			}
			value = p.getPlayerShop().getSellPrice(id);
		} else {
			value = Shop.getShops()[((int) shopId)].getSellPrice(id);
		}
		String itemName = Item.getDefinition(id).getName();
		if (shopType != ShopType.PLAYER) {
			if (Shop.getShops()[((int) shopId)].getCurrencyName() == null) {
				String price = "" + value;
				String coin = value == 1 ? "coin" : "coins";
				if ((value > 1000) && (value < 1000000))
					price = value / 1000 + "k (" + value + ")";
				else if (value >= 1000000)
					price = value / 1000000 + "m (" + value / 1000 + "k)";
				else if (value == 1) {
					price = "one";
				}
				player.getClient().queueOutgoingPacket(
						new SendMessage(itemName + ": currently costs " + price + " " + coin + "."));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage(itemName + ": currently costs " + value + " "
						+ Shop.getShops()[((int) shopId)].getCurrencyName() + "."));
			}
		} else {
			String price = "" + value;
			String coin = value == 1 ? "coin" : "coins";
			if ((value > 1000) && (value < 1000000))
				price = value / 1000 + "k (" + value + ")";
			else if (value >= 1000000)
				price = value / 1000000 + "m (" + value / 1000 + "k)";
			else if (value == 1) {
				price = "one";
			}
			player.getClient()
					.queueOutgoingPacket(new SendMessage(itemName + ": currently costs " + price + " " + coin + "."));
		}
	}

	public boolean shopping() {
		return shopId > -1L;
	}

	public void update() {
		if (!shopping()) {
			return;
		}
		Shop shop;
		if (shopType == ShopType.DEFAULT) {
			shop = Shop.getShops()[((int) shopId)];
		} else {
			Player p = World.getPlayerByName(shopId);
			if (p == null) {
				DialogueManager.sendStatement(player, new String[] { "This player is no longer online." });
				shopId = -1L;
				return;
			}
			shop = p.getPlayerShop();
		}
		if (shop.isUpdate()) {
			player.getClient().queueOutgoingPacket(new SendUpdateItems(3900, shop.getItems()));
			doShopPriceUpdate();
		}
	}
}
