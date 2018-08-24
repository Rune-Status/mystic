package org.mystic.game.model.content.skill.crafting;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.impl.ProductionTask;

public class GlassMelting extends ProductionTask {

	private final int productionCount;

	private final Glass glass;

	public GlassMelting(final Player entity, int productionCount, final Glass glass) {
		super(entity, 0);
		this.productionCount = productionCount;
		this.glass = glass;
	}

	@Override
	public boolean canProduce() {
		return true;
	}

	@Override
	public Animation getAnimation() {
		return new Animation(899);
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { new Item(1783), new Item(glass.getMaterialId()) };
	}

	@Override
	public int getCycleCount() {
		return 2;
	}

	@Override
	public double getExperience() {
		return glass.getExperience();
	}

	@Override
	public Graphic getGraphic() {
		return null;
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + Skills.SKILL_NAMES[getSkill()] + " level of " + getRequiredLevel() + " to melt glass.";
	}

	@Override
	public int getProductionCount() {
		return productionCount;
	}

	@Override
	public int getRequiredLevel() {
		return glass.getRequiredLevel();
	}

	@Override
	public Item[] getRewards() {
		return new Item[] { new Item(glass.getRewardId()) };
	}

	@Override
	public int getSkill() {
		return 12;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		return "You heat the sand and soda ash in the furnace to make glass.";
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}

	@Override
	public void onStop() {

	}

}