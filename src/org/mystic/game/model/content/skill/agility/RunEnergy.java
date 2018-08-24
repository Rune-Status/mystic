package org.mystic.game.model.content.skill.agility;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendUpdateEnergy;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class RunEnergy {

	private double energy = 100.0D;
	private boolean allowed = true;
	private boolean running = false;
	private boolean resting = false;
	private final Player player;

	public RunEnergy(Player player) {
		this.player = player;
	}

	/**
	 * Adds energy to the players current energy
	 * 
	 * @param amount
	 *            The amount to add
	 */
	public void add(int amount) {
		energy += amount;
		if (energy > 100.0D) {
			energy = 100.0D;
		}
		update();
	}

	/**
	 * Gets if a player can run
	 * 
	 * @return
	 */
	public boolean canRun() {
		return energy > 0.0D;
	}

	/**
	 * Deducts energy from the player by percentage
	 * 
	 * @param percent
	 *            The percentage to remove
	 */
	public void deduct(double percent) {
		energy -= (int) (energy * percent);
		if (energy < 0.0D) {
			energy = 0.0D;
		}
		update();
	}

	/**
	 * Deducts energy from the players energy
	 * 
	 * @param amount
	 *            The amount to deduct
	 */
	public void deduct(int amount) {
		energy -= amount;
		if (energy < 0.0D) {
			energy = 0.0D;
		}
		update();
	}

	public int getEnergy() {
		return (int) energy;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public boolean isResting() {
		return resting;
	}

	public boolean isRunning() {
		return running;
	}

	/**
	 * Handles what happens when a player is running
	 */
	public void onRun() {
		// if (player.getMaxLevels()[Skills.AGILITY] == 99) {
		// return;
		// }
		//
		// energy -= (1.0D - player.getMaxLevels()[Skills.AGILITY] * 0.005D) *
		// (player.isGoldMember() ? 0.5D : 1.0D)
		// * (player.getEquipment().getItems()[EquipmentConstants.BOOTS_SLOT] != null
		// && player.getEquipment().getItems()[EquipmentConstants.BOOTS_SLOT].getId() ==
		// 88 ? 0.8 : 1.0);
		//
		// update();
		//
		// allowed = false;
		//
		// if (energy <= 0) {
		// running = false;
		// player.getClient().queueOutgoingPacket(new SendConfig(149,
		// player.getRunEnergy().isRunning() ? 1 : 0));
		// player.getClient().queueOutgoingPacket(new SendConfig(429,
		// player.getRunEnergy().isRunning() ? 1 : 0));
		// player.getClient().queueOutgoingPacket(new SendConfig(173, 0));
		// player.getClient().queueOutgoingPacket(new SendMessage("You have run out of
		// energy."));
		// }

	}

	/**
	 * Resets the player running
	 */
	public void reset() {
		running = false;
		allowed = true;
	}

	/**
	 * Restores the players energy
	 */
	public void restoreAll() {
		energy = 100.0D;
		update();
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Handles the energy tick as well as resting
	 */
	public void tick() {
		TaskQueue.queue(new Task(player, 3, false, Task.StackType.NEVER_STACK, Task.BreakType.NEVER,
				TaskIdentifier.RUN_ENERGY) {
			@Override
			public void execute() {
				if (allowed) {
					final RunEnergy en = RunEnergy.this;
					if (resting) {
						en.energy += Misc.randomNumber(15);
					} else {
						en.energy += player.isGoldMember() ? 2 : 1;
					}
					if (energy > 100.0D) {
						energy = 100.0D;
					}
					update();
				} else if (!allowed) {
					allowed = true;
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	public void toggleResting(boolean toggle) {
		if (toggle && resting) {
			return;
		}
		resting = toggle;
		if (!resting) {
			player.getUpdateFlags().sendAnimation(11788, 0);
			player.getEquipment().updatePlayerAnimations();
		} else {
			player.getUpdateFlags().sendAnimation(11786, 0);
			player.getAnimations().setStandEmote(11786);
		}
		player.getMovementHandler().reset();
		player.setAppearanceUpdateRequired(true);
	}

	/**
	 * Updates the players energy client sided
	 */
	public void update() {
		player.getClient().queueOutgoingPacket(new SendUpdateEnergy(energy));
	}
}
