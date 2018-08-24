package org.mystic.game.model.content.skill.crafting;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.impl.ProductionTask;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class GlassBlowing extends ProductionTask {

	private final int productionCount;

	private final Glass glass;

	public GlassBlowing(final Player entity, int productionCount, final Glass glass) {
		super(entity, 1);
		this.productionCount = productionCount;
		this.glass = glass;
	}

	@Override
	public boolean canProduce() {
		return true;
	}

	@Override
	public Animation getAnimation() {
		return new Animation(884);
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { new Item(glass.getMaterialId()) };
	}

	@Override
	public int getCycleCount() {
		return 3;
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
		return "You need a " + Skills.SKILL_NAMES[getSkill()] + " level of " + getRequiredLevel()
				+ " to blow this glass.";
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
		return Skills.CRAFTING;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		String itemName = GameDefinitionLoader.getItemDef(getRewards()[0].getId()).getName().toLowerCase();
		return "You make " + (Misc.startsWithVowel(itemName) ? "an" : "a") + " " + itemName + ".";
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}

	@Override
	public void onStop() {

	}
}
