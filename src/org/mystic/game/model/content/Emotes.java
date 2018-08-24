package org.mystic.game.model.content;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.definition.ItemDefinition;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendConfig;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.PerformTimedEmote;
import org.mystic.utility.GameDefinitionLoader;

public class Emotes {

	public enum Emote {

		YES(168, 855, -1),

		NO(169, 856, -1),

		BOW(164, 858, -1),

		ANGRY(167, 864, -1),

		THINK(162, 857, -1),

		WAVE(163, 863, -1),

		SHRUG(52058, 2113, -1),

		CHEER(171, 862, -1),

		BECKON(165, 859, -1),

		LAUGH(170, 861, -1),

		JUMP_FOR_JOY(52054, 2109, -1),

		YAWN(52056, 2111, -1),

		DANCE(166, 866, -1),

		JID(52051, 2106, -1),

		TWIRL(52052, 2107, -1),

		HEADBANG(52053, 2108, -1),

		CRY(161, 860, -1),

		BLOW_KISS(43092, 1368, 574),

		PANIC(52050, 2105, -1),

		RASBERRY(52055, 2110, -1),

		CLAP(172, 865, -1),

		SALUTE(52057, 2112, -1),

		GOBLIN_BOW(52071, 2127, -1),

		GOBLIN_SALUTE(52072, 2128, -1),

		GLASS_BOX(2155, 1131, -1),

		CLIMB_ROPE(25103, 1130, -1),

		LEAN(25106, 1129, -1),

		GLASS_WALL(2154, 1128, -1),

		IDEA(88060, 4276, 712),

		STOMP(88061, 4278, -1),

		FLAP(88062, 4280, -1),

		SLAP_HEAD(88063, 4275, -1),

		ZOMBIE_WALK(72032, 3544, -1),

		ZOMBIE_DANCE(72033, 3543, -1),

		ZOMBIE_HAND(88065, 7272, 1244),

		SCARED(59062, 2836, -1),

		BUNNY_HOP(72254, 6111, -1),

		SKILLCAPE(154, 1, 1),

		AIR_GUITAR(88059, 2414, 1537);

		private final int graphicId, animationId, buttonId;

		Emote(int buttonId, int animationId, int graphicId) {
			this.buttonId = buttonId;
			this.animationId = animationId;
			this.graphicId = graphicId;
		}

		public final int getAnimation() {
			return animationId;
		}

		public final int getGraphic() {
			return graphicId;
		}

		public final int getButton() {
			return buttonId;
		}

	}

	private static class SkillCapeEmote {

		private int anim, gfx;

		public SkillCapeEmote(int anim, int gfx) {
			this.anim = anim;
			this.gfx = gfx;
		}

		public int getAnim() {
			return anim;
		}

		public int getGfx() {
			return gfx;
		}
	}

	private static final Map<Integer, SkillCapeEmote> cape_emotes = new HashMap<Integer, SkillCapeEmote>();

	public static boolean clickButton(Player player, int id) {
		if (id == 74108) {
			performSkillCape(player);
			return true;
		}
		for (Emote emote : Emote.values()) {
			if (emote.getButton() == id) {
				if (emote.getAnimation() != 1) {
					if (emote.getAnimation() == 866 && player.getEquipment().isWearingItem(10394)) {
						player.getMovementHandler().reset();
						player.getUpdateFlags().sendAnimation(new Animation(5316));
					} else if (emote.getAnimation() == 859 && player.getEquipment().isWearingItem(10392)) {
						player.getMovementHandler().reset();
						player.getUpdateFlags().sendAnimation(new Animation(5315));
					} else if (emote.getAnimation() == 858 && player.getEquipment().isWearingItem(10396)) {
						player.getMovementHandler().reset();
						player.getUpdateFlags().sendAnimation(new Animation(5312));
					} else if (emote.getAnimation() == 2111 && player.getEquipment().isWearingItem(10398)) {
						player.getMovementHandler().reset();
						player.getUpdateFlags().sendAnimation(new Animation(5313));
					} else {
						player.getMovementHandler().reset();
						player.getUpdateFlags().sendAnimation(new Animation(emote.getAnimation()));
					}
				}
				if (emote.getGraphic() != 1) {
					player.getMovementHandler().reset();
					player.getUpdateFlags().sendGraphic(Graphic.lowGraphic(emote.getGraphic(), 0));
				}
				return true;
			}
		}
		return false;
	}

	static {
		for (int i = 0; i < 20145; i++) {
			ItemDefinition def = GameDefinitionLoader.getItemDef(i);
			if ((def != null) && (def.getName() != null)) {
				String name = def.getName();
				if (name.contains("Attack cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4959, 823));
				else if (name.contains("Defence cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4961, 824));
				else if (name.contains("Strength cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4981, 824));
				else if (name.contains("Constitution cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4971, 833));
				else if (name.contains("Ranging cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4973, 832));
				else if (name.contains("Magic cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4939, 813));
				else if (name.contains("Prayer cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4979, 829));
				else if (name.contains("Cooking cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4955, 821));
				else if ((name.contains("Woodcut. cape")) || (name.contains("Woodcutting cape")))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4957, 822));
				else if (name.contains("Fletching cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4937, 812));
				else if (name.contains("Fishing cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4951, 819));
				else if (name.contains("Firemaking cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4975, 831));
				else if (name.contains("Crafting cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4949, 818));
				else if (name.contains("Smithing cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4943, 815));
				else if (name.contains("Mining cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4941, 814));
				else if (name.contains("Herblore cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4969, 835));
				else if (name.contains("Agility cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4977, 830));
				else if (name.contains("Thieving cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4965, 826));
				else if (name.contains("Slayer cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4967, 1656));
				else if (name.contains("Farming cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4963, 825));
				else if (name.contains("Runecraft cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4947, 817));
				else if (name.contains("Construction cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4953, 820));
				else if (name.contains("Summoning cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(8525, 1515));
				else if (name.contains("Quest point cape"))
					cape_emotes.put(Integer.valueOf(i), new SkillCapeEmote(4945, 816));
			}
		}
	}

	public static void performSkillCape(Player player) {
		Item cape = player.getEquipment().getItems()[EquipmentConstants.CAPE_SLOT];
		if (player.getCombat().inCombat() || player.isBusy()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are busy."));
			return;
		}
		if (cape == null) {
			return;
		}
		SkillCapeEmote emote = cape_emotes.get(Integer.valueOf(cape.getId()));
		if (emote != null) {
			TaskQueue.queue(
					new PerformTimedEmote(player, 6, new Animation(emote.getAnim()), new Graphic(emote.getGfx())));
		}
		return;
	}

	public static void onLogin(Player player) {
		for (int i = 744; i <= 760; i++) {
			player.getClient().queueOutgoingPacket(new SendConfig(i, 1));
		}
	}

}