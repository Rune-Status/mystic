package org.mystic.game.model.content.skill.thieving;

import org.mystic.game.World;
import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.randomevent.impl.Dwarf;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class StallTask extends Task {

	public static final int[][] ARDY_ITEMS = {

			{ 1891, 1 }, // 0

			{ 1897, 1 }, // 1

			{ 950, 1 }, // 2

			{ 948, 1 }, // 3

			{ 958, 1 }, // 4

			{ 10093, 1 }, // 5

			{ 10095, 1 }, // 6

			{ 10099, 1 }, // 7

			{ 10115, 1 }, // 8

			{ 1617, 1 }, // 9

			{ 1623, 1 }, // 10

			{ 1621, 1 }, // 11

			{ 1619, 1 }, // 12

			{ 442, 1 }, // 13

			{ 2007, 1 }, // 14

			{ 1631, 1 }// 15

	};

	public static final int[][] THEIVING_ITEMS = {

			{ 1277, 1 },

			{ 1281, 1 },

			{ 1211, 1 },

			{ 1285, 1 },

			{ 1331, 1 },

			{ 1289, 1 }

	};

	public static void attemptStealFromStall(Player player, int id, Location location) {
		StallData data = StallData.getObjectById(id);
		if (data == null) {
			return;
		}
		if (player.getSkill().locked()) {
			return;
		}
		if (!meetsRequirements(player, data)) {
			return;
		}
		if (player.getAttributes().get("randomEvent") != null) {
			if ((Boolean) player.getAttributes().get("randomEvent") == true) {
				player.send(new SendMessage("You must deal with the drunken dwarf before doing this."));
				return;
			}
		}
		player.getSkill().lock(3);
		player.send(new SendRemoveInterfaces());
		player.getUpdateFlags().sendAnimation(new Animation(832));
		TaskQueue.queue(new StallTask(1, player, data));
	}

	private static boolean meetsRequirements(Player player, StallData data) {
		if (player.getInventory().getFreeSlots() == 0) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You don't have enough inventory spaces left to do this."));
			return false;
		}
		if (player.getSkill().getLevels()[17] < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(new SendMessage(
					"You need a thieving level of " + data.getLevelRequired() + " to steal from this stall."));
			return false;
		}
		return true;
	}

	private final Player player;

	private final StallData data;

	public StallTask(final int delay, final Player player, final StallData data) {
		super(player, delay, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.data = data;
	}

	@Override
	public void execute() {
		if (!meetsRequirements(player, data)) {
			stop();
			return;
		}
		successfulAttempt(player, data);
		stop();
	}

	@Override
	public void onStop() {

	}

	private void successfulAttempt(Player player, StallData data) {
		final int randomItem = Misc.randomNumber(data.getRewards().length);
		if (data.getObjectId() == 2561) {
			player.getInventory().add(new Item(995, IronMan.isIronMan(player) ? 750 : 1093));
		}
		if (data.getObjectId() == 2560) {
			player.getInventory().add(new Item(995, IronMan.isIronMan(player) ? 1200 : 2361));
		}
		if (data.getObjectId() == 2565) {
			player.getInventory().add(new Item(995, IronMan.isIronMan(player) ? 1750 : 3277));
		}
		if (data.getObjectId() == 2564) {
			player.getInventory().add(new Item(995, IronMan.isIronMan(player) ? 2500 : 4324));
		}
		if (data.getObjectId() == 2562) {
			if (Misc.random(2500) == 1) {
				player.getInventory().add(new Item(6571, 1));
				World.sendGlobalMessage("@dre@" + player.getUsername() + " has stolen an onyx from the gem stall!",
						true);
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " received an uncut onyx from the gem stall.");
				PlayerLogs.log("SERVER", "" + player.getUsername() + " received an uncut onyx from the gem stall.");
			}
			player.getInventory().add(new Item(995, IronMan.isIronMan(player) ? 3500 : 6461));
		}
		if (Misc.random(80) == 1) {
			final Dwarf dwarf = new Dwarf();
			dwarf.start(player);
		}
		SoundPlayer.play(player, Sounds.PICKUP_ITEM);
		player.send(new SendMessage("You steal from the stall."));
		player.getInventory().add(new Item(data.getRewards()[randomItem][0], data.getRewards()[randomItem][1]));
		player.getSkill().addExperience(Skills.THIEVING, data.getExperience());
	}

}