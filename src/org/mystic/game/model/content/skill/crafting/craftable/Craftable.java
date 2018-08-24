package org.mystic.game.model.content.skill.crafting.craftable;

import org.mystic.game.model.entity.item.Item;

public interface Craftable {

	public String getName();

	public int getAnimation();

	public Item getUse();

	public Item getWith();

	public CraftableItem[] getCraftableItems();

	public Item[] getIngediants(int index);

	public String getProductionMessage();

}