package org.mystic.game.model.content.skill.firemaking;

import org.mystic.cache.map.Region;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItem;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;

public class Firemaking extends Task {

	public static boolean attemptFiremaking(Player player, Item used, Item usedWith) {
		if (Log.getLogById(used.getId()) != null && Log.getLogById(usedWith.getId()) != null) {
			return false;
		}
		Item log = usedWith.getId() != 590 ? usedWith : used.getId() != 590 ? used : null;
		Log logData = Log.getLogById(log.getId());
		if (logData == null || used.getId() == 946 || usedWith.getId() == 946) {
			return false;
		}
		if (!meetsRequirements(player, log)) {
			return true;
		}
		player.getMovementHandler().reset();
		player.send(new SendRemoveInterfaces());
		player.send(new SendMessage("You attempt to light the logs."));
		player.getUpdateFlags().sendAnimation(new Animation(733));
		GroundItem groundLog = new GroundItem(new Item(log.getId(), 1), new Location(player.getLocation()),
				player.getUsername());
		GroundItemHandler.add(groundLog);
		player.getInventory().remove(log);
		TaskQueue.queue(new Firemaking(1, player, log, logData, groundLog));
		return true;
	}

	private static boolean meetsRequirements(Player player, Item log) {
		int skillLevel = player.getSkill().getLevels()[11];
		Log data = Log.getLogById(log.getId());
		int x = player.getLocation().getX();
		int y = player.getLocation().getY();
		if (!player.getInventory().hasItemId(590)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a tinderbox to light this log."));
			return false;
		}
		if (skillLevel < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(new SendMessage(
					"You need a firemaking level of " + data.getLevelRequired() + " to light this log."));
			return false;
		}
		if ((x >= 3090) && (y >= 3488) && (x <= 3098) && (y <= 3500)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't light fires here."));
			return false;
		}
		if (skillLevel < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(new SendMessage(
					"You need a firemaking level of " + data.getLevelRequired() + " to light this log."));
			return false;
		}
		if (ObjectManager.objectExists(player.getLocation())) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't light a fire here."));
			return false;
		}
		return true;
	}

	public static boolean success(Player player, Item log) {
		return Skills.isSuccess(player, Skills.FIREMAKING, Log.getLogById(log.getId()).getLevelRequired());
	}

	private final Player player;

	private final Log data;

	private final Item log;

	private int animationCycle;

	private final GroundItem item;

	public Firemaking(final int delay, final Player entity, final Item log, final Log data, GroundItem item) {
		super(entity, delay, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = entity;
		this.log = log;
		this.data = data;
		this.item = item;
	}

	@Override
	public void execute() {
		if (!meetsRequirements(player, log)) {
			stop();
			return;
		}
		if (success(player, log)) {
			complete(player);
			stop();
			return;
		}
		if (animationCycle < 6) {
			animationCycle += 1;
		} else {
			player.getUpdateFlags().sendAnimation(new Animation(733));
			animationCycle = 0;
		}
	}

	@Override
	public void onStop() {

	}

	private void complete(Player player) {
		SoundPlayer.play(player, Sounds.LIGHT_FIRE);
		GroundItemHandler.remove(item);
		player.getUpdateFlags().sendAnimation(65535, 0);
		GameObject object = new GameObject(2732, new Location(player.getLocation()), 10, 0);
		TaskQueue.queue(new FireTask(player, 50 + data.ordinal() * 15, object));
		player.getSkill().addExperience(11, data.getExperience());
		player.getAchievements().incr(player, "Burn 1,000 logs");
		player.send(new SendMessage("The fire catches and the logs begin to burn."));
		walk(player);
		player.getUpdateFlags().sendFaceToDirection(object.getLocation());
	}

	private void walk(Player player) {
		if (Region.getRegion(player.getLocation().getX(), player.getLocation().getY()) != null) {
			if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedWest(
					player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
				player.getMovementHandler().walkTo(-1, 0);
			} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedEast(
					player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
				player.getMovementHandler().walkTo(1, 0);
			} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedNorth(
					player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
				player.getMovementHandler().walkTo(0, 1);
			} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedSouth(
					player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
				player.getMovementHandler().walkTo(0, -1);
			}
		}
	}
}
