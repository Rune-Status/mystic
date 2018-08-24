package org.mystic.game.model.networking.packet.impl;

import java.util.Optional;

import org.mystic.game.GameConstants;
import org.mystic.game.model.content.$100Scroll;
import org.mystic.game.model.content.$10Scroll;
import org.mystic.game.model.content.$50Scroll;
import org.mystic.game.model.content.CashCasket;
import org.mystic.game.model.content.ClueCasket;
import org.mystic.game.model.content.Food;
import org.mystic.game.model.content.HerbBox;
import org.mystic.game.model.content.ItemConstructing;
import org.mystic.game.model.content.ItemDeconstructing;
import org.mystic.game.model.content.MembershipBond;
import org.mystic.game.model.content.MysteryBox;
import org.mystic.game.model.content.Potion;
import org.mystic.game.model.content.bank.Banking.RearrangeTypes;
import org.mystic.game.model.content.book.impl.Baxtorian;
import org.mystic.game.model.content.book.impl.Guide;
import org.mystic.game.model.content.dialogue.impl.ConfirmDialogue;
import org.mystic.game.model.content.dialogue.impl.WildernessCloak;
import org.mystic.game.model.content.dialogue.impl.teleport.GloryDialogue;
import org.mystic.game.model.content.dialogue.impl.teleport.RingOfDuelingDialogue;
import org.mystic.game.model.content.dialogue.impl.teleport.RingOfSlayingDialogue;
import org.mystic.game.model.content.pets.Pets;
import org.mystic.game.model.content.skill.crafting.AmuletStringing;
import org.mystic.game.model.content.skill.crafting.Gem;
import org.mystic.game.model.content.skill.crafting.GemCutting;
import org.mystic.game.model.content.skill.crafting.Glass;
import org.mystic.game.model.content.skill.crafting.GlassBlowing;
import org.mystic.game.model.content.skill.crafting.HideCrafting;
import org.mystic.game.model.content.skill.crafting.JewelryCreationTask;
import org.mystic.game.model.content.skill.firemaking.Firemaking;
import org.mystic.game.model.content.skill.fletching.Fletching;
import org.mystic.game.model.content.skill.herblore.CleanHerbTask;
import org.mystic.game.model.content.skill.herblore.ConstructPotionAction;
import org.mystic.game.model.content.skill.herblore.FinishedPotionData;
import org.mystic.game.model.content.skill.herblore.GrimyHerbData;
import org.mystic.game.model.content.skill.herblore.PotionDecanting;
import org.mystic.game.model.content.skill.herblore.PotionType;
import org.mystic.game.model.content.skill.herblore.UnfinishedPotionData;
import org.mystic.game.model.content.skill.magic.spells.BoltEnchanting;
import org.mystic.game.model.content.skill.prayer.BoneBurying;
import org.mystic.game.model.content.skill.runecrafting.Pouches;
import org.mystic.game.model.content.skill.slayer.EnchantedGemDialogue;
import org.mystic.game.model.content.skill.smithing.SmithingTask;
import org.mystic.game.model.content.skill.summoning.impl.PackYak;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Inventory;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.StreamBuffer.InBuffer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.DiceTask;
import org.mystic.game.task.impl.DigTask;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class ItemActionPacketListener extends IncomingPacket {

	private final int ITEM_OPERATE = 75;

	private final int DROP_ITEM = 87;

	private final int PICKUP_ITEM = 236;

	private final int HANDLE_OPTIONS = 214;

	private final int PACKET_145 = 145;

	private final int PACKET_117 = 117;

	private final int PACKET_43 = 43;

	private final int PACKET_129 = 129;

	private final int EQUIP_ITEM = 41;

	private final int USE_ITEM_ON_ITEM = 53;

	private final int FIRST_CLICK_ITEM = 122;

	private final int SECOND_CLICK_ITEM = 16;

	@Override
	public int getMaxDuplicates() {
		return 40;
	}

	@SuppressWarnings("unused")
	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isDead())
				|| (!player.getController().canClick() || player.isSpeared() || player.getMagic().isTeleporting())) {
			return;
		}
		if (GameConstants.DEV_MODE) {
			player.send(new SendMessage("Packet: " + opcode));
		}
		int x;
		int magicId;
		int z;
		switch (opcode) {
		case PACKET_145:
			int interfaceId = in.readShort(StreamBuffer.ValueType.A);
			int slot = in.readShort(StreamBuffer.ValueType.A);
			int itemId = in.readShort(StreamBuffer.ValueType.A);
			switch (interfaceId) {
			case 41710:
				player.getRunePouch().withdraw(itemId, slot, 10);
				break;
			case 41711:
				player.getRunePouch().store(new Item(itemId, 10));
				break;
			case 2700:
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().withdraw(slot, 1);
				}
				break;
			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				SmithingTask.start(player, itemId, 1, interfaceId, slot);
				break;
			case 42752:
				if (player.getInterfaceManager().main == 42750) {
					BoltEnchanting.handle(player, itemId);
				}
				break;
			case 1688:
				if (!player.getEquipment().slotHasItem(slot)) {
					return;
				}
				if (player.getDialogue() != null) {
					player.getDialogue().end();
				}
				player.getEquipment().unequip(slot);
				break;
			case 4233:
			case 4239:
			case 4245:
				JewelryCreationTask.start(player, itemId, 1);
				break;
			case 5064:
				if (!player.getInventory().slotContainsItem(slot, itemId)) {
					return;
				}
				if (player.getInterfaceManager().hasBankOpen()) {
					deposit(player, slot, itemId, 1);
					return;
				}
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().store(itemId, 1, slot);
					return;
				}
				break;
			case 5382:
				if (player.getInterfaceManager().hasBankOpen()) {
					withdrawBankItem(player, slot, itemId, 1);
				}
				break;
			case 3322:
				if (player.getTrade().trading()) {
					player.getTrade().getContainer().offer(itemId, 1, slot);
				} else if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().offer(itemId, 1, slot);
				}
				break;
			case 3415:
				if (player.getTrade().trading()) {
					player.getTrade().getContainer().withdraw(slot, 1);
				}
				break;
			case 6669:
				if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().withdraw(slot, 1);
				}
				break;
			case 3900:
				player.getShopping().sendSellPrice(itemId);
				break;
			case 3823:
				player.getShopping().sendBuyPrice(itemId);
				break;
			}
			break;
		case PACKET_117:
			interfaceId = in.readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			itemId = in.readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			slot = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);
			switch (interfaceId) {
			case 41710:
				player.getRunePouch().withdraw(itemId, slot, 100);
				break;
			case 41711:
				player.getRunePouch().store(new Item(itemId, 100));
				break;
			case 2700:
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().withdraw(slot, 5);
				}
				break;
			case 1688:
				if (itemId == 1712 || itemId == 1710 || itemId == 1708 || itemId == 1706) {
					player.start(new GloryDialogue(player, true, itemId));
					return;
				}
				if (itemId == 13281 || itemId == 1710 || itemId == 1708 || itemId == 1706) {
					player.start(new RingOfSlayingDialogue(player, false, itemId));
					return;
				}
				if (itemId == 15345) {
					player.start(new WildernessCloak(player));
					return;
				}
				if (itemId == 2552 || itemId == 2554 || itemId == 2556 || itemId == 2558 || itemId == 2560
						|| itemId == 2562 || itemId == 2564 || itemId == 2566) {
					player.start(new RingOfDuelingDialogue(player));
					return;
				}
				if (itemId == 1704) {
					player.getClient().queueOutgoingPacket(new SendMessage("Your amulet has no charges."));
					return;
				}
				if (itemId == 10499) {
					player.getClient().queueOutgoingPacket(new SendMessage("You have nothing to collect."));
					return;
				}
				break;
			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				SmithingTask.start(player, itemId, 5, interfaceId, slot);
				break;
			case 4233:
			case 4239:
			case 4245:
				JewelryCreationTask.start(player, itemId, 5);
				break;
			case 5064:
				if (!player.getInventory().slotContainsItem(slot, itemId)) {
					return;
				}
				if (player.getInterfaceManager().hasBankOpen()) {
					deposit(player, slot, itemId, 5);
					return;
				}
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().store(itemId, 5, slot);
					return;
				}
				break;
			case 5382:
				if (player.getInterfaceManager().hasBankOpen()) {
					withdrawBankItem(player, slot, itemId, 5);
				}
				break;
			case 3322:
				if (player.getTrade().trading()) {
					player.getTrade().getContainer().offer(itemId, 5, slot);
				} else if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().offer(itemId, 5, slot);
				}
				break;
			case 6669:
				if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().withdraw(slot, 5);
				}
				break;
			case 3415:
				if (player.getTrade().trading()) {
					player.getTrade().getContainer().withdraw(slot, 5);
				}
				break;
			case 3900:
				player.getShopping().buy(itemId, 1, slot);
				break;
			case 3823:
				player.getShopping().sell(itemId, 1, slot);
				break;
			}
			break;
		case PACKET_43:
			interfaceId = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			itemId = in.readShort(StreamBuffer.ValueType.A);
			slot = in.readShort(StreamBuffer.ValueType.A);
			switch (interfaceId) {
			case 41710:
				player.getRunePouch().withdraw(itemId, slot, player.getRunePouch().getItemAmount(itemId));
				break;
			case 41711:
				player.getRunePouch().store(new Item(itemId, player.getInventory().getItemAmount(itemId)));
				break;
			case 2700:
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().withdraw(slot, 10);
				}
				break;
			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				SmithingTask.start(player, itemId, 10, interfaceId, slot);
				break;
			case 4233:
			case 4239:
			case 4245:
				JewelryCreationTask.start(player, itemId, 10);
				break;
			case 5064:
				if (!player.getInventory().slotContainsItem(slot, itemId)) {
					return;
				}
				if (player.getInterfaceManager().hasBankOpen()) {
					deposit(player, slot, itemId, 10);
				} else if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().store(itemId, 10, slot);
				}
				break;
			case 5382:
				if (player.getInterfaceManager().hasBankOpen()) {
					withdrawBankItem(player, slot, itemId, 10);
				}
				break;
			case 3322:
				if (player.getTrade().trading()) {
					player.getTrade().getContainer().offer(itemId, 10, slot);
				} else if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().offer(itemId, 10, slot);
				}
				break;
			case 6669:
				if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().withdraw(slot, 10);
				}
				break;
			case 3415:
				if (player.getTrade().trading()) {
					player.getTrade().getContainer().withdraw(slot, 10);
				}
				break;
			case 3900:
				player.getShopping().buy(itemId, 5, slot);
				break;
			case 3823:
				player.getShopping().sell(itemId, 5, slot);
				break;
			}
			break;
		case PACKET_129:
			slot = in.readShort(StreamBuffer.ValueType.A);
			interfaceId = in.readShort();
			itemId = in.readShort(StreamBuffer.ValueType.A);
			switch (interfaceId) {
			case 2700:
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().withdraw(slot, 2147483647);
				}
				break;
			case 5064:
				if (!player.getInventory().slotContainsItem(slot, itemId)) {
					return;
				}
				if (player.getInterfaceManager().hasBankOpen()) {
					deposit(player, slot, itemId, 2147483647);
				} else if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().store(itemId, 2147483647, slot);
				}
				break;
			case 5382:
				if (player.getInterfaceManager().hasBankOpen()) {
					withdrawBankItem(player, slot, itemId, 2147483647);
				}
				break;
			case 3322:
				if (player.getTrade().trading()) {
					player.getTrade().getContainer().offer(itemId, 2147483647, slot);
				} else if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().offer(itemId, 2147483647, slot);
				}
				break;
			case 6669:
				if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().withdraw(slot, 2147483647);
				}
				break;
			case 3415:
				if (player.getTrade().trading()) {
					player.getTrade().getContainer().withdraw(slot, 2147483647);
				}
				break;
			case 3900:
				player.getShopping().buy(itemId, 10, slot);
				break;
			case 3823:
				player.getShopping().sell(itemId, 10, slot);
				break;
			}
			break;
		case EQUIP_ITEM:
			itemId = in.readShort();
			slot = in.readShort(StreamBuffer.ValueType.A);
			in.readShort();
			if (!player.getInventory().slotContainsItem(slot, itemId)) {
				return;
			}
			if (!player.getInventory().playerHasItem(new Item(itemId))) {
				return;
			}
			if (itemId == 4079) {
				player.getUpdateFlags().sendAnimation(1458, 0);
				return;
			}
			if (itemId == 4155) {
				player.send(new SendMessage("You have been assigned to kill another " + player.getSlayer().getAmount()
						+ " x " + Misc.formatPlayerName(
								GameDefinitionLoader.getNpcDefinition(player.getSlayer().getTask()).getName() + ".")));
				return;
			}
			if (Pouches.clickPouch(player, itemId, 2)) {
				return;
			}
			if (player.getDialogue() != null) {
				player.getDialogue().end();
			}
			if (player.getInventory().get(slot).getId() == 20747) {
				player.getAchievements().incr(player, "Equip the Max cape");
			}
			player.getEquipment().equip(player.getInventory().get(slot), slot);
			break;
		case HANDLE_OPTIONS:
			interfaceId = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			final int transfer = in.readByte(StreamBuffer.ValueType.C);
			final int fromSlot = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			final int toSlot = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			switch (interfaceId) {
			case 5382:
				if (player.getTrade().trading()) {
					player.send(new SendMessage("You can not do that right now!"));
					return;
				}
				if (transfer == 2) {
					player.getBank().itemToTab(fromSlot, toSlot, true);
				} else {
					if (transfer == 1) {
						int fromTab = player.getBank().getData(fromSlot, 0);
						int toTab = player.getBank().getData(toSlot, 0);
						player.getBank().changeTabAmount(toTab, 1, false);
						player.getBank().changeTabAmount(fromTab, -1, true);
						RearrangeTypes temp = player.getBank().rearrangeType;
						player.getBank().rearrangeType = RearrangeTypes.INSERT;
						player.getBank().swap(toSlot - (toTab > fromTab ? 1 : 0), fromSlot);
						player.getBank().rearrangeType = temp;
						player.getBank().update();
					} else {
						RearrangeTypes temp = player.getBank().rearrangeType;
						player.getBank().rearrangeType = RearrangeTypes.SWAP;
						player.getBank().swap(toSlot, fromSlot);
						player.getBank().rearrangeType = temp;
					}
				}
				break;

			case 3214:
			case 5064:
				player.getInventory().swap(toSlot, fromSlot);
				break;

			}
			break;

		case DROP_ITEM:
			dropItem(player, in, opcode, length);
			break;

		case PICKUP_ITEM:
			pickupItem(player, in, opcode, length);
			break;

		case USE_ITEM_ON_ITEM:
			itemOnItem(player, in, opcode, length);
			break;

		case 25:
			in.readShort();
			int itemInInven = in.readShort(StreamBuffer.ValueType.A);
			int groundItem = in.readShort();
			int y = in.readShort(StreamBuffer.ValueType.A);
			z = player.getLocation().getZ();
			in.readShort();
			x = in.readShort();
			break;

		case 237:
			slot = in.readShort();
			itemId = in.readShort(StreamBuffer.ValueType.A);
			interfaceId = in.readShort();
			magicId = in.readShort(StreamBuffer.ValueType.A);
			if (!player.getInventory().slotContainsItem(slot, itemId)) {
				return;
			}
			player.getAttributes().set("magicitem", Integer.valueOf(itemId));
			player.getMagic().useMagicOnItem(itemId, magicId);
			break;

		case FIRST_CLICK_ITEM:
			firstItemClick(player, in, opcode, length);
			break;

		case SECOND_CLICK_ITEM:
			secondItemClick(player, in, opcode, length);
			break;

		case ITEM_OPERATE:
			operateItem(player, in, opcode, length);
			break;
		}
	}

	private void itemOnItem(Player player, InBuffer in, int opcode, int length) {
		int firstSlot = in.readShort();
		int secondSlot = in.readShort(StreamBuffer.ValueType.A);
		if ((!player.getInventory().slotHasItem(firstSlot)) || (!player.getInventory().slotHasItem(secondSlot))) {
			return;
		}
		final Item first = player.getInventory().get(firstSlot);
		final Item second = player.getInventory().get(secondSlot);
		if (!player.getInventory().hasAllItems(new Item[] { second, first })) {
			return;
		}
		if ((second == null) || (first == null)) {
			return;
		}
		if (GameConstants.DEV_MODE) {
			player.send(new SendMessage("First: " + first.getId() + " : Second: " + second.getId() + "."));
		}
		if (Item.getDefinition(first.getId()).getName().toLowerCase().contains("(unf)")) {
			final Optional<FinishedPotionData> data = FinishedPotionData.get(first, second);
			if (data.isPresent()) {
				if (second.getId() == data.get().getIngredient().getId()) {
					TaskQueue.queue(new ConstructPotionAction(player, PotionType.FINISHED, data, null, 28));
				}
			}
			return;
		}
		if (first.getId() == ConstructPotionAction.VIAL_OF_WATER.getId()) {
			if (Item.getDefinition(second.getId()).getName().toLowerCase().contains("clean")) {
				final Optional<UnfinishedPotionData> data = UnfinishedPotionData.get(second);
				if (data.isPresent()) {
					TaskQueue.queue(new ConstructPotionAction(player, PotionType.UNFINISHED, null, data, 28));
				}
				return;
			}
		}
		if (first.getId() == 1755 || second.getId() == 1755) {
			final int gem = (first.getId() == 1755 ? second.getId() : first.getId());
			if (Gem.forId(gem) != null) {
				TaskQueue.queue(new GemCutting(player, 28, Gem.forId(gem)));
				return;
			}
		}
		if (first.getId() == 1785 && second.getId() == 1775) {
			TaskQueue.queue(new GlassBlowing(player, 28, Glass.VIAL));
			return;
		}
		if (first.getId() == 1759 || second.getId() == 1759) {
			AmuletStringing.stringAmulet(player, first.getId(), second.getId());
			return;
		}
		if (first.getId() == 12435) {
			PackYak.winterStorage(player, first, second);
			return;
		} else if (second.getId() == 12435) {
			PackYak.winterStorage(player, second, first);
			return;
		}
		if (first.getId() == 20085) {
			player.getRunePouch().store(second);
			return;
		}
		if (ItemConstructing.handle(player, first.getId(), second.getId())) {
			return;
		}
		if (Fletching.SINGLETON.itemOnItem(player, first, second)) {
			return;
		}
		if (HideCrafting.SINGLETON.itemOnItem(player, first, second)) {
			return;
		}
		if (player.getInventory().hasItemId(590)) {
			if (Firemaking.attemptFiremaking(player, first, second)) {
				return;
			}
		}
		if (PotionDecanting.decant(player, firstSlot, secondSlot)) {
			return;
		}
	}

	private void dropItem(final Player player, final StreamBuffer.InBuffer in, final int opcode, final int legnth) {
		final int itemId = in.readShort(StreamBuffer.ValueType.A);
		final int interfaceId = in.readShort();
		final int slot = in.readShort(StreamBuffer.ValueType.A);
		if (interfaceId != Inventory.INTERFACE_ID) {
			return;
		}
		if (player.isBusy()) {
			return;
		}
		if (slot < 0 || slot > player.getInventory().getSize()) {
			return;
		}
		final Item interacted = player.getInventory().get(slot);
		if (interacted == null || itemId != interacted.getId() || !player.getInventory().playerHasItem(interacted)
				|| !player.getInventory().slotContainsItem(slot, itemId)) {
			return;
		}
		if ((player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER))
				&& (player.getCombat().inCombat())) {
			if (GameDefinitionLoader.getHighAlchemyValue(interacted.getId()) > 10000) {
				player.getClient().queueOutgoingPacket(new SendMessage("You can't drop this item while in combat."));
			}
		}
		if (!player.getController().canDrop(player)) {
			player.send(new SendMessage("You are not allowed to drop items here."));
			return;
		}
		if (interacted.getId() == 20086 || interacted.getId() == 15098 || interacted.getId() == 6949
				|| interacted.getId() == 608 || interacted.getId() == 607 || interacted.getId() == 6758
				|| interacted.getId() == 15099) {
			player.send(new SendMessage("You can't drop or destory this item."));
			return;
		}
		if (Pets.isItemPet(interacted.getId())) {
			player.getPets().init(interacted.getId());
			return;
		}
		if (interacted.getId() == 773) {
			player.getInventory().remove(interacted.getId());
			return;
		}
		if (interacted.getId() == 1052 || interacted.getId() == 18365) {
			player.start(new ConfirmDialogue(player,
					new String[] { "@dre@WARNING: @bla@If you drop this item there is no getting it back.",
							"You will have to buy another one from the Legends Guard.", "Are you sure?" }) {
				@Override
				public void onConfirm() {
					if (!player.getInventory().hasItemId(interacted.getId())) {
						return;
					}
					player.getInventory().remove(interacted.getId());
				}
			});
			return;
		}
		if (interacted.getId() == 20085) {
			player.start(new ConfirmDialogue(player,
					new String[] { "@dre@WARNING: @bla@If you destory this item there is no getting it back.",
							"Runes inside, if any. Will be lost.", "Are you sure?" }) {
				@Override
				public void onConfirm() {
					if (!player.getInventory().hasItemId(interacted.getId())) {
						return;
					}
					player.getRunePouch().clear();
					player.getRunePouch().update();
					player.getInventory().remove(interacted.getId());
				}
			});
			return;
		}
		if (!GameDefinitionLoader.getItemDef(interacted.getId()).getName().contains("0")) {
			if (GameDefinitionLoader.getItemDef(interacted.getId()).getName().contains("Verac's")
					|| GameDefinitionLoader.getItemDef(interacted.getId()).getName().contains("Karil's")
					|| GameDefinitionLoader.getItemDef(interacted.getId()).getName().contains("Torag's")
					|| GameDefinitionLoader.getItemDef(interacted.getId()).getName().contains("Ahrim's")
					|| GameDefinitionLoader.getItemDef(interacted.getId()).getName().contains("Dharok's")
					|| GameDefinitionLoader.getItemDef(interacted.getId()).getName().contains("Guthan's")) {
				player.start(new ConfirmDialogue(player, new String[] {
						"@dre@WARNING: @bla@Dropping this item will cause it to break.", "Are you sure?" }) {
					@Override
					public void onConfirm() {
						if (!player.getInventory().hasItemId(interacted.getId())) {
							return;
						}
						player.getGroundItems().drop(interacted.getId(), slot);
					}
				});
				return;
			}
		}
		SoundPlayer.play(player, Sounds.DROP_ITEM);
		TaskQueue.onMovement(player);
		player.send(new SendRemoveInterfaces());
		if (player.getDialogue() != null) {
			player.getDialogue().end();
		}
		player.getGroundItems().drop(interacted.getId(), slot);
	}

	private void pickupItem(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		final int y = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		final int itemId = in.readShort();
		final int x = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		if (player.getSkill().locked()) {
			return;
		}
		if (player.getTrade().trading()) {
			player.getTrade().end(false);
		}
		player.getCombat().reset();
		player.getGroundItems().pickup(x, y, itemId);
	}

	private void firstItemClick(final Player player, final StreamBuffer.InBuffer in, final int opcode,
			final int length) {
		final int interfaceId = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		final int slot = in.readShort(StreamBuffer.ValueType.A);
		final int itemId = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		if (interfaceId != Inventory.INTERFACE_ID) {
			return;
		}
		final Item interacted = player.getInventory().get(slot);
		if (interacted == null || itemId != interacted.getId() || !player.getInventory().playerHasItem(interacted)
				|| !player.getInventory().slotContainsItem(slot, itemId)) {
			return;
		}
		if (interacted.getId() == ClueCasket.CASKET_ID) {
			ClueCasket.open(player, interacted.getId(), slot);
			return;
		}
		if (interacted.getId() == CashCasket.CASKET_ID) {
			CashCasket.open(player, interacted.getId(), slot);
			return;
		}
		if (interacted.getId() == HerbBox.HERB_BOX) {
			HerbBox.open(player, interacted.getId());
			return;
		}
		if (interacted.getId() == MysteryBox.BOX_ID) {
			MysteryBox.open(player, interacted.getId());
			return;
		}
		if (interacted.getId() == MembershipBond.MEMBERSHIP_BOND_ID) {
			MembershipBond.activate(player, interacted.getId());
			return;
		}
		if (interacted.getId() == 6758) {
			$10Scroll.activate(player, interacted.getId());
			return;
		}
		if (interacted.getId() == 607) {
			$50Scroll.activate(player, interacted.getId());
			return;
		}
		if (interacted.getId() == 608) {
			$100Scroll.activate(player, interacted.getId());
			return;
		}
		if (Pouches.clickPouch(player, interacted.getId(), 1)) {
			return;
		}
		if (interacted.getId() == 4079) {
			player.getUpdateFlags().sendAnimation(1457, 0);
			return;
		}
		if (BoneBurying.bury(player, interacted.getId(), slot)) {
			return;
		}
		if (Food.consume(player, itemId, slot) || (Potion.drink(player, itemId, slot))) {
			return;
		}
		if (player.getMagic().clickMagicItems(interacted.getId())) {
			return;
		}
		if (Item.getDefinition(interacted.getId()).getName().toLowerCase().contains("grimy")) {
			TaskQueue.queue(new CleanHerbTask(player, slot, GrimyHerbData.forId(interacted.getId())));
			return;
		}
		switch (interacted.getId()) {
		case 1856:
			player.read(new Guide(player).evaluate());
			break;
		case 292:
			player.read(new Baxtorian(player).evaluate());
			break;
		case 4155:
			player.start(new EnchantedGemDialogue(player));
			break;
		case 20085:
			player.getRunePouch().open();
			break;

		case 15098:
			if (player.getSkill().locked()) {
				return;
			} else {
				TaskQueue.queue(new DiceTask(player, 1));
			}
			break;

		case 952:
			if (player.getSkill().locked()) {
				return;
			} else {
				TaskQueue.queue(new DigTask(player));
			}
			break;

		default:
			player.send(new SendMessage("Nothing interesting happens."));
			break;

		}
	}

	private void secondItemClick(final Player player, final StreamBuffer.InBuffer in, final int opcode,
			final int length) {
		final int itemId = in.readShort(StreamBuffer.ValueType.A);
		final int slot = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		final int interfaceId = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		if (interfaceId != Inventory.INTERFACE_ID) {
			return;
		}
		final Item interacted = player.getInventory().get(slot);
		if (interacted == null || itemId != interacted.getId() || !player.getInventory().playerHasItem(interacted)
				|| !player.getInventory().slotContainsItem(slot, itemId)) {
			return;
		}
		if (Pouches.clickPouch(player, interacted.getId(), 3)) {
			return;
		}
		if (ItemDeconstructing.handle(player, interacted)) {
			return;
		}
		switch (interacted.getId()) {
		case 4079:
			player.getUpdateFlags().sendAnimation(1459, 0);
			break;

		default:
			player.send(new SendMessage("Nothing interesting happens."));
			break;
		}
	}

	private void operateItem(final Player player, final StreamBuffer.InBuffer in, final int opcode, final int length) {
		final int interfaceId = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		final int slot = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		final int itemId = in.readShort(true, StreamBuffer.ValueType.A);
		if (interfaceId != Inventory.INTERFACE_ID) {
			return;
		}
		final Item interacted = player.getInventory().get(slot);
		if (interacted == null || itemId != interacted.getId() || !player.getInventory().playerHasItem(interacted)
				|| !player.getInventory().slotContainsItem(slot, itemId)) {
			return;
		}
		if (player.getSummoning().summon(itemId)) {
			return;
		}
		if (Potion.empty(player, itemId, slot)) {
			return;
		}
		switch (itemId) {

		case 20085:
			player.getRunePouch().withdrawAll();
			break;

		case 1704:
			player.send(new SendMessage("This amulet is all out of charges."));
			break;

		case 1712:
		case 1710:
		case 1708:
		case 1706:
			player.start(new GloryDialogue(player, false, itemId));
			break;

		case 13281:
			player.start(new RingOfSlayingDialogue(player, false, itemId));
			break;

		case 15345:
			player.start(new WildernessCloak(player));
			break;

		case 2552:
		case 2554:
		case 2556:
		case 2558:
		case 2560:
		case 2562:
		case 2564:
		case 2566:
			player.start(new RingOfDuelingDialogue(player));
			break;

		default:
			player.send(new SendMessage("Nothing interesting happens."));
			break;
			
		}
	}

	public void deposit(Player player, int slot, int itemId, int amount) {
		player.getBank().deposit(itemId, amount, slot);
	}

	public void withdrawBankItem(Player player, int slot, int itemId, int amount) {
		player.getBank().withdraw(itemId, amount);
	}

}