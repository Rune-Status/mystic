package org.mystic.game.model.content.minigames.clanwars;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendPlayerOption;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;
import org.mystic.utility.Misc;

public class SafeWarsController extends Controller {

	@Override
	public void tick(Player paramPlayer) {

	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canMove(Player paramPlayer) {
		return true;
	}

	@Override
	public boolean canSave() {
		return false;
	}

	@Override
	public boolean canClick() {
		return true;
	}

	@Override
	public boolean canAttackNPC() {
		return false;
	}

	@Override
	public boolean canAttackPlayer(Player p1, Player p2) {
		if (p1 == null || p2 == null) {
			return false;
		}
		if (!p1.getController().equals(this) || p1.getY() <= 5511) {
			p1.getClient().queueOutgoingPacket(new SendMessage("You are outside the combat zone. Go north to fight."));
			return false;
		}
		if (!p2.getController().equals(this) || p2.getY() <= 5511) {
			p1.getClient().queueOutgoingPacket(new SendMessage("This player is outside the combat zone."));
			return false;
		}
		return true;
	}

	@Override
	public boolean allowMultiSpells() {
		return true;
	}

	@Override
	public boolean allowPvPCombat() {
		return true;
	}

	@Override
	public void onDeath(Player paramPlayer) {
	}

	@Override
	public Location getRespawnLocation(Player player) {
		if (Misc.randomNumber(1) == 0) {
			return new Location(2809 - Misc.randomNumber(3), 5509);
		} else {
			return new Location(2809 + Misc.randomNumber(3), 5509);
		}
	}

	@Override
	public boolean canLogOut() {
		return true;
	}

	@Override
	public void onDisconnect(Player paramPlayer) {
		SafeWars.interactPortal(paramPlayer, true);
	}

	@Override
	public void onControllerInit(Player player) {
		player.getClient().queueOutgoingPacket(new SendWalkableInterface(4535));
		player.getClient().queueOutgoingPacket(new SendString("@or1@Clan Wars (@gre@Safe@or1@)", 4536));
		player.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
		player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
	}

	@Override
	public boolean canUseCombatType(Player paramPlayer, CombatTypes paramCombatTypes) {
		return true;
	}

	@Override
	public boolean canTeleport() {
		return false;
	}

	@Override
	public void onTeleport(Player paramPlayer) {
	}

	@Override
	public boolean canTrade() {
		return false;
	}

	@Override
	public boolean canEquip(Player paramPlayer, int paramInt1, int paramInt2) {
		return true;
	}

	@Override
	public boolean canEat(Player paramPlayer) {
		return true;
	}

	@Override
	public boolean canDrink(Player paramPlayer) {
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player paramPlayer) {
		return true;
	}

	@Override
	public boolean transitionOnWalk(Player paramPlayer) {
		return false;
	}

	@Override
	public String toString() {
		return "Clan Wars Safe PK";
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