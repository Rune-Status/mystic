package org.mystic.game.model.content.minigames.pestcontrol;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.utility.Misc;

public class PestControlConstants {

	public static final short GAME_START_TIME = 500;
	public static final String NEXT_DEPARTURE = "Next Departure: ";
	public static final String PLAYERS_READY = "Players Ready: ";
	public static final String PEST_POINTS = "Points: ";
	public static final int MAX_GAME_COUNT = 7;
	public static final int MAX_PLAYER_COUNT = 25;
	public static final int MIN_PLAYER_COUNT = 5;
	public static final int START_X = 2656;
	public static final int START_Y = 2609;
	public static final int START_X_MOD = 4;
	public static final int START_Y_MOD = 6;
	public static final Location VOID_KNIGHT_SPAWN = new Location(2656, 2592);
	public static final int VOID_KNIGHT_ID = 7203;

	public static final int[] PORTAL_IDS = { 6142, 6143, 6144, 6145 };

	public static final Location[] PORTAL_SPAWN_LOCATIONS = { new Location(2628, 2591), new Location(2680, 2588),
			new Location(2669, 2570), new Location(2645, 2569), };

	public static final int[] SPLATTERS = { 3727, 3728, 3729, 3730, 3731, };

	public static final int[] SHIFTERS = { 3732, 3733, 3734, 3735, 3736, 3737, 3738, 3739, 3740, 3741, };

	public static final int[] RAVAGERS = { 3742, 3743, 3744, 3745, 3746, };

	public static final int[] SPINNERS = { 3748, 3749, 3750, 3751 };

	public static final int[] TORCHERS = { 3752, 3753, 3754, 3755, 3756, 3757, 3758, 3759, 3760, 3761, };

	public static final int[] DEFILERS = { 3762, 3763, 3764, 3765, 3766, 3767, 3768, 3769, 3770, 3771, };

	public static final int[] BRAWLERS = { 3772, 3773, 3774, 3775, 3776, };

	public static final int[][] PESTS = { BRAWLERS, DEFILERS, TORCHERS, RAVAGERS, SPLATTERS, SPINNERS };

	public static Location getRandomBoatLocation(int z) {
		return new Location(START_X + Misc.randomNumber(START_X_MOD), START_Y + Misc.randomNumber(START_Y_MOD));
	}

	public static Npc getRandomPest(Location l, PestControlGame game, Portal portal) {
		int r = Misc.randomNumber(PESTS.length);
		final int id = PESTS[r][Misc.randomNumber(PESTS[r].length)];

		for (int i : SPLATTERS) {
			if (id == i) {
				return new Splatter(l, game);
			}
		}

		for (int i : SHIFTERS) {
			if (id == i) {
				return new Shifter(l, game);
			}
		}

		for (int i : SPINNERS) {
			if (id == i) {
				return new Spinner(l, game, portal);
			}
		}

		return new Pest(game, id, l) {

			@Override
			public void tick() {
			}

		};
	}

	public static void setLevels(Npc mob) {
		int level = mob.getDefinition().getLevel();

		mob.getLevels()[Skills.ATTACK] = (short) level;
		mob.getLevels()[Skills.STRENGTH] = (short) level;
		mob.getLevels()[Skills.DEFENCE] = (short) level;
		mob.getLevels()[Skills.MAGIC] = (short) level;
		mob.getLevels()[Skills.RANGED] = (short) level;
		mob.getLevels()[Skills.HITPOINTS] = (short) (level * 2);

		mob.getMaxLevels()[Skills.ATTACK] = (short) level;
		mob.getMaxLevels()[Skills.STRENGTH] = (short) level;
		mob.getMaxLevels()[Skills.DEFENCE] = (short) level;
		mob.getMaxLevels()[Skills.MAGIC] = (short) level;
		mob.getMaxLevels()[Skills.RANGED] = (short) level;
		mob.getMaxLevels()[Skills.HITPOINTS] = (short) (level * 2);
	}
}
