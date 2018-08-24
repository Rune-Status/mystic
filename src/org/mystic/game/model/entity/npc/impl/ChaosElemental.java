package org.mystic.game.model.entity.npc.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.Misc;

public class ChaosElemental extends Npc {

	public ChaosElemental() {
		super(3200, true, new Location(3277, 3921, 0));
	}

	private boolean attacking() {
		if (getCombat().getAttacking() != null) {
			if (!getCombat().getAttacking().isNpc()) {
				return true;
			}
		}
		return false;
	}

	private void equipmentSpecial() {
		if (attacking()) {
			final Player player = getCombat().getAttacking().getPlayer();
			World.sendProjectile(getProjectile(556), player, this);
			if (player.getEquipment().getItems()[3] != null) {
				if (player.getInventory().getFreeSlots() == 0) {
					int id = player.getInventory().getSlotId(0);
					player.getGroundItems().dropFull(id, 0);
				}
				player.getEquipment().unequip(3);
				player.send(new SendMessage("The chaos elemental has removed some of your worn equipment."));
				player.getUpdateFlags().sendGraphic(new Graphic(557));
			}
		}
	}

	private Projectile getProjectile(int id) {
		return new Projectile(id, 1, 40, 70, 43, 31, 16);
	}

	@Override
	public int getRespawnTime() {
		return 60;
	}

	@Override
	public void hit(Hit hit) {
		if (isDead()) {
			return;
		}
		super.hit(hit);
		int random = Misc.random(1, 11);
		if (random == 5) {
			teleportSpecial();
		} else if (random == 10) {
			equipmentSpecial();
		}
	}

	private void teleportSpecial() {
		if (attacking()) {
			final Player player = (Player) getCombat().getAttacking();
			World.sendProjectile(getProjectile(553), player, this);
			player.teleport(
					new Location(player.getX() - Misc.randomNumber(3), player.getY() - Misc.randomNumber(3), 0));
			player.getUpdateFlags().sendGraphic(new Graphic(554));
		}
	}

}