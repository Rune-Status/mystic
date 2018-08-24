package org.mystic.game.model.content;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.Misc;

public enum ItemConstructing {

	ARMADYL_GODSWORD("Armadyl godsword", new Item[] { new Item(11694) },
			new Item[] { new Item(11690), new Item(11702) }, new int[][] {}),

	BANDOS_GODSWORD("Bandos godsword", new Item[] { new Item(11696) }, new Item[] { new Item(11690), new Item(11704) },
			new int[][] {}),

	GODSWORD_BLADE("Godsword blade", new Item[] { new Item(11690) },
			new Item[] { new Item(11710), new Item(11712), new Item(11714) }, new int[][] {}),

	SARADOMIN_GODSWORD("Saradomin godsword", new Item[] { new Item(11698) },
			new Item[] { new Item(11690), new Item(11706) }, new int[][] {}),

	ZAMORAK_GODSWORD("Zamorak godsword", new Item[] { new Item(11700) },
			new Item[] { new Item(11690), new Item(11708) }, new int[][] {}),

	DRAGONFIRE_SHIELD("Dragonfire shield", new Item[] { new Item(11283) },
			new Item[] { new Item(1540), new Item(11286) }, new int[][] { { Skills.SMITHING, 90 } }),

	BLESSED_SHIELD("Blessed spirit shield", new Item[] { new Item(13736) },
			new Item[] { new Item(13754), new Item(13734) }, new int[][] { { Skills.PRAYER, 85 } }),

	SPECTRAL_SHIELD("Spectral spirit shield", new Item[] { new Item(13744) },
			new Item[] { new Item(13736), new Item(13752) },
			new int[][] { { Skills.PRAYER, 90, Skills.SMITHING, 85 } }),

	ARCANE_SHIELD("Arcane spirit shield", new Item[] { new Item(13738) },
			new Item[] { new Item(13736), new Item(13746) },
			new int[][] { { Skills.PRAYER, 90, Skills.SMITHING, 85 } }),

	ELY_SHIELD("Elysian spirit shield", new Item[] { new Item(13742) }, new Item[] { new Item(13736), new Item(13750) },
			new int[][] { { Skills.PRAYER, 90, Skills.SMITHING, 85 } }),

	DIVINE_SHIELD("Divine spirit shield", new Item[] { new Item(13740) },
			new Item[] { new Item(13736), new Item(13748) },
			new int[][] { { Skills.PRAYER, 90, Skills.SMITHING, 85 } }),

	VINE_WHIP("Abyssal vine whip", new Item[] { new Item(21373) }, new Item[] { new Item(21369), new Item(4151) },
			new int[][] {}),

	YELLOW_WHIP("Abyssal whip (c)", new Item[] { new Item(15441) }, new Item[] { new Item(4151), new Item(1765) },
			new int[][] {}),

	BLUE_WHIP("Abyssal whip (c)", new Item[] { new Item(15442) }, new Item[] { new Item(4151), new Item(1767) },
			new int[][] {}),

	WHITE_WHIP("Abyssal whip (c)", new Item[] { new Item(15443) }, new Item[] { new Item(4151), new Item(1769) },
			new int[][] {}),

	GREEN_WHIP("Abyssal whip (c)", new Item[] { new Item(15444) }, new Item[] { new Item(4151), new Item(1771) },
			new int[][] {}),

	YELLOW_DBOW("Dark bow (c)", new Item[] { new Item(15701) }, new Item[] { new Item(11235), new Item(1765) },
			new int[][] {}),

	BLUE_DBOW("Dark bow (c)", new Item[] { new Item(15702) }, new Item[] { new Item(11235), new Item(1767) },
			new int[][] {}),

	WHITE_DBOW("Dark bow (c)", new Item[] { new Item(15703) }, new Item[] { new Item(11235), new Item(1769) },
			new int[][] {}),

	GREEN_DBOW("Dark bow (c)", new Item[] { new Item(15704) }, new Item[] { new Item(11234), new Item(1771) },
			new int[][] {}),

	YELLOW_SOL("Staff of light (c)", new Item[] { new Item(22207) }, new Item[] { new Item(15486), new Item(1765) },
			new int[][] {}),

	BLUE_SOL("Staff of light (c)", new Item[] { new Item(22209) }, new Item[] { new Item(15486), new Item(1767) },
			new int[][] {}),

	WHITE_SOL("Staff of light (c)", new Item[] { new Item(22211) }, new Item[] { new Item(15486), new Item(1769) },
			new int[][] {}),

	GREEN_SOL("Staff of light (c)", new Item[] { new Item(22213) }, new Item[] { new Item(15486), new Item(1771) },
			new int[][] {}),

	KEY_HALF("Crystal key", new Item[] { new Item(989) }, new Item[] { new Item(985), new Item(987) }, new int[][] {}),

	IMBUE_ZERKER("Berserker ring (i)", new Item[] { new Item(15220) }, new Item[] { new Item(6737), new Item(6949) },
			new int[][] {}),

	IMBUE_ARCHER("Archers' ring (i)", new Item[] { new Item(15019) }, new Item[] { new Item(6733), new Item(6949) },
			new int[][] {}),

	IMBUE_WARRIOR("Warrior ring (i)", new Item[] { new Item(15020) }, new Item[] { new Item(6735), new Item(6949) },
			new int[][] {}),

	FULL_SLAYER_HELMET("Full slayer helmet", new Item[] { new Item(15492) },
			new Item[] { new Item(13263), new Item(15488), new Item(15490) }, new int[][] {}),

	FIRE_MAX("Fire max cape", new Item[] { new Item(20095), new Item(20096) },
			new Item[] { new Item(6570), new Item(20747), new Item(20751) }, new int[][] {}),

	SARA_MAX("Saradomin max cape", new Item[] { new Item(20097), new Item(20098) },
			new Item[] { new Item(2412), new Item(20747), new Item(20751) }, new int[][] {}),

	ZAMMY_MAX("Zamorak max cape", new Item[] { new Item(20099), new Item(20100) },
			new Item[] { new Item(2414), new Item(20747), new Item(20751) }, new int[][] {}),

	GUTH_MAX("Guthix max cape", new Item[] { new Item(20101), new Item(20102) },
			new Item[] { new Item(2413), new Item(20747), new Item(20751) }, new int[][] {}),

	AVA_MAX("Ava's max cape", new Item[] { new Item(20103), new Item(20104) },
			new Item[] { new Item(10499), new Item(20747), new Item(20751) }, new int[][] {}),

	FURY_OR("Amulet of fury (or)", new Item[] { new Item(19335) }, new Item[] { new Item(19333), new Item(6585) },
			new int[][] {});

	private final String name;

	private final Item[] product;

	private final Item[] items;

	private final int[][] skills;

	private ItemConstructing(String name, Item[] product, Item[] items, int[][] skills) {
		this.name = name;
		this.product = product;
		this.items = items;
		this.skills = skills;
	}

	public String getName() {
		return name;
	}

	public Item[] getProduct() {
		return product;
	}

	public Item[] getItems() {
		return items;
	}

	public int[][] getSkills() {
		return skills;
	}

	public static ItemConstructing getDataForItems(int item1, int item2) {
		for (ItemConstructing data : ItemConstructing.values()) {
			int found = 0;
			for (Item it : data.items) {
				if (it.getId() == item1 || it.getId() == item2) {
					found++;
				}
			}
			if (found >= 2) {
				return data;
			}
		}
		return null;
	}

	public static boolean handle(Player player, int use, int with) {
		ItemConstructing data = ItemConstructing.getDataForItems(use, with);
		if (data == null || !player.getInventory().hasItemId(use) || !player.getInventory().hasItemId(with)) {
			return false;
		}
		if (use == with) {
			return false;
		}
		if (!player.getInventory().hasAllItems(data.getItems())) {
			player.send(new SendMessage("You do not have the correct items needed to do this."));
			return false;
		}
		for (int i = 0; i < data.getSkills().length; i++) {
			if (player.getSkill().getLevels()[data.getSkills()[i][0]] < data.getSkills()[i][1]) {
				DialogueManager.sendStatement(player, "You do not have the required level needed to do this.",
						"You need a " + Skills.SKILL_NAMES[(data.getSkills()[i][0])] + " level of "
								+ data.getSkills()[i][1] + ".");
				return true;
			}
		}
		if (player.getInventory().getFreeSlots() < data.product.length) {
			player.send(
					new SendMessage("You need at least " + data.product.length + " free inventory space to do this."));
			return false;
		}
		for (Item req : data.getItems()) {
			player.getInventory().remove(req);
		}
		for (Item product : data.getProduct()) {
			player.getInventory().add(product);
		}
		player.send(new SendMessage(
				"You have created " + Misc.getAOrAn(data.getName()) + " " + data.getName().toLowerCase() + "."));
		return true;
	}

}
