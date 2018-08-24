package org.mystic.game.model.content.skill.fishing;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class FishingAction {

	public enum FishingSpots {

		SMALL_NET_OR_BAIT(316, new Fish[] { Fish.SHRIMP, Fish.ANCHOVIES },
				new Fish[] { Fish.SARDINE, Fish.HERRING, Fish.PIKE }),

		LURE_OR_BAIT(317, new Fish[] { Fish.TROUT, Fish.SALMON }, new Fish[] { Fish.SARDINE, Fish.HERRING, Fish.PIKE }),

		LURE_OR_BAIT2(328, new Fish[] { Fish.TROUT, Fish.SALMON },
				new Fish[] { Fish.SARDINE, Fish.HERRING, Fish.PIKE }),

		LURE_OR_BAIT3(310, new Fish[] { Fish.TROUT, Fish.SALMON },
				new Fish[] { Fish.SARDINE, Fish.HERRING, Fish.PIKE }),

		LURE_OR_BAIT4(311, new Fish[] { Fish.TROUT, Fish.SALMON },
				new Fish[] { Fish.SARDINE, Fish.HERRING, Fish.PIKE }),

		LURE_OR_BAIT5(309, new Fish[] { Fish.TROUT, Fish.SALMON },
				new Fish[] { Fish.SARDINE, Fish.HERRING, Fish.PIKE }),

		CAGE_OR_HARPOON(324, new Fish[] { Fish.LOBSTER }, new Fish[] { Fish.TUNA, Fish.SWORD_FISH }),

		CAGE_OR_HARPOON2(312, new Fish[] { Fish.LOBSTER }, new Fish[] { Fish.TUNA, Fish.SWORD_FISH }),

		LARGE_NET_OR_HARPOON(334, new Fish[] { Fish.MACKEREL, Fish.COD, Fish.BASS }, new Fish[] { Fish.SHARK }),

		LARGE_NET_OR_HARPOON2(313, new Fish[] { Fish.MACKEREL, Fish.COD, Fish.BASS }, new Fish[] { Fish.SHARK }),

		HARPOON_OR_SMALL_NET(322, new Fish[] { Fish.MONK_FISH }, new Fish[] { Fish.TUNA, Fish.SWORD_FISH }),

		ROCKTAIL_SPOT(7046, new Fish[] { Fish.LOBSTER }, new Fish[] { Fish.ROCKTAIL }),

		ROCKTAIL_SPOT2(3019, new Fish[] { Fish.ROCKTAIL }, new Fish[] { Fish.KARAMBWAN }),;

		private static final Map<Integer, FishingSpots> fishingSpots = new HashMap<Integer, FishingSpots>();

		static {
			for (FishingSpots spots : values()) {
				fishingSpots.put(Integer.valueOf(spots.getId()), spots);
			}
		}

		public static FishingSpots forId(int id) {
			return fishingSpots.get(Integer.valueOf(id));
		}

		private final int id;

		private final Fish[] option_1;

		private final Fish[] option_2;

		private FishingSpots(final int id, final Fish[] option_1, final Fish[] option_2) {
			this.id = id;
			this.option_1 = option_1;
			this.option_2 = option_2;
		}

		public final int getId() {
			return id;
		}

		public final Fish[] getOption_1() {
			return option_1;
		}

		public final Fish[] getOption_2() {
			return option_2;
		}

	}

	public static boolean canFish(Player player, Fish fish, boolean message) {
		if (player.getMaxLevels()[10] < fish.getRequiredLevel()) {
			if (message) {
				DialogueManager.sendStatement(player,
						"You need a fishing level of " + fish.getRequiredLevel() + " to fish here.");
			}
			return false;
		}
		if (!hasFishingItems(player, fish, message)) {
			return false;
		}
		return true;
	}

	public static boolean hasFishingItems(Player player, Fish fish, boolean message) {
		int tool = fish.getToolId();
		int bait = fish.getBaitRequired();
		if (tool == 311) {
			if (!player.getInventory().hasItemAmount(new Item(tool, 1))) {
				Item weapon = player.getEquipment().getItems()[3];

				if ((weapon != null) && (weapon.getId() == 10129)) {
					return true;
				}
				DialogueManager.sendStatement(player, "You don't have the right equipment to fish here.");
				return false;
			}
		} else if ((!player.getInventory().hasItemAmount(new Item(tool, 1))) && (message)) {
			String name = Item.getDefinition(tool).getName();
			DialogueManager.sendStatement(player, "You need " + Misc.getAOrAn(name) + " " + name + " to fish here.");
			return false;
		}
		if ((bait > -1) && (!player.getInventory().hasItemAmount(new Item(bait, 1)))) {
			String name = Item.getDefinition(bait).getName();
			if (message) {
				DialogueManager.sendStatement(player,
						"You need " + Misc.getAOrAn(name) + " " + name + " to fish here.");
			}
			return false;
		}
		return true;
	}

	private final Player player;

	private Fish[] fishing = null;

	private Tool tool = null;

	public FishingAction(Player player) {
		this.player = player;
	}

	public boolean click(final Npc mob, final int id, final int option) {
		if (FishingSpots.forId(id) == null) {
			return false;
		}
		final FishingSpots spot = FishingSpots.forId(id);
		final Fish[] f = new Fish[3];
		final Fish[] fish;
		int amount = 0;
		switch (option) {
		case 1:
			fish = spot.option_1;
			for (int i = 0; i < fish.length; i++) {
				if (canFish(player, fish[i], i == 0)) {
					f[i] = fish[i];
					amount++;
				}
			}
			break;
		case 2:
			fish = spot.option_2;
			for (int i = 0; i < fish.length; i++) {
				if (canFish(player, fish[i], i == 0)) {
					f[i] = fish[i];
					amount++;
				}
			}
		}
		if (amount == 0) {
			return true;
		}
		Fish[] fishing = new Fish[amount];
		for (int i = 0; i < amount; i++) {
			fishing[i] = f[i];
		}
		start(mob, fishing);
		return true;
	}

	public boolean fish() {
		if (fishing == null) {
			return false;
		}
		Fish[] fish = new Fish[5];
		byte c = 0;
		for (int i = 0; i < fishing.length; i++) {
			if (canFish(player, fishing[i], i == 0)) {
				fish[c] = fishing[i];
				c = (byte) (c + 1);
			}
		}
		if (c == 0) {
			return false;
		}
		Fish f = fish[Misc.randomNumber(c)];
		if (player.getInventory().getFreeSlots() == 0) {
			player.getUpdateFlags().sendAnimation(new Animation(65535));
			DialogueManager.sendStatement(player, "You can't carry anymore fish.");
			return false;
		}
		if (success(f)) {
			if (f.getBaitRequired() != -1) {
				int r = player.getInventory().remove(new Item(f.getBaitRequired(), 1));
				if (r == 0) {
					DialogueManager.sendStatement(player, "You have ran out of bait.");
					return false;
				}
			}
			SoundPlayer.play(player, Sounds.CATCH_FISH);
			int id = f.getRawFishId();
			String name = Item.getDefinition(id).getName();
			player.getInventory().add(new Item(id, 1));
			player.getSkill().addExperience(10, f.getExperience());
			player.getClient().queueOutgoingPacket(
					new SendMessage("You catch " + getFishStringMod(name) + name.toLowerCase() + "."));
			player.send(new SendRemoveInterfaces());
			player.getAchievements().incr(player, "Fish 1,000 items");
		}
		player.getSkill().lock(4);
		return true;
	}

	public String getFishStringMod(String name) {
		return name.substring(name.length() - 2, name.length() - 1).equals("s") ? "some " : "a ";
	}

	public void reset() {
		fishing = null;
		tool = null;
	}

	public void start(final Npc mob, Fish[] fishing) {
		if ((fishing == null) || (fishing[0] == null) || (fishing[0].getToolId() == -1)) {
			return;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.getUpdateFlags().sendAnimation(new Animation(65535));
			DialogueManager.sendStatement(player, "You can't carry anymore fish.");
			return;
		}
		this.fishing = fishing;
		tool = Tool.forId(fishing[0].getToolId());
		if (!hasFishingItems(player, fishing[0], true)) {
			return;
		}
		SoundPlayer.play(player, Sounds.CAST_LINE);
		player.getUpdateFlags().sendAnimation(tool.getAnimationId(), 0);
		Task skill = new Task(player, 4, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE,
				TaskIdentifier.CURRENT_ACTION) {
			@Override
			public void execute() {
				player.face(mob);
				player.getUpdateFlags().sendAnimation(tool.getAnimationId(), 0);
				if (!fish()) {
					stop();
					reset();
					return;
				}
			}

			@Override
			public void onStop() {
				player.getUpdateFlags().sendAnimation(new Animation(65535));
			}
		};
		TaskQueue.queue(skill);
	}

	public boolean success(Fish fish) {
		return Skills.isSuccess(player, Skills.FISHING, fish.getRequiredLevel());
	}
}
