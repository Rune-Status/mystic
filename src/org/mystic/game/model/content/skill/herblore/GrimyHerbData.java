package org.mystic.game.model.content.skill.herblore;

import java.util.HashMap;
import java.util.Map;

public enum GrimyHerbData {

	GUAM(199, 249, 3, 3),

	MARRENTILL(201, 251, 5, 4),

	TARROMIN(203, 253, 11, 5),

	HARRALANDER(205, 255, 20, 6),

	RANARR(207, 257, 25, 8),

	TOADFLAX(3049, 2998, 30, 8),

	SPIRITWEED(12174, 12172, 35, 8),

	IRIT(209, 259, 40, 9),

	WERGALI(14836, 14854, 30, 8),

	AVANTOE(211, 261, 48, 10),

	KWUARM(213, 263, 54, 11),

	SNAPDRAGON(3051, 3000, 59, 12),

	CADANTINE(215, 265, 65, 13),

	LANTADYME(2485, 2481, 67, 13),

	DWARFWEED(217, 267, 70, 14),

	TORSTOL(219, 269, 75, 15);

	private static final Map<Integer, GrimyHerbData> herbs = new HashMap<Integer, GrimyHerbData>();

	static {
		for (GrimyHerbData data : values()) {
			herbs.put(Integer.valueOf(data.getGrimyHerb()), data);
		}
	}

	public static GrimyHerbData forId(int herbId) {
		return herbs.get(Integer.valueOf(herbId));
	}

	private final int grimyHerb, cleanHerb, levelReq, cleaningExp;

	private GrimyHerbData(final int grimyHerb, final int cleanHerb, final int levelReq, final int cleaningExp) {
		this.grimyHerb = grimyHerb;
		this.cleanHerb = cleanHerb;
		this.levelReq = levelReq;
		this.cleaningExp = cleaningExp;
	}

	public final int getCleanHerb() {
		return cleanHerb;
	}

	public final int getExp() {
		return cleaningExp;
	}

	public final int getGrimyHerb() {
		return grimyHerb;
	}

	public final int getLevelReq() {
		return levelReq;
	}

}