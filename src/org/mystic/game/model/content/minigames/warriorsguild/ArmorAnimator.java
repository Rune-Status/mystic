package org.mystic.game.model.content.minigames.warriorsguild;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;

public class ArmorAnimator extends WarriorsGuildConstants {

	public static boolean armorOnAnimator(Player player, int itemId, GameObject object, int x, int y) {
		if (player.getAttributes().get("warriorGuildAnimator") != null) {
			return true;
		}
		int objectX = object.getLocation().getX();
		int objectY = object.getLocation().getY();
		int animatorIndex = -1;
		for (int i = 0; i < ANIMATOR_LOCATIONS.length; i++) {
			if ((objectX == ANIMATOR_LOCATIONS[i][0]) && (objectY == ANIMATOR_LOCATIONS[i][1])) {
				animatorIndex = i;
				break;
			}
		}
		if (animatorIndex == -1) {
			return false;
		}
		player.getAttributes().set("warriorGuildAnimator", Integer.valueOf(animatorIndex));
		int armorIndex = hasArmor(player, itemId);
		if (armorIndex != -1) {
			createAnimatedArmor(player, armorIndex);
		}
		return true;
	}

	public static void createAnimatedArmor(final Player player, final int index) {
		if (!player.getAttributes().isSet("warriorGuildAnimator")) {
			return;
		}
		player.getUpdateFlags().sendAnimation(new Animation(827));
		player.getAttributes().set("stopMovement", Boolean.valueOf(true));
		for (int i = 0; i < ARMOR_SETS[index].length; i++) {
			player.getInventory().remove(ARMOR_SETS[index][i]);
		}
		final int animatorIndex = ((Integer) player.getAttributes().get("warriorGuildAnimator")).intValue();
		TaskQueue.queue(new Task(2) {
			int i = 0;
			Npc mob = null;

			@Override
			public void execute() {
				if (i == 0) {
					setTaskDelay(1);
				} else if (i == 1) {
					DialogueManager.sendTimedStatement(player,
							"You place the armour on the animator and it begins to hum.",
							"Something appears to be working, You stand back..");
				} else if (i == 3) {
					int x = player.getLocation().getX();
					int y = player.getLocation().getY();
					player.getMovementHandler().addToPath(new Location(x, y + 3));
					player.getMovementHandler().finish();
					setTaskDelay(3);
				} else if (i == 4) {
					setTaskDelay(1);
					mob = new Npc(player, WarriorsGuildConstants.ANIMATED_ARMOR[index], false, false, false,
							new Location(WarriorsGuildConstants.ANIMATOR_LOCATIONS[animatorIndex][0],
									WarriorsGuildConstants.ANIMATOR_LOCATIONS[animatorIndex][1], 0));
				} else if (i == 5) {
					mob.getUpdateFlags().sendForceMessage("I'M ALIVE!");
					mob.getCombat().setAttack(player);
					mob.getFollowing().setIgnoreDistance(true);
					player.getAttributes().remove("stopMovement");
					player.face(mob);
					stop();
				}
				i += 1;
			}

			@Override
			public void onStop() {
			}
		});
	}

	public static void dropForAnimatedArmour(Player player, Npc mob) {
		int index = -1;
		for (int i = 0; i < WarriorsGuildConstants.ANIMATED_ARMOR.length; i++) {
			if (mob.getId() == WarriorsGuildConstants.ANIMATED_ARMOR[i]) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return;
		}
		for (int i : WarriorsGuildConstants.ARMOR_SETS[index]) {
			GroundItemHandler.add(new Item(i), mob.getLocation(), player);
		}
		GroundItemHandler.add(new Item(8851, WarriorsGuildConstants.TOKENS[index]), mob.getLocation(), player);
		player.getAttributes().remove("warriorGuildAnimator");
	}

	protected static int hasArmor(Player player, int itemId) {
		int itemIndex = -1;
		for (int i = 0; i < ARMOR_SETS.length; i++) {
			for (int j = 0; j < ARMOR_SETS[i].length; j++) {
				if (itemId == ARMOR_SETS[i][j]) {
					itemIndex = i;
					for (int k = 0; k < ARMOR_SETS[i].length; k++) {
						if (!player.getInventory().hasItemId(ARMOR_SETS[i][k])) {
							player.getClient().queueOutgoingPacket(new SendMessage(
									"You need a complete set of " + ARMOR_TYPE[i].toLowerCase() + " armour."));
							return -1;
						}
					}
					break;
				}
			}
		}
		return itemIndex;
	}

}