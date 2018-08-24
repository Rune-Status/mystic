package org.mystic.game.model.content.wilderness;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendPlayerOption;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;

public class WildernessController extends Controller {

	public static final String LEVEL_ATTRIBUTE = "wildlvlattr";

	@Override
	public boolean allowMultiSpells() {
		return true;
	}

	@Override
	public boolean allowPvPCombat() {
		return true;
	}

	@Override
	public boolean canAttackNPC() {
		return true;
	}

	@Override
	public boolean canAttackPlayer(Player p, Player p2) {
		if (p == null || p2 == null) {
			return false;
		}
		if (p2.getController().equals(ControllerManager.WILDERNESS_CONTROLLER)) {
			int difference = Math.abs(p.getSkill().getCombatLevel() - p2.getSkill().getCombatLevel());
			if (difference > p.getWildernessLevel()) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("You must move deeper in the wilderness to attack this player."));
				return false;
			}
			if (difference > p2.getWildernessLevel()) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("This player must move deeper in the wilderness for you to attack them."));
				return false;
			}
		} else {
			p.getClient()
					.queueOutgoingPacket(new SendMessage("This player is busy or they are not in the wilderness."));
			return false;
		}
		return true;
	}

	@Override
	public boolean canClick() {
		return true;
	}

	@Override
	public boolean canDrink(Player p) {
		return true;
	}

	@Override
	public boolean canEat(Player p) {
		return true;
	}

	@Override
	public boolean canEquip(Player p, int id, int slot) {
		return true;
	}

	@Override
	public boolean canLogOut() {
		return true;
	}

	@Override
	public boolean canMove(Player p) {
		return true;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canTeleport() {
		return true;
	}

	@Override
	public boolean canTrade() {
		return true;
	}

	@Override
	public boolean canUseCombatType(Player p, CombatTypes type) {
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player p) {
		return true;
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(PlayerConstants.RESPAWN);
	}

	@Override
	public void onControllerInit(Player player) {
		player.getSkill().updateCombatLevel();
		player.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
		player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
		player.getClient().queueOutgoingPacket(new SendWalkableInterface(1060));
		if (player.getAttributes().get("gainEP") == null && player.getEP() != 100) {
			Task task = new EPTask(player, (byte) 1);
			player.getAttributes().set("gainEP", task);
			TaskQueue.queue(task);
		}
		if (player.getAttributes().get("gainTarget") == null && player.getTarget() == null) {
			Task task = new TargetTask(player, (byte) 1);
			player.getAttributes().set("gainTarget", task);
			TaskQueue.queue(task);
		}
	}

	@Override
	public void onDeath(Player p) {
		p.getAttributes().remove("gainTarget");
		p.getAttributes().remove("gainEP");
		if (p.getTarget() != null) {
			WildernessTargets.resetTarget(p, p.getTarget(), false);
		}
	}

	@Override
	public void onDisconnect(Player p) {

	}

	@Override
	public void onTeleport(Player p) {

	}

	@Override
	public void tick(Player player) {
		int lvl = player.getWildernessLevel();
		if ((player.getAttributes().get("wildlvlattr") == null)
				|| (player.getAttributes().getInt("wildlvlattr") != lvl)) {
			player.getAttributes().set("wildlvlattr", Integer.valueOf(lvl));
		}
		player.getClient().queueOutgoingPacket(new SendString("Level: " + player.getWildernessLevel(), 1342));
		player.getClient()
				.queueOutgoingPacket(new SendString("EP: " + player.getEPColor() + player.getEP() + "%", 1344));
		if (player.getTarget() != null) {
			player.getClient().queueOutgoingPacket(new SendString("@gre@" + player.getTarget().getUsername(), 1348));
		} else {
			player.getClient().queueOutgoingPacket(new SendString("@dre@Nobody", 1348));
		}
		if (player.displayKDR) {
			player.getClient().queueOutgoingPacket(new SendString("@yel@Kills: " + player.getKills(), 1639));
			player.getClient().queueOutgoingPacket(new SendString("@yel@Deaths: " + player.getDeaths(), 1638));
			player.getClient().queueOutgoingPacket(new SendString("@yel@K/D Ratio: " + player.getKDR(), 1637));
		} else {
			player.getClient().queueOutgoingPacket(new SendString("", 1639));
			player.getClient().queueOutgoingPacket(new SendString("", 1638));
			player.getClient().queueOutgoingPacket(new SendString("", 1637));
		}
	}

	@Override
	public String toString() {
		return "WILDERNESS";
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return true;
	}

	@Override
	public boolean canUnequip(Player player) {
		return true;
	}

	@Override
	public boolean canDrop(Player player) {
		return true;
	}

	@Override
	public boolean canUsePrayer(Player player, int id) {
		return true;
	}

	@Override
	public boolean isSafe(Player player) {
		return false;
	}

	@Override
	public void onKill(Player player, Entity killed) {
		// TODO Auto-generated method stub
	}

}