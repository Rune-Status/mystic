package org.mystic.game.model.content.skill.prayer;

import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.skill.prayer.impl.CursesPrayerBook;
import org.mystic.game.model.content.skill.prayer.impl.DefaultPrayerBook;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendSidebarInterface;

public abstract class PrayerBook {

	public enum PrayerBookType {
		DEFAULT, CURSES;
	}

	public static void setBook(Player player, PrayerBookType type) {
		player.getPrayer().disable();
		if (type == PrayerBookType.DEFAULT) {
			player.setPrayerInterface(5608);
			player.setPrayer(new DefaultPrayerBook(player));
		} else if (type == PrayerBookType.CURSES) {
			player.setPrayerInterface(21356);
			player.setPrayer(new CursesPrayerBook(player));
		}
		player.getClient().queueOutgoingPacket(new SendSidebarInterface(5, player.getPrayerInterface()));
	}

	protected byte[] active = null;

	protected byte[] drain = null;

	protected byte headIcon = -1;

	protected Player player = null;

	protected boolean flag = false;

	public boolean active(int id) {
		return active[id] == 1;
	}

	public abstract boolean clickButton(int paramInt);

	public void disable() {
		for (int i = 0; i < active.length; i++) {
			active[i] = 0;
			onDisable(i);
		}
		headIcon = -1;
		player.setAppearanceUpdateRequired(true);
	}

	public void disable(int i) {
		active[i] = 0;
		onDisable(i);
	}

	public abstract void disableProtection(int paramInt);

	public abstract void doEffectOnHit(Entity paramEntity, Hit paramHit);

	public void drain() {
		int amount = 0;
		for (int i = 0; i < active.length; i++) {
			if (active[i] == 1) {
				if (++drain[i] >= getAffectedDrainRate(i) / 2) {
					amount++;
					drain[i] = 0;
				}
			}
		}
		if (amount > 0) {
			drain(amount);
		}
	}

	public void drain(int drain) {
		final int prayer = player.getSkill().getLevels()[5];
		if (drain >= prayer) {
			for (int i = 0; i < this.drain.length; i++) {
				this.drain[i] = 0;
			}
			disable();
			player.getSkill().setLevel(5, 0);
			player.getClient().queueOutgoingPacket(
					new SendMessage("You have run out of prayer points; you must recharge at an altar."));
		} else {
			player.getSkill().deductFromLevel(5, drain < 1 ? 1 : (int) Math.ceil(drain));
			if (player.getSkill().getLevels()[5] <= 0) {
				disable();
				player.getClient().queueOutgoingPacket(
						new SendMessage("You have run out of prayer points; you must recharge at an altar."));
			}
		}
	}

	public boolean flag() {
		return flag;
	}

	public double getAffectedDrainRate(int id) {
		return getDrainRate(id) * (1 + 0.035 * player.getBonuses()[EquipmentConstants.PRAYER]);
	}

	public abstract int getDamage(Hit paramHit);

	public abstract double getDrainRate(int paramInt);

	public int getHeadicon() {
		return headIcon;
	}

	public byte getHeadIcon() {
		return headIcon;
	}

	public PrayerBookType getPrayerBookType() {
		if (player.getPrayerInterface() == 21356) {
			return PrayerBookType.CURSES;
		}
		return PrayerBookType.DEFAULT;
	}

	public abstract boolean isProtectionActive();

	public abstract void onDisable(int paramInt);

	public abstract void process();

	public void setHeadIcon(byte headIcon) {
		this.headIcon = headIcon;
	}

	public abstract void toggle(int paramInt);

}