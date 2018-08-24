package org.mystic.game.model.content.shopping;

import org.mystic.game.model.content.shopping.impl.AgilityShop;
import org.mystic.game.model.content.shopping.impl.DonatorShop1;
import org.mystic.game.model.content.shopping.impl.DonatorShop2;
import org.mystic.game.model.content.shopping.impl.DonatorShop3;
import org.mystic.game.model.content.shopping.impl.DonatorShop4;
import org.mystic.game.model.content.shopping.impl.PKPShop;
import org.mystic.game.model.content.shopping.impl.PestShop;
import org.mystic.game.model.content.shopping.impl.RFDChestShop;
import org.mystic.game.model.content.shopping.impl.RunecraftingShop;
import org.mystic.game.model.content.shopping.impl.SlayerShop;
import org.mystic.game.model.content.shopping.impl.TokkulShop;
import org.mystic.game.model.content.shopping.impl.VoteShop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.ItemContainer;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.GameDefinitionLoader;

public class Shop extends ItemContainer {

	public static final int SHOP_SIZE = 40;

	private static final Shop[] shops = new Shop[100];

	static {
		shops[PKPShop.SHOP_ID] = new PKPShop();
		shops[TokkulShop.SHOP_ID] = new TokkulShop();
		shops[VoteShop.SHOP_ID] = new VoteShop();
		shops[SlayerShop.SHOP_ID] = new SlayerShop();
		shops[PestShop.SHOP_ID] = new PestShop();
		shops[DonatorShop1.SHOP_ID] = new DonatorShop1();
		shops[DonatorShop2.SHOP_ID] = new DonatorShop2();
		shops[DonatorShop3.SHOP_ID] = new DonatorShop3();
		shops[DonatorShop4.SHOP_ID] = new DonatorShop4();
		shops[RunecraftingShop.SHOP_ID] = new RunecraftingShop();
		shops[AgilityShop.SHOP_ID] = new AgilityShop();
		shops[RFDChestShop.RFD_CHEST_SHOP_ID] = new RFDChestShop();
	}

	public static Shop[] getShops() {
		return shops;
	}

	private final int id;

	private final Item[] defaultItems;

	private boolean general = false;

	private String name;

	private int restock = 50;

	private long update = System.currentTimeMillis();

	public Shop(int id, Item[] stock, boolean general, String name) {
		super(40, ItemContainer.ContainerTypes.ALWAYS_STACK, true, false);
		this.general = general;
		this.name = name;
		this.id = id;
		shops[id] = this;
		this.defaultItems = (stock.clone());
		for (int i = 0; i < stock.length; i++) {
			if (stock[i] != null) {
				setSlot(new Item(stock[i]), i);
			}
		}
		shift();
		TaskQueue.queue(new Task(restock) {
			@Override
			public void execute() {
				refreshContainers();
			}

			@Override
			public void onStop() {
			}
		});
	}

	public Shop(Item[] stock, String name) {
		super(40, ItemContainer.ContainerTypes.ALWAYS_STACK, false, true);
		this.general = false;
		this.name = name;
		this.id = -1;
		this.defaultItems = (stock.clone());
	}

	@Override
	public boolean allowZero(int id) {
		return isDefaultItem(id);
	}

	public void buy(Player player, int slot, int id, int amount) {
		if (amount > 500) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You can only buy maximum 500 items at a time from these shops."));
			amount = 500;
		}
		if ((this.id == 21) && (!player.isGoldMember())) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You must be a gold member to purchase from this shop."));
			return;
		}
		if (!hasItem(slot, id)) {
			return;
		}
		if (get(slot).getAmount() == 0) {
			player.getClient().queueOutgoingPacket(new SendMessage("The shop is out of stock."));
			return;
		}
		if (amount > get(slot).getAmount()) {
			amount = get(slot).getAmount();
		}
		final Item buying = new Item(id, amount);
		final Item gold = new Item(995, getSellPrice(id) * amount);
		if (!player.getInventory().hasSpaceOnRemove(gold, buying)) {
			if (!buying.getDefinition().isStackable()) {
				int slots = player.getInventory().getFreeSlots();
				if (slots > 0) {
					buying.setAmount(slots);
					amount = slots;
					gold.setAmount(getSellPrice(id) * amount);
				} else {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You do not have enough inventory space to buy this item."));
					return;
				}
			} else {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have enough inventory space to buy this item."));
				return;
			}
		}
		if (gold.getAmount() > 0) {
			if (!player.getInventory().hasItemAmount(gold)) {
				player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough coins to buy that."));
				return;
			}
		}
		if (this.id != 21) {
			int newAmount = get(slot).getAmount() - amount;
			if (newAmount < 1) {
				if (isDefaultItem(id)) {
					get(slot).setAmount(0);
				} else {
					remove(get(slot));
				}
			} else {
				get(slot).setAmount(newAmount);
			}
		}
		if (gold.getAmount() > 0) {
			player.getInventory().remove(gold, false);
		}
		player.getAchievements().incr(player, "Spend 10,000,000 in Shops", gold.getAmount());
		player.getAchievements().incr(player, "Spend 10,000,000 in Shops", gold.getAmount());
		if (((this.id == 19) || (this.id == 20)) && (buying.getDefinition().getName().contains("cape"))
				&& (player.getSkill().hasTwo99s()) && (buying.getId() != 18654)) {
			buying.setId(buying.getId() + 1);
		}
		if (this.id == 13) {
			buying.setId(buying.getDefinition().getNoteId() > -1 ? buying.getDefinition().getNoteId() : buying.getId());
		}
		PlayerLogs.log(player.getUsername(), "Bought Item from shop (" + this.id + ") (Id: " + buying.getId()
				+ ", Amount: " + buying.getAmount() + ")");
		player.getInventory().add(buying);
		update();
	}

	public boolean empty(int slot) {
		return (get(slot) == null) || (get(slot).getAmount() == 0);
	}

	public int getBuyPrice(int id) {
		if (this.id == 21) {
			return 0;
		}
		return GameDefinitionLoader.getStoreSellToValue(id);
	}

	public String getCurrencyName() {
		return null;
	}

	public Item getDefaultItem(int id) {
		for (Item i : defaultItems) {
			if ((i != null) && (i.getId() == id)) {
				return i;
			}
		}
		return null;
	}

	public Item[] getDefaultItems() {
		return defaultItems;
	}

	public String getName() {
		return name;
	}

	public int getSellPrice(int id) {
		if (this.id == 21) {
			return 0;
		}
		return GameDefinitionLoader.getStoreBuyFromValue(id);
	}

	public boolean hasItem(int slot, int id) {
		return (get(slot) != null) && (get(slot).getId() == id);
	}

	public boolean isDefaultItem(int id) {
		for (Item i : defaultItems) {
			if ((i != null) && (i.getId() == id)) {
				return true;
			}
		}
		return false;
	}

	public boolean isGeneral() {
		return general;
	}

	public boolean isUpdate() {
		return System.currentTimeMillis() - update < 1000L;
	}

	@Override
	public void onAdd(Item item) {
	}

	@Override
	public void onFillContainer() {
	}

	@Override
	public void onMaxStack() {
	}

	@Override
	public void onRemove(Item item) {
	}

	public void refreshContainers() {
		final Item[] items = getItems();
		for (int j = 0; j < 40; j++) {
			if (items[j] == null) {
				break;
			}
			Item stock = getDefaultItem(items[j].getId());
			if (stock != null) {
				if (items[j].getAmount() < stock.getAmount())
					items[j].add(1);
				else if (items[j].getAmount() > stock.getAmount()) {
					items[j].remove(1);
				}
			} else if (items[j].getAmount() > 1)
				items[j].remove(1);
			else {
				remove(getItems()[j]);
			}
		}
		update();
	}

	public boolean sell(Player player, int id, int amount) {
		if (id == 995) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't sell coins to a shop."));
			return false;
		}
		if (!Item.getDefinition(id).isTradable() || id == 995 || id == 20086 || id == 15098 || id == 15099) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't sell this item here."));
			return false;
		}
		if ((this.id == 21) || ((!general) && (!isDefaultItem(id)))) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("The shopkeeper is not interested in purchasing this item."));
			return false;
		}
		int invAmount = player.getInventory().getItemAmount(id);
		if (invAmount == 0) {
			return false;
		}
		if (invAmount < amount) {
			amount = invAmount;
		}
		final Item item = new Item(id, amount);
		if (!hasSpaceFor(item)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("The shop does not have enough space to buy this item."));
			return false;
		}
		final Item gold = new Item(995, getBuyPrice(id) * amount);
		if (!player.getInventory().hasSpaceOnRemove(item, gold)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You do not have enough inventory space to sell this item."));
			return false;
		}
		PlayerLogs.log(player.getUsername(),
				"Sold Item to shop (Id: " + item.getId() + ", Amount: " + item.getAmount() + ")");
		player.getInventory().remove(item);
		if (gold.getAmount() > 0) {
			player.getInventory().add(gold);
		}
		add(item);
		update();
		return true;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void update() {
		update = System.currentTimeMillis();
	}
}