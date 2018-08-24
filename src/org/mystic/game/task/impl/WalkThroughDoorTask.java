package org.mystic.game.task.impl;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.task.Task;

public class WalkThroughDoorTask extends Task {

	protected final Player player;

	// protected final Door door;

	protected final int xMod, yMod;

	protected byte stage = 0;

	protected final Controller start;

	private final Location dest;

	public WalkThroughDoorTask(Player player, int x, int y, int z, Location dest) {
		super(player, 1, true);
		this.player = player;
		// this.door = Region.getDoor(x, y, z);
		this.start = player.getController();
		player.setController(ControllerManager.FORCE_MOVEMENT_CONTROLLER);
		player.getMovementHandler().setForceMove(true);
		this.xMod = dest.getX() - player.getLocation().getX();
		this.yMod = dest.getY() - player.getLocation().getY();
		this.dest = dest;
		// if ((xMod != 0 && yMod != 0) || door == null) {
		// player.setController(start);
		// stop();
		// }
	}

	@Override
	public void execute() {
		// if (stage == 0) {
		// SoundPlayer.play(player, Sounds.DOOR);
		// ObjectManager.remove2(new GameObject(ObjectManager.BLANK_OBJECT_ID,
		// door.getX(), door.getY(), door.getZ(),
		// door.getType(), door.getCurrentFace()));
		// door.append();
		// ObjectManager.send(new GameObject(door.getCurrentId(), door.getX(),
		// door.getY(), door.getZ(),
		// door.getType(), door.getCurrentFace()));
		// } else if (stage == 1) {
		// player.getMovementHandler().walkTo(xMod, yMod);
		// player.setController(start);
		// } else if (stage == 2) {
		// ObjectManager.send(new GameObject(ObjectManager.BLANK_OBJECT_ID, door.getX(),
		// door.getY(), door.getZ(),
		// door.getType(), door.getCurrentFace()));
		// door.append();
		// ObjectManager.send(new GameObject(door.getCurrentId(), door.getX(),
		// door.getY(), door.getZ(),
		// door.getType(), door.getCurrentFace()));
		// stop();
		// }
		// stage++;
	}

	@Override
	public void onStop() {
		player.setController(start);
		player.teleportWithDamage(dest);
		player.getMovementHandler().setForceMove(false);
	}

}