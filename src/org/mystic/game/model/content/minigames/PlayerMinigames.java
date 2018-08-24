package org.mystic.game.model.content.minigames;

import java.util.Arrays;

import org.mystic.game.model.content.minigames.barrows.Barrows;
import org.mystic.game.model.content.minigames.godwars.GodWarsData.Allegiance;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendString;

public class PlayerMinigames {

	private final Player player;

	private boolean[] barrowsKilled = new boolean[6];

	public int tunnel = 0;

	public PlayerMinigames(Player player) {
		this.player = player;
	}

	private short[] killcount = new short[Allegiance.values().length];

	public short[] getGWKC() {
		return killcount;
	}

	public void setGWKC(short[] kc) {
		this.killcount = kc;
	}

	public void changeGWDKills(int delta, Allegiance allegiance) {
		killcount[allegiance.ordinal()] += delta;
		updateGWKC(allegiance);
	}

	public int getGWDKills(Allegiance allegiance) {
		return killcount[allegiance.ordinal()];
	}

	public void updateGWKC(Allegiance allegiance) {
		player.send(new SendString(String.valueOf(getGWDKills(allegiance)), 16216 + allegiance.ordinal()));
	}

	public void resetGWD() {
		Arrays.fill(killcount, (short) 0);
		updateGWKC(Allegiance.ARMADYL);
		updateGWKC(Allegiance.BANDOS);
		updateGWKC(Allegiance.SARADOMIN);
		updateGWKC(Allegiance.ZAMORAK);
	}

	public int getBarrowsKillcount() {
		int i = 0;
		for (boolean k : barrowsKilled) {
			if (k) {
				i++;
			}
		}
		return i;
	}

	public boolean[] getBarrowsKilled() {
		return barrowsKilled;
	}

	public int getTunnel() {
		return tunnel;
	}

	public boolean killedBarrow(int i) {
		return barrowsKilled[i];
	}

	public void resetBarrows() {
		barrowsKilled = new boolean[6];
		Barrows.assignTunnel(player);
		for (int i = 2025; i <= 2030; i++) {
			player.getAttributes().remove("barrowsActive" + i);
		}
	}

	public void setBarrowKilled(int i) {
		barrowsKilled[i] = true;
	}

	public void setBarrowsKilled(boolean[] barrowsKilled) {
		this.barrowsKilled = barrowsKilled;
	}

	public int setTunnel(int tunnel) {
		return this.tunnel = tunnel;
	}

}