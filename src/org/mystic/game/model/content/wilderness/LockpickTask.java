package org.mystic.game.model.content.wilderness;

import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.WalkThroughDoorTask;

/**
 * Lockpicking System
 * 
 * @author Valiant - http://www.rune-sever.org/members/Valiant
 * @since April 6th, 2015
 * 
 */
public class LockpickTask extends Task {

	private final Player player;

	private final int x, y, z;

	private boolean success = false;

	private boolean insideExiting = false;

	public LockpickTask(final Player player, final byte delay, final int x, final int y, final int z) {
		super(delay);
		this.player = player;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void execute() {
		player.getUpdateFlags().sendAnimation(new Animation(2246));
		// final Door door = Region.getDoor(x, y, z);
		// if (door.getX() == 3041 && door.getY() == 3959) {
		// if (player.getX() != door.getX()) {
		// player.getMovementHandler().addToPath(new Location(door.getX(), door.getY(),
		// door.getZ()));
		// return;
		// }
		// }
		// if (door.getX() == 3191 && door.getY() == 3963) {
		// if (player.getX() != door.getX()) {
		// player.getMovementHandler().addToPath(new Location(door.getX(), door.getY(),
		// door.getZ()));
		// return;
		// }
		// }
		// if (door.getX() == 3190 && door.getY() == 3957) {
		// if (player.getX() != door.getX()) {
		// player.getMovementHandler().addToPath(new Location(door.getX(), door.getY(),
		// door.getZ()));
		// return;
		// }
		// }
		// if (door.getX() == 3038 || door.getX() == 3044 && door.getY() == 3956) {
		// if (player.getY() != door.getY()) {
		// player.getMovementHandler().addToPath(new Location(door.getX(), door.getY(),
		// door.getZ()));
		// return;
		// }
		// }
		// if (player.getX() == 3038 && player.getY() == 3956 || player.getX() == 3044
		// && player.getY() == 3956
		// || player.getX() == 3041 && player.getY() == 3959 || player.getX() == 3190 &&
		// player.getY() == 3957
		// || player.getX() == 3191 && player.getY() == 3963) {
		// insideExiting = true;
		// stop();
		// return;
		// }
		// int chance = Misc.randomNumber(4);
		// if (chance == 3 || chance == 1 || chance == 2) {
		// player.getClient().queueOutgoingPacket(new SendMessage("You successfully pick
		// the lock.."));
		// success = true;
		// stop();
		// return;
		// } else {
		// player.getClient().queueOutgoingPacket(new SendMessage("You fail to pick the
		// lock on the door.."));
		// stop();
		// return;
		// }
	}

	@Override
	public void onStop() {
		if (success) {
			Task task = new WalkThroughDoorTask(player, x, y, z, new Location(x, y, z));
			player.getAttributes().set("lockPick", task);
			TaskQueue.queue(task);
			return;
		} else if (insideExiting && x == 3038) {
			Task task = new WalkThroughDoorTask(player, x, y, z, new Location(x - 1, y, z));
			player.getAttributes().set("lockPick", task);
			TaskQueue.queue(task);
		} else if (insideExiting && x == 3044) {
			Task task = new WalkThroughDoorTask(player, x, y, z, new Location(x + 1, y, z));
			player.getAttributes().set("lockPick", task);
			TaskQueue.queue(task);
		} else if (insideExiting && x == 3041) {
			Task task = new WalkThroughDoorTask(player, x, y, z, new Location(x, y + 1, z));
			player.getAttributes().set("lockPick", task);
			TaskQueue.queue(task);
		} else if (insideExiting && x == 3190) {
			Task task = new WalkThroughDoorTask(player, x, y, z, new Location(x, y + 1, z));
			player.getAttributes().set("lockPick", task);
			TaskQueue.queue(task);
		} else if (insideExiting && x == 3191) {
			Task task = new WalkThroughDoorTask(player, x, y, z, new Location(x, y - 1, z));
			player.getAttributes().set("lockPick", task);
			TaskQueue.queue(task);
		}
	}

}