package org.mystic.game.model.content.skill.crafting.craftable;

import org.mystic.game.model.entity.item.Item;

public final class CraftableItem {

	private final Item product;
	private final Item requiredItem;
	private final int level;
	private final double experience;

	public CraftableItem(final Item product, final Item requiredItem, final int level, final double experience) {
		this.product = product;
		this.requiredItem = requiredItem;
		this.level = level;
		this.experience = experience;
	}

	public Item getProduct() {
		return product;
	}

	public Item getRequiredItem() {
		return requiredItem;
	}

	public int getLevel() {
		return level;
	}

	public double getExperience() {
		return experience;
	}
}