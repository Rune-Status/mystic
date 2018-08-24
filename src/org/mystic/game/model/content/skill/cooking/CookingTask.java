package org.mystic.game.model.content.skill.cooking;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendChatInterface;
import org.mystic.game.model.networking.outgoing.SendEnterXInterface;
import org.mystic.game.model.networking.outgoing.SendItemOnInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;

public class CookingTask extends Task {

	public static final int COOKING_GAUNTLETS = 775;

	private static final int[][] BUTTON_IDS = { { 53152, 1 }, { 53151, 5 }, { 53149, 28 }, { 53150, 100 } };

	private static final int[] COOKABLE_OBJECTS = { 9682, 9374, 21302, 12269, 114, 2728, 2732, 5981, 6096, 49035 };

	public static void attemptCooking(Player player, int cook, int object, int amount) {
		Cookable data = Cookable.forId(cook);
		if (data == null) {
			return;
		}
		if (!meetsRequirements(player, data, cook, object)) {
			return;
		}
		TaskQueue.queue(new CookingTask(player, data, cook, object, amount));
		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
	}

	public static boolean handleCookingByAmount(Player player, int buttonId) {
		int amount = 0;
		for (int i = 0; i < BUTTON_IDS.length; i++) {
			if (BUTTON_IDS[i][0] == buttonId) {
				amount = BUTTON_IDS[i][1];
				break;
			}
		}
		if (amount == 0) {
			return false;
		}
		if (amount != 100) {
			attemptCooking(player, ((Integer) player.getAttributes().get("cookingitem")).intValue(),
					((Integer) player.getAttributes().get("cookingobject")).intValue(), amount);
		} else {
			player.getClient().queueOutgoingPacket(
					new SendEnterXInterface(1743, ((Integer) player.getAttributes().get("cookingitem")).intValue()));
		}
		return true;
	}

	public static boolean isCookableObject(GameObject object) {
		for (int i : COOKABLE_OBJECTS) {
			if (object.getId() == i) {
				return true;
			}
		}
		return false;
	}

	private static boolean meetsRequirements(Player player, Cookable data, int used, int usedOn) {
		int cookingLevel = player.getSkill().getLevels()[7];
		if (cookingLevel < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a cooking level of "
					+ data.getLevelRequired() + " to cook " + Item.getDefinition(used).getName() + "."));
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return false;
		}
		if (!player.getInventory().hasItemId(used)) {
			return false;
		}
		if (!ObjectManager.getActive().contains(player.getInteractingObject())) {
			return false;
		}
		return true;
	}

	public static void showInterface(Player player, GameObject usedOn, Item used) {
		if (used == null || Cookable.forId(used.getId()) == null) {
			player.getClient().queueOutgoingPacket(new SendMessage("Nothing interesting happens."));
			return;
		}
		player.getClient().queueOutgoingPacket(new SendChatInterface(1743));
		player.getClient().queueOutgoingPacket(new SendItemOnInterface(13716, 250, used.getId()));
		player.getClient()
				.queueOutgoingPacket(new SendString("\\n\\n\\n\\n\\n" + used.getDefinition().getName(), 13717));

		player.setInteractingObject(usedOn);
		player.getAttributes().set("cookingobject", Integer.valueOf(usedOn.getId()));
		player.getAttributes().set("cookingitem", Integer.valueOf(used.getId()));
	}

	private final Cookable data;

	private final Player player;

	private int used, usedOn, amount;

	public CookingTask(Player player, Cookable data, int used, int usedOn, int amount) {
		super(player, 3, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.data = data;
		this.used = used;
		this.usedOn = usedOn;
		this.amount = amount;
	}

	private void burnFood() {
		player.getInventory().add(new Item(data.getBurnt(), 1));
		player.getClient().queueOutgoingPacket(
				new SendMessage("You accidentally burn the " + Item.getDefinition(used).getName().toLowerCase() + "."));
	}

	private void cookFood() {
		player.getInventory().add(new Item(data.getReplacement(), 1));
		double experience = data.getExperience();
		player.getSkill().addExperience(7, experience);
		player.getClient().queueOutgoingPacket(
				new SendMessage("You roast the " + Item.getDefinition(used).getName().toLowerCase() + "."));
		player.getAchievements().incr(player, "Cook 1,000 items");
	}

	@Override
	public void execute() {
		if (!meetsRequirements(player, data, used, usedOn)) {
			stop();
			return;
		}
		SoundPlayer.play(player, Sounds.COOK_FOOD);
		player.getInventory().remove(used);
		int animation = usedOn == 2732 ? 897 : 833;
		player.getUpdateFlags().sendAnimation(new Animation(animation));
		if (successfulAttempt()) {
			cookFood();
		} else {
			burnFood();
		}
		amount--;
		if (amount == 0) {
			stop();
		}
	}

	private int getCookingLevelBoost() {
		final Item cape = player.getEquipment().getItems()[EquipmentConstants.CAPE_SLOT];
		final Item gloves = player.getEquipment().getItems()[9];
		if ((gloves != null) && (gloves.getId() == 775)) {
			return 3;
		}
		if (cape != null) {
			if (cape.getId() == 9801) {
				return 1000;
			} else if (cape.getId() == 9802) {
				return 1000;
			}
		}
		return 0;
	}

	@Override
	public void onStop() {
		player.getUpdateFlags().sendAnimation(new Animation(65535));
	}

	private boolean successfulAttempt() {
		if (player.getSkill().getLevels()[7] > data.getNoBurnLevel()) {
			return true;
		}
		return Skills.isSuccess(player, Skills.COOKING, data.getLevelRequired(), getCookingLevelBoost());
	}
}
