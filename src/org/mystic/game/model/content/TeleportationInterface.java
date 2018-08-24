package org.mystic.game.model.content;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;

import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendString;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * Manages the main teleportation interface
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class TeleportationInterface {

	/**
	 * The teleport interface id constant
	 */
	public static final int INTERFACE_ID = 51000;

	public static void open(Player player, TeleportType type) {
		for (int index = 0; index < 17; index++) {
			player.send(new SendString(" ", 40505 + index));
		}
		for (Teleports data : Teleports.values()) {
			if (data.getType() == type) {
				player.send(new SendString(data.getName(), data.getLine()));
			}
		}
		player.setType(type);
		player.send(new SendInterface(INTERFACE_ID));
	}

	public static void click(Player player, TeleportType type, int buttonId) {
		Optional<Teleports> data = Teleports.forId(type, buttonId);
		if (!data.isPresent()) {
			return;
		}
		player.setPrevious(data.get().getLocation());
		player.getMagic().teleport(data.get().getLocation().getX(), data.get().getLocation().getY(),
				data.get().getLocation().getZ(), TeleportTypes.WIZARD);
	}

	public enum TeleportType {
		MONSTERS, DUNGEONS, BOSSES, WILDERNESS, MINIGAMES, AREAS, GUILDS
	}

	public enum Teleports {

		MAGE_BANK(TeleportType.WILDERNESS, "Mage bank", 40505, 180181, new Location(2540, 4715, 0)),

		WEST_DRAGONS(TeleportType.WILDERNESS, "West Dragons", 40506, 180184, new Location(2958, 3615, 0)),

		EAST_DRAGONS(TeleportType.WILDERNESS, "East Dragons", 40507, 180188, new Location(3361, 3687, 0)),

		GRAVEYARD(TeleportType.WILDERNESS, "Graveyard", 40508, 180191, new Location(3158, 3670, 0)),

		DESERTED_KEEP(TeleportType.WILDERNESS, "Deserted Keep", 40509, 180194, new Location(3153, 3923, 0)),

		OBELISKS(TeleportType.WILDERNESS, "Level 44 Obelisk", 40510, 180197, new Location(2974, 3868, 0)),

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		ROCK_CRABS(TeleportType.MONSTERS, "Rock Crabs", 40505, 180181, new Location(2707, 3719, 0)),

		COWS(TeleportType.MONSTERS, "Cows", 40506, 180184, new Location(3260, 3274, 0)),

		YAKS(TeleportType.MONSTERS, "Yaks", 40507, 180188, new Location(2325, 3804, 0)),

		GORILLAS(TeleportType.MONSTERS, "Monkey Guards", 40508, 180191, new Location(2785, 2786, 0)),

		EXPERIMENTS(TeleportType.MONSTERS, "Experiments", 40509, 180194, new Location(3484, 9940, 0)),

		ELF(TeleportType.MONSTERS, "Elf Warriors", 40510, 180197, new Location(2186, 3147, 0)),

		WHITE_KNIGHTS(TeleportType.MONSTERS, "White Knights", 40511, 180200, new Location(2964, 3360, 0)),

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		TORMENTED_DEMONS(TeleportType.BOSSES, "Tormented Demons", 40505, 180181, new Location(2582, 5728, 0)),

		DAGANNOTH_KINGS(TeleportType.BOSSES, "Dagannoth Kings", 40506, 180184, new Location(1908, 4365, 0)),

		BANDOS(TeleportType.BOSSES, "Bandos", 40507, 180188, new Location(2861, 5354, 2)),

		SARADOMIN(TeleportType.BOSSES, "Saradomin", 40508, 180191, new Location(2910, 5265, 0)),

		ARMADYL(TeleportType.BOSSES, "Armadyl", 40509, 180194, new Location(2839, 5293, 2)),

		ZAMORAK(TeleportType.BOSSES, "Zamorak", 40510, 180197, new Location(2925, 5335, 2)),

		CORP(TeleportType.BOSSES, "Corporeal Beast", 40511, 180200, new Location(2924, 4384, 0)),

		BORK(TeleportType.BOSSES, "Bork", 40512, 180203, new Location(3113, 5528, 0)),

		MOLE(TeleportType.BOSSES, "Giant Mole", 40513, 180206, new Location(1760, 5181, 0)),

		KALPHITE_QUEEN(TeleportType.BOSSES, "Kalphite Queen", 40514, 180209, new Location(3504, 9494, 0)),

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		BARROWS(TeleportType.MINIGAMES, "Barrows", 40505, 180181, new Location(3565, 3316, 0)),

		PESTCONTROL(TeleportType.MINIGAMES, "Pest Control", 40506, 180184, new Location(2658, 2661, 0)),

		WARRIORS(TeleportType.MINIGAMES, "Warriors Guild", 40507, 180188, new Location(2877, 3546, 0)),

		FIGHTPITS(TeleportType.MINIGAMES, "Fight Caves", 40508, 180191, new Location(2445, 5175, 0)),

		DUEL_ARENA(TeleportType.MINIGAMES, "Duel Arena", 40509, 180194, new Location(3356, 3268, 0)),

		FIGHT_PITS(TeleportType.MINIGAMES, "Fight Pits", 40510, 180197, new Location(3356, 3268, 0)),

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		GNOME(TeleportType.AREAS, "Gnome Course", 40505, 180181, new Location(2473, 3437, 0)),

		ESSENCE(TeleportType.AREAS, "Essence Mine", 40506, 180184, new Location(2899, 4818, 0)),

		WARRIORS_GUILD(TeleportType.AREAS, "Warriors Guild", 40507, 180188, new Location(2877, 3546, 0)),

		MINING_GUILD(TeleportType.AREAS, "Mining Guild", 40508, 180191, new Location(3016, 3339, 0)),

		FISHING_GUILD(TeleportType.AREAS, "Fishing Guild", 40509, 180194, new Location(2589, 3421, 0)),

		CRAFTING_GUILD(TeleportType.AREAS, "Crafting Guild", 40510, 180197, new Location(2935, 3283, 0)),

		DRAYNOR(TeleportType.AREAS, "Draynor Village", 40511, 180200, new Location(3081, 3251, 0)),

		APE_ATOLL(TeleportType.AREAS, "Ape Atoll", 40512, 180203, new Location(2758, 2781, 0)),

		CATHERBY(TeleportType.AREAS, "Catherby", 40513, 180206, new Location(2809, 3435, 0)),

		PISCATORIS(TeleportType.AREAS, "Piscatoris Fishing Colony", 40514, 180209, new Location(2331, 3680, 0)),

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		SLAYER_TOWER(TeleportType.DUNGEONS, "Slayer Tower", 40505, 180181, new Location(3428, 3538, 0)),

		LUMBRIDGE_DUNGEON(TeleportType.DUNGEONS, "Lumbridge Swamp Cave", 40506, 180184, new Location(3167, 9572, 0)),

		FREMENNIK_DUNGEON(TeleportType.DUNGEONS, "Fremennik Slayer Dungeon", 40507, 180188,
				new Location(2807, 10003, 0)),

		BRIMHAVEN_DUNGEON(TeleportType.DUNGEONS, "Brimhaven Dungeon", 40508, 180191, new Location(2693, 9564, 0)),

		TAVERLY_DUNGEON(TeleportType.DUNGEONS, "Taverly Dungeon", 40509, 180194, new Location(2884, 9796, 0)),

		WATERBIRTH_DUNGEON(TeleportType.DUNGEONS, "Waterbirth Dungeon", 40510, 180197, new Location(2524, 3740, 0)),

		ANCIENT_CAVERN(TeleportType.DUNGEONS, "Ancient Cavern", 40511, 180200, new Location(1767, 5366, 1)),

		KALPHITE_LAIR(TeleportType.DUNGEONS, "Kalphite Lair", 40512, 180203, new Location(3498, 9510, 2)),

		ASGARNIAN_DUNGEON(TeleportType.DUNGEONS, "Asgarnian Dungeon", 40513, 180206, new Location(2996, 9546, 0)),

		CAVE_OF_HORRORS(TeleportType.DUNGEONS, "Cave Of Horrors", 40514, 180209, new Location(3744, 9374, 0));

		private final TeleportType type;

		private final String name;

		private final int interface_line, buttonId;

		private final Location loc;

		public final static ImmutableSet<Teleports> TELES = Sets.immutableEnumSet(EnumSet.allOf(Teleports.class));

		public static Optional<Teleports> forId(TeleportType type, int button) {
			return TELES.stream().filter(Objects::nonNull).filter(t -> t.getType() == type)
					.filter(t -> t.getButtonId() == button).findFirst();
		}

		Teleports(final TeleportType type, final String name, final int interface_line, final int buttonId,
				final Location loc) {
			this.type = type;
			this.name = name;
			this.interface_line = interface_line;
			this.buttonId = buttonId;
			this.loc = loc;
		}

		public TeleportType getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public int getLine() {
			return interface_line;
		}

		public int getButtonId() {
			return buttonId;
		}

		public Location getLocation() {
			return loc;
		}
	}
}