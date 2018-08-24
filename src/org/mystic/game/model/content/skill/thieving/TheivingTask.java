package org.mystic.game.model.content.skill.thieving;

import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class TheivingTask extends Task {

	public static final int[][] SEEDS = {
			{ 5291, 5292, 5293, -1, 5294, 5318, 5319, 5324, 5322, 5320, 5096, 5097, 5098, -1, 5318, 5318, 5318, 5318,
					-1, 5318, 5319, 5319, 5319, 5319, -1, 5324, 5324, 5324 },
			{ 5295, 5296, 5297, 5298, 14870, 5299, 5300, 5301, 5302, 5303, 5304, 5323, 5321, 5099, 5100, 14589 }

	};

	public static boolean attemptThieving(Player player, Npc mob) {
		if (player.getSkill().locked()) {
			return false;
		}
		if (mob.isDead()) {
			return false;
		}
		ThievingData data = ThievingData.getNpcById(mob.getId());
		if (data == null) {
			return false;
		}
		if (System.currentTimeMillis() - player.getCurrentStunDelay() < player.getSetStunDelay()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are stunned!"));
			return false;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have any inventory space."));
			return true;
		}
		if (!meetsRequirements(player, data)) {
			return false;
		}
		player.send(new SendRemoveInterfaces());
		player.getClient().queueOutgoingPacket(new SendMessage(
				"You attempt to pick the " + mob.getDefinition().getName().toLowerCase() + "'s pocket."));
		player.getUpdateFlags().sendAnimation(new Animation(881));
		TaskQueue.queue(new TheivingTask(3, player, data, mob));
		return true;
	}

	private static void failedAttempt(Player player, ThievingData data, Npc mob) {
		mob.getUpdateFlags().sendForceMessage("What are ye doing in me pockets?");
		mob.face(player);
		mob.getUpdateFlags().sendAnimation(new Animation(422));
		SoundPlayer.play(player, Sounds.PICKPOCKET_STUN);
		player.getClient().queueOutgoingPacket(
				new SendMessage("You fail to pick the " + mob.getDefinition().getName().toLowerCase() + "'s pocket."));
		player.getUpdateFlags().sendGraphic(Graphic.highGraphic(80, 0));
		player.hit(new Hit(1));
		player.getUpdateFlags().sendAnimation(new Animation(404));
		player.setCurrentStunDelay(System.currentTimeMillis() + data.getStunTime() * 100);
		player.setSetStunDelay(500L);
		player.getCombat().setInCombat(null);
		player.send(new SendRemoveInterfaces());
	}

	private static boolean meetsRequirements(Player player, ThievingData data) {
		if (player.getMaxLevels()[17] < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a Thieving level of " + data.getLevelRequired() + " to do this."));
			return false;
		}

		return true;
	}

	private static void successfulAttempt(Player player, ThievingData data, Npc mob) {
		player.getSkill().addExperience(17, data.getExperience());
		Item stolen = null;
		if (data == ThievingData.MASTER_FARMER || data == ThievingData.MASTER_FARMER2) {
			int roll = Misc.randomNumber(SEEDS[0].length);
			if (SEEDS[0][roll] == -1) {
				stolen = new Item(SEEDS[1][Misc.randomNumber(SEEDS[1].length)]);
			} else {
				stolen = new Item(SEEDS[0][roll]);
			}
		} else {
			int roll = Misc.randomNumber(data.getItems().length);
			stolen = new Item(data.getItems()[roll][0], data.getItems()[roll][1]);
		}
		if (!player.getInventory().hasSpaceFor(stolen)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You do not have enough inventory space to carry that."));
			return;
		}
		player.getInventory().add(stolen);
		player.getClient().queueOutgoingPacket(new SendMessage(
				"You successfully pick the " + mob.getDefinition().getName().toLowerCase() + "'s pocket."));
	}

	private static boolean successfulAttemptChance(Player player, ThievingData data) {
		return Skills.isSuccess(player, 17, (int) (data.getLevelRequired() * 0.7D));
	}

	private final Player player;

	private final ThievingData data;

	private final Npc mob;

	public TheivingTask(final int delay, final Player player, final ThievingData data, final Npc mob) {
		super(player, delay, false, Task.StackType.NEVER_STACK, Task.BreakType.NEVER, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.data = data;
		this.mob = mob;
	}

	@Override
	public void execute() {
		if (successfulAttemptChance(player, data)) {
			successfulAttempt(player, data, mob);
		} else {
			failedAttempt(player, data, mob);
		}
		stop();
	}

	@Override
	public void onStop() {
	}
}
