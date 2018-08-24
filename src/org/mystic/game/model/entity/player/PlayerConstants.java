package org.mystic.game.model.entity.player;

import java.util.Arrays;

import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.networking.outgoing.SendInterface;

public final class PlayerConstants {

	private static final String[] OWNER_USERNAME = { "prickachu, vali", "thrasher", "zelazz", "situations" };

	public static final int PLAYER_CREATION_INTERFACE = 3559;

	public static final int MAX_ITEM_COUNT = 30000;

	public static final Location START = new Location(3094, 3470, 0);

	public static final Location RESPAWN = new Location(3100, 3493, 0);

	public static final Location HOME = new Location(3087, 3490, 0);

	public static final byte GENDER_MALE = 0;
	public static final byte GENDER_FEMALE = 1;

	public static final byte APPEARANCE_SLOT_CHEST = 0;
	public static final byte APPEARANCE_SLOT_ARMS = 1;
	public static final byte APPEARANCE_SLOT_LEGS = 2;
	public static final byte APPEARANCE_SLOT_HEAD = 3;
	public static final byte APPEARANCE_SLOT_HANDS = 4;
	public static final byte APPEARANCE_SLOT_FEET = 5;
	public static final byte APPEARANCE_SLOT_BEARD = 6;

	public static final short DEFAULT_DEATH_ANIMATION = 9055;
	public static final short DEFAULT_STAND_EMOTE = 808;
	public static final short DEFAULT_TURN_EMOTE = 823;
	public static final short DEFAULT_WALK_EMOTE = 819;
	public static final short DEFAULT_TURN_180_EMOTE = 820;
	public static final short DEFAULT_TURN_90CW_EMOTE = 821;
	public static final short DEFAULT_TURN_90CCW_EMOTE = 822;
	public static final short DEFAULT_RUN_EMOTE = 824;

	public static final String LOGIN_MESSAGE = "Welcome back to <col=FFFF64><shad=000000>Mystic.</shad></col>";
	public static final String OTHER_PLAYER_IS_BUSY = "The other player is busy at the moment.";
	public static final String ABSORB_HEALTH_MESSAGE = "You absorb some of your opponent's hitpoints.";
	public static final String FROZEN_MESSAGE = "A magical force stops you from moving.";
	public static final String NOT_ENOUGH_INV_SPACE_MESSAGE = "You do not have enough inventory space to hold that.";

	public static final byte COMBAT_TAB = 0;
	public static final byte SKILLS_TAB = 1;
	public static final byte QUEST_TAB = 2;
	public static final byte INVENTORY_TAB = 3;
	public static final byte EQUIPMENT_TAB = 4;
	public static final byte PRAYER_TAB = 5;
	public static final byte MAGIC_TAB = 6;
	public static final byte CLANCHAT_TAB = 7;
	public static final byte FRIENDS_TAB = 8;
	public static final byte IGNORE_TAB = 9;
	public static final byte SETTINGS_TAB = 10;
	public static final byte EMOTES_TAB = 11;
	public static final byte SUMMONING_TAB = 12;

	public static final int[] SIDEBAR_INTERFACE_IDS =

			{

					2423,

					3917,

					638,

					3213,

					1644,

					5608,

					0,

					25605,

					5065,

					5715,

					2449,

					904,

					147,

					18128, };

	public static void doStarter(Player player, boolean normal) {
		if (IronMan.isIronMan(player)) {
			player.getInventory().add(1856, 1);
			player.getInventory().add(995, 10000);
			player.getInventory().add(1095, 1);
			player.getInventory().add(1129, 1);
			player.getInventory().add(882, 50);
			player.getInventory().add(841, 1);
			player.getInventory().add(1381, 1);
			player.getInventory().add(558, 200);
			player.getInventory().add(556, 200);
			player.getInventory().add(555, 100);
			player.getInventory().add(557, 100);
			player.getInventory().add(554, 100);
			player.getInventory().add(579, 1);
			player.getInventory().add(577, 1);
			player.getInventory().add(1011, 1);
			player.setAppearanceUpdateRequired(true);
			player.getEquipment().addOnLogin(new Item(7803, 1), EquipmentConstants.NECKLACE_SLOT);
			player.getEquipment().addOnLogin(new Item(1153, 1), EquipmentConstants.HELM_SLOT);
			player.getEquipment().addOnLogin(new Item(1059, 1), EquipmentConstants.GLOVES_SLOT);
			player.getEquipment().addOnLogin(new Item(1191, 1), EquipmentConstants.SHIELD_SLOT);
			player.getEquipment().addOnLogin(new Item(1021, 1), EquipmentConstants.CAPE_SLOT);
			player.getEquipment().addOnLogin(new Item(1061, 1), EquipmentConstants.BOOTS_SLOT);
			player.getEquipment().addOnLogin(new Item(1067, 1), EquipmentConstants.LEGS_SLOT);
			player.getEquipment().addOnLogin(new Item(1115, 1), EquipmentConstants.TORSO_SLOT);
			player.getEquipment().addOnLogin(new Item(1323, 1), EquipmentConstants.WEAPON_SLOT);
			player.getEquipment().onLogin();
		} else {
			player.getInventory().add(1856, 1);
			player.getInventory().add(995, normal ? 500000 : 50000);
			player.getInventory().add(8013, 10);
			player.getInventory().add(1095, 1);
			player.getInventory().add(1129, 1);
			player.getInventory().add(882, 250);
			player.getInventory().add(841, 1);
			player.getInventory().add(1379, 1);
			player.getInventory().add(558, 400);
			player.getInventory().add(556, 400);
			player.getInventory().add(555, 200);
			player.getInventory().add(557, 200);
			player.getInventory().add(554, 200);
			player.getInventory().add(579, 1);
			player.getInventory().add(577, 1);
			player.getInventory().add(1011, 1);
			player.getInventory().add(380, 50);
			player.setAppearanceUpdateRequired(true);
			player.getEquipment().addOnLogin(new Item(1712, 1), EquipmentConstants.NECKLACE_SLOT);
			player.getEquipment().addOnLogin(new Item(11118, 1), EquipmentConstants.GLOVES_SLOT);
			player.getEquipment().addOnLogin(new Item(3842, 1), EquipmentConstants.SHIELD_SLOT);
			player.getEquipment().addOnLogin(new Item(1007, 1), EquipmentConstants.CAPE_SLOT);
			player.getEquipment().addOnLogin(new Item(1837, 1), EquipmentConstants.BOOTS_SLOT);
			player.getEquipment().addOnLogin(new Item(1712, 1), EquipmentConstants.NECKLACE_SLOT);
			player.getEquipment().addOnLogin(new Item(1067, 1), EquipmentConstants.LEGS_SLOT);
			player.getEquipment().addOnLogin(new Item(1115, 1), EquipmentConstants.TORSO_SLOT);
			player.getEquipment().addOnLogin(new Item(1323, 1), EquipmentConstants.WEAPON_SLOT);
			player.getEquipment().onLogin();
		}
	}

	public static final int getDeathAnimation() {
		return 836;
	}

	public static boolean isOverrideObjectExistance(Player p, int objectId, int x, int y, int z) {
		if ((x == 2851) && (y == 5333)) {
			return true;
		}
		if (objectId == 5932 || objectId == 4936 || objectId == 2732 || objectId == 28213 || objectId == 9293
				|| objectId == 11557) {
			return true;
		}
		if (objectId == 26342 && p.getX() >= 2916 && p.getY() >= 3744 && p.getX() <= 2921 && p.getY() <= 3749) {
			return true;
		}
		return false;
	}

	public static boolean isOwner(Player p) {
		return Arrays.asList(OWNER_USERNAME).contains(p.getUsername().toLowerCase());
	}

	public static boolean isOwner(String username) {
		return Arrays.asList(OWNER_USERNAME).contains(username.toLowerCase());
	}

	public static void setAppearance(Player player) {
		player.getClient().queueOutgoingPacket(new SendInterface(3559));
	}

}