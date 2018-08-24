package org.mystic.game.model.content.skill.crafting;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.impl.ProductionTask;

/**
 * Gem crafting is an action in which a player cuts an uncut gem into a precious
 * gem which can further be crafted in jewelry and from there, Enchanted.
 * 
 * 
 * @author Joshua Barry <Ares>
 * 
 */
public class GemCutting extends ProductionTask {

	/**
	 * How many gems we will be crafting.
	 */
	private final int productionCount;

	/**
	 * The gem we will be crafting.
	 */
	private final Gem gem;

	/**
	 * Initialize our data.
	 * 
	 * @param entity
	 *            The entity who is cutting gems.
	 * @param productionCount
	 * @param gem
	 */
	public GemCutting(Player entity, int productionCount, Gem gem) {
		super(entity, 0);
		this.productionCount = productionCount;
		this.gem = gem;
	}

	@Override
	public boolean canProduce() {
		return true;
	}

	@Override
	public int getCycleCount() {
		return 3;
	}

	@Override
	public Graphic getGraphic() {
		return null;
	}

	@Override
	public Animation getAnimation() {
		return new Animation(gem.getAnimation());
	}

	@Override
	public double getExperience() {
		return gem.getExperience();
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { new Item(gem.getUncutGem(), 1) };
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + Skills.SKILL_NAMES[getSkill()] + " level of " + getRequiredLevel() + " to cut that gem.";
	}

	@Override
	public int getProductionCount() {
		return productionCount;
	}

	@Override
	public int getRequiredLevel() {
		return gem.getRequiredLevel();
	}

	@Override
	public Item[] getRewards() {
		return new Item[] { new Item(gem.getCutGem(), 1) };
	}

	@Override
	public int getSkill() {
		return Skills.CRAFTING;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		return "You cut the " + Item.getDefinition(getRewards()[0].getId()).getName().toLowerCase() + ".";
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}

	@Override
	public void onStop() {

	}

}