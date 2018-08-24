package org.mystic.game.model.content.skill.magic.spells;

import java.util.ArrayList;

import org.mystic.game.GameConstants;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.magic.Spell;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendOpenTab;
import org.mystic.utility.GameDefinitionLoader;

/**
 * Handles super heating items
 * 
 * @author Daniel
 *
 */
public class SuperHeat extends Spell {

	/**
	 * Super Heat data
	 * 
	 * @author Daniel
	 *
	 */
	public enum HeatData {

		BRONZE_BAR(2349, new int[][] { { 438, 1 }, { 436, 1 } }, 1, 16),

		BLURITE_BAR(9467, new int[][] { { 688, 1 } }, 8, 8),

		IRON_BAR(2351, new int[][] { { 440, 1 } }, 15, 12.5),

		SILVER_BAR(2355, new int[][] { { 442, 1 } }, 20, 13.75),

		STEEL_BAR(2353, new int[][] { { 440, 1 }, { 453, 2 } }, 30, 17.5),

		GOLD_BAR(2357, new int[][] { { 444, 1 } }, 40, 22.5),

		MITHRIL_BAR(2359, new int[][] { { 447, 1 }, { 453, 4 } }, 50, 30),

		ADAMANT_BAR(2361, new int[][] { { 449, 1 }, { 453, 6 } }, 70, 37.5),

		RUNITE_BAR(2363, new int[][] { { 451, 1 }, { 453, 8 } }, 85, 50);

		private final int productId;
		private final int[][] requiredItems;
		private final int levelRequired;
		private final double experience;

		private HeatData(int productId, int[][] requiredItems, int levelRequired, double experience) {
			this.productId = productId;
			this.requiredItems = requiredItems;
			this.levelRequired = levelRequired;
			this.experience = experience;
		}

		public int getProduct() {
			return productId;
		}

		public int[][] getRequired() {
			return requiredItems;
		}

		public int getLevel() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public static HeatData getItem(Player player, int item) {
			for (HeatData data : HeatData.values()) {
				for (int i = 0; i < data.getRequired().length; i++) {
					if (item == data.getRequired()[i][0])
						return data;
				}
			}
			player.getUpdateFlags().sendGraphic(Graphic.highGraphic(339, 0));
			player.send(new SendMessage("You can't superheat this."));
			return null;
		}

	}

	@Override
	public boolean execute(Player player) {
		if (player.getSkill().locked()) {
			return false;
		}
		if (player.getAttributes().get("magicitem") == null) {
			return false;
		}
		int item = player.getAttributes().getInt("magicitem");
		final HeatData data = HeatData.getItem(player, item);
		if (data == null) {
			return false;
		}
		ArrayList<String> required = new ArrayList<String>();
		for (int i = 0; i < data.getRequired().length; i++) {
			if (!player.getInventory().hasItemAmount(data.getRequired()[i][0], data.getRequired()[i][1])) {
				required.add(data.getRequired()[i][1] + "x "
						+ GameDefinitionLoader.getItemDef(data.getRequired()[i][0]).getName() + " ");
				continue;
			}
		}
		if (!required.isEmpty()) {
			player.send(new SendMessage("Super heating " + GameDefinitionLoader.getItemDef(data.getProduct()).getName()
					+ " requires: " + required + "."));
			return false;
		}
		if (player.getSkill().getLevels()[Skills.SMITHING] < data.getLevel()) {
			DialogueManager.sendStatement(player, "You need a smithing level of " + data.getLevel() + " to do this!");
			return false;
		}
		player.getInventory().remove(561, 1);
		player.getInventory().remove(554, 4);
		for (int i = 0; i < data.getRequired().length; i++) {
			player.getInventory().remove(data.getRequired()[i][0], data.getRequired()[i][1]);
		}
		player.getUpdateFlags().sendAnimation(new Animation(722));
		player.getSkill().lock(3);
		player.getSkill().addExperience(Skills.SMITHING, data.getExperience());
		player.getInventory().add(data.getProduct(), 1);
		player.send(new SendOpenTab(6));
		return true;
	}

	@Override
	public double getExperience() {
		return 53 * GameConstants.SKILL_RATE;
	}

	@Override
	public int getLevel() {
		return 43;
	}

	@Override
	public String getName() {
		return "super heat";
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(561, 1), new Item(554, 4) };
	}

}