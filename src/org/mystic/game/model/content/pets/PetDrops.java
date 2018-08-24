package org.mystic.game.model.content.pets;

import org.mystic.game.World;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

/**
 * Manages the random chance of receiving a pet as a drop from a monster
 * 
 * @author Reece - http://www.rune-server.org/members/Valiant
 * @since November 8th 2015
 * 
 */
public class PetDrops {

	public enum PetType {

		GREEN_DRAGON(941, 12473, "Pet baby dragon (green)"),

		BLUE_DRAGON(55, 12471, "Pet baby dragon (blue)"),

		RED_DRAGON(53, 12469, "Pet baby dragon (red)"),

		KBD(50, 20081, "Prince black dragon"),

		BANDOS(6260, 20078, "Pet general graardor"),

		CHAOS_ELEMENTAL(3200, 20073, "Pet chaos elemental"),

		DAGGANOTH_SUPREME(2881, 20074, "Pet daganoth supreme"),

		DAGGANOTH_PRIME(2882, 20075, "Pet dagganoth prime"),

		DAGGANOTH_REX(2883, 20076, "Pet dagganoth rex"),

		KREE_ARRA(6222, 20077, "Pet Kree'arra"),

		COMMANDER_ZIYLANA(6247, 20079, "Pet Zil"),

		KR_ILL(6203, 20080, "Pet K'ril Tsutasroth"),

		BLACK_DRAGON(54, 12475, "Pet baby dragon (black)");

		public static final PetType checkDrop(Item drop) {
			for (PetType type : PetType.values()) {
				if (drop.getId() == type.getId()) {
					return type;
				}
			}
			return null;
		}

		public static final PetType forId(Npc mob) {
			for (PetType type : PetType.values()) {
				if (mob.getId() == type.getMobId()) {
					return type;
				}
			}
			return null;
		}

		private final int mob, itemId;

		private final String string;

		PetType(int mob, int itemId, String string) {
			this.mob = mob;
			this.itemId = itemId;
			this.string = string;
		}

		public int getId() {
			return itemId;
		}

		public int getMobId() {
			return mob;
		}

		public String getName() {
			return string;
		}
	}

	public static void assignFollower(Player player, Npc mob) {
		PetType pet = PetType.forId(mob);
		if (pet != null) {
			if (player.getPets().getPet() == null && !player.getSummoning().hasFamiliar()) {
				player.getPets().spawn(pet.getId());
				player.send(new SendMessage("You have a funny feeling as if you're being followed.."));
				World.sendGlobalMessage("@dre@(PVM) -> " + player.getUsername()
						+ "@dre@ has just gained a new follower. (" + pet.getName() + ")", false);
			}
		}
		return;
	}

}