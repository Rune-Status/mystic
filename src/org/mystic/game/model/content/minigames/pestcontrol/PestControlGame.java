package org.mystic.game.model.content.minigames.pestcontrol;

import java.util.List;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.VirtualNpcRegion;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;
import org.mystic.utility.Misc;

public class PestControlGame {

	public static final String PEST_DAMAGE_KEY = "pestdamagekey";
	public static final String PEST_GAME_KEY = "pestgamekey";
	private final List<Player> players;
	private Npc voidKnight;

	private final int z;
	private final VirtualNpcRegion region;
	private final Portal[] portals;

	private int time = 500;

	private boolean ended = false;

	public PestControlGame(List<Player> players, int count) {
		this.players = players;
		this.region = new VirtualNpcRegion();
		this.z = (count << 2);
		this.portals = new Portal[] {
				new Portal(this, PestControlConstants.PORTAL_IDS[0], PestControlConstants.PORTAL_SPAWN_LOCATIONS[0], z),
				new Portal(this, PestControlConstants.PORTAL_IDS[1], PestControlConstants.PORTAL_SPAWN_LOCATIONS[1], z),
				new Portal(this, PestControlConstants.PORTAL_IDS[2], PestControlConstants.PORTAL_SPAWN_LOCATIONS[2], z),
				new Portal(this, PestControlConstants.PORTAL_IDS[3], PestControlConstants.PORTAL_SPAWN_LOCATIONS[3],
						z), };
		init();
	}

	public void end(boolean success) {
		ended = true;
		for (Portal i : portals) {
			i.remove();
		}
		voidKnight.remove();
		for (Player p : players) {
			p.teleport(new Location(2657, 2639));
			p.setController(ControllerManager.DEFAULT_CONTROLLER);
			p.getCombat().reset();
			p.getMagic().setVengeanceActive(false);
			p.resetLevels();
			p.curePoison(0);
			if (success) {
				if (p.getAttributes().get(PEST_DAMAGE_KEY) != null && p.getAttributes().getInt(PEST_DAMAGE_KEY) >= 80) {
					DialogueManager.sendNpcChat(p, 3789, Emotion.HAPPY_TALK,
							"You have managed to destroy all the portals!",
							"We've awarded you with Void Knight Commendation",
							"points and some coins to show our appreciation.");
					p.getInventory().addOrCreateGroundItem(995, p.getAttributes().getInt(PEST_DAMAGE_KEY) * 6, true);
					p.setPestPoints(p.getPestPoints() + (p.isGoldMember() ? 7 : 5));
					QuestTab.update(p);
				} else {
					DialogueManager.sendNpcChat(p, 3789, Emotion.CALM,
							"You were successful but did not contribute enough",
							"to the void knights. Try harder next time!");
				}
			} else {
				DialogueManager.sendNpcChat(p, 3789, Emotion.SAD, "The Void Knight has fallen!", "All hope is lost..");
			}
			p.getAttributes().remove(PEST_DAMAGE_KEY);
			p.getAttributes().remove(PEST_GAME_KEY);
		}
		for (Portal i : portals) {
			i.cleanup();
		}
		voidKnight.remove();
		PestControl.onGameEnd(this);
	}

	public int getAttackers(Player p) {
		int i = 0;
		for (Portal k : portals) {
			for (Npc j : k.getPests()) {
				if (j.getCombat().getAttacking() != null && j.getCombat().getAttacking().equals(p)) {
					i++;
				}
			}
		}
		return i;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public VirtualNpcRegion getVirtualRegion() {
		return region;
	}

	public Npc getVoidKnight() {
		return voidKnight;
	}

	public int getZ() {
		return z;
	}

	public boolean hasEnded() {
		return ended;
	}

	public void init() {
		for (Player p : players) {
			p.teleport(new Location(2656 + Misc.randomNumber(4), 2609 + Misc.randomNumber(6), z));
			p.getClient().queueOutgoingPacket(new SendWalkableInterface(21100));
			p.getAttributes().set(PEST_DAMAGE_KEY, 0);
			p.getAttributes().set(PEST_GAME_KEY, this);
			p.setController(ControllerManager.PEST_CONTROLLER);
			p.getSpecialAttack().setSpecialAmount(100);
			p.curePoison(0);
			DialogueManager.sendNpcChat(p, 7203, Emotion.CALM, "Go with strength!",
					"Defend the void knight and destroy the portals!", "You are our only hope!");
		}
		voidKnight = new Npc(region, 7203, false, false, new Location(PestControlConstants.VOID_KNIGHT_SPAWN, z));
		voidKnight.getLevels()[Skills.HITPOINTS] = 200;
		voidKnight.getMaxLevels()[Skills.HITPOINTS] = 200;
		voidKnight.getLevels()[Skills.DEFENCE] = 300;
		voidKnight.setRespawnable(false);
		voidKnight.getAttributes().set(PEST_GAME_KEY, this);
	}

	public void process() {
		time--;
		if (time <= 0) {
			end(false);
			return;
		}
		if (voidKnight.isDead()) {
			end(false);
			return;
		}
		if (!portals[0].isActive() && !portals[1].isActive() && !portals[2].isActive() && !portals[3].isActive()) {
			end(true);
		}
		for (Player p : players) {
			p.getClient().queueOutgoingPacket(new SendString(((time / 100) + 1) + " minutes", 21117));
			p.getClient().queueOutgoingPacket(new SendString("" + voidKnight.getLevels()[Skills.HITPOINTS], 21115));
			for (int i = 0; i < 4; i++) {
				boolean dead = portals[i].isDead();
				p.getClient().queueOutgoingPacket(
						new SendString((dead ? "@red@" : "") + portals[i].getLevels()[Skills.HITPOINTS], 21111 + i));
			}
			if (p.getAttributes().get(PEST_DAMAGE_KEY) != null) {
				int damage = p.getAttributes().getInt(PEST_DAMAGE_KEY);
				p.getClient().queueOutgoingPacket(new SendString(
						(damage >= 80 ? "" : "@red@") + p.getAttributes().getInt(PEST_DAMAGE_KEY), 21116));
			}
		}
	}

	public void remove(Player p) {
		players.remove(p);
		if (players.size() == 0) {
			for (Portal i : portals) {
				i.cleanup();
			}
			voidKnight.remove();
			PestControl.onGameEnd(this);
		}
	}

}