package org.mystic.game.model.content;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public enum ItemDeconstructing {

	ARMADYL_GODSWORD("Armadyl godsword", new int[] { 11690, 11702 }, 11694, new int[][] {}),

	BANDOS_GODSWORD("Bandos godsword", new int[] { 11690, 11704 }, 11696, new int[][] {}),

	SARADOMIN_GODSWORD("Saradomin godsword", new int[] { 11690, 11706 }, 11698, new int[][] {}),

	ZAMORAK_GODSWORD("Zamorak godsword", new int[] { 11690, 11708 }, 11700, new int[][] {}),

	VINE_WHIP("Abyssal vine whip", new int[] { 21369, 4151 }, 21373, new int[][] {});

	private final String name;
	private final int[] product;
	private final int items;
	private final int[][] skills;

	private ItemDeconstructing(String name, int[] product, int items, int[][] skills) {
		this.name = name;
		this.product = product;
		this.items = items;
		this.skills = skills;
	}

	public String getName() {
		return name;
	}

	public int[] getProduct() {
		return product;
	}

	public int getItem() {
		return items;
	}

	public int[][] getSkills() {
		return skills;
	}

	public final static ImmutableSet<ItemDeconstructing> ITEMS = Sets
			.immutableEnumSet(EnumSet.allOf(ItemDeconstructing.class));

	public static Optional<ItemDeconstructing> get(Item item) {
		return ITEMS.stream().filter(Objects::nonNull).filter(i -> i.getItem() == item.getId()).findFirst();
	}

	public static boolean handle(Player player, Item item) {
		Optional<ItemDeconstructing> data = ItemDeconstructing.get(item);
		if (!data.isPresent() || data == null || !player.getInventory().hasItemId(item)) {
			return false;
		}
		if (player.getInventory().getFreeSlots() >= data.get().getProduct().length) {
			player.getInventory().remove(item);
			for (int product : data.get().getProduct()) {
				player.getInventory().add(product, 1);
			}
		} else {
			player.send(new SendMessage("You do not have enough free inventory space(s) to do this."));
			return true;
		}
		return true;
	}

}
