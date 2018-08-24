package org.mystic.game.model.content.skill.summoning.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.skill.summoning.FamiliarMob;
import org.mystic.game.model.content.skill.summoning.FamiliarSpecial;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.player.Player;

public class SpiritSpider implements FamiliarSpecial {
	@Override
	public boolean execute(Player player, FamiliarMob mob) {
		Location a = new Location(player.getX() + 1, player.getY(), player.getZ());
		Location b = new Location(player.getX(), player.getY() + 1, player.getZ());
		GroundItemHandler.add(new Item(223), a, player);
		GroundItemHandler.add(new Item(223), b, player);
		World.sendStillGraphic(1342, 0, a);
		World.sendStillGraphic(1342, 0, b);
		return true;
	}

	@Override
	public int getAmount() {
		return 6;
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
