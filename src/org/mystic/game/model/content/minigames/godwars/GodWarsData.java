package org.mystic.game.model.content.minigames.godwars;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.entity.player.Player;

public class GodWarsData {

	private static final int[] ARMADYL_ITEMS = new int[] { 12512, 12510, 12508, 12506, 12478, 12476, 12474, 12472,
			12470, 12263, 12261, 12259, 12257, 12255, 12253, 11830, 11828, 11826, 11802, 11785, 87, 84 };

	private static final int[] BANDOS_ITEMS = new int[] { 11726, 11728, 11724 };

	private static final int[] SARADOMIN_ITEMS = new int[] { 10470, 10464, 10458, 10452, 10446, 10440, 10390, 10388,
			10386, 10384, 6762, 3479, 2667, 2665, 2663, 2661, 2415, 2412, 3840 };

	private static final int[] ZAMORAK_ITEMS = new int[] { 1033, 1035, 2414, 2417, 2653, 2655, 2657, 2659, 3478, 10368,
			10370, 10372, 10374, 6764, 10444, 10450, 10456, 10460, 10468, 10474, 11808, 11824, 11889, 12638, 3842 };

	public enum Allegiance {
		ARMADYL, BANDOS, SARADOMIN, ZAMORAK
	}

	public enum GodWarsNpc {

		/**
		 * Bandos
		 */
		GENERAL_GRAARDOR(6260, Allegiance.BANDOS),

		SERGEANT_STRONGSTACK(6261, Allegiance.BANDOS),

		SERGEANT_STEELWILL(6263, Allegiance.BANDOS),

		SERGEANT_GRIMSPIKE(6265, Allegiance.BANDOS),

		GOBLIN(6283, Allegiance.BANDOS),

		HOBGOBLIN(6275, Allegiance.BANDOS),

		OGRE(2233, Allegiance.BANDOS),

		JOGRE(2234, Allegiance.BANDOS),

		ORK(6271, Allegiance.BANDOS),

		BANDOS_SPIRITUAL_RANGER(6276, Allegiance.BANDOS),

		BANDOS_SPIRITUAL_WARRIOR(6277, Allegiance.BANDOS),

		BANDOS_SPIRITUAL_MAGE(6278, Allegiance.BANDOS),

		/**
		 * Aramdyl
		 */
		KREE_ARRA(3162, Allegiance.ARMADYL),

		WINGMAN_SKREE(3163, Allegiance.ARMADYL),

		FLOCKLEADER_GEERIN(3164, Allegiance.ARMADYL),

		FLIGHT_KILISA(3165, Allegiance.ARMADYL),

		AVIANSIE_79(3170, Allegiance.ARMADYL),

		AVIANSIE_125(3172, Allegiance.ARMADYL),

		AVIANSIE_97(3174, Allegiance.ARMADYL),

		AVIANSIE_148(3176, Allegiance.ARMADYL),

		ARMADYL_SPIRITUAL_WARRIOR(3166, Allegiance.ARMADYL),

		ARMADYL_SPIRITUAL_RANGER(3167, Allegiance.ARMADYL),

		ARMADYL_SPIRITUAL_MAGE(3168, Allegiance.ARMADYL),

		/**
		 * Saradomin
		 */
		COMMANDER_ZILYANA(6247, Allegiance.SARADOMIN),

		STARLIGHT(6248, Allegiance.SARADOMIN),

		GROWLER(6250, Allegiance.SARADOMIN),

		BREE(6252, Allegiance.SARADOMIN),

		SARADOMIN_PRIEST(6254, Allegiance.SARADOMIN),

		KNIGHT_OF_SARADOMIN(6258, Allegiance.SARADOMIN),

		SARADOMIN_SPIRITUAL_WARRIOR(6255, Allegiance.SARADOMIN),

		SARADOMIN_SPIRITUAL_RANGER(6256, Allegiance.SARADOMIN),

		SARADOMIN_SPIRITUAL_MAGE(6257, Allegiance.SARADOMIN),

		/**
		 * Zamorak
		 */
		KRILL_TSUTSAROTH(3129, Allegiance.ZAMORAK),

		WEREWOLF(3136, Allegiance.ZAMORAK),

		VAMPYRE(3137, Allegiance.ZAMORAK),

		BLOODVELD(3138, Allegiance.ZAMORAK),

		PYREFIEND(3139, Allegiance.ZAMORAK),

		ICEFIEND(3140, Allegiance.ZAMORAK),

		GORAK(3141, Allegiance.ZAMORAK),

		HELLHOUND(3133, Allegiance.ZAMORAK),

		ZAMORAK_SPIRITUAL_WARRIOR(3159, Allegiance.ZAMORAK),

		ZAMORAK_SPIRITUAL_RANGER(3160, Allegiance.ZAMORAK),

		ZAMORAK_SPIRITUAL_MAGE(3161, Allegiance.ZAMORAK);

		private final int npc;

		private final Allegiance allegiance;

		private GodWarsNpc(final int npc, final Allegiance allegiance) {
			this.npc = npc;
			this.allegiance = allegiance;
		}

		public final int getNpc() {
			return npc;
		}

		public final Allegiance getAllegiance() {
			return allegiance;
		}

	}

	private static final Map<Integer, GodWarsNpc> godWarsNpcs = new HashMap<Integer, GodWarsNpc>();

	static {
		for (GodWarsNpc npc : GodWarsNpc.values()) {
			godWarsNpcs.put(Integer.valueOf(npc.getNpc()), npc);
		}
	}

	public static GodWarsNpc forId(int id) {
		return godWarsNpcs.get(Integer.valueOf(id));
	}

	public static boolean bossNpc(GodWarsNpc npc) {
		switch (npc) {
		case KRILL_TSUTSAROTH:
		case COMMANDER_ZILYANA:
		case STARLIGHT:
		case GROWLER:
		case BREE:
		case KREE_ARRA:
		case WINGMAN_SKREE:
		case FLOCKLEADER_GEERIN:
		case FLIGHT_KILISA:
		case GENERAL_GRAARDOR:
		case SERGEANT_STRONGSTACK:
		case SERGEANT_STEELWILL:
		case SERGEANT_GRIMSPIKE:
			return true;
		default:
			return false;
		}
	}

	public static boolean isProtected(Player player, GodWarsNpc npc) {
		switch (npc) {
		case KRILL_TSUTSAROTH:
		case COMMANDER_ZILYANA:
		case STARLIGHT:
		case GROWLER:
		case BREE:
		case KREE_ARRA:
		case WINGMAN_SKREE:
		case FLOCKLEADER_GEERIN:
		case FLIGHT_KILISA:
		case GENERAL_GRAARDOR:
		case SERGEANT_STRONGSTACK:
		case SERGEANT_STEELWILL:
		case SERGEANT_GRIMSPIKE:
			return false;
		default:
			if (npc.getAllegiance() == Allegiance.ARMADYL) {
				for (int item : ARMADYL_ITEMS) {
					if (player.getEquipment().contains(item)) {
						return true;
					}
				}
			} else if (npc.getAllegiance() == Allegiance.BANDOS) {
				for (int item : BANDOS_ITEMS) {
					if (player.getEquipment().contains(item)) {
						return true;
					}
				}
			} else if (npc.getAllegiance() == Allegiance.SARADOMIN) {
				for (int item : SARADOMIN_ITEMS) {
					if (player.getEquipment().contains(item)) {
						return true;
					}
				}
			} else if (npc.getAllegiance() == Allegiance.ZAMORAK) {
				for (int item : ZAMORAK_ITEMS) {
					if (player.getEquipment().contains(item)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}