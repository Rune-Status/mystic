package org.mystic.game.model.content.skill.woodcutting;

import org.mystic.cache.map.RSObject;
import org.mystic.cache.map.Region;
import org.mystic.game.World;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class WoodcuttingTask extends Task {

	private static final int[] AXES = { 13661, 6739, 1359, 1357, 1355, 1361, 1353, 1349, 1351 };

	public static void attemptWoodcutting(Player player, GameObject object) {
		Tree tree = Tree.forId(object.getId());
		player.setInteractingObject(object);
		if (tree == null) {
			return;
		}
		if (!meetsRequirements(player, tree, object)) {
			return;
		}
		WoodcuttingAxeData[] axes = new WoodcuttingAxeData[15];
		int d = 0;
		for (int s : AXES) {
			if (player.getEquipment().getItems()[3] != null && player.getEquipment().getItems()[3].getId() == s) {
				axes[d] = WoodcuttingAxeData.forId(s);
				d++;
				break;
			}
		}
		if (d == 0) {
			for (Item i : player.getInventory().getItems()) {
				if (i != null) {
					for (int c : AXES) {
						if (i.getId() == c) {
							axes[d] = WoodcuttingAxeData.forId(c);
							d++;
							break;
						}
					}
				}
			}
		}
		int skillLevel = 0;
		int anyLevelAxe = 0;
		int index = -1;
		int indexb = 0;
		for (WoodcuttingAxeData i : axes) {
			if (i != null) {
				if (meetsAxeRequirements(player, i)) {
					if (index == -1 || i.getLevelRequired() > skillLevel) {
						index = indexb;
						skillLevel = i.getLevelRequired();
					}
				}
				anyLevelAxe = i.getLevelRequired();
			}
			indexb++;
		}
		if (index == -1) {
			if (anyLevelAxe != 0) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You need a woodcutting level of " + anyLevelAxe + " to use this axe."));
			} else {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have an axe which you have the woodcutting level to use."));
			}
			return;
		}
		WoodcuttingAxeData axe = axes[index];
		player.send(new SendRemoveInterfaces());
		player.getClient().queueOutgoingPacket(new SendMessage("You swing your axe at the tree."));
		player.getUpdateFlags().sendAnimation(axe.getAnimation());
		player.getUpdateFlags().sendFaceToDirection(object.getLocation());
		TaskQueue.queue(new WoodcuttingTask(player, object.getId(), tree, object, axe));
	}

	private static boolean meetsAxeRequirements(Player player, WoodcuttingAxeData data) {
		if (data == null) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have a hatchet."));
			return false;
		}
		if (player.getSkill().getLevels()[8] < data.getLevelRequired()) {
			return false;
		}
		return true;
	}

	private static boolean meetsRequirements(Player player, Tree data, GameObject object) {
		if (player.getSkill().getLevels()[Skills.WOODCUTTING] < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(new SendMessage(
					"You need a woodcutting level of " + data.getLevelRequired() + " to cut this tree."));
			return false;
		}
		if (!Region.objectExists(object.getId(), object.getLocation().getX(), object.getLocation().getY(),
				object.getLocation().getZ())) {
			return false;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.getUpdateFlags().sendAnimation(-1, 0);
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You don't have enough inventory space to cut this."));
			return false;
		}
		if (player.getInteractingObject() == null) {
			return false;
		}
		return true;
	}

	private final Player player;

	private final GameObject object;

	private final WoodcuttingAxeData axe;

	private final Tree tree;

	private final int treeId;

	private int animation, pos;

	private final int[] NORMAL_TREES = { 1278, 1276 };

	public WoodcuttingTask(final Player player, final int treeId, final Tree tree, final GameObject object,
			WoodcuttingAxeData axe) {
		super(player, 1, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.object = object;
		this.tree = tree;
		this.axe = axe;
		this.treeId = treeId;
		player.getSkill().lock(2);
	}

	// TODO: cancel and stop anim, same for cooking.

	private void animate() {
		SoundPlayer.play(player, Sounds.CHOP_TREE);
		if (++animation == 1) {
			player.getUpdateFlags().sendAnimation(axe.getAnimation());
			animation = 0;
		}
	}

	@Override
	public void execute() {
		if (!meetsRequirements(player, tree, object)) {
			stop();
			return;
		}
		if (pos == 3) {
			if ((successfulAttemptChance()) && (handleTreeChopping())) {
				stop();
				return;
			}
			pos = 0;
		} else {
			pos += 1;
		}
		animate();
	}

	private void handleGivingLogs() {
		player.getInventory().add(new Item(tree.getReward(), 1));
		player.getSkill().addExperience(Skills.WOODCUTTING, tree.getExperience());
	}

	private boolean handleTreeChopping() {
		if (isNormalTree()) {
			successfulAttempt();
			return true;
		}
		if (Misc.randomNumber(10 + (World.getActivePlayers() / 20)) == 1) {
			successfulAttempt();
			return true;
		}
		handleGivingLogs();
		return false;
	}

	private boolean isNormalTree() {
		for (int i : NORMAL_TREES) {
			if (i == treeId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onStop() {
		player.getUpdateFlags().sendAnimation(new Animation(65535));
	}

	private void successfulAttempt() {
		SoundPlayer.play(player, Sounds.CHOP_TREE_DOWN);
		player.getClient().queueOutgoingPacket(new SendMessage("You successfully chop down the tree."));
		player.getInventory().add(new Item(tree.getReward(), 1));
		player.getSkill().addExperience(Skills.WOODCUTTING, tree.getExperience());
		player.getUpdateFlags().sendAnimation(new Animation(65535));
		GameObject replacement = new GameObject(tree.getReplacement(), object.getLocation().getX(),
				object.getLocation().getY(), object.getLocation().getZ(), 10, 0);
		RSObject rsObject = new RSObject(replacement.getLocation().getX(), replacement.getLocation().getY(),
				replacement.getLocation().getZ(), object.getId(), 10, 0);
		player.getAchievements().incr(player, "Cut 1,000 trees");
		if (rsObject != null) {
			ObjectManager.register(replacement);
			Region.getRegion(rsObject.getX(), rsObject.getY()).removeObject(rsObject);
			TaskQueue.queue(new StumpTask(object, treeId, tree.getRespawnTimer()));
		}
	}

	private boolean successfulAttemptChance() {
		return Skills.isSuccess(player, Skills.WOODCUTTING, tree.getLevelRequired(), axe.getLevelRequired());
	}

}