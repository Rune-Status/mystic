package org.mystic.game.model.content.minigames.fightcave;

import java.util.ArrayList;
import java.util.List;

import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;

public final class WaveInformation {

	private int stage = 0;

	private List<Npc> mobs = new ArrayList<Npc>();

	private int z;

	public void addNpc(Npc mob) {
		mobs.add(mob);
	}

	public int getKillAmount() {
		return mobs.size();
	}

	public List<Npc> getMobs() {
		return mobs;
	}

	public int getStage() {
		return stage;
	}

	public int getZ() {
		return z;
	}

	public void increaseStage() {
		stage++;
	}

	public boolean removeNpc(Npc mob) {
		int index = mobs.indexOf(mob);
		if (index == -1) {
			return false;
		}
		mobs.remove(mob);
		return true;
	}

	public void reset() {
		stage = 0;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public void setZ(Player p) {
		z = (p.getIndex() * 4);
	}

}
