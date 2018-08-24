package org.mystic.game.model.networking.packet.impl;

import org.mystic.cache.map.Region;
import org.mystic.game.World;
import org.mystic.game.model.content.skill.magic.spells.VengeanceOther;
import org.mystic.game.model.entity.following.Following.FollowType;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.StreamBuffer.ByteOrder;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.FollowToEntityTask;

@SuppressWarnings("all")
public class PlayerOptionPacket extends IncomingPacket {

	public static final int TRADE = 153;

	public static final int FOLLOW = 128;

	public static final int ATTACK = 73;

	public static final int OPTION_4 = 139;

	public static final int MAGIC_ON_PLAYER = 249;

	public static final int USE_ITEM_ON_PLAYER = 14;

	public static final int TRADE_ANSWER2 = 39;

	@Override
	public int getMaxDuplicates() {
		return 2;
	}

	@Override
	public void handle(final Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isDead()) || (player.getMagic().isTeleporting())
				|| (!player.getController().canClick() || player.isSpeared())) {
			return;
		}
		int playerSlot = -1;
		int itemSlot = -1;
		Player other = null;
		if (player.getTrade().trading()) {
			player.getTrade().end(false);
		}
		if (player.getDueling().getInteracting() != null) {
			if (player.getDueling().isScreen()) {
				player.getDueling().decline();
			}
		}
		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		player.getMagic().getSpellCasting().resetOnAttack();
		TaskQueue.onMovement(player);
		switch (opcode) {

		case OPTION_4:
			final int slot = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);
			if ((!World.isPlayerWithinRange(slot)) || (World.getPlayers()[slot] == null)
					|| (slot == player.getIndex())) {
				return;
			}
			TaskQueue.queue(new FollowToEntityTask(player, World.getPlayers()[slot]) {
				@Override
				public void onDestination() {
					final Player other = World.getPlayers()[slot];
					if (other == null) {
						player.getMovementHandler().reset();
						return;
					}
					if (player.getController().equals(ControllerManager.DUEL_ARENA_CONTROLLER)) {
						if (player.getTarget() != null) {
							player.getClient().queueOutgoingPacket(
									new SendMessage("You can't duel while you have an active wilderness target."));
							return;
						}
						if (player.getLocation().equals(other.getLocation().copy())) {
							if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY())
									.blockedSouth(player.getLocation().getX(), player.getLocation().getY(),
											player.getLocation().getZ())) {
								player.getMovementHandler().walkTo(0, -1);
							} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY())
									.blockedWest(player.getLocation().getX(), player.getLocation().getY(),
											player.getLocation().getZ())) {
								player.getMovementHandler().walkTo(-1, 0);
							} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY())
									.blockedEast(player.getLocation().getX(), player.getLocation().getY(),
											player.getLocation().getZ())) {
								player.getMovementHandler().walkTo(1, 0);
							} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY())
									.blockedNorth(player.getLocation().getX(), player.getLocation().getY(),
											player.getLocation().getZ())) {
								player.getMovementHandler().walkTo(0, 1);
							}
						}
						player.face(other);
						player.getDueling().request(other);
					}
				}
			});
			break;

		case TRADE:
			final int tradeSlot = in.readShort(true, ByteOrder.LITTLE);
			if ((!World.isPlayerWithinRange(tradeSlot)) || (World.getPlayers()[tradeSlot] == null)
					|| (tradeSlot == player.getIndex())) {
				return;
			}
			TaskQueue.queue(new FollowToEntityTask(player, World.getPlayers()[tradeSlot]) {
				@Override
				public void onDestination() {
					final Player other = World.getPlayers()[tradeSlot];
					if (other == null) {
						stop();
						player.getMovementHandler().reset();
						return;
					}
					if (player.getLocation().equals(other.getLocation().copy())) {
						if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedSouth(
								player.getLocation().getX(), player.getLocation().getY(),
								player.getLocation().getZ())) {
							player.getMovementHandler().walkTo(0, -1);
						} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY())
								.blockedWest(player.getLocation().getX(), player.getLocation().getY(),
										player.getLocation().getZ())) {
							player.getMovementHandler().walkTo(-1, 0);
						} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY())
								.blockedEast(player.getLocation().getX(), player.getLocation().getY(),
										player.getLocation().getZ())) {
							player.getMovementHandler().walkTo(1, 0);
						} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY())
								.blockedNorth(player.getLocation().getX(), player.getLocation().getY(),
										player.getLocation().getZ())) {
							player.getMovementHandler().walkTo(0, 1);
						}
					}
					player.face(other);
					player.getTrade().request(other);
					stop();
				}
			});
			break;

		case FOLLOW:
			player.getMovementHandler().reset();
			playerSlot = in.readShort();
			if ((!World.isPlayerWithinRange(playerSlot)) || (playerSlot == player.getIndex())) {
				return;
			}
			other = World.getPlayers()[playerSlot];
			if (other == null) {
				return;
			}
			if (player.isDoingCape()) {
				return;
			}
			player.face(other);
			player.getFollowing().setFollow(other);
			break;

		case ATTACK:
			playerSlot = in.readShort(true, ByteOrder.LITTLE);
			player.getMovementHandler().reset();
			if ((playerSlot == player.getIndex()) || (!World.isPlayerWithinRange(playerSlot))) {
				return;
			}
			other = World.getPlayers()[playerSlot];
			if (other == null) {
				return;
			}
			if (player.getController().equals(ControllerManager.DUEL_ARENA_CONTROLLER)) {
				final Player o = other;
				TaskQueue.queue(new FollowToEntityTask(player, o) {
					@Override
					public void onDestination() {
						if (o == null) {
							player.getMovementHandler().reset();
							return;
						}
						if (player.getController().equals(ControllerManager.DUEL_ARENA_CONTROLLER)) {
							player.face(o);
							player.getDueling().request(o);
						}
					}
				});
				return;
			}
			if (player.getController().canMove(player)) {
				player.getFollowing().setFollow(other, FollowType.COMBAT);
			}
			player.getCombat().setAttacking(other);
			player.getMagic().getSpellCasting().disableClickCast();
			break;

		case MAGIC_ON_PLAYER:
			playerSlot = in.readShort(true, StreamBuffer.ValueType.A);
			final int magicId = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);
			player.getMovementHandler().reset();
			if ((!World.isPlayerWithinRange(playerSlot)) || (World.getPlayers()[playerSlot] == null)
					|| (playerSlot == player.getIndex())) {
				return;
			}
			other = World.getPlayers()[playerSlot];
			if (other != null) {
				if (magicId == 30298) {
					VengeanceOther.execute(player, other);
				} else {
					player.getMagic().getSpellCasting().castCombatSpell(magicId, other);
				}
			}
			break;

		case USE_ITEM_ON_PLAYER:
			playerSlot = in.readShort();
			itemSlot = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			if ((!World.isPlayerWithinRange(playerSlot)) || (playerSlot == player.getIndex())) {
				return;
			} else {
				player.send(new SendMessage("Nothing interesting happens."));
			}
			break;

		}
	}

}