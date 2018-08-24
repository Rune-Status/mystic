package org.mystic.game.model.content.skill.runecrafting;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.impl.ProductionTask;

public class RunecraftingTask extends ProductionTask {

	private RunecraftingData data;

	private Player player;

	private int amount, essence;

	public RunecraftingTask(final Player player, final RunecraftingData data) {
		super(player, 1);
		this.player = player;
		this.data = data;
		this.essence = 7936;
		this.amount = player.getInventory().getItemAmount(essence);
	}

	public int getEssence() {
		return essence;
	}

	public int getEssenceAmount() {
		return amount;
	}

	@Override
	public boolean canProduce() {
		return 0 < getEssenceAmount();
	}

	@Override
	public int getCycleCount() {
		return 4;
	}

	@Override
	public Graphic getGraphic() {
		return new Graphic(186, 0, true);
	}

	@Override
	public Animation getAnimation() {
		return new Animation(791);
	}

	@Override
	public double getExperience() {
		return data.getXp() * amount;
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { new Item(getEssence(), getEssenceAmount()) };
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + Skills.SKILL_NAMES[Skills.RUNECRAFTING] + " level of " + data.getLevel()
				+ " to craft runes here.";
	}

	@Override
	public int getProductionCount() {
		return 1;
	}

	@Override
	public int getRequiredLevel() {
		return data.getLevel();
	}

	@Override
	public Item[] getRewards() {
		return new Item[] { new Item(data.getRuneId(), amount * getMultiplier()) };
	}

	@Override
	public int getSkill() {
		return Skills.RUNECRAFTING;
	}

	private int getMultiplier() {
		int multiplier = 1;
		for (int i = 1; i < data.getMultiplier().length; i++) {
			if (player.getSkill().getLevels()[Skills.RUNECRAFTING] >= data.getMultiplier()[i]) {
				multiplier = i;
			}
		}
		return multiplier;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		String itemName = getRewards()[0].getDefinition().getName().toLowerCase();
		String suffix = (getEssenceAmount() == 1) ? "." : "s.";
		player.setRunecraftingPoints(getEssenceAmount() * 2);
		return "You bind the temple's power into " + itemName + suffix;
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}

	@Override
	public void onStop() {
		QuestTab.update(player);
	}
}