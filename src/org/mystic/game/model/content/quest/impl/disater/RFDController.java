package org.mystic.game.model.content.quest.impl.disater;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.quest.QuestConstants;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.entity.player.controllers.GenericMinigameController;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class RFDController extends GenericMinigameController {

	@Override
	public void onDeath(Player player) {
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(3098, 3512, 0);
	}

	@Override
	public void onDisconnect(Player player) {
		player.getQuesting().setQuestActive(QuestConstants.RECIPE_FOR_DISASTER, false);
		player.teleport(getRespawnLocation(player));
	}

	@Override
	public void tick(Player player) {
	}

	@Override
	public boolean canAttackNPC() {
		return true;
	}

	@Override
	public boolean canAttackPlayer(Player player, Player player2) {
		return false;
	}

	@Override
	public boolean allowMultiSpells() {
		return false;
	}

	@Override
	public boolean allowPvPCombat() {
		return false;
	}

	@Override
	public void onControllerInit(Player player) {
	}

	@Override
	public boolean canUseCombatType(Player player, CombatTypes type) {
		return true;
	}

	@Override
	public boolean canEquip(Player player, int id, int slot) {
		return true;
	}

	@Override
	public boolean canEat(Player player) {
		return true;
	}

	@Override
	public boolean canDrink(Player player) {
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player player) {
		return true;
	}

	@Override
	public String toString() {
		return "Recipe For Disaster";
	}

	@Override
	public boolean canLogOut() {
		return true;
	}

	@Override
	public boolean canMove(Player player) {
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
	public boolean canUsePrayer(Player player, int id) {
		player.send(new SendMessage("You can't use prayers here."));
		return false;
	}

	@Override
	public boolean isSafe(Player player) {
		return true;
	}

	@Override
	public void onTeleport(Player player) {
	}

	@Override
	public boolean canUnequip(Player player) {
		return true;
	}

	@Override
	public boolean canDrop(Player player) {
		return false;
	}

}