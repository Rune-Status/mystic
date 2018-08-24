package org.mystic.game.model.content.skill.fletching.fletchable;

import org.mystic.game.model.entity.item.Item;

public interface Fletchable {

	public int getAnimation();

	public Item getUse();

	public Item getWith();

	public FletchableItem[] getFletchableItems();

	public Item[] getIngediants();

	public String getProductionMessage();

}