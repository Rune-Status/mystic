package org.mystic.game.model.content.minigames.fightcave;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.Controller;

public final class CavesController extends Controller {

	public static final String MINIGAME = "Tzharr Fight Caves";

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
		return FightCave.LEAVE;
	}

	@Override
	public void onControllerInit(Player p) {
	}

	@Override
	public void onDeath(Player player) {
		FightCave.finish(player, false);
	}

	@Override
	public void onDisconnect(Player p) {
	}

	@Override
	public void onTeleport(Player p) {
	}

	@Override
	public void tick(Player p) {
	}

	@Override
	public String toString() {
		return "Tzharr Fight Caves";
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
		return true;
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
