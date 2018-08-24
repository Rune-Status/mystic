package org.mystic.game.model.entity.npc.impl;

import java.util.List;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class DarkEnergyCore extends Npc {

	public static final int CORPOREAL_BEAST_INDEX = 1;

	public static final int DARK_ENERGY_CORE_ID = 8127;

	public static final Npc getCorp() {
		return World.getNpcs()[1];
	}

	private static final Projectile getProjectile() {
		return new Projectile(1828);
	}

	public static final Npc[] spawn() {
		List<Player> players = getCorp().getCombatants();

		Npc[] cores = new Npc[players.size()];

		for (int i = 0; i < players.size(); i++) {
			Location l = new Location(players.get(i).getLocation());
			l.move(1, 0);
			cores[i] = new DarkEnergyCore(l, players.get(i));
		}

		return cores;
	}

	public boolean moving = false;

	private final Player bind;

	private byte pause = -1;

	public DarkEnergyCore(Location location, Player bind) {
		super(8127, false, location, null, false, false, null);
		this.bind = bind;
	}

	public Hit getHit() {
		return new Hit(this, Misc.randomNumber(10), Hit.HitTypes.NONE);
	}

	@Override
	public void onHit(Entity e, Hit hit) {
		Npc corp = getCorp();
		int tmp9_8 = 3;
		short[] tmp9_5 = corp.getLevels();
		tmp9_5[tmp9_8] = ((short) (tmp9_5[tmp9_8] + hit.getDamage() / 4));

		if (corp.getLevels()[3] > corp.getMaxLevels()[3])
			corp.getLevels()[3] = corp.getMaxLevels()[3];
	}

	@Override
	public void process() {
		if ((bind.isDead()) || (!bind.isActive()) || (getCorp().isDead())
				|| (!getCorp().getCombatants().contains(bind))) {
			remove();
			return;
		}

		if ((!moving) && (!isDead()))
			if ((Math.abs(bind.getLocation().getX() - getLocation().getX()) <= 1)
					&& (Math.abs(bind.getLocation().getY() - getLocation().getY()) <= 1)) {
				bind.hit(getHit());
			} else {
				if (pause == -1) {
					pause = 4;
				}

				if ((this.pause = (byte) (pause - 1)) == 0) {
					pause = -1;
					travel();
				}
			}
	}

	public void travel() {
		moving = true;

		final int lockon = -bind.getIndex() - 1;
		final byte offsetX = (byte) ((bind.getLocation().getY() - bind.getLocation().getY()) * -1);
		final byte offsetY = (byte) ((bind.getLocation().getX() - bind.getLocation().getX()) * -1);

		final Projectile p = getProjectile();

		TaskQueue.queue(new Task(this, 1) {
			byte stage = 0;

			@Override
			public void execute() {
				if (stage == 0) {
					World.sendProjectile(p, getLocation(), lockon, offsetX, offsetY);
				} else if (stage == 1) {
					getUpdateFlags().sendAnimation(10393, 0);
					face(bind);
				} else if (stage == 2) {
					setVisible(false);
				} else if (stage == 4) {
					moving = false;
					teleport(new Location(bind.getLocation().getX() + 1, bind.getLocation().getY()));
					setVisible(true);
					stop();
				}

				stage = ((byte) (stage + 1));
			}

			@Override
			public void onStop() {
			}
		});
	}
}
