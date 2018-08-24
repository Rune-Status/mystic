package org.mystic.game.model.content.skill.summoning.impl;

import org.mystic.game.model.content.skill.summoning.FamiliarMob;
import org.mystic.game.model.content.skill.summoning.FamiliarSpecial;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class Wolpertinger implements FamiliarSpecial {

	@Override
	public boolean execute(Player player, FamiliarMob mob) {
		int id = 6;
		short[] level = player.getLevels();
		level[id] = ((short) (level[id] + (short) (int) (player.getMaxLevels()[6] * 0.08)));
		if (player.getLevels()[6] > 106) {
			player.getLevels()[6] = 106;
		}
		player.getSkill().update(6);
		player.getClient().queueOutgoingPacket(new SendMessage("Your wolpertinger boosts your magical ability."));
		player.getUpdateFlags().sendAnimation(new Animation(7660));
		player.getUpdateFlags().sendGraphic(new Graphic(1306, 0, false));
		mob.getUpdateFlags().sendGraphic(new Graphic(1464, 0, false));
		mob.getUpdateFlags().sendAnimation(8267, 0);
		return true;
	}

	@Override
	public int getAmount() {
		return 10;
	}

	@Override
	public double getExperience() {
		return 20.0D;
	}

	@Override
	public FamiliarSpecial.SpecialType getSpecialType() {
		return FamiliarSpecial.SpecialType.NONE;
	}

}
