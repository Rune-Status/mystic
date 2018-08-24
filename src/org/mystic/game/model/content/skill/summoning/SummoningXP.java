package org.mystic.game.model.content.skill.summoning;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class SummoningXP {

	public enum Charms {

		GOLD(12158, 50),

		GREEN(12159, 100),

		CRIMSON(12160, 150),

		BLUE(12163, 200);

		public static Optional<Charms> forCharm(int id) {
			Predicate<Charms> charm = (Charms b) -> b.itemId == id;
			return Arrays.stream(values()).filter(charm).findFirst();
		}

		private final int itemId, experience;

		private Charms(final int itemId, final int experience) {
			this.itemId = itemId;
			this.experience = experience;
		}

	}

	public static void process(final Player player, int itemId) {
		Optional<Charms> charm = Charms.forCharm(itemId);
		if (!charm.isPresent()) {
			return;
		}
		if (player.getSkill().locked()) {
			player.send(new SendMessage("You must wait a second before doing this."));
			return;
		}
		player.getSkill().lock(2);
		player.getInventory().remove(itemId, 1);
		player.getUpdateFlags().sendAnimation(new Animation(8502));
		player.getSkill().addExperience(Skills.SUMMONING, charm.get().experience);
		player.send(new SendMessage("You use the charm on the obelisk."));
	}

}