package org.mystic.game.model.entity.player.controllers;

import org.mystic.game.model.content.dialogue.impl.Tutorial.TutorialController;
import org.mystic.game.model.content.minigames.barrows.BarrowsController;
import org.mystic.game.model.content.minigames.castlewars.CastleWars;
import org.mystic.game.model.content.minigames.castlewars.CastleWarsLobbyController;
import org.mystic.game.model.content.minigames.clanwars.SafeWars;
import org.mystic.game.model.content.minigames.clanwars.SafeWarsController;
import org.mystic.game.model.content.minigames.duelarena.DuelArenaController;
import org.mystic.game.model.content.minigames.duelarena.DuelStakeController;
import org.mystic.game.model.content.minigames.duelarena.DuelingController;
import org.mystic.game.model.content.minigames.fightcave.CavesController;
import org.mystic.game.model.content.minigames.fightpits.FightPits;
import org.mystic.game.model.content.minigames.fightpits.FightPitsController;
import org.mystic.game.model.content.minigames.fightpits.FightPitsWaitingController;
import org.mystic.game.model.content.minigames.godwars.GodWarsController;
import org.mystic.game.model.content.minigames.pestcontrol.PestControl;
import org.mystic.game.model.content.minigames.pestcontrol.PestControlController;
import org.mystic.game.model.content.minigames.pestcontrol.PestWaitingRoomController;
import org.mystic.game.model.content.wilderness.WildernessController;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.task.impl.ForceMovementController;

public class ControllerManager {

	public static final TutorialController TUTORIAL_CONTROLLER = new TutorialController();
	public static final BarrowsController BARROWS_CONTROLLER = new BarrowsController();
	public static final DefaultController DEFAULT_CONTROLLER = new DefaultController();
	public static final WildernessController WILDERNESS_CONTROLLER = new WildernessController();
	public static final DuelArenaController DUEL_ARENA_CONTROLLER = new DuelArenaController();
	public static final DuelingController DUELING_CONTROLLER = new DuelingController();
	public static final DuelStakeController DUEL_STAKE_CONTROLLER = new DuelStakeController();
	public static final FightPitsController FIGHT_PITS_CONTROLLER = new FightPitsController();
	public static final FightPitsWaitingController FIGHT_PITS_WAITING_CONTROLLER = new FightPitsWaitingController();
	public static final CavesController TZHARR_CAVES_CONTROLLER = new CavesController();
	public static final PestWaitingRoomController PEST_WAITING_ROOM_CONTROLLER = new PestWaitingRoomController();
	public static final ForceMovementController FORCE_MOVEMENT_CONTROLLER = new ForceMovementController();
	public static final GodWarsController GOD_WARS_CONTROLLER = new GodWarsController();
	public static final Controller PEST_CONTROLLER = new PestControlController();
	public static final SafeWarsController SAFE_WARS = new SafeWarsController();
	public static final CastleWarsLobbyController CASTLEWARS_LOBBY = new CastleWarsLobbyController();

	public static void onForceLogout(Player player) {
		Controller controller = player.getController();
		if (controller.equals(CASTLEWARS_LOBBY)) {
			CastleWars.exitLobby(player, player.getTeam());
		} else if (controller.equals(DUELING_CONTROLLER)) {
			player.getDueling().onForceLogout();
		} else if (controller.equals(FIGHT_PITS_WAITING_CONTROLLER)) {
			player.teleport(new Location(2399, 5177));
			FightPits.removeFromWaitingRoom(player);
		} else if (controller.equals(FIGHT_PITS_CONTROLLER)) {
			player.teleport(new Location(2399, 5177));
			FightPits.removeFromGame(player);
		} else if (controller.equals(PEST_WAITING_ROOM_CONTROLLER)) {
			PestControl.clickObject(player, 14314);
		} else if (controller.equals(FORCE_MOVEMENT_CONTROLLER) && !player.inWilderness()) {
			player.teleport(PlayerConstants.HOME);
		} else if (controller.equals(SAFE_WARS)) {
			SafeWars.interactPortal(player, true);
		}
	}

	public static void setControllerOnWalk(Player player) {
		if ((player.getController() != null) && (!player.getController().transitionOnWalk(player))) {
			return;
		}
		Controller controller = DEFAULT_CONTROLLER;
		if (player.inWilderness()) {
			controller = WILDERNESS_CONTROLLER;
		} else if (player.inDuelArena()) {
			controller = DUEL_ARENA_CONTROLLER;
		} else if (player.inGodwars()) {
			controller = GOD_WARS_CONTROLLER;
		} else if (player.inBarrows()) {
			controller = BARROWS_CONTROLLER;
		}
		if ((controller == null) || (player.getController() == null) || (!player.getController().equals(controller))) {
			player.setController(controller);
		}
	}
}
