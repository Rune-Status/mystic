package org.mystic.game.model.content.minigames.fightpits;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendPlayerOption;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;

public class FightPitsWaitingController extends Controller {

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
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player p) {
		return true;
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(PlayerConstants.HOME);
	}

	@Override
	public void onControllerInit(Player p) {
		FightPits.updateInterface(p);
		p.getClient().queueOutgoingPacket(new SendPlayerOption("null", 3));
		p.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
		for (int i = 2; i < FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS.length; i++) {
			p.getClient().queueOutgoingPacket(new SendString("", FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[i]));
		}
		p.getClient().queueOutgoingPacket(new SendWalkableInterface(17600));
	}

	@Override
	public void onDeath(Player p) {
		p.setController(ControllerManager.DEFAULT_CONTROLLER);
		FightPits.removeFromWaitingRoom(p);
	}

	@Override
	public void onDisconnect(Player p) {
		FightPits.removeFromWaitingRoom(p);
	}

	@Override
	public void onTeleport(Player p) {
		FightPits.removeFromWaitingRoom(p);
	}

	@Override
	public void tick(Player p) {
		FightPits.updateInterface(p);
	}

	@Override
	public String toString() {
		return "FIGHT PITS WAITING ROOM";
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
