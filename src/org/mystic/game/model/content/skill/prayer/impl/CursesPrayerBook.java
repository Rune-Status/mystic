package org.mystic.game.model.content.skill.prayer.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.Hit.HitTypes;
import org.mystic.game.model.content.skill.prayer.PrayerBook;
import org.mystic.game.model.content.skill.prayer.PrayerSounds;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.networking.outgoing.SendConfig;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendOpenTab;
import org.mystic.game.model.networking.outgoing.SendSidebarInterface;
import org.mystic.game.task.RunOnceTask;
import org.mystic.game.task.TaskQueue;

public class CursesPrayerBook extends PrayerBook {

	public static final int CURSES_PROTECT_ITEM = 0;

	public static final int SAP_WARRIOR = 1;

	public static final int SAP_RANGER = 2;

	public static final int SAP_MAGE = 3;

	public static final int SAP_SPIRIT = 4;

	public static final int BERSERKER = 5;

	public static final int DEFLECT_SUMMONING = 6;

	public static final int DEFLECT_MAGIC = 7;

	public static final int DEFLECT_MISSILES = 8;

	public static final int DEFLECT_MELEE = 9;

	public static final int LEECH_ATTACK = 10;

	public static final int LEECH_RANGED = 11;

	public static final int LEECH_MAGIC = 12;

	public static final int LEECH_DEFENCE = 13;

	public static final int LEECH_STRENGTH = 14;

	public static final int LEECH_ENERGY = 15;

	public static final int LEECH_SPECIAL_ATTACK = 16;

	public static final int WRATH = 17;

	public static final int SOUL_SPLIT = 18;

	public static final int TURMOIL = 19;

	public static final int[] CURSE_CONFIG_IDS = { 724, 725, 726, 727, 728, 729, 730, 731, 732, 733, 734, 735, 736, 737,
			738, 739, 740, 741, 742, 743 };

	public static final String[] CURSE_NAMES = { "Protect Item", "Sap Warrior", "Sap Ranger", "Sap Mage", "Sap Spirit",
			"Berserker", "Deflect Summoning", "Deflect Magic", "Deflect Missiles", "Deflect Melee", "Leech Attack",
			"Leech Ranged", "Leech Magic", "Leech Defence", "Leech Strength", "Leech Energy", "Leech Special Attack",
			"Wrath", "Soul Split", "Turmoil" };

	public static final int[] CURSE_DRAIN_RATES = { 30, 6, 6, 6, 6, 12, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2 };

	public static final int[][] CURSE_DISABLED_PRAYERS = {

			new int[0],

			{ 2, 3, 4, 10, 11, 12, 13, 14, 15, 16, 19, 19 },

			{ 1, 3, 4, 10, 11, 12, 13, 14, 15, 16, 19, 19 },

			{ 1, 2, 4, 10, 11, 12, 13, 14, 15, 16, 19, 19 },

			{ 1, 2, 3, 10, 11, 12, 13, 14, 15, 16, 19, 19 },

			new int[0],

			new int[0],

			{ 8, 9, 17, 18 },

			{ 7, 9, 17, 18 },

			{ 8, 7, 17, 18 },

			{ 1, 2, 3, 4, 11, 12, 14, 15, 16, 19, 19, 19 },

			{ 1, 2, 3, 4, 10, 12, 14, 15, 16, 19, 19 },

			{ 1, 2, 3, 4, 10, 11, 14, 15, 16, 19, 19 },

			{ 1, 2, 3, 4, 10, 11, 12, 14, 15, 16, 19, 19 },

			{ 1, 2, 3, 4, 10, 11, 12, 15, 16, 19, 19 },

			{ 1, 2, 3, 4, 10, 11, 12, 13, 14, 16, 19, 19 },

			{ 1, 2, 3, 4, 10, 11, 12, 13, 14, 15, 19, 19 },

			{ 8, 9, 7, 18 },

			{ 8, 9, 7, 17 },

			{ 1, 2, 3, 4, 10, 11, 13, 14, 15, 16, 12 }

	};

	public static final int[] CURSE_REQUIREMENTS = { 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86,
			89, 92, 95 };

	public static final int[] CURSE_PROTECTION_PRAYERS = { 8, 9, 7, 6 };

	static {
		for (int i = 0; i < CURSE_CONFIG_IDS.length; i++) {
			CURSE_DISABLED_PRAYERS[i] = getDefaultDisabledPrayers(i);
		}
	}

	private static int[] getDefaultDisabledPrayers(int id) {
		return CURSE_DISABLED_PRAYERS[id];
	}

	public static Projectile getProjectileForId(int id) {
		Projectile p = new Projectile(id);
		p.setCurve(0);
		p.setStartHeight(5);
		p.setEndHeight(5);
		p.setDuration(75);
		return p;
	}

	private long pOffset = 0L;

	public CursesPrayerBook(Player player) {
		this.player = player;
		active = new byte[CURSE_NAMES.length];
		drain = new byte[CURSE_NAMES.length];
		if (player.isActive()) {
			for (int i = 0; i < player.getQuickPrayersCurses().length; i++) {
				player.send(new SendConfig(630 + i, player.getQuickPrayersCurses()[i]));
			}
		}
	}

	public boolean canAnimate() {
		return pOffset < World.getCycles();
	}

	@Override
	public boolean clickButton(int id) {
		if (id >= 67050 && id <= 67069) {
			int prayerId = id - 67050;
			if (player.getQuickPrayersCurses()[prayerId] == 0 && !hasPrayerRequirements(prayerId)) {
				return true;
			} else {
				if (player.getQuickPrayersCurses()[prayerId] == 0) {
					int[] disabled = CURSE_DISABLED_PRAYERS[prayerId];
					for (int i = 0; i < disabled.length; i++) {
						player.getQuickPrayersCurses()[disabled[i]] = 0;
					}
				}
				player.getQuickPrayersCurses()[prayerId] = (player.getQuickPrayersCurses()[prayerId] == 1 ? (byte) 0
						: (byte) 1);
				for (int i = 0; i < player.getQuickPrayersCurses().length; i++) {
					player.send(new SendConfig(630 + i, player.getQuickPrayersCurses()[i]));
				}
			}
			return true;
		}
		switch (id) {
		case 83121:
			player.send(new SendMessage("This prayer will be added soon."));
			break;
		case 83129:
			toggle(10);
			break;
		case 83131:
			toggle(11);
			break;
		case 83133:
			toggle(12);
			break;
		case 83135:
			toggle(13);
			break;
		case 83137:
			toggle(14);
			break;
		case 83139:
			toggle(15);
			break;
		case 83141:
			toggle(16);
			break;
		case 83143:
			toggle(17);
			break;
		case 83119:
			toggle(5);
			if (active(5)) {
				if (canAnimate()) {
					player.getUpdateFlags().sendAnimation(new Animation(12589));
					player.getUpdateFlags().sendGraphic(new Graphic(2266));
				}
			}
			return true;
		case 67089:
			player.send(new SendSidebarInterface(PlayerConstants.PRAYER_TAB, 21356));
			return true;
		case 87082:
			player.send(new SendSidebarInterface(PlayerConstants.PRAYER_TAB, 22000));
			player.send(new SendOpenTab(PlayerConstants.PRAYER_TAB));
			return true;
		case 19136:
			int quicks = 0;
			boolean on = false;
			for (int i = 0; i < player.getQuickPrayersCurses().length; i++) {
				if (player.getQuickPrayersCurses()[i] == 1) {
					if (active[i] == 0) {
						on = true;
					}
					quicks++;
				}
			}
			if (quicks == 0) {
				player.send(new SendMessage("You have not set any quick prayers."));
				return true;
			}
			for (int i = 0; i < player.getQuickPrayersCurses().length; i++) {
				if (player.getQuickPrayersCurses()[i] == 1) {
					if (on) {
						if (active[i] == 0) {
							toggle(i);
						}
					} else {
						if (active[i] == 1) {
							toggle(i);
						}
					}
				}
			}
			return true;
		case 83147:
			toggle(19);
			if (active(19)) {
				player.getUpdateFlags().sendAnimation(new Animation(12565));
				player.getUpdateFlags().sendGraphic(new Graphic(2226));
			}
			return true;
		case 83145:
			toggle(18);
			return true;
		case 83109:
			toggle(0);
			if (active(0)) {
				if (canAnimate()) {
					player.getUpdateFlags().sendAnimation(new Animation(12567));
					player.getUpdateFlags().sendGraphic(new Graphic(2213));
				}
			}
			return true;
		case 83123:
			toggle(7);
			return true;
		case 83125:
			toggle(8);
			return true;
		case 83127:
			toggle(9);
			return true;
		case 83111:
			toggle(1);
			return true;
		case 83113:
			toggle(2);
			return true;
		case 83115:
			toggle(3);
			return true;
		}
		return false;
	}

	@Override
	public void disableProtection(int id) {
	}

	@Override
	public void doEffectOnHit(Entity attacked, Hit hit) {
		if (player.isDead()) {
			return;
		}
		if (active(18)) {
			int eff = (int) (hit.getDamage() * 0.25);
			if (!attacked.isNpc()) {
				if (attacked.getLevels()[5] < eff) {
					eff = attacked.getLevels()[5];
				}
				attacked.getLevels()[5] -= eff;
				World.getPlayers()[attacked.getIndex()].getSkill().update(5);
			}
			player.getLevels()[3] += eff;
			if (player.getLevels()[3] > player.getMaxLevels()[3]) {
				player.getLevels()[3] = player.getMaxLevels()[3];
			}
		}
		doGraphicEffectsOnHit(attacked);
	}

	public void doGraphicEffectsOnHit(final Entity attacked) {
		if (canAnimate()) {
			pOffset = (World.getCycles() + 6);
			if (active(18)) {
				World.sendProjectile(getProjectileForId(2263), player, attacked);
				TaskQueue.queue(new RunOnceTask(attacked, 2) {
					@Override
					public void onStop() {
						attacked.getUpdateFlags().sendGraphic(new Graphic(2264, 0, false));
						World.sendProjectile(CursesPrayerBook.getProjectileForId(2263), attacked, player);
					}
				});
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public int getDamage(Hit hit) {
		switch (hit.getType()) {
		case MELEE:
			if (active(9)) {
				Entity e = hit.getAttacker();
				if ((e != null) && (hit.getDamage() > 0) && (hit.isSuccess()) && ((int) (hit.getDamage() * 0.1D) > 0)) {
					e.hit(new Hit((int) (hit.getDamage() * 0.1D), HitTypes.DEFLECT));
					player.getUpdateFlags().sendAnimation(new Animation(12573));
					player.getUpdateFlags().sendGraphic(new Graphic(2230));
				}
				if ((e != null) && (!e.isNpc())) {
					Player p2 = World.getPlayers()[e.getIndex()];

					if ((p2 == null) || (!p2.getMelee().isVeracEffectActive())) {
						return hit.getDamage() / 2;
					}
				} else if ((e != null) && (e.isNpc())) {
					Npc mob = World.getNpcs()[e.getIndex()];
					if (mob == null) {
						return hit.getDamage() / 2;
					}
					int id = mob.getId();
					if (id == 10057) {
						return hit.getDamage() / 2;
					}
					if (id == 8596) {
						return hit.getDamage();
					}
					if ((id != 2030) && (id != 8133))
						return 0;
				} else {
					return hit.getDamage() / 2;
				}
			}
			break;
		case MAGIC:
			if (active(7)) {
				Entity e = hit.getAttacker();
				if ((e != null) && (hit.getDamage() > 0) && (hit.isSuccess()) && ((int) (hit.getDamage() * 0.1D) > 0)) {
					e.hit(new Hit((int) (hit.getDamage() * 0.1D), HitTypes.DEFLECT));
					player.getUpdateFlags().sendAnimation(new Animation(12573));
					player.getUpdateFlags().sendGraphic(new Graphic(2227));
				}
				if ((e != null) && (e.isNpc())) {
					Npc mob = World.getNpcs()[e.getIndex()];
					int id = mob.getId();
					if (id == 12826) {
						return hit.getDamage();
					}
					if (id == 8133) {
						return (int) (hit.getDamage() * 0.8D);
					}
					return 0;
				}
				return hit.getDamage() / 2;
			}
			break;
		case RANGED:
			if (active(8)) {
				Entity e = hit.getAttacker();
				if ((e != null) && (hit.getDamage() > 0) && (hit.isSuccess()) && ((int) (hit.getDamage() * 0.1D) > 0)) {
					e.hit(new Hit((int) (hit.getDamage() * 0.1D), HitTypes.DEFLECT));
					player.getUpdateFlags().sendAnimation(new Animation(12573));
					player.getUpdateFlags().sendGraphic(new Graphic(2229));
				}
				if ((e != null) && (e.isNpc())) {
					Npc mob = World.getNpcs()[e.getIndex()];
					int id = mob.getId();
					if (id == 8133) {
						return (int) (hit.getDamage() * 0.8D);
					}
					return 0;
				}
				return hit.getDamage() / 2;
			}
			break;
		}
		return hit.getDamage();
	}

	@Override
	public double getDrainRate(int id) {
		return CURSE_DRAIN_RATES[id];
	}

	public boolean hasPrayerRequirements(int id) {
		String name = CURSE_NAMES[id];
		int requiredLevel = CURSE_REQUIREMENTS[id];
		boolean canUse = true;
		if (player.getMaxLevels()[5] < requiredLevel) {
			player.getClient().queueOutgoingPacket(new SendMessage(
					"You need a @blu@Prayer@bla@ level of " + requiredLevel + " to use @blu@" + name + "@bla@."));
			canUse = false;
		} else if ((id == 19) && (player.getMaxLevels()[1] < 30)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a @blu@Defence@bla@ level of 30 to use @blu@Turmoil@bla@."));
			canUse = false;
		}
		return canUse;
	}

	@Override
	public boolean isProtectionActive() {
		return false;
	}

	@Override
	public void onDisable(int id) {
		player.getClient().queueOutgoingPacket(new SendConfig(CURSE_CONFIG_IDS[id], 0));

		if ((id == 18) || (id == 7) || (id == 9) || (id == 8)) {
			headIcon = -1;
			player.setAppearanceUpdateRequired(true);
		}
	}

	@Override
	public void process() {

	}

	@Override
	public void toggle(int id) {
		String name = CURSE_NAMES[id];
		int requiredLevel = CURSE_REQUIREMENTS[id];
		int configId = CURSE_CONFIG_IDS[id];
		boolean canUse = true;
		if ((player.isDead()) || (!player.getController().canUsePrayer(player, id))) {
			canUse = false;
		} else if (player.getMaxLevels()[5] < requiredLevel) {
			player.getClient().queueOutgoingPacket(new SendMessage(
					"You need a @blu@Prayer@bla@ level of " + requiredLevel + " to use @blu@" + name + "@bla@."));
			canUse = false;
		} else if ((id == 19) && (player.getMaxLevels()[1] < 30)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a @blu@Defence@bla@ level of 30 to use @blu@Turmoil@bla@."));
			canUse = false;
		} else if (player.getSkill().getLevels()[5] == 0) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You have run out of prayer points; you must recharge at an altar."));
			canUse = false;
		}
		if (!canUse) {
			player.getClient().queueOutgoingPacket(new SendConfig(configId, 0));
			return;
		}
		active[id] = ((byte) (active[id] == 1 ? 0 : 1));
		player.getClient().queueOutgoingPacket(new SendConfig(configId, active[id]));
		if (active[id] == 1) {
			if (PrayerSounds.getSoundId(id) != -1)
				SoundPlayer.play(player, PrayerSounds.getSoundId(id), 1, 0);
			else {
				SoundPlayer.play(player, 435, 1, 0);
			}
		}
		if (active[id] == 1) {
			int[] disabled = CURSE_DISABLED_PRAYERS[id];
			for (int i = 0; i < disabled.length; i++)
				if (disabled[i] != id) {
					player.getClient().queueOutgoingPacket(new SendConfig(CURSE_CONFIG_IDS[disabled[i]], 0));
					active[disabled[i]] = 0;
				}
			switch (id) {
			case 6:
				break;
			case 7:
				headIcon = 10;
				break;
			case 8:
				headIcon = 11;
				break;
			case 9:
				headIcon = 9;
				break;
			case 17:
				headIcon = 16;
				break;
			case 18:
				headIcon = 17;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			}
			if (headIcon != -1)
				player.setAppearanceUpdateRequired(true);
		} else {
			switch (id) {
			case 6:
			case 7:
			case 8:
			case 9:
			case 17:
			case 18:
				headIcon = -1;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			}
			player.setAppearanceUpdateRequired(true);
		}
	}

}