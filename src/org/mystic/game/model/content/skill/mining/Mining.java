package org.mystic.game.model.content.skill.mining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.Task.BreakType;
import org.mystic.game.task.Task.StackType;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class Mining {

	private final static Set<Location> DEAD_ORES = new HashSet<>();

	public enum Pickaxe {

		BRONZE_PICKAXE(1265, 1, 8, new Animation(625)),

		IRON_PICKAXE(1267, 1, 7, new Animation(626)),

		STEEL_PICKAXE(1269, 6, 6, new Animation(627)),

		MITHRIL_PICKAXE(1273, 21, 5, new Animation(629)),

		ADAMANT_PICKAXE(1271, 31, 4, new Animation(628)),

		RUNE_PICKAXE(1275, 41, 3, new Animation(624)),

		DRAGON_PICKAXE(15259, 61, 2, new Animation(12188)),

		DRAGON_PICKAXE_OR(20786, 61, 1, new Animation(272));

		private final int item, level, weight;

		private final Animation animation;

		private static final HashMap<Integer, Pickaxe> PICKAXES = new HashMap<>();

		static {
			for (Pickaxe pickaxe : values()) {
				PICKAXES.put(pickaxe.item, pickaxe);
			}
		}

		private Pickaxe(final int item, final int level, final int weight, final Animation animation) {
			this.item = item;
			this.level = level;
			this.animation = animation;
			this.weight = weight;
		}

		public final int getItem() {
			return item;
		}

		public final int getLevel() {
			return level;
		}

		public final Animation getAnimation() {
			return animation;
		}

		public final int getWeight() {
			return weight;
		}

		public static Pickaxe get(Player player) {
			Pickaxe highest = null;
			Queue<Pickaxe> picks = new PriorityQueue<>((first, second) -> second.getLevel() - first.getLevel());
			if (player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT] != null) {
				highest = PICKAXES.get(player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT].getId());
				if (highest != null) {
					picks.add(highest);
					highest = null;
				}
			}
			for (Item item : player.getInventory().getItems()) {
				if (item == null) {
					continue;
				}
				Pickaxe pick = PICKAXES.get(item.getId());
				if (pick == null) {
					continue;
				}
				picks.add(pick);
			}
			Pickaxe pick = picks.poll();
			if (pick == null) {
				return null;
			}
			while (player.getLevels()[Skills.MINING] < pick.getLevel()) {
				if (highest == null) {
					highest = pick;
				}

				pick = picks.poll();
			}
			return pick;
		}
	}

	public enum Ore {

		CLAY("Clay", new int[] { 11190, 11189, 11191 }, 1, 25.5, new int[] { 434 }, 11557, 6, 4),

		COPPER("Copper ore", new int[] { 11938, 11936, 11937, 11961, 11960, 11962, 15504, 2090, 2091, 31082, 31080,
				31081, 11938, 11937, 11936 }, 1, 30.5, new int[] { 436 }, 11557, 6, 4),

		TIN("Tin ore", new int[] { 11187, 11188, 11186, 11933, 11934, 11935, 11957, 11958, 11959, 2094, 2095, 31077,
				31078, 31079, 37304, 37306 }, 1, 30.5, new int[] { 438 }, 11557, 6, 4),

		IRON("Iron ore",
				new int[] { 14858, 14856, 14857, 11955, 11956, 11954, 2092, 2093, 31072, 31073, 31071, 37309, 37307 },
				15, 50, new int[] { 440 }, 11557, 12, 7),

		SILVER_ORE("Silver ore", new int[] { 11948, 11950, 11949, 37670, 37306, 37304, 37305 }, 20, 45,
				new int[] { 442 }, 11557, 15, 10),

		COAL_ORE("Coal ore",
				new int[] { 14852, 14851, 14850, 15503, 15505, 2096, 2097, 31068, 31070, 31069, 11932, 11930 }, 30, 75,
				new int[] { 453 }, 11557, 12, 10),

		GOLD_ORE("Gold ore", new int[] { 15576, 15578, 15577, 15581, 15579, 15580, 11184, 11185, 11183, 2098, 2099,
				45067, 45068, 31065, 31066, 37310, 37312 }, 40, 95, new int[] { 444 }, 11557, 15, 10),

		MITHRIL_ORE("Mithril ore", new int[] { 2102, 2103, 31086, 31087, 31088, 11942, 11944 }, 55, 125,
				new int[] { 447 }, 11557, 15, 13),

		ADAMANT_ORE("Adamant ore",
				new int[] { 14864, 14863, 14862, 2104, 2105, 29233, 29235, 31085, 31083, 11939, 11941 }, 70, 150,
				new int[] { 449 }, 11557, 15, 16),

		RUNITE_ORE("Runite ore", new int[] { 14859, 14860, 45070, 45069 }, 85, 195, new int[] { 451 }, 11557, 15, 18),

		ESSENCE("Essence", new int[] { 2491 }, 30, 10, new int[] { 7936 }, -1, -1, -1),

		GEM_ROCK("Gem Rock", new int[] { 14856, 14855, 14854 }, 40, 65,
				new int[] { 1625, 1627, 1629, 1623, 1621, 1619, 1617 }, 11557, 135, 140);

		private final String name;

		private final int[] objects, ore;

		private final double exp;

		private final int level, replacement, respawn, immunity;

		private static final HashMap<Integer, Ore> ORES = new HashMap<>();

		static {
			for (Ore ore : values()) {
				for (int object : ore.objects) {
					ORES.put(object, ore);
				}
			}
		}

		private Ore(final String name, final int[] objects, final int level, final double exp, final int[] ore,
				final int replacement, final int respawn, final int immunity) {
			this.name = name;
			this.objects = objects;
			this.level = level;
			this.exp = exp;
			this.ore = ore;
			this.replacement = replacement;
			this.respawn = respawn;
			this.immunity = immunity;
		}

		public final String getName() {
			return name;
		}

		public final int getLevel() {
			return level;
		}

		public final double getExp() {
			return exp;
		}

		public final int[] getOre() {
			return ore;
		}

		public final int getReplacement() {
			return replacement;
		}

		public final int getRespawn() {
			return respawn;
		}

		public final int getImmunity() {
			return immunity;
		}

		public static Ore get(int id) {
			return ORES.get(id);
		}

	}

	public static boolean clickRock(Player player, GameObject object) {
		if (player.getSkill().locked() || object == null) {
			return false;
		}
		Ore ore = Ore.get(object.getId());
		if (ore == null) {
			return false;
		}
		if (player.getLevels()[Skills.MINING] < ore.getLevel()) {
			DialogueManager.sendStatement(player,
					"You need a Mining level of " + ore.getLevel() + " to mine that ore.");
			return false;
		}
		Pickaxe pickaxe = Pickaxe.get(player);
		if (pickaxe == null) {
			DialogueManager.sendStatement(player, "You need a pickaxe to mine this rock. You do not have a pickaxe",
					"which you have the Mining level to use.");
			return false;
		}
		if (player.getLevels()[Skills.MINING] < pickaxe.getLevel()) {
			player.send(new SendMessage("You need a Mining level of " + pickaxe.getLevel() + " to use that pickaxe."));
			DialogueManager.sendStatement(player,
					"You need a Mining level of " + pickaxe.getLevel() + " to use that pickaxe.");
			return false;
		}
		if (player.getCombat().inCombat() || player.getCombat().getAttacking() != null) {
			player.send(new SendMessage("You can't do that right now!"));
			return false;
		}
		if (player.getInventory().getTakenSlots() == 28) {
			DialogueManager.sendStatement(player, "You don't have enough inventory spaces to mine this.");
			return false;
		}
		player.send(new SendMessage("You swing your pick at the rock."));
		int ticks = ore.immunity == -1 ? 2
				: ore.getImmunity() - (int) ((player.getLevels()[Skills.MINING] - ore.getLevel()) * 2
						/ (double) pickaxe.getWeight());
		int gemTick = ore.getImmunity();
		if (ticks < 1) {
			ticks = 1;
		}
		int time = ore.getName().equalsIgnoreCase("gem rock") ? gemTick : ticks;
		TaskQueue.queue(
				new Task(player, 1, false, StackType.NEVER_STACK, BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION) {

					int ticks = 0;

					@Override
					public void execute() {
						if (ore == Ore.ESSENCE) {
							if (player.getInventory().getFreeSlots() == 0) {
								DialogueManager.sendStatement(player, "Your inventory is full!");
								stop();
								return;
							}
						}
						if (ticks == 0) {
							player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
						}
						if (ticks++ == time || DEAD_ORES.contains(new Location(object.getLocation().getX(),
								object.getLocation().getY(), object.getLocation().getZ()))) {
							if (ore == Ore.ESSENCE) {
								player.getInventory().add(ore.getOre()[Misc.random(ore.getOre().length)], 1);
								player.getSkill().addExperience(Skills.MINING, ore.getExp());
								ticks = 0;
								if (player.getInventory().getFreeSlots() == 0) {
									DialogueManager.sendStatement(player, "Your inventory is full!");
									stop();
								}
								return;
							} else {
								stop();
								return;
							}
						}
					}

					@Override
					public void onStop() {
						player.getUpdateFlags().sendAnimation(new Animation(65535));
						if (time + 1 == ticks) {
							if (ore != Ore.ESSENCE) {
								SoundPlayer.play(player, 431, 10, 0);
								player.getInventory().add(ore.getOre()[Misc.randomNumber(ore.getOre().length)], 1);
								player.getSkill().addExperience(Skills.MINING, ore.getExp());
								player.getAchievements().incr(player, "Mine 500 ores");
							}
							if (ore.getReplacement() > 0) {
								GameObject depleted = new GameObject(ore.getReplacement(), object.getLocation().getX(),
										object.getLocation().getY(), object.getLocation().getZ(), object.getType(),
										object.getFace());
								ObjectManager.register(depleted);
								DEAD_ORES.add(new Location(object.getLocation().getX(), object.getLocation().getY(),
										object.getLocation().getZ()));
								TaskQueue.queue(new Task(player, ore.getRespawn(), false, StackType.STACK,
										BreakType.NEVER, TaskIdentifier.MINING_ROCK) {

									@Override
									public void execute() {
										stop();
									}

									@Override
									public void onStop() {
										DEAD_ORES.remove(new Location(object.getLocation().getX(),
												object.getLocation().getY(), object.getLocation().getZ()));
										GameObject rock = new GameObject(object.getId(), object.getLocation().getX(),
												object.getLocation().getY(), object.getLocation().getZ(),
												object.getType(), object.getFace());
										ObjectManager.register(rock);
									}
								});
							}
						}
					}
				});

		return true;
	}

}