package org.mystic.game.model.content.skill.fletching.fletchable;

import org.mystic.game.model.entity.item.Item;

public final class FletchableItem {

	private final Item product;

	private final int level;

	private final double experience;

	public FletchableItem(final Item product, final int level, final double experience) {
		this.product = product;
		this.level = level;
		this.experience = experience;
	}

	public Item getProduct() {
		return product;
	}

	public int getLevel() {
		return level;
	}

	public double getExperience() {
		return experience;
	}
}