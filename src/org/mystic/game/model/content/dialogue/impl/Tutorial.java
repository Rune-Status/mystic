package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.entity.player.controllers.DefaultController;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendSidebarInterface;
import org.mystic.game.model.networking.packet.impl.ChangeAppearancePacket;
import org.mystic.game.model.player.io.PlayerSave;
import org.mystic.game.model.player.io.PlayerSaveUtil;

public class Tutorial extends Dialogue {

	public static class TutorialController extends DefaultController {

		@Override
		public boolean canAttackNPC() {
			return false;
		}

		@Override
		public boolean canClick() {
			return true;
		}

		@Override
		public boolean canMove(Player p) {
			return false;
		}

		@Override
		public boolean canSave() {
			return false;
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
		public void onDisconnect(Player p) {

		}

		@Override
		public boolean transitionOnWalk(Player p) {
			return false;
		}
	}

	public static final TutorialController TUTORIAL_CONTROLLER = new TutorialController();

	public Tutorial(Player player) {
		this.player = player;
		player.setController(TUTORIAL_CONTROLLER);
		for (int i = 0; i < PlayerConstants.SIDEBAR_INTERFACE_IDS.length; i++) {
			player.send(new SendSidebarInterface(i, -1));
		}
		player.getEquipment().updateSidebar();
		player.send(new SendSidebarInterface(0, -1));
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9178:
			if (option == 1) {
				next = 3;
				execute();
			}
			return true;
		case 9179:
			if (option == 1) {
				next = 4;
				execute();
			}
			return true;
		case 9180:
			if (option == 1) {
				next = 5;
				execute();
			}
			return true;
		case 9181:
			if (option == 1) {
				next = 6;
				execute();
			}
			return true;
		}
		return false;
	}

	public void completeTutorial(Player player) {
		ChangeAppearancePacket.setToDefault(player);
		if (!PlayerSaveUtil.hasReceived2Starters(player)) {
			PlayerConstants.doStarter(player, true);
			PlayerSaveUtil.setReceivedStarter(player);
		} else {
			PlayerConstants.doStarter(player, false);
		}
		for (int i = 0; i < PlayerConstants.SIDEBAR_INTERFACE_IDS.length; i++) {
			if (i != PlayerConstants.PRAYER_TAB && i != PlayerConstants.MAGIC_TAB) {
				player.send(new SendSidebarInterface(i, PlayerConstants.SIDEBAR_INTERFACE_IDS[i]));
			}
		}
		player.getMagic().setMagicBook(1151);
		player.setPrayerInterface(5608);
		player.send(new SendSidebarInterface(PlayerConstants.PRAYER_TAB, player.getPrayerInterface()));
		player.getAchievements().incr(player, "Learning the ropes");
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
		player.send(new SendRemoveInterfaces());
		player.getDialogue().end();
		QuestTab.update(player);
		player.setAppearanceUpdateRequired(true);
		PlayerSave.save(player);
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			nChat(new String[] { "Welcome to Mystic, " + player.getUsername() + "!" });
			break;
		case 1:
			nChat(new String[] { "Before I let you go please select your game mode." });
			break;
		case 2:
			DialogueManager.sendOption(player, new String[] { "Regular Mode", "@cr8@ Iron Man",
					"@cr9@ Ultimate Iron Man", "@cr10@ Hardcore Iron Man" });
			option = 1;
			break;
		case 3:
			nChat(new String[] { "You have selected to play as a Regular player." });
			next = 7;
			break;
		case 4:
			nChat(new String[] { "You have chosen to play as an @cr8@ Iron man." });
			player.setRights(Rights.IRON_MAN);
			next = 7;
			break;
		case 5:
			nChat(new String[] { "You have chosen to play as a @cr9@ Ultimate Iron man." });
			player.setRights(Rights.ULTIMATE_IRON_MAN);
			next = 7;
			break;
		case 6:
			nChat(new String[] { "You have chosen to play as a @cr10@ Hardcore Iron man." });
			player.setRights(Rights.HARDCORE_IRON_MAN);
			next = 7;
			break;
		case 7:
			completeTutorial(player);
			end();
			break;
		}
	}

	public void nChat(String[] chat) {
		DialogueManager.sendNpcChat(player, 945, Emotion.HAPPY_TALK, chat);
		next += 1;
	}

	public void pChat(String[] chat) {
		DialogueManager.sendPlayerChat(player, Emotion.HAPPY_TALK, chat);
		next += 1;
	}

	public void tele(int x, int y) {
		player.teleport(new Location(x, y, 0));
	}

	public void tele(int x, int y, int z) {
		player.teleport(new Location(x, y, z));
	}
}
