package org.mystic.game.model.content;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

/**
 * Represents food which can be consumed by the player to restore hitpoints.
 * 
 * @author Professor Oak
 */

public class Food {

	/**
	 * The {@link Animation} that will be played when consuming food.
	 */
	private static final Animation ANIMATION = new Animation(829);

	/**
	 * Handles the player eating said food type.
	 * 
	 * @param player
	 *            The player eating the consumable.
	 * @param item
	 *            The food item being consumed.
	 * @param slot
	 *            The slot of the food being eaten.
	 */
	public static boolean consume(Player player, int item, int slot) {
		Edible food = Edible.types.get(item);
		if (food == null) {
			return false;
		}
		// Check if we can drink in our current controller
		if (!player.getController().canEat(player)) {
			return true;
		}
		if (food == Edible.KARAMBWAN) {
			if (!player.getKarambwanTimer().elapsed(600)) {
				return true;
			}
			player.getKarambwanTimer().reset();
		} else {
			if (!player.getFoodTimer().elapsed(1800)) {
				return true;
			}
			if (!player.getPotionTimer().elapsed(1800)) {
				return true;
			}
			player.getFoodTimer().reset();
		}

		// First reset combat-related tasks.
		TaskQueue.onMovement(player);
		if (player.getInterfaceManager().hasInterfaceOpen()) {
			player.send(new SendRemoveInterfaces());
		}
		player.getCombat().reset();
		player.getCombat().setAttackTimer(player.getCombat().getAttackTimer() + 2);

		SoundPlayer.play(player, Sounds.EAT_FOOD);

		// Start animation..
		player.getUpdateFlags().sendAnimation(ANIMATION);

		// Delete food from inventory..
		player.getInventory().clear(slot);
		player.getInventory().update();

		// Heal the player..
		int heal = food.heal;
		int current = player.getSkill().getLevels()[Skills.HITPOINTS];
		int max = player.getMaxLevels()[Skills.HITPOINTS];
		if (heal + current > max) {
			heal = max - current;
			if (heal < 0) {
				heal = 0;
			}
		}

		// Send message to player..
		String e = food == Edible.BANDAGES ? "use" : "eat";
		player.send(new SendMessage("You " + e + " the " + food.name + "."));

		// Increment the players hitpoints by the heal amount
		player.getSkill().setLevel(Skills.HITPOINTS, current + heal);

		if (heal > 0) {
			player.send(new SendMessage("It heals some health."));
		}

		// Handle cake slices..
		if (food == Edible.CAKE || food == Edible.SECOND_CAKE_SLICE || food == Edible.CHOCOLATE_CAKE
				|| food == Edible.SECOND_CHOCOLATE_CAKE_SLICE) {
			player.getInventory().add(new Item(food.item.getId() + 2, 1));
		}
		return true;
	}

	/**
	 * Represents all types of food currently available.
	 * 
	 * @author relex lawl
	 */
	public enum Edible {

		ONION(new Item(1957), 1),

		KEBAB(new Item(1971), 4),

		CHEESE(new Item(1985), 4),

		CAKE(new Item(1891), 5),

		CHOCOLATE_CAKE(new Item(1897), 5),

		SECOND_CHOCOLATE_CAKE_SLICE(new Item(1899), 5),

		THIRD_CHOCOLATE_CAKE_SLICE(new Item(1901), 5),

		SECOND_CAKE_SLICE(new Item(1893), 5),

		THIRD_CAKE_SLICE(new Item(1895), 5),

		BANDAGES(new Item(14640), 12),

		JANGERBERRIES(new Item(247), 2),

		HERRING(new Item(347), 5),

		WORM_CRUNCHIES(new Item(2205), 7),

		EDIBLE_SEAWEED(new Item(403), 4),

		ANCHOVIES(new Item(319), 1),

		SHRIMPS(new Item(315), 3),

		SARDINE(new Item(325), 4),

		COD(new Item(339), 7),

		TROUT(new Item(333), 7),

		PIKE(new Item(351), 8),

		SALMON(new Item(329), 9),

		TUNA(new Item(361), 10),

		LOBSTER(new Item(379), 12),

		BASS(new Item(365), 13),

		ROCKTAIL(new Item(15272), 23),

		SWORDFISH(new Item(373), 14),

		MEAT_PIZZA(new Item(2293), 14),

		MONKFISH(new Item(7946), 16),

		SHARK(new Item(385), 20),

		SEA_TURTLE(new Item(397), 21),

		MANTA_RAY(new Item(391), 22),

		KARAMBWAN(new Item(3144), 18),

		/*
		 * Baked goods food types a player can make with the cooking skill.
		 */
		POTATO(new Item(1942), 1),

		BAKED_POTATO(new Item(6701), 4),

		POTATO_WITH_BUTTER(new Item(6703), 14),

		CHILLI_POTATO(new Item(7054), 14),

		EGG_POTATO(new Item(7056), 16),

		POTATO_WITH_CHEESE(new Item(6705), 16),

		MUSHROOM_POTATO(new Item(7058), 20),

		TUNA_POTATO(new Item(7060), 20),

		/*
		 * Fruit food types which a player can get by picking from certain trees or
		 * hand-making them (such as pineapple chunks/rings).
		 */
		SPINACH_ROLL(new Item(1969), 2),

		BANANA(new Item(1963), 2),

		BANANA_(new Item(18199), 2),

		CABBAGE(new Item(1965), 2),

		ORANGE(new Item(2108), 2),

		PINEAPPLE_CHUNKS(new Item(2116), 2),

		PINEAPPLE_RINGS(new Item(2118), 2),

		PEACH(new Item(6883), 8),

		/*
		 * Other food types.
		 */
		PURPLE_SWEETS(new Item(10476), Misc.randomNumber(3));

		private Edible(final Item item, final int heal) {
			this.item = item;
			this.heal = heal;
			this.name = (toString().toLowerCase().replaceAll("__", "-").replaceAll("_", " "));
		}

		private final Item item;

		private final int heal;

		private final String name;

		public final int getHeal() {
			return heal;
		}

		static Map<Integer, Edible> types = new HashMap<Integer, Edible>();

		static {
			for (Edible type : Edible.values()) {
				types.put(type.item.getId(), type);
			}
		}
	}
}