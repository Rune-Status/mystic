package org.mystic.game.model.entity.npc.drops;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.mystic.game.World;
import org.mystic.game.model.content.minigames.barrows.Barrows;
import org.mystic.game.model.content.minigames.fightcave.FightCave;
import org.mystic.game.model.content.minigames.godwars.GodWars;
import org.mystic.game.model.content.minigames.warriorsguild.ArmorAnimator;
import org.mystic.game.model.content.minigames.warriorsguild.CyclopsRoom;
import org.mystic.game.model.content.minigames.warriorsguild.WarriorsGuildConstants;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.prayer.BoneBurying;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.JsonLoader;
import org.mystic.utility.Misc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class NPCDrops {

	public static final int[] CHARMS = { 12158, 12159, 12160, 12163 };

	/**
	 * The map containing all the npc drops.
	 */
	private static Map<Integer, NPCDrops> dropControllers = new HashMap<Integer, NPCDrops>();

	public static JsonLoader parseDrops() {
		return new JsonLoader() {

			@Override
			public void load(JsonObject reader, Gson builder) {

				final int[] npcIds = builder.fromJson(reader.get("npcIds"), int[].class);
				final NpcDropItem[] drops = builder.fromJson(reader.get("drops"), NpcDropItem[].class);
				final NPCDrops d = new NPCDrops();

				d.npcIds = npcIds;
				d.drops = drops;

				for (int id : npcIds) {
					dropControllers.put(id, d);
				}
			}

			@Override
			public String filePath() {
				return "./data/def/npcs/drops.json";
			}
		};
	}

	/**
	 * The id's of the NPC's that "owns" this class.
	 */
	private int[] npcIds;

	/**
	 * All the drops that belongs to this class.
	 */
	private NpcDropItem[] drops;

	/**
	 * Gets the NPC drop controller by an id.
	 * 
	 * @return The NPC drops associated with this id.
	 */
	public static NPCDrops forId(int id) {
		return dropControllers.get(id);
	}

	public static Map<Integer, NPCDrops> getDrops() {
		return dropControllers;
	}

	/**
	 * Gets the drop list
	 * 
	 * @return the list
	 */
	public NpcDropItem[] getDropList() {
		return drops;
	}

	/**
	 * Gets the npcIds
	 * 
	 * @return the npcIds
	 */
	public int[] getNpcIds() {
		return npcIds;
	}

	/**
	 * Represents a npc drop item
	 */
	public static class NpcDropItem {

		/**
		 * The id.
		 */
		private final int id;

		/**
		 * Array holding all the amounts of this item.
		 */
		private final int[] count;

		/**
		 * The chance of getting this item.
		 */
		private final int chance;

		/**
		 * New npc drop item
		 * 
		 * @param id
		 *            the item
		 * @param count
		 *            the count
		 * @param chance
		 *            the chance
		 */
		public NpcDropItem(int id, int[] count, int chance) {
			this.id = id;
			this.count = count;
			this.chance = chance;
		}

		/**
		 * Gets the item id.
		 * 
		 * @return The item id.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the chance.
		 * 
		 * @return The chance.
		 */
		public int[] getCount() {
			return count;
		}

		/**
		 * Gets the chance.
		 * 
		 * @return The chance.
		 */
		public DropChance getChance() {
			switch (chance) {
			case 1:
				return DropChance.ALMOST_ALWAYS;
			case 2:
				return DropChance.COMMON;
			case 3:
				return DropChance.UNCOMMON;
			case 4:
				return DropChance.RARE;
			case 5:
				return DropChance.MORE_RARE;
			case 6:
				return DropChance.VERY_RARE;
			case 7:
				return DropChance.EXTREMELY_RARE;
			case 8:
				return DropChance.OMG_RARE;
			case 9:
				return DropChance.PET_RARE;
			case 10:
				return DropChance.PET_RARE1;
			case 11:
				return DropChance.VERY_COMMON;
			default:
				return DropChance.ALWAYS;
			}
		}

		/**
		 * Gets the item
		 * 
		 * @return the item
		 */
		public Item getItem() {
			int amount = 0;
			for (int i = 0; i < count.length; i++) {
				amount += count[i];
			}
			if (amount > count[0]) {
				amount = count[0] + Misc.getRandom(count[1]);
			}
			return new Item(id, amount);
		}
	}

	public enum DropChance {

		ALWAYS(0), // 0

		ALMOST_ALWAYS(4), // 1

		COMMON(10), // 2

		UNCOMMON(30), // 3

		VERY_COMMON(75), // 11

		RARE(128), // 4

		MORE_RARE(384), // 5

		VERY_RARE(512), // 6

		EXTREMELY_RARE(1365), // 7

		OMG_RARE(4095), // 8

		PET_RARE(2500), // 9

		PET_RARE1(5000); // 10

		DropChance(int randomModifier) {
			this.random = randomModifier;
		}

		private int random;

		public int getRandom() {
			return this.random;
		}
	}

	public static void checkMob(Player player, Npc mob) {
		if (mob != null) {
			if (WarriorsGuildConstants.isAnimatedArmour(mob.getId())) {
				ArmorAnimator.dropForAnimatedArmour(player, mob);
				return;
			}
			player.getAchievements().onKillMob(player, mob);
			CyclopsRoom.dropDefender(player, mob);
			player.getSlayer().checkForSlayer(mob);
			GodWars.onGodwarsKill(player, mob.getId());
			Barrows.onBarrowsDeath(player, mob);
			FightCave.checkForFightCave(player, mob);
		}
	}

	public static void test(Player player, int npcId) {
		NPCDrops drops = NPCDrops.forId(npcId);
		if (drops == null) {
			return;
		}
		final boolean wealth = player.getEquipment().isWearingItem(2572);
		boolean[] dropsReceived = new boolean[12];
		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0 || drops.getDropList()[i].getItem().getId() > 22694
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}
			final DropChance dropChance = drops.getDropList()[i].getChance();
			if (dropChance == DropChance.ALWAYS) {
				player.getBank().add(drops.getDropList()[i].getItem(), true);
			} else {
				if (shouldDrop(dropsReceived, dropChance, wealth)) {
					player.getBank().add(drops.getDropList()[i].getItem(), true);
					dropsReceived[dropChance.ordinal()] = true;
				}
			}
		}
		if (random.nextInt(100) <= 20) {
			if (Misc.randomNumber(100) > 50) {
				player.getBank().add(new Item(6759, 1));
			} else {
				player.getBank().add(new Item(2714, 1));
			}
		}
		int level = 80;
		if (random.nextInt(100) <= 35) {
			int[] charm = new int[level / 20 == 0 ? 1 : level / 20 > 4 ? 4 : level / 20];
			for (int i = 0; i < charm.length; i++) {
				if (i >= CHARMS.length) {
					break;
				}
				charm[i] = CHARMS[i];
			}
			int charmDrop = charm[random.nextInt(charm.length)];
			if (charmDrop > 0) {
				player.getBank().add(new Item(charmDrop, 1 + Misc.random(4)));
			}
		}
		if (random.nextInt(100) <= 10) {
			if (Misc.randomNumber(100) < 50) {
				player.getBank().add(new Item(985, 1));
			} else {
				player.getBank().add(new Item(987, 1));
			}
		}
	}

	public static void announce(Player player, Item item, Npc npc) {
		String itemName = item.getDefinition().getName();
		String npcName = Misc.formatPlayerName(npc.getDefinition().getName());
		switch (item.getId()) {
		case 14484:
			itemName = "a pair of Dragon Claws";
			break;
		case 4151:
			itemName = "an " + itemName;
			break;
		}
		switch (npc.getId()) {
		case 50:
		case 3200:
		case 8133:
		case 4540:
		case 1160:
		case 8549:
			npcName = "The " + npcName + "";
			break;
		case 51:
		case 54:
		case 5363:
		case 8349:
		case 1592:
		case 1591:
		case 1590:
		case 1615:
		case 9463:
		case 9465:
		case 9467:
		case 1382:
		case 13659:
		case 11235:
			npcName = "" + Misc.anOrA(npcName) + " " + npcName + "";
			break;
		}
		String message = "<col=009966>" + player.getUsername() + " has just received " + itemName + " from " + npcName
				+ "!";
		World.sendGlobalMessage(message, true);
		PlayerLogs.log(player.getUsername(),
				"" + player.getUsername() + " received " + itemName + " from " + npcName + "");
		PlayerLogs.log("SERVER", "" + player.getUsername() + " received " + itemName + " from " + npcName + "");
	}

	public static void dropItems(Player player, Npc npc) {
		NPCDrops drops = NPCDrops.forId(npc.getId());
		if (drops == null) {
			return;
		}
		final boolean goGlobal = player.getLocation().getZ() >= 0 && player.getLocation().getZ() < 4;
		final boolean ringOfWealth = player.getEquipment().isWearingItem(2572);
		final Location npcPos = npc.getLocation().copy();
		boolean[] dropsReceived = new boolean[12];
		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0 || drops.getDropList()[i].getItem().getId() > 22694
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}
			final DropChance dropChance = drops.getDropList()[i].getChance();
			if (dropChance == DropChance.ALWAYS) {
				drop(player, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal, false);
			} else {
				if (shouldDrop(dropsReceived, dropChance, ringOfWealth)) {
					if (dropChance == DropChance.EXTREMELY_RARE || dropChance == DropChance.MORE_RARE
							|| dropChance == DropChance.OMG_RARE || dropChance == DropChance.VERY_RARE) {
						if (ringOfWealth) {
							player.send(new SendMessage("<col=802DA0>Your ring of wealth shines bright."));
						}
						player.getAchievements().incr(player, "Obtain 50 rare drops");
						player.getAchievements().incr(player, "Obtain 250 rare drops");
						announce(player, drops.getDropList()[i].getItem(), npc);
					}
					drop(player, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal, true);
					dropsReceived[dropChance.ordinal()] = true;
				}
			}
		}
		roll_extras(player, npc, npcPos);
	}

	private static final SecureRandom random = new SecureRandom();

	public static boolean shouldDrop(boolean[] b, DropChance chance, boolean ringOfWealth) {
		int random = chance.getRandom();
		if (ringOfWealth && random >= 60) {
			random -= (random / 10);
		}
		return !b[chance.ordinal()] && Misc.getRandom(random) == 1;
	}

	public static void roll_extras(Player player, Npc npc, Location pos) {
		if (random.nextInt(100) <= 20) {
			if (random.nextInt(100) > 25) {
				GroundItemHandler.add(new Item(6759, 1), pos, player);
				player.send(new SendMessage("<col=0B610B>You have recieved a drop: "
						+ Misc.formatPlayerName(GameDefinitionLoader.getItemDef(6759).getName())));
			} else {
				GroundItemHandler.add(new Item(2714, 1), pos, player);
				player.send(new SendMessage("<col=0B610B>You have recieved a drop: "
						+ Misc.formatPlayerName(GameDefinitionLoader.getItemDef(2714).getName())));
			}
		}
		int level = npc.getDefinition().getLevel();
		if (random.nextInt(100) <= 35) {
			int[] charm = new int[level / 20 == 0 ? 1 : level / 20 > 4 ? 4 : level / 20];
			for (int i = 0; i < charm.length; i++) {
				if (i >= CHARMS.length) {
					break;
				}
				charm[i] = CHARMS[i];
			}
			int charmDrop = charm[random.nextInt(charm.length)];
			int amount = 1 + Misc.random(7);
			if (charmDrop > 0) {
				GroundItemHandler.add(new Item(charmDrop, amount), pos, player);
				player.send(new SendMessage("<col=0B610B>You have recieved a drop: " + amount + " x "
						+ Misc.formatPlayerName(GameDefinitionLoader.getItemDef(charmDrop).getName())));
			}
		}
		if (random.nextInt(100) <= 10) {
			if (Misc.randomNumber(100) > 50) {
				GroundItemHandler.add(new Item(985, 1), pos, player);
				player.send(new SendMessage("<col=0B610B>You have recieved a drop: "
						+ Misc.formatPlayerName(GameDefinitionLoader.getItemDef(985).getName())));
			} else {
				GroundItemHandler.add(new Item(987, 1), pos, player);
				player.send(new SendMessage("<col=0B610B>You have recieved a drop: "
						+ Misc.formatPlayerName(GameDefinitionLoader.getItemDef(987).getName())));
			}
		}
	}

	public static void drop(Player player, Item item, Npc npc, Location pos, boolean goGlobal, boolean announce) {
		if (player.getInventory().hasItemId(18337) && BoneBurying.forId(item.getId()) != null) {
			player.getAchievements().incr(player, "Bury 500 bones");
			player.getAchievements().incr(player, "Bury 5,000 bones");
			player.getSkill().addExperience(Skills.PRAYER, BoneBurying.forId(item.getId()).getXP() * 3);
			return;
		}
		if (!item.getDefinition().isStackable() && !item.getDefinition().isNote()) {
			for (int i = 0; i < item.getAmount(); i++) {
				if (item.getAmount() >= 1) {
					GroundItemHandler.add(new Item(item.getId(), 1), pos, player);
				}
			}
		} else {
			GroundItemHandler.add(item, pos, player);
		}
		player.send(new SendMessage("<col=0B610B>You have recieved a drop: " + item.getAmount() + " x "
				+ Misc.formatPlayerName(GameDefinitionLoader.getItemDef(item.getId()).getName())));
	}
}