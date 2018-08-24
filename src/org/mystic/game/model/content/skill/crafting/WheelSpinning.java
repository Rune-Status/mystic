package org.mystic.game.model.content.skill.crafting;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.impl.ProductionTask;
import org.mystic.utility.GameDefinitionLoader;

public class WheelSpinning extends ProductionTask {

	private final Spinnable spinnable;
	private final int productionCount;

	public WheelSpinning(final Player entity, int productionCount, final Spinnable spin) {
		super(entity, 0);
		this.productionCount = productionCount;
		this.spinnable = spin;
	}

	@Override
	public boolean canProduce() {
		return true;
	}

	@Override
	public Animation getAnimation() {
		return new Animation(896);
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { spinnable.getItem() };
	}

	@Override
	public int getCycleCount() {
		return 2;
	}

	@Override
	public double getExperience() {
		return spinnable.getExperience();
	}

	@Override
	public Graphic getGraphic() {
		return null;
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + Skills.SKILL_NAMES[getSkill()] + " level of " + getRequiredLevel() + " to spin "
				+ GameDefinitionLoader.getItemDef(spinnable.getOutcome().getId()).getName().toLowerCase() + ".";
	}

	@Override
	public int getProductionCount() {
		return productionCount;
	}

	@Override
	public int getRequiredLevel() {
		return spinnable.getRequiredLevel();
	}

	@Override
	public Item[] getRewards() {
		return new Item[] { spinnable.getOutcome() };
	}

	@Override
	public int getSkill() {
		return 12;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		return "You spin the " + GameDefinitionLoader.getItemDef(getConsumedItems()[0].getId()).getName().toLowerCase()
				+ " into a " + GameDefinitionLoader.getItemDef(getRewards()[0].getId()).getName().toLowerCase() + ".";
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}

	@Override
	public void onStop() {
	}
}
