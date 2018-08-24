package org.mystic.game.model.entity.npc;

import java.util.ArrayList;
import java.util.List;

import org.mystic.cache.map.Tile;

public class VirtualNpcRegion {

	private final List<Tile> occ = new ArrayList<Tile>();

	public VirtualNpcRegion() {

	}

	public VirtualNpcRegion(int baseX, int baseY, int size) {

	}

	public boolean isMobOnTile(int x, int y, int z) {
		if (z > 3) {
			z %= 4;
		}
		return occ.contains(new Tile(x, y, z));
	}

	public void setMobOnTile(int x, int y, int z, boolean set) {
		if (z > 3) {
			z %= 4;
		}
		if (set) {
			occ.add(new Tile(x, y, z));
		} else {
			occ.remove(new Tile(x, y, z));
		}
	}
}
