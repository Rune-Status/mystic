package org.mystic.game.model.content.skill.slayer;

public class SlayerTasks {

	public enum Low {

		CRAWLING_HAND(1648, new String[] { "This monster can be found in the Slayer Tower." }, 5),

		ROCK_CRAB(1265, new String[] { "This monster can be found in Rellekka." }, 1),

		CHAOS_DRUID(181, new String[] { "This monster can be found in the Edgeville Dungeon", "and Taverly Dungeon." },
				1),

		YAK(5529, new String[] { "This monster can be found on the Neitiznot Island." }, 1),

		CAVE_CRAWLER(1600, new String[] { "This monster can be found in the Fremennik Slayer Dungeon." }, 10),

		ROCK_SLUG(1631, new String[] { "This monster can be found in the Fremennik Slayer Dungeon." }, 20),

		COCKATRICE(1620, new String[] { "This monster can be found in the Fremennik Slayer Dungeon." }, 25),

		PYREFIEND(1633, new String[] { "This monster can be found in the Fremennik Slayer Dungeon." }, 30),

		KALPHITE_WORKER(1153, new String[] { "This monster can be found in the Kalphite Lair." }, 1),

		HOB_GOBLIN(2687, new String[] { "This monster can be found in the Asgarnian Ice Dungeon." }, 1),

		ICE_WARRIOR(125, new String[] { "This monster can be found in the Asgarnian Ice Dungeon." }, 1),

		ICE_GIANT(111, new String[] { "This monster can be found in the Asgarnian Ice Dungeon." }, 1),

		MOSS_GIANT(112, new String[] { "This monster can be found in the Brimhaven Dungeon", "and Edgeville Dungeon." },
				1),

		HILL_GIANT(117, new String[] { "This monster can be found in the Brimhaven Dungeon", "and Edgeville Dungeon." },
				1),

		BANSHEE(1612, new String[] { "This monster can be found in the Slayer Tower." }, 15),

		GHOST(103, new String[] { "This monster can be found in the Taverly Dungeon." }, 1),

		GIANT_BAT(78, new String[] { "This monster can be found in the Taverly Dungeon." }, 1),

		CHAOS_DWARF(119, new String[] { "This monster can be found in the Taverly Dungeon." }, 1);

		private final int mobId;

		private final String[] location;

		private final int level;

		private Low(final int mobId, final String[] location, final int level) {
			this.mobId = mobId;
			this.location = location;
			this.level = level;
		}

		public int getMobId() {
			return mobId;
		}

		public String[] getLocation() {
			return location;
		}

		public int getLevel() {
			return level;
		}
	}

	public static SlayerTasks.Low forLow(int mobId) {
		for (SlayerTasks.Low data : SlayerTasks.Low.values()) {
			if (mobId == data.getMobId()) {
				return data;
			}
		}
		return null;
	}

	public enum Medium {

		GREEN_DRAGONS(941, new String[] { "This monster can be found in the Wilderness." }, 1),

		GORILLAS(1459, new String[] { "This monster can be found in Ape Atoll." }, 1),

		CAVE_HORRORS(4353, new String[] { "This monster can be found in Cave Of Horrors." }, 58),

		BASILISK(1616, new String[] { "This monster can be found in Fremennik Slayer Dungeon." }, 40),

		JELLY(1637, new String[] { "This monster can be found in Fremennik Slayer Dungeon." }, 52),

		TUROTH(1626, new String[] { "This monster can be found in Fremennik Slayer Dungeon." }, 55),

		KALPHITE_SOLIDER(1154, new String[] { "This monster can be found in the Kalphite Lair." }, 1),

		ELF(1184, new String[] { "This monster can be found in the Elf Warriors teleport" }, 1),

		INFERNAL_MAGE(1643, new String[] { "This monster can be found in the Slayer Tower." }, 45),

		FIRE_GIANT(110, new String[] { "This monster can be found in the Brimhaven Dungeon." }, 1),

		LESSER_DEMON(82, new String[] { "This monster can be found in the Taverly Dungeon." }, 1);

		private final int mobId;

		private final String[] location;

		private final int level;

		private Medium(final int mobId, final String[] location, final int level) {
			this.mobId = mobId;
			this.location = location;
			this.level = level;
		}

		public int getMobId() {
			return mobId;
		}

		public String[] getLocation() {
			return location;
		}

		public int getLevel() {
			return level;
		}
	}

	public static SlayerTasks.Medium forMedium(int mobId) {
		for (SlayerTasks.Medium data : SlayerTasks.Medium.values()) {
			if (mobId == data.getMobId()) {
				return data;
			}
		}
		return null;
	}

	public enum Boss {

		BORK(7133, new String[] { "This monster can be found using the boss teleport." }, 85),

		ARMA(6222, new String[] { "This monster can be found using the boss teleport." }, 85),

		BANDOS(6260, new String[] { "This monster can be found using the boss teleport." }, 85),

		ZIL(6247, new String[] { "This monster can be found using the boss teleport." }, 85),

		ZAMMY(6203, new String[] { "This monster can be found using the boss teleport." }, 85),

		CHAOS(3200, new String[] { "This monster can be found using the boss teleport." }, 85);

		private final int mobId;

		private final String[] location;

		private final int level;

		private Boss(final int mobId, final String[] location, final int level) {
			this.mobId = mobId;
			this.location = location;
			this.level = level;
		}

		public int getMobId() {
			return mobId;
		}

		public String[] getLocation() {
			return location;
		}

		public int getLevel() {
			return level;
		}
	}

	public static SlayerTasks.Boss forBoss(int mobId) {
		for (SlayerTasks.Boss data : SlayerTasks.Boss.values()) {
			if (mobId == data.getMobId()) {
				return data;
			}
		}
		return null;
	}

	public enum High {

		ABYSSAL_DEMON(1615, new String[] { "This monster can be found in Slayer Tower." }, 85),

		RED_DRAGON(53, new String[] { "This monster can be found in Brimhaven Dungeon." }, 1),

		BLACK_DRAGON(54, new String[] { "This monster can be found in Taverly Dungeon." }, 1),

		BLUE_DRAGONS(55, new String[] { "This monster can be found in Taverly Dungeon." }, 1),

		HELL_HOUND(49, new String[] { "This monster can be found in Taverly Dungeon." }, 1),

		AQUANITE(9172, new String[] { "This monster can be found in Slayer Dungeon 1." }, 78),

		KURASK(1608, new String[] { "This monster can be found in Fremennik Slayer Dungeon." }, 70),

		WATERFIEND(5361, new String[] { "This monster can be found in the Ancient Cavern." }, 1),

		MITHRIL_DRAGON(5363, new String[] { "This monster can be found in the Ancient Cavern." }, 1),

		BLOODVELD(1618, new String[] { "This monster can be found in the Slayer Tower." }, 50),

		ABERRANT_SPECTRE(1604, new String[] { "This monster can be found in the Slayer Tower." }, 60),

		GARGOYLE(1610, new String[] { "This monster can be found in the Slayer Tower." }, 75),

		NECHRYAEL(1613, new String[] { "This monster can be found in the Slayer Tower." }, 80),

		DARK_BEAST(2783, new String[] { "This monster can be found in the Slayer Tower." }, 90),

		DAGANNOTH(2455, new String[] { "This monster can be found in the Waterbirth Dungeon." }, 1),

		BLACK_DEMON(84, new String[] { "This monster can be found in the Taverly Dungeon", "and Brimhaven Dungeon." },
				1),

		ICE_WORM(9463, new String[] { "This monster can be found in the cold part of the wilderness!" }, 93),

		BRONZE_DRAGON(1590, new String[] { "This monster can be found in the Brimhaven Dungeon." }, 1),

		IRON_DRAGON(1591, new String[] { "This monster can be found in the Brimhaven Dungeon." }, 1),

		STEEL_DRAGON(1592, new String[] { "This monster can be found in the Brimhaven Dungeon." }, 1);

		private final int mobId;

		private final String[] location;

		private final int level;

		private High(final int mobId, final String[] location, final int level) {
			this.mobId = mobId;
			this.location = location;
			this.level = level;
		}

		public int getMobId() {
			return mobId;
		}

		public String[] getLocation() {
			return location;
		}

		public int getLevel() {
			return level;
		}
	}

	public static SlayerTasks.High forHigh(int mobId) {
		for (SlayerTasks.High data : SlayerTasks.High.values()) {
			if (mobId == data.getMobId()) {
				return data;
			}
		}
		return null;
	}

}