package org.mystic.game.model.content;

import java.util.Optional;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.AntifireTask;

/**
 * The enumerated type managing consumable potion types.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author lare96 <http://github.com/lare96>
 * @editor Professor oak
 */
public enum Potion {
	ANTIFIRE_POTIONS(2452, 2454, 2456, 2458) {
		@Override
		public void onEffect(Player player) {
			Potion.onAntifireEffect(player, false);
		}
	},
	ANTIPOISON_POTIONS(2446, 175, 177, 179, 2448, 181, 183, 185) {
		@Override
		public void onEffect(Player player) {
			Potion.onAntipoisonEffect(player, 60 * 6);
		}
	},
	COMBAT_POTIONS(9739, 9741, 9743, 9745) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.ATTACK, BoostType.LOW);
			Potion.onBasicEffect(player, Skills.STRENGTH, BoostType.LOW);
		}
	},
	SUPER_COMBAT_POTIONS(20087, 20089, 20091, 20093) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.ATTACK, BoostType.SUPER);
			Potion.onBasicEffect(player, Skills.STRENGTH, BoostType.SUPER);
			Potion.onBasicEffect(player, Skills.DEFENCE, BoostType.SUPER);
		}
	},
	MAGIC_POTIONS(3040, 3042, 3044, 3046) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.MAGIC, BoostType.NORMAL);
		}
	},
	SUPER_MAGIC_POTIONS(11726, 11727, 11728, 11729) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.MAGIC, BoostType.SUPER);
		}
	},
	DEFENCE_POTIONS(2432, 133, 135, 137) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.DEFENCE, BoostType.NORMAL);
		}
	},
	STRENGTH_POTIONS(113, 115, 117, 119) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.STRENGTH, BoostType.NORMAL);
		}
	},
	ATTACK_POTIONS(2428, 121, 123, 125) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.ATTACK, BoostType.NORMAL);
		}
	},
	SUPER_DEFENCE_POTIONS(2442, 163, 165, 167, 11497, 11499) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.DEFENCE, BoostType.SUPER);
		}
	},
	SUPER_ATTACK_POTIONS(2436, 145, 147, 149) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.ATTACK, BoostType.SUPER);
		}
	},
	SUPER_STRENGTH_POTIONS(2440, 157, 159, 161) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.STRENGTH, BoostType.SUPER);
		}
	},
	RANGE_POTIONS(2444, 169, 171, 173) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.RANGED, BoostType.NORMAL);
		}
	},
	SUPER_RANGE_POTIONS(11722, 11723, 11724, 11725) {
		@Override
		public void onEffect(Player player) {
			Potion.onBasicEffect(player, Skills.RANGED, BoostType.SUPER);
		}
	},
	ZAMORAK_BREW(2450, 189, 191, 193) {
		@Override
		public void onEffect(Player player) {
			Potion.onZamorakEffect(player);
		}
	},
	SARADOMIN_BREW(6685, 6687, 6689, 6691) {
		@Override
		public void onEffect(Player player) {
			Potion.onSaradominEffect(player);
		}
	},
	SUPER_RESTORE_POTIONS(3024, 3026, 3028, 3030) {
		@Override
		public void onEffect(Player player) {
			Potion.onPrayerEffect(player, true);
			Potion.onRestoreEffect(player);
		}
	},
	ENERGY_POTIONS(3008, 3010, 3012, 3014) {
		@Override
		public void onEffect(Player player) {
			Potion.onEnergyEffect(player, false);
		}
	},
	SUP_ENERGY_POTIONS(3016, 3018, 3020, 3022, 11481, 11483) {
		@Override
		public void onEffect(Player player) {
			Potion.onEnergyEffect(player, true);
		}
	},
	PRAYER_POTIONS(2434, 139, 141, 143) {
		@Override
		public void onEffect(Player player) {
			Potion.onPrayerEffect(player, false);
		}
	};

	/**
	 * The {@link Animation} that will be played when consuming food.
	 */
	private static final Animation ANIMATION = new Animation(829);

	/**
	 * The default item representing the final potion dose.
	 */
	private static final int VIAL = 229;

	/**
	 * The identifiers which represent this potion type.
	 */
	private final int[] ids;

	/**
	 * Create a new {@link PotionConsumable}.
	 *
	 * @param ids
	 *            the identifiers which represent this potion type.
	 */
	private Potion(int... ids) {
		this.ids = ids;
	}

	/**
	 * Attempts to consume {@code item} in {@code slot} for {@code player}.
	 *
	 * @param player
	 *            the player attempting to consume the item.
	 * @param item
	 *            the item being consumed by the player.
	 * @param slot
	 *            the slot the player is consuming from.
	 * @return {@code true} if the item was consumed, {@code false} otherwise.
	 */
	public static boolean drink(Player player, int item, int slot) {
		Optional<Potion> potion = forId(item);
		if (!potion.isPresent()) {
			return false;
		}
		// Check if we can drink in our current controller
		if (!player.getController().canDrink(player)) {
			return true;
		}
		if (player.getInterfaceManager().hasInterfaceOpen()) {
			player.send(new SendRemoveInterfaces());
		}
		if (player.getPotionTimer().elapsed(1800)) {
			player.getCombat().reset();
			TaskQueue.onMovement(player);
			player.getUpdateFlags().sendAnimation(ANIMATION);
			player.getPotionTimer().reset();
			player.getInventory().setId(slot, getReplacementItem(item).getId());
			player.getInventory().update();
			player.send(new SendMessage("You drink some of your " + Item.getDefinition(item).getName() + "."));
			if (getReplacementItem(item).getId() == VIAL) {
				player.send(new SendMessage("You have finished your potion."));
			}
			potion.get().onEffect(player);
			SoundPlayer.play(player, Sounds.DRINK_POTION);
		}
		return true;
	}

	public static boolean empty(Player player, int item, int slot) {
		Optional<Potion> potion = forId(item);
		if (!potion.isPresent()) {
			return false;
		}
		if (!player.getController().canDrink(player)) {
			return true;
		}
		if (player.getInterfaceManager().hasInterfaceOpen()) {
			player.send(new SendRemoveInterfaces());
		}
		player.send(new SendMessage("You pour out the liquid."));
		player.getInventory().setId(slot, VIAL);
		player.getInventory().update();
		return true;
	}

	/**
	 * Retrieves the replacement item for {@code item}.
	 *
	 * @param item
	 *            the item to retrieve the replacement item for.
	 * @return the replacement item wrapped in an optional, or an empty optional if
	 *         no replacement item is available.
	 */
	private static Item getReplacementItem(int item) {
		Optional<Potion> potion = forId(item);
		if (potion.isPresent()) {
			int length = potion.get().getIds().length;
			for (int index = 0; index < length; index++) {
				if (potion.get().getIds()[index] == item && index + 1 < length) {
					return new Item(potion.get().getIds()[index + 1]);
				}
			}
		}
		return new Item(VIAL);
	}

	/**
	 * Retrieves the potion consumable element for {@code id}.
	 *
	 * @param id
	 *            the id that the potion consumable is attached to.
	 * @return the potion consumable wrapped in an optional, or an empty optional if
	 *         no potion consumable was found.
	 */
	private static Optional<Potion> forId(int id) {
		for (Potion potion : Potion.values()) {
			for (int potionId : potion.getIds()) {
				if (id == potionId) {
					return Optional.of(potion);
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * The method executed when this potion type activated.
	 *
	 * @param player
	 *            the player to execute this effect for.
	 */
	public abstract void onEffect(Player player);

	/**
	 * Gets the identifiers which represent this potion type.
	 *
	 * @return the identifiers for this potion.
	 */
	public final int[] getIds() {
		return ids;
	}

	/**
	 * The method that executes the Saradomin brew action.
	 *
	 * @param player
	 *            the player to do this action for.
	 */
	private static void onSaradominEffect(Player player) {

		player.getSkill().increaseCurrentLevelMax(Skills.DEFENCE,
				(int) Math.floor(2 + (0.120 * player.getMaxLevels()[Skills.DEFENCE])));

		player.getSkill().increaseCurrentLevelMax(Skills.HITPOINTS,
				(int) Math.floor(2 + (0.15 * player.getMaxLevels()[Skills.HITPOINTS])));

		player.getSkill().decreaseCurrentLevel(Skills.ATTACK,
				(int) Math.floor(0.10 * player.getMaxLevels()[Skills.ATTACK]), -1);

		player.getSkill().decreaseCurrentLevel(Skills.STRENGTH,
				(int) Math.floor(0.10 * player.getSkill().getLevels()[Skills.STRENGTH]), -1);

		player.getSkill().decreaseCurrentLevel(Skills.MAGIC,
				(int) Math.floor(0.10 * player.getSkill().getLevels()[Skills.MAGIC]), -1);

		player.getSkill().decreaseCurrentLevel(Skills.RANGED,
				(int) Math.floor(0.10 * player.getSkill().getLevels()[Skills.RANGED]), -1);
	}

	/**
	 * The method that executes the Zamorak brew action.
	 *
	 * @param player
	 *            the player to do this action for.
	 */
	private static void onZamorakEffect(Player player) {

		player.getSkill().increaseCurrentLevelMax(Skills.ATTACK,
				(int) Math.floor(2 + (0.20 * player.getMaxLevels()[Skills.ATTACK])));

		player.getSkill().increaseCurrentLevelMax(Skills.STRENGTH,
				(int) Math.floor(2 + (0.12 * player.getMaxLevels()[Skills.STRENGTH])));

		player.getSkill().decreaseCurrentLevel(Skills.DEFENCE,
				(int) Math.floor(2 + (0.10 * player.getMaxLevels()[Skills.DEFENCE])), -1);

		player.getSkill().decreaseCurrentLevel(Skills.HITPOINTS,
				(int) Math.floor(2 + (0.10 * player.getSkill().getLevels()[Skills.HITPOINTS])), 1);

		player.getSkill().increaseCurrentLevel(Skills.PRAYER,
				(int) Math.floor(0.10 * player.getMaxLevels()[Skills.PRAYER]), player.getMaxLevels()[Skills.PRAYER]);

	}

	/**
	 * The method that executes the prayer potion action.
	 *
	 * @param player
	 *            the player to do this action for.
	 * @param restorePotion
	 *            {@code true} if this potion is a restore potion, {@code false}
	 *            otherwise.
	 */
	private static void onEnergyEffect(Player player, boolean sup) {
		player.getRunEnergy().add(sup ? 20 : 10);
	}

	/**
	 * The method that executes the prayer potion action.
	 *
	 * @param player
	 *            the player to do this action for.
	 * @param restorePotion
	 *            {@code true} if this potion is a restore potion, {@code false}
	 *            otherwise.
	 */
	private static void onPrayerEffect(Player player, boolean restorePotion) {
		int maxLevel = player.getMaxLevels()[Skills.PRAYER];
		double min = (int) Math.floor((restorePotion ? 8 : 7) + (maxLevel / 4));
		player.getSkill().increaseCurrentLevel(Skills.PRAYER, (int) min, maxLevel);
	}

	/**
	 * The method that executes the restore potion action.
	 *
	 * @param player
	 *            the player to do this action for.
	 */
	private static void onRestoreEffect(Player player) {
		for (int index = 0; index <= 6; index++) {
			int skill = index;
			if ((skill == Skills.PRAYER) || (skill == Skills.HITPOINTS)) {
				continue;
			}
			int maxLevel = player.getMaxLevels()[skill];
			int currLevel = player.getSkill().getLevels()[skill];
			if (currLevel < maxLevel) {
				player.getSkill().increaseCurrentLevel(skill, (int) Math.floor(8 + (maxLevel / 4)), maxLevel);
			}
		}
	}

	/**
	 * The method that executes the basic effect potion action that will increment
	 * the level of {@code skill}.
	 *
	 * @param player
	 *            the player to do this action for.
	 */
	private static void onBasicEffect(Player player, int skill, BoostType type) {
		int maxLevel = player.getMaxLevels()[skill];
		int boostLevel = Math.round(maxLevel * type.getAmount());
		if (type == BoostType.LOW) {
			boostLevel += 3;
		}
		int cap = maxLevel + boostLevel;
		if (maxLevel + boostLevel > player.getSkill().getLevels()[skill]) {
			player.getSkill().increaseCurrentLevel(skill, boostLevel, cap);
		}
	}

	/**
	 * The method that executes the anti-fire potion action.
	 *
	 * @param player
	 *            the player to do this action for.
	 */
	private static void onAntifireEffect(Player player, boolean isSuper) {
		TaskQueue.queue(new AntifireTask(player, true));
	}

	/**
	 * The method that executes the anti-poison potion action.
	 *
	 * @param player
	 *            the player to do this action for.
	 */
	private static void onAntipoisonEffect(Player player, int seconds) {
		player.curePoison(30);
		player.send(new SendMessage("Your poison has been cured."));
	}

	/**
	 * The enumerated type whose elements represent the boost types for potions.
	 *
	 * @author Ryley Kimmel <ryley.kimmel@live.com>
	 * @author lare96 <http://github.com/lare96>
	 */
	private enum BoostType {

		LOW(.10F),

		NORMAL(.13F),

		SUPER(.19F);

		/**
		 * The amount this type will boost by.
		 */
		private final float amount;

		/**
		 * Creates a new {@link BoostType}.
		 *
		 * @param boostAmount
		 *            the amount this type will boost by.
		 */
		private BoostType(float boostAmount) {
			this.amount = boostAmount;
		}

		/**
		 * Gets the amount this type will boost by.
		 *
		 * @return the boost amount.
		 */
		public final float getAmount() {
			return amount;
		}
	}
}