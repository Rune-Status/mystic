package org.mystic.game.model.entity.npc.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class Kreearra extends Npc {

	public static final int KREEARRA = 6222;

	public static final int SPECIAL_CHANCE = 20;

	public static final int TORNADO_A = 1196;

	public static final int TORNADO_B = 1197;

	public static final int TORNADO_X = 2833;

	public static final int TORNADO_Y = 5302;

	private boolean tornados = false;

	public Kreearra() {
		super(6222, true, new Location(2831, 5303, 2));
	}

	public void checkForDamage(Location a) {
		for (Player player : getCombatants()) {
			Location b = player.getLocation();
			if ((Math.abs(a.getX() - b.getX()) <= 1) && (Math.abs(a.getY() - b.getY()) <= 1))
				player.hit(new Hit(Misc.randomNumber(20)));
		}
	}

	@Override
	public void doAliveMobProcessing() {
		if ((getCombat().getAttacking() != null) && (!tornados) && (Misc.randomNumber(20) == 0)) {
			tornados = true;
			initTornados();
		}
	}

	public void initTornados() {
		final Location a = new Location(2833, 5302, 2);
		final Location b = new Location(2833, 5302, 2);
		final Location c = new Location(2833, 5302, 2);
		final Location d = new Location(2833, 5302, 2);

		getUpdateFlags().sendForceMessage("Feel Armadyl's power!");

		TaskQueue.queue(new Task(1) {
			byte stage = 0;

			@Override
			public void execute() {
				if (isDead()) {
					stop();
					return;
				}

				a.move(1, 1);
				World.sendStillGraphic(1196, 0, a);
				checkForDamage(a);

				b.move(-1, 1);
				World.sendStillGraphic(1197, 0, b);
				checkForDamage(b);

				c.move(-1, -1);
				World.sendStillGraphic(1196, 0, c);
				checkForDamage(c);

				d.move(1, -1);
				World.sendStillGraphic(1197, 0, d);
				checkForDamage(d);

				if ((this.stage = (byte) (stage + 1)) == 5) {
					tornados = false;
					stop();
				}
			}

			@Override
			public void onStop() {
			}
		});
	}
}
