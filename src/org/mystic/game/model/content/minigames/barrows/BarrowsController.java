package org.mystic.game.model.content.minigames.barrows;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;
import org.mystic.utility.Misc;

public class BarrowsController extends Controller {

	private int drain = 50;

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
		return PlayerConstants.RESPAWN;
	}

	@Override
	public void onControllerInit(Player p) {
		p.getClient().queueOutgoingPacket(new SendWalkableInterface(4535));
		p.getClient().queueOutgoingPacket(new SendString("Killcount: " + p.getMinigames().getBarrowsKillcount(), 4536));
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
	public void tick(Player p) {
		p.getClient().queueOutgoingPacket(new SendWalkableInterface(4535));
		p.getClient().queueOutgoingPacket(new SendString("Killcount: " + p.getMinigames().getBarrowsKillcount(), 4536));
		if (p.inBarrows2()) {
			drain--;
			if (drain == 0) {
				if (p.getLevels()[Skills.PRAYER] > 0) {
					p.getSkill().deductFromLevel(Skills.PRAYER, 5 + Misc.random(15));
					p.send(new SendMessage("Your prayer has been drained.."));
				}
				drain = 30;
			}
		}
	}

	@Override
	public String toString() {
		return "BARROWS";
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return true;
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
		return false;
	}

	@Override
	public void onKill(Player player, Entity killed) {
		// TODO Auto-generated method stub

	}

}