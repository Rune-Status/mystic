package org.mystic.game.model.content.sound;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendSound;

/**
 * 
 * Handles the sending of many sounds to the player
 * 
 * @author Valli - http://www.rune-server.org/members/Valiant
 * @since December 20th, 2016.
 *
 */
public class SoundPlayer {

	public enum Sounds {

		PICKPOCKET_STUN(458, 0, 0),

		CLICK_PIN_BUTTON(319, 0, 0),

		G_MAUL_SPEC(1082, 0, 18),

		DRAGON_MACE_SPEC(387, 0, 5),

		DRAGON_SPEAR_SPEC(388, 0, 5),

		DRAGON_BAXE_SPEC(389, 0, 5),

		MAGIC_BOW_SPEC(386, 0, 5),

		WHIP_SPEC(1081, 0, 9),

		DRAGON_DAGGER_SPEC(385, 0, 5),

		COOK_FOOD(357, 0, 0),

		TELEPORT_NORMAL(202, 0, 10),

		TELEPORT_NORMAL_END(201, 0, 10),

		TELEPORT_ANCIENTS(1048, 0, 40),

		ATTEMPT_LIGHT_FIRE(375, 0, 0),

		LIGHT_FIRE(235, 0, 0),

		CREATE_POTION(281, 0, 0),

		HIGH_ALCHEMY(223, 0, 0),

		LOW_ALCHEMY(224, 0, 0),

		BURY_BONE(380, 1, 10),

		CATCH_FISH(378, 0, 0),

		CAST_LINE(289, 0, 0),

		CREATE_JEWELRY(469, 0, 0),

		CUT_GEM(464, 0, 0),

		DRINK_POTION(334, 1, 0),

		EAT_FOOD(317, 0, 4),

		FILL_VIAL(371, 0, 0),

		RUNECRAFT(481, 0, 15),

		PICKUP_ITEM(358, 1, 8),

		RESTORE_PRAYER(442, 1, 0),

		SMITH(468, 0, 10),

		CHOP_TREE(472, 1, 0),

		CHOP_TREE_DOWN(1312, 5, 0),

		DROP_ITEM(376, 1, 0),

		DIG(380, 0, 0),

		DOOR(326, 0, 0);

		private final int soundId, type, delay;

		Sounds(final int soundId, final int type, final int delay) {
			this.soundId = soundId;
			this.type = type;
			this.delay = delay;
		}

		public final int getId() {
			return soundId;
		}

		public final int getType() {
			return type;
		}

		public final int getDelay() {
			return delay;
		}

	}

	public static void play(Player player, Sounds sound) {
		player.send(new SendSound(sound.getId(), sound.getType(), sound.getDelay()));
	}

	public static void play(Player player, int sound, int type, int delay) {
		player.send(new SendSound(sound, type, delay));
	}

}