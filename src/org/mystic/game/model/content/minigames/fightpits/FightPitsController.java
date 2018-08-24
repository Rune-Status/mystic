package org.mystic.game.model.content.minigames.fightpits;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendPlayerOption;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;

public class FightPitsController extends Controller {

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
		return (p.getController().equals(ControllerManager.FIGHT_PITS_CONTROLLER))
				&& (p2.getController().equals(ControllerManager.FIGHT_PITS_CONTROLLER));
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
		return false;
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
		return false;
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
		return new Location(2399, 5169);
	}

	@Override
	public void onControllerInit(Player p) {
		p.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
		p.getClient().queueOutgoingPacket(new SendWalkableInterface(17600));
	}

	@Override
	public void onDeath(Player p) {
		FightPits.onPlayerDeath(p);
	}

	@Override
	public void onDisconnect(Player p) {
	}

	@Override
	public void onTeleport(Player p) {
	}

	@Override
	public void tick(Player p) {
		FightPits.updateInterface(p);
	}

	@Override
	public String toString() {
		return "FIGHT PITS GAME";
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public boolean canUnequip(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canDrop(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUsePrayer(Player player, int id) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isSafe(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onKill(Player player, Entity killed) {
		// TODO Auto-generated method stub

	}

}