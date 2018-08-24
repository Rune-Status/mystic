package org.mystic.game.model.content.combat.impl;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.Hit.HitPriority;
import org.mystic.game.model.content.combat.Hit.HitTypes;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class RingOfRecoil implements CombatEffect2 {

	public static final String RECOIL_STAGE_KEY = "recoilhits";

	public static final String RECOIL_DAMAGE_KEY = "rordamage";

	public static void doRecoil(Player p, Entity e, int damage) {
		Item ring = p.getEquipment().getItems()[12];
		if ((ring != null) && (ring.getId() == 2550)) {
			int dmg = (int) Math.ceil(damage * 0.1D);
			if (dmg > 0) {
				p.getAttributes().set("rordamage", Integer.valueOf(dmg));
				new RingOfRecoil().execute(p, e);
				onRecoil(p, dmg);
			}
		}
	}

	public static void onRecoil(Player p, int dmg) {
		if (p.getAttributes().get("recoilhits") == null) {
			p.getAttributes().set("recoilhits", Integer.valueOf(dmg));
		} else {
			byte damage = (byte) (p.getAttributes().getByte("recoilhits") + dmg);
			if (damage >= 40) {
				p.getAttributes().remove("recoilhits");
				p.getEquipment().getItems()[12] = null;
				p.getEquipment().update(12);
				p.getClient().queueOutgoingPacket(new SendMessage("Your ring of recoil crumbles into dust."));
			} else {
				p.getAttributes().set("recoilhits", Byte.valueOf(damage));
			}
		}
	}

	@Override
	public void execute(Player p, Entity e) {
		if (e.isDead()) {
			return;
		}
		e.hit(new Hit(p.getAttributes().getInt("rordamage"), HitPriority.LOW_PRIORITY, HitTypes.DEFLECT));
		e.getAttributes().remove("rordamage");
	}
}
