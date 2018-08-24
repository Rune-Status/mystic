package org.mystic.game.model.content.skill.farming;

import java.util.Calendar;

import org.mystic.game.GameConstants;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.RunOnceTask;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class Plant {

	private final int patch;

	private final int plant;

	private int minute;

	private int hour;

	private int day;

	private int year;

	private byte stage = 0;

	private byte disease = -1;

	private byte watered = 0;

	private boolean dead = false;

	private byte harvested = 0;

	public Plant(int patchId, int plantId) {
		patch = patchId;
		plant = plantId;
	}

	public void click(Player player, int option) {
		if (option == 1) {
			if (dead) {
				player.getClient().queueOutgoingPacket(new SendMessage("It seems as if your plants have died."));
			} else if (isDiseased()) {
				player.getClient().queueOutgoingPacket(new SendMessage("Your plants look to be diseased."));
			} else if (stage == Plants.values()[plant].stages) {
				harvest(player);
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Your plants are healthy."));
			}
		} else if ((option == 2) && (stage == Plants.values()[plant].stages)) {
			player.getClient().queueOutgoingPacket(new SendMessage("Your plants are healthy and ready to harvest."));
		}
	}

	public void doConfig(Player player) {
		player.getFarming().doConfig();
	}

	public boolean doDisease() {
		return false;
	}

	public boolean doWater() {
		return false;
	}

	public int getConfig() {
		if ((Plants.values()[plant].type == SeedType.ALLOTMENT) && (stage == 0) && (isWatered())) {
			return (Plants.values()[plant].healthy + stage + 64) * FarmingPatches.values()[patch].mod;
		}
		return (Plants.values()[plant].healthy + stage) * FarmingPatches.values()[patch].mod;
	}

	public FarmingPatches getPatch() {
		return FarmingPatches.values()[patch];
	}

	public void harvest(final Player player) {
		if (player.getInventory().hasItemId(FarmingPatches.values()[patch].harvestItem)) {
			final Plant instance = this;
			TaskQueue.queue(new Task(player, 2, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE,
					TaskIdentifier.CURRENT_ACTION) {
				@Override
				public void execute() {
					player.getUpdateFlags().sendAnimation(FarmingPatches.values()[patch].harvestAnimation, 0);
					int id = Plants.values()[plant].harvest;
					final Item add = Item.getDefinition(id).isNote() ? new Item(Item.getDefinition(id).getNoteId(), 1)
							: new Item(id, 1);
					player.getAchievements().incr(player, "Harvest 500 crops");
					player.getInventory().addOrCreateGroundItem(add.getId(), add.getAmount(), true);
					String name = Item.getDefinition(Plants.values()[plant].harvest).getName();
					player.getClient().queueOutgoingPacket(
							new SendMessage("You harvest " + Misc.getAOrAn(name) + " " + name + "."));
					player.getSkill().addExperience(Skills.FARMING, Plants.values()[plant].harvestExperience);
					if (harvested++ > 4 && Misc.randomNumber(3) == 0) {
						player.getFarming().remove(instance);
						stop();
						return;
					}
				}

				@Override
				public void onStop() {
					player.getUpdateFlags().sendAnimation(65535, 0);
				}
			});
		} else {
			final String name = Item.getDefinition(FarmingPatches.values()[patch].harvestItem).getName();
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need " + Misc.getAOrAn(name) + " " + name + " to harvest these plants."));
		}
	}

	public boolean isDiseased() {
		return disease > -1;
	}

	public boolean isWatered() {
		return watered == -1;
	}

	public void process(Player player) {
		if (dead || stage >= Plants.values()[plant].stages) {
			return;
		}
		int elapsed = GameConstants.DEV_MODE ? 3 : Misc.getMinutesElapsed(minute, hour, day, year);
		int grow = GameConstants.DEV_MODE ? 1 : Plants.values()[plant].minutes;
		if (elapsed >= grow) {
			for (int i = 0; i < elapsed / grow; i++) {
				if (isDiseased()) {
					if (!doDisease())
						;
				} else if (!isWatered()) {
					if (!doWater())
						;
				} else {
					stage++;
					if (stage >= Plants.values()[plant].stages) {
						return;
					}
				}

			}
			setTime();
		}
	}

	public void setTime() {
		minute = Calendar.getInstance().get(12);
		hour = Calendar.getInstance().get(11);
		day = Calendar.getInstance().get(6);
		year = Calendar.getInstance().get(1);
	}

	public boolean useItemOnPlant(final Player player, int item) {
		if (item == 952) {
			player.getUpdateFlags().sendAnimation(830, 0);
			player.getFarming().remove(this);
			TaskQueue.queue(new RunOnceTask(player, 2) {
				@Override
				public void onStop() {
					player.getUpdateFlags().sendAnimation(65535, 0);
					player.getClient().queueOutgoingPacket(new SendMessage("You remove your plants from the plot."));
				}
			});
			return true;
		}
		if (item == 6036) {
			if (dead) {
				player.getClient().queueOutgoingPacket(new SendMessage("Your plant is dead!"));
			} else if (isDiseased()) {
				player.getClient().queueOutgoingPacket(new SendMessage("You cure the plant.."));
				player.getUpdateFlags().sendAnimation(2288, 0);
				player.getInventory().remove(6036);
				disease = -1;
				doConfig(player);
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Your plant does not need this."));
			}
			return true;
		}
		if ((item >= 5331) && (item <= 5340)) {
			water(player, item);
			return true;
		}
		return false;
	}

	public void water(Player player, int item) {
		if (item == 5332) {
			return;
		}
		if (isWatered()) {
			player.getClient().queueOutgoingPacket(new SendMessage("These plants have already been watered."));
			return;
		}
		if (item == 5331) {
			player.getClient().queueOutgoingPacket(new SendMessage("Your watering can is empty."));
			return;
		}
		player.getClient().queueOutgoingPacket(new SendMessage("You pour water onto the plant.."));
		player.getFarming().nextWateringCan(item);
		player.getUpdateFlags().sendAnimation(2293, 0);
		watered = -1;
		doConfig(player);
	}

}