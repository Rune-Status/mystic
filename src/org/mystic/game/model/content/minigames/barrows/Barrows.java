package org.mystic.game.model.content.minigames.barrows;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.following.Following;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendPlayerHint;
import org.mystic.utility.Misc;

public class Barrows {

	public static void assignTunnel(Player player) {
		player.getMinigames().setTunnel(
				BarrowsConstants.CRYPT_COFFIN_IDS[Misc.randomNumber(BarrowsConstants.CRYPT_COFFIN_IDS.length)]);
	}

	public static final boolean clickObject(Player player, int id, int x, int y, int z) {
		for (int i = 0; i < BarrowsConstants.CRYPT_STAIRCASE_IDS.length; i++) {
			if (id == BarrowsConstants.CRYPT_STAIRCASE_IDS[i]) {
				player.teleport(BarrowsConstants.BARROWS_STAIRCASE_DEST[i]);
				return true;
			}
		}
		for (int i = 0; i < BarrowsConstants.CRYPT_COFFIN_IDS.length; i++) {
			if (id == BarrowsConstants.CRYPT_COFFIN_IDS[i]) {
				if (id == player.getMinigames().getTunnel()) {
					player.start(new BarrowsTunnelDialogue());
				} else if ((!player.getMinigames().killedBarrow(i)) && (player.getAttributes()
						.get("barrowsActive" + BarrowsConstants.BARROWS_NPC_IDS[i]) == null)) {
					spawnBrother(player, BarrowsConstants.BARROWS_NPC_IDS[i], new Location(x + 3, y + 3, z));
				} else {
					player.getClient()
							.queueOutgoingPacket(new SendMessage("You search the sarcophagus and find nothing..."));
				}
				return true;
			}
		}
		return false;
	}

	public static final boolean dig(Player player) {
		if (player.getLocation().getZ() != 0) {
			return false;
		}
		for (int i = 0; i < BarrowsConstants.BARROWS_CRYPT_DIG_BOUNDS.length; i++) {
			if ((player.getLocation().getX() > BarrowsConstants.BARROWS_CRYPT_DIG_BOUNDS[i][0])
					&& (player.getLocation().getY() > BarrowsConstants.BARROWS_CRYPT_DIG_BOUNDS[i][1])
					&& (player.getLocation().getX() < BarrowsConstants.BARROWS_CRYPT_DIG_BOUNDS[i][2])
					&& (player.getLocation().getY() < BarrowsConstants.BARROWS_CRYPT_DIG_BOUNDS[i][3])) {
				player.teleport(BarrowsConstants.BARROWS_CRYPT_DIG_DEST[i]);
				player.setController(ControllerManager.BARROWS_CONTROLLER);
				player.getClient().queueOutgoingPacket(new SendMessage("You've broken into a crypt!"));
				return true;
			}
		}
		return false;
	}

	public static boolean isBarrowsBrother(Npc mob) {
		return (mob.getId() >= 2025) && (mob.getId() <= 2030);
	}

	public static final void onBarrowsDeath(Player p, Npc killed) {
		if (isBarrowsBrother(killed)) {
			p.getMinigames().setBarrowKilled(BarrowsConstants.getIndexForId(killed.getId()));
			p.getAttributes().remove("barrowsActive" + killed.getId());
		}
	}

	public static final void search_chest(Player player, int id) {
		if (player.getMinigames().getBarrowsKillcount() == 5) {
			for (int i = 0; i < BarrowsConstants.CRYPT_COFFIN_IDS.length; i++) {
				if (id == BarrowsConstants.CRYPT_COFFIN_IDS[i]) {
					if ((!player.getMinigames().killedBarrow(i)) && (player.getAttributes()
							.get("barrowsActive" + BarrowsConstants.BARROWS_NPC_IDS[i]) == null)) {
						spawnBrother(player, BarrowsConstants.BARROWS_NPC_IDS[i],
								new Location(player.getLocation().getX(), player.getLocation().getY() - 1,
										player.getLocation().getZ()));
						return;
					}
				}
			}
		}
		if (player.getMinigames().getBarrowsKillcount() >= 6) {
			player.getClient().queueOutgoingPacket(new SendMessage("You search through the chest.."));
			player.getUpdateFlags().sendAnimation(881, 0);
			BarrowsRewardTable.displayReward(player);
			player.getMinigames().resetBarrows();
			return;
		} else if (player.getMinigames().getBarrowsKillcount() <= 5) {
			player.send(new SendMessage("You must kill the rest of the brothers before you loot the chest!"));
			return;
		}
		return;
	}

	public static final void spawnBrother(Player player, int id, Location location) {
		Npc brother = new Npc(player, id, false, false, false, location);
		player.send(new SendPlayerHint(false, brother.getIndex()));
		brother.getUpdateFlags().sendForceMessage("You dare distrub my rest!");
		brother.getFollowing().setFollow(player, Following.FollowType.COMBAT);
		brother.getCombat().setAttacking(player);
		player.getAttributes().set("barrowsActive" + id, new Object());
	}
}
