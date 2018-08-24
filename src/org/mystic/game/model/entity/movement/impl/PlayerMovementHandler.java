package org.mystic.game.model.entity.movement.impl;

import org.mystic.cache.map.Region;
import org.mystic.game.GameConstants;
import org.mystic.game.model.content.minigames.duelarena.DuelingConstants;
import org.mystic.game.model.entity.movement.MovementHandler;
import org.mystic.game.model.entity.movement.Point;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendMultiInterface;

public class PlayerMovementHandler extends MovementHandler {

	private final Player player;

	public PlayerMovementHandler(Player player) {
		super(player);
		this.player = player;
	}

	@Override
	public boolean canMoveTo(int direction) {
		int x = player.getLocation().getX();
		int y = player.getLocation().getY();
		int z = player.getLocation().getZ();
		return Region.getRegion(x, y).canMove(x, y, z, direction);
	}

	@Override
	public boolean canMoveTo(int x, int y, int size, int direction) {
		int z = player.getLocation().getZ();
		return Region.getRegion(x, y).canMove(x, y, z, direction);
	}

	@Override
	public void process() {
		if ((player.isDead()) || (player.isFrozen()) || (player.getMagic().isTeleporting())
				|| ((player.getDueling().isDueling())
						&& player.getDueling().getRuleToggle()[DuelingConstants.NO_MOVEMENT])) {
			reset();
			return;
		}
		Point walkPoint = waypoints.poll();
		if ((walkPoint != null) && (walkPoint.getDirection() != -1)) {
			if (player.getRunEnergy().isResting()) {
				player.getRunEnergy().toggleResting(false);
			}
			if ((!forceMove) && (!GameConstants.WALK_CHECK) && (!Region.getRegion(player.getLocation())
					.canMove(player.getLocation(), walkPoint.getDirection()))) {
				reset();
				return;
			}
			player.getMovementHandler().getLastLocation().setAs(player.getLocation());
			player.getLocation().move(org.mystic.game.GameConstants.DIR[walkPoint.getDirection()][0],
					org.mystic.game.GameConstants.DIR[walkPoint.getDirection()][1]);
			primaryDirection = walkPoint.getDirection();
			flag = true;
		} else {
			if (flag) {
				flag = false;
			}
			return;
		}
		if (player.getRunEnergy().isRunning()) {
			if (player.getRunEnergy().canRun()) {
				Point runPoint = waypoints.poll();
				if ((runPoint != null) && (runPoint.getDirection() != -1)) {
					if ((!forceMove) && (!GameConstants.WALK_CHECK) && (!Region.getRegion(player.getLocation())
							.canMove(player.getLocation(), runPoint.getDirection()))) {

						reset();
						return;
					}
					player.getMovementHandler().getLastLocation().setAs(player.getLocation());
					player.getLocation().move(org.mystic.game.GameConstants.DIR[runPoint.getDirection()][0],
							org.mystic.game.GameConstants.DIR[runPoint.getDirection()][1]);
					secondaryDirection = runPoint.getDirection();
					player.getRunEnergy().onRun();
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You have run out of energy."));
				player.getRunEnergy().reset();
			}
		}
		ControllerManager.setControllerOnWalk(player);
		if (player.inMultiArea())
			player.getClient().queueOutgoingPacket(new SendMultiInterface(true));
		else {
			player.getClient().queueOutgoingPacket(new SendMultiInterface(false));
		}
		player.checkForRegionChange();
		if ((forceMove) && (waypoints.size() == 0)) {
			forceMove = false;
		}
	}
}
