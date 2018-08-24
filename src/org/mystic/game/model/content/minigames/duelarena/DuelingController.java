package org.mystic.game.model.content.minigames.duelarena;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.ItemCheck;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendPlayerOption;
import org.mystic.utility.GameDefinitionLoader;

public class DuelingController extends Controller {

	@Override
	public boolean allowMultiSpells() {
		return false;
	}

	@Override
	public boolean allowPvPCombat() {
		return true;
	}

	@Override
	public boolean canAttackNPC() {
		return false;
	}

	@Override
	public boolean canAttackPlayer(Player p, Player p2) {
		if (p.getDueling().getInteracting() == null) {
			return false;
		}
		if (!p.getDueling().getInteracting().equals(p2)) {
			p.getClient().queueOutgoingPacket(new SendMessage("You are not dueling this player!"));
			return false;
		}
		if (!p.getDueling().canAttack()) {
			p.getClient().queueOutgoingPacket(new SendMessage("The duel hasn't started yet!"));
			return false;
		}
		return p.getDueling().canUseWeapon();
	}

	@Override
	public boolean canClick() {
		return true;
	}

	@Override
	public boolean canDrink(Player p) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[5]) {
			p.getClient().queueOutgoingPacket(new SendMessage("You can't use drinks during this duel!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canEat(Player p) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[6]) {
			p.getClient().queueOutgoingPacket(new SendMessage("You can't use food during this duel!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canEquip(Player p, int id, int slot) {
		if ((p.getDueling().getToRemove()[slot])
				|| ((slot == 3) && (p.getDueling().getToRemove()[5]) && (Item.getWeaponDefinition(id).isTwoHanded()))) {
			p.getClient().queueOutgoingPacket(new SendMessage("You can't wear this during this duel!"));
			return false;
		}
		if (p.getDueling().getRuleToggle()[9]) {
			if (!GameDefinitionLoader.getItemDef(id).getName().toLowerCase().contains("whip")) {
				p.getClient().queueOutgoingPacket(new SendMessage("You can't wear this during this duel!"));
				return false;
			}
		} else if (p.getDueling().getRuleToggle()[0]) {
			if ((id != 12006) && (id != 4151) && (!ItemCheck.isItemDyed(new Item(id))) && (id != 5698)
					&& (id != 1215)) {
				p.getClient().queueOutgoingPacket(new SendMessage("You can't wear this during this duel!"));
				return false;
			}
		}
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
	public boolean canLogOut() {
		return false;
	}

	@Override
	public boolean canMove(Player p) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[1]) {
			return false;
		}
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
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		switch (type) {
		case MAGIC:
			if (p.getDueling().getRuleToggle()[4]) {
				p.getClient().queueOutgoingPacket(new SendMessage("You can't use magic during this duel!"));
				return false;
			}
			break;
		case MELEE:
			if (p.getDueling().getRuleToggle()[3]) {
				p.getClient().queueOutgoingPacket(new SendMessage("You can't use melee during this duel!"));
				return false;
			}
			break;
		case RANGED:
			if (p.getDueling().getRuleToggle()[2]) {
				p.getClient().queueOutgoingPacket(new SendMessage("You can't use ranged during this duel!"));
				return false;
			}
			break;
		}
		return true;
	}

	@Override
	public boolean canUsePrayer(Player p, int id) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[7]) {
			p.getClient().queueOutgoingPacket(new SendMessage("You can't use prayer during this duel!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player p) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[10]) {
			p.getClient().queueOutgoingPacket(new SendMessage("You can't use special attacks during this duel!"));
			return false;
		}
		return true;
	}

	@Override
	public Location getRespawnLocation(Player player) {
		final Location p = DuelingConstants.RESPAWN_LOCATIONS[org.mystic.utility.Misc
				.randomNumber(DuelingConstants.RESPAWN_LOCATIONS.length)];
		int[] dir = org.mystic.game.GameConstants.DIR[org.mystic.utility.Misc
				.randomNumber(org.mystic.game.GameConstants.DIR.length)];
		return new Location(p.getX() + dir[0], p.getY() + dir[1]);
	}

	@Override
	public boolean isSafe(Player player) {
		return true;
	}

	@Override
	public void onControllerInit(Player player) {
		player.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
	}

	@Override
	public void onDeath(Player p) {
		p.duelLosses++;
		p.send(new SendMessage("@dre@You have lost the duel! You have now lost: " + p.getDuelLosses() + " duels."));
		QuestTab.update(p);
		p.getDueling().onDuelEnd(false, false);
		p.getUpdateFlags().sendAnimation(new Animation(860));
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
		return null;
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public void onKill(Player player, Entity killed) {

	}
}