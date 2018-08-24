package org.mystic.game.model.content.skill.magic;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

/**
 * SpellBookTeleportation
 * 
 * @author Reece - http://www.rune-server.org/members/Valiant
 * @since April 28th, 2015
 * 
 */
public final class SpellBookTeleporting {

	/**
	 * Teleportation Enumeration
	 */
	public enum TeleportationData {

		HOME_TELEPORT(3087, 3490, 75010, 1, 0, new int[] { AIR_RUNE, 3, FIRE_RUNE, 1, LAW_RUNE, 1 }),

		HOME_TELEPORT_LUNAR(3087, 3490, 117048, 1, 0, new int[] { AIR_RUNE, 3, FIRE_RUNE, 1, LAW_RUNE, 1 }),

		VARROCK(3210, 3424, 4140, 25, 35, new int[] { AIR_RUNE, 3, FIRE_RUNE, 1, LAW_RUNE, 1 }),

		LUMBRIDGE(3222, 3218, 4143, 31, 41, new int[] { AIR_RUNE, 3, EARTH_RUNE, 1, LAW_RUNE, 1 }),

		FALADOR(2964, 3378, 4146, 37, 48, new int[] { AIR_RUNE, 3, WATER_RUNE, 1, LAW_RUNE, 1 }),

		CAMELOT(2757, 3477, 4150, 45, 55, new int[] { AIR_RUNE, 5, LAW_RUNE, 1, LAW_RUNE, 1 }),

		ARDOUGNE(2662, 3305, 6004, 51, 61, new int[] { WATER_RUNE, 2, LAW_RUNE, 2, LAW_RUNE, 2 }),

		WATCH_TOWER(3087, 3500, 6005, 58, 68, new int[] { EARTH_RUNE, 2, LAW_RUNE, 2, LAW_RUNE, 2 }),

		TROLLHEIM(3243, 3513, 29031, 61, 68, new int[] { FIRE_RUNE, 2, LAW_RUNE, 2, LAW_RUNE, 2 }),

		HOME_TELEPORT_ANCIENT(3087, 3490, 84237, 1, 0, new int[] { AIR_RUNE, 3, FIRE_RUNE, 1, LAW_RUNE, 1 }),

		PADDEWWA(3096, 3468, 50235, 54, 64, new int[] { AIR_RUNE, 1, FIRE_RUNE, 1, LAW_RUNE, 2 }),

		SENNTISTEN(3322, 3336, 50245, 60, 70, new int[] { LAW_RUNE, 2, SOUL_RUNE, 1, SOUL_RUNE, 1 }),

		KHARYLL(3492, 3471, 50253, 66, 76, new int[] { LAW_RUNE, 2, BLOOD_RUNE, 1, BLOOD_RUNE, 1 }),

		LASSAR(3013, 3501, 51005, 72, 82, new int[] { LAW_RUNE, 2, WATER_RUNE, 4 }),

		DAREEYAK(2979, 3754, 51013, 78, 88, new int[] { LAW_RUNE, 2, FIRE_RUNE, 3, AIR_RUNE, 2 }),

		CARRALLANGER(3157, 3668, 51023, 84, 94, new int[] { LAW_RUNE, 2, SOUL_RUNE, 2 }),

		ANNAKARL(3287, 3884, 51031, 90, 100, new int[] { LAW_RUNE, 2, BLOOD_RUNE, 2 }),

		GHORROCK(2962, 3924, 51039, 96, 106, new int[] { LAW_RUNE, 2, WATER_RUNE, 8 });

		/** Returns the data for the clicking button id recieved */
		private static final TeleportationData forId(int button) {
			for (TeleportationData data : TeleportationData.values()) {
				if (button == data.getButton()) {
					return data;
				}
			}
			return null;
		}

		// The teleport location
		private final int x, y, req, button, xp;

		// the runes required to teleport
		private final int[] runes;

		// Enum Constructment
		TeleportationData(final int x, final int y, final int button, final int req, final int xp, final int[] runes) {
			this.x = x;
			this.y = y;
			this.button = button;
			this.req = req;
			this.runes = runes;
			this.xp = xp;
		}

		// gets the button clicked on for teleport
		private int getButton() {
			return button;
		}

		// gets the magic requirement to teleport
		private int getReq() {
			return req;
		}

		// gets the required runes to teleport
		private int[] getRunes() {
			return runes;
		}

		// gets the X axis teleport location
		private int getX() {
			return x;
		}

		// gets the Y axis teleport location
		private int getY() {
			return y;
		}

		// gets the xp per cast
		private int getXP() {
			return xp;
		}
	}

	/* Teleportation Rune Constants */
	private final static int LAW_RUNE = 563;
	private final static int AIR_RUNE = 556;
	private final static int FIRE_RUNE = 554;
	private final static int EARTH_RUNE = 557;
	private final static int WATER_RUNE = 555;
	private final static int SOUL_RUNE = 566;
	private final static int BLOOD_RUNE = 565;

	/**
	 * Manages the actual teleport action for the player
	 * 
	 * @param player
	 *            the player teleporting
	 * @param button
	 *            the button recieved
	 */
	public static final void teleport(Player player, int button) {
		if (player.getSkill().locked()) {
			return;
		}
		final TeleportationData data = TeleportationData.forId(button);
		if (data == null) {
			return;
		}
		if (!player.teleporting) {
			if (button == 75010 || button == 84237 || button == 117048) {
				player.getMagic().teleport(data.getX(), data.getY(), 0, TeleportTypes.SPELL_BOOK);
				return;
			}
			if (player.getInventory().playerHasItem(new Item(data.getRunes()[0], data.getRunes()[1]))
					&& player.getInventory().playerHasItem(new Item(data.getRunes()[2], data.getRunes()[3]))
					&& player.getInventory().playerHasItem(new Item(data.getRunes()[4], data.getRunes()[5]))) {
				if (player.getSkill().getLevels()[Skills.MAGIC] >= data.getReq()) {
					player.getSkill().lock(5);
					player.getMagic().teleport(data.getX(), data.getY(), 0, TeleportTypes.SPELL_BOOK);
					player.getSkill().addExperience(Skills.MAGIC, data.getXP());
					player.getInventory().remove(new Item(data.getRunes()[0], data.getRunes()[1]));
					player.getInventory().remove(new Item(data.getRunes()[2], data.getRunes()[3]));
					if (data.getRunes()[2] == data.getRunes()[4] && data.getRunes()[3] == data.getRunes()[5]) {
						return;
					} else {
						player.getInventory().remove(new Item(data.getRunes()[4], data.getRunes()[5]));
					}
					return;
				} else {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You don't have a high enough magic level to cast this spell."));
					return;
				}
			} else {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You don't have the required runes to cast this spell."));
				return;
			}
		}
	}
}
