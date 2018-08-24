package org.mystic.game.model.entity.player.controllers;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;

public abstract class GenericMinigameController extends Controller {

	@Override
	public abstract boolean allowMultiSpells();

	@Override
	public abstract boolean allowPvPCombat();

	@Override
	public abstract boolean canAttackNPC();

	@Override
	public abstract boolean canAttackPlayer(Player player, Player player2);

	@Override
	public boolean canClick() {
		return true;
	}

	@Override
	public abstract boolean canDrink(Player player);

	@Override
	public abstract boolean canEat(Player player);

	@Override
	public abstract boolean canEquip(Player player, int paramInt1, int paramInt2);

	@Override
	public abstract boolean canLogOut();

	@Override
	public abstract boolean canMove(Player player);

	@Override
	public abstract boolean canSave();

	@Override
	public abstract boolean canTalk();

	@Override
	public abstract boolean canTeleport();

	@Override
	public abstract boolean canTrade();

	@Override
	public abstract boolean canUseCombatType(Player player, CombatTypes type);

	@Override
	public abstract boolean canUsePrayer(Player player, int id);

	@Override
	public abstract boolean canUseSpecialAttack(Player player);

	@Override
	public abstract Location getRespawnLocation(Player player);

	@Override
	public abstract boolean isSafe(Player player);

	@Override
	public abstract void onControllerInit(Player player);

	@Override
	public void onKill(Player player, Entity killed) {
	}

	@Override
	public abstract void onDeath(Player player);

	@Override
	public abstract void onDisconnect(Player player);

	@Override
	public abstract void onTeleport(Player player);

	@Override
	public abstract void tick(Player player);

	@Override
	public abstract String toString();

	@Override
	public boolean transitionOnWalk(Player player) {
		return false;
	}
}
