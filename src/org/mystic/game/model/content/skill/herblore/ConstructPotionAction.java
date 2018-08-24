package org.mystic.game.model.content.skill.herblore;

import java.util.Optional;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.impl.ProductionTask;

/**
 * The Action of constructing a potion, either finished or unfinished.
 * 
 * @author Vl1 - http:://www.rune-server.org/members/Valii
 * @since March 10th, 2017.
 */
public class ConstructPotionAction extends ProductionTask {

	private final PotionType type;

	private final Optional<FinishedPotionData> finished;

	private final Optional<UnfinishedPotionData> unfinished;

	private int productionCount;

	public static final Item VIAL_OF_WATER = new Item(227);

	public ConstructPotionAction(Player player, PotionType type, Optional<FinishedPotionData> finished,
			Optional<UnfinishedPotionData> unfinished, int productionCount) {
		super(player, 1);
		this.type = type;
		this.finished = finished;
		this.unfinished = unfinished;
		this.productionCount = productionCount;
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
		return new Animation(363);
	}

	@Override
	public double getExperience() {
		switch (type) {
		case FINISHED:
			return finished.get().getExperience();
		case UNFINISHED:
			return 0;
		}
		return 0;
	}

	@Override
	public Item[] getConsumedItems() {
		switch (type) {
		case FINISHED:
			return new Item[] { (finished.get().getIngredient()), (finished.get().getUnfinishedPotion()) };
		case UNFINISHED:
			return new Item[] { (unfinished.get().getIngredient()), ((VIAL_OF_WATER)) };
		}
		return null;
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + Skills.SKILL_NAMES[Skills.HERBLORE] + " level of " + getRequiredLevel()
				+ " to make this potion.";
	}

	@Override
	public int getProductionCount() {
		return productionCount;
	}

	@Override
	public int getRequiredLevel() {
		return type == PotionType.FINISHED ? finished.get().getRequirement() : unfinished.get().getRequirement();
	}

	@Override
	public Item[] getRewards() {
		switch (type) {
		case FINISHED:
			return new Item[] { (finished.get().getFinishedPotion()) };
		case UNFINISHED:
			return new Item[] { (unfinished.get().getUnfinishedPotion()) };
		}
		return null;
	}

	@Override
	public int getSkill() {
		return Skills.HERBLORE;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		switch (type) {
		case FINISHED:
			return "You mix the " + Item.getDefinition(finished.get().getIngredient().getId()).getName().toLowerCase()
					+ " into your potion.";
		case UNFINISHED:
			return "You put the " + Item.getDefinition(unfinished.get().getIngredient().getId()).getName().toLowerCase()
					+ " into the vial of water.";
		}
		player.getAchievements().incr(player, "Create 500 potions");
		return "";
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}

	@Override
	public void onStop() {

	}

}