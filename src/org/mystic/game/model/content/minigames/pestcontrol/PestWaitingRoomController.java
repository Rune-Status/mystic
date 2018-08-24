package org.mystic.game.model.content.minigames.pestcontrol;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;
import org.mystic.utility.Misc;

public class PestWaitingRoomController extends Controller {

	@Override
	public boolean allowMultiSpells() {
		return false;
	}

	@Override
	public boolean allowPvPCombat() {
		return false;
	}

	@Override
	public boolean canAttackNPC() {
		return false;
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
	public boolean canUnequip(Player player) {
		return true;
	}

	@Override
	public boolean canDrop(Player player) {
		return false;
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
		return false;
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canTeleport() {
		return false;
	}

	@Override
	public boolean canTrade() {
		return true;
	}

	@Override
	public boolean canUseCombatType(Player p, CombatTypes type) {
		return false;
	}

	@Override
	public boolean canUsePrayer(Player p, int id) {
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player p) {
		return false;
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(2657, 2639, 0);
	}

	@Override
	public boolean isSafe(Player player) {
		return true;
	}

	@Override
	public void onControllerInit(Player p) {
		p.getClient().queueOutgoingPacket(new SendWalkableInterface(21119));
	}

	@Override
	public void onDeath(Player p) {
	}

	@Override
	public void onDisconnect(Player p) {
		p.teleport(new Location(2657, 2639, 0));
		p.setController(ControllerManager.DEFAULT_CONTROLLER);
	}

	@Override
	public void onTeleport(Player p) {
	}

	@Override
	public void tick(Player p) {
		p.getClient().queueOutgoingPacket(
				new SendString("Next Departure: " + PestControl.getMinutesTillDepart() + "", 21120));
		p.getClient().queueOutgoingPacket(new SendString("Players Ready: " + PestControl.getPlayersReady(), 21121));
		p.getClient().queueOutgoingPacket(new SendString("(Need 2 to 25 players)", 21122));
		p.getClient().queueOutgoingPacket(new SendString("Points: " + Misc.format(p.getPestPoints()), 21123));
	}

	@Override
	public String toString() {
		return "PEST CONTROL";
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public void onKill(Player player, Entity killed) {

	}

}