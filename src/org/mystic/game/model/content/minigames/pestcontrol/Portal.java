package org.mystic.game.model.content.minigames.pestcontrol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mystic.cache.map.Region;
import org.mystic.game.GameConstants;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.utility.Misc;

public class Portal extends Npc {

	private final List<Npc> pests = new ArrayList<Npc>();

	private final List<Npc> shifters = new ArrayList<Npc>();

	private final PestControlGame game;

	public Portal(PestControlGame game, int id, Location p, int z) {
		super(game.getVirtualRegion(), id, false, false, new Location(p, z));
		setRetaliate(false);
		init();

		getLevels()[Skills.HITPOINTS] = 250;
		getLevels()[Skills.DEFENCE] = 250;
		getLevels()[Skills.MAGIC] = 250;

		getMaxLevels()[Skills.HITPOINTS] = 250;
		getMaxLevels()[Skills.DEFENCE] = 250;
		getMaxLevels()[Skills.MAGIC] = 250;

		setRetaliate(false);
		setRespawnable(false);

		this.game = game;

		getAttributes().set(PestControlGame.PEST_GAME_KEY, game);
	}

	public void cleanup() {
		if (pests.size() > 0) {
			for (Npc i : pests) {
				i.remove();
			}
		}

		if (shifters.size() > 0) {
			for (Npc i : shifters) {
				i.remove();
			}
		}
	}

	@Override
	public Animation getDeathAnimation() {
		return new Animation(65535, 0);
	}

	public List<Npc> getPests() {
		return pests;
	}

	public void heal(int amount) {
		getLevels()[3] += amount;

		if (getLevels()[3] > getMaxLevels()[3])
			getLevels()[3] = getMaxLevels()[3];
	}

	public void init() {
	}

	public boolean isDamaged() {
		return getLevels()[3] < getMaxLevels()[3];
	}

	@Override
	public void onDeath() {
		remove();
	}

	@Override
	public void process() {
		if (isDead()) {
			return;
		}

		for (Iterator<Npc> i = pests.iterator(); i.hasNext();) {
			if (i.next().isDead()) {
				i.remove();
			}
		}

		for (Iterator<Npc> i = shifters.iterator(); i.hasNext();) {
			if (i.next().isDead()) {
				i.remove();
			}
		}

		if (Misc.randomNumber(35) == 0 && pests.size() < 3) {
			Location l = GameConstants.getClearAdjacentLocation(getLocation(), getSize(), game.getVirtualRegion());

			if (l != null) {
				pests.add(PestControlConstants.getRandomPest(l, game, this));
			}
		}

		if (game.getPlayers().size() > 6) {
			if (Misc.randomNumber(35) == 0 && shifters.size() < 1) {
				int baseX = 2656;
				int baseY = 2592;

				int x = baseX + (Misc.randomNumber(2) == 0 ? Misc.randomNumber(7) : -Misc.randomNumber(7));
				int y = baseY + (Misc.randomNumber(2) == 0 ? Misc.randomNumber(7) : -Misc.randomNumber(7));

				while (Region.getRegion(x, y).getClip(x, y, 0) == 256) {
					x = baseX + (Misc.randomNumber(2) == 0 ? Misc.randomNumber(7) : -Misc.randomNumber(7));
					y = baseY + (Misc.randomNumber(2) == 0 ? Misc.randomNumber(7) : -Misc.randomNumber(7));
				}

				shifters.add(new Shifter(new Location(x, y, game.getZ()), game));
			}
		}
	}
}
