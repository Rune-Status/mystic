package org.mystic.game.model.entity.player.controllers;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.wilderness.WildernessTargets;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.networking.outgoing.SendPlayerOption;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;
import org.mystic.game.task.Task;

public class DefaultController extends Controller {
	@Override
	public boolean allowMultiSpells() {
		return true;
	}

	@Override
	public boolean allowPvPCombat() {
		return false;
	}

	@Override
	public boolean canAttackNPC() {
		return true;
	}

	@Override
	public boolean canAttackPlayer(Player p, Player p2) {
		return false;
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
	public boolean canUnequip(Player player) {
		return true;
	}

	@Override
	public boolean canDrop(Player player) {
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
	public boolean canUsePrayer(Player p, int id) {
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
	public boolean isSafe(Player player) {
		return false;
	}

	@Override
	public void onControllerInit(Player player) {
		if (!player.inWilderness()) {
			if (player.getAttributes().get("gainEP") != null) {
				((Task) player.getAttributes().get("gainEP")).stop();
			}
			if (player.getAttributes().get("gainTarget") != null) {
				((Task) player.getAttributes().get("gainTarget")).stop();
			}
			player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 3));
			player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
			player.getClient().queueOutgoingPacket(new SendWalkableInterface(-1));
			player.getSkill().updateCombatLevel();
		}
		if (player.getTarget() != null) {
			WildernessTargets.resetTarget(player, player.getTarget(), false);
		}
	}

	@Override
	public void onDeath(Player p) {
	}

	@Override
	public void onDisconnect(Player p) {
	}

	@Override
	public void onTeleport(Player p) {
	}

	@Override
	public void tick(Player player) {
	}

	@Override
	public String toString() {
		return "DEFAULT";
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return true;
	}

	@Override
	public void onKill(Player player, Entity killed) {
		// TODO Auto-generated method stub

	}
}
