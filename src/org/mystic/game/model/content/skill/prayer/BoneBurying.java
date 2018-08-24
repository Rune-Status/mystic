package org.mystic.game.model.content.skill.prayer;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendSound;
import org.mystic.game.task.Task;
import org.mystic.game.task.Task.BreakType;
import org.mystic.game.task.Task.StackType;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;

public enum BoneBurying {

	MONKEY_BONES(3179, 25.5D),

	NORMAL_BONES(526, 4.5D),

	WOLF_BONES(2859, 4.5D),

	BAT_BONES(530, 4.5D),

	BIG_BONES(532, 18.0D),

	BABYDRAGON_BONES(534, 30.0D),

	DRAGON_BONES(536, 72.0D),

	DAGG_BONES(6729, 125.0D),

	OURG_BONES(4834, 140.0D),

	WYVERN_BONES(6812, 150.0D),

	FROST_DRAGON_BONES(3123, 200.0D),

	LONG_BONE(10976, 250.0D);

	private static Map<Integer, BoneBurying> bones = new HashMap<Integer, BoneBurying>();

	static {
		for (BoneBurying b : values()) {
			bones.put(Integer.valueOf(b.getId()), b);
		}
	}

	public static BoneBurying forId(int id) {
		return bones.get(Integer.valueOf(id));
	}

	private final int id;

	private final double experience;

	private BoneBurying(final int id, final double experience) {
		this.id = id;
		this.experience = experience;
	}

	public final int getId() {
		return id;
	}

	public final double getXP() {
		return experience;
	}

	public static final String USING_ON_ALTAR_KEY = "boneonaltarkey";

	public static final int BURYING_ANIMATION = 827;

	public static boolean bury(Player player, int id, int slot) {
		BoneBurying bones = BoneBurying.forId(id);
		if (bones == null) {
			return false;
		}
		if (player.getSkill().locked()) {
			return true;
		}
		player.getSkill().lock(2);
		SoundPlayer.play(player, Sounds.BURY_BONE);
		player.getUpdateFlags().sendAnimation(827, 0);
		player.getClient().queueOutgoingPacket(
				new SendMessage("You bury the " + Item.getDefinition(bones.id).getName().toLowerCase() + "."));
		player.getInventory().clear(slot);
		player.getSkill().addExperience(5, bones.experience);
		player.getAchievements().incr(player, "Bury 500 bones");
		player.getAchievements().incr(player, "Bury 5,000 bones");
		return true;
	}

	public static void finishOnAltar(Player player, int amount) {
		if (player.getAttributes().get("boneonaltarkey") == null) {
			return;
		}
		int item = player.getAttributes().getInt("boneonaltarkey");
		BoneBurying bones = BoneBurying.forId(item);
		if (bones == null) {
			return;
		}
		int invAmount = player.getInventory().getItemAmount(item);
		if (invAmount == 0) {
			return;
		}
		if (invAmount < amount) {
			amount = invAmount;
		}
		player.getSkill().lock(2);
		player.getClient().queueOutgoingPacket(new SendMessage(
				"You sacrifice the " + Item.getDefinition(bones.id).getName().toLowerCase() + " at the altar."));
		player.getUpdateFlags().sendAnimation(3705, 5);
		player.getInventory().remove(new Item(item, amount));
		player.getSkill().addExperience(Skills.PRAYER, (bones.experience * 4.0D) * amount);
		player.getAchievements().incr(player, "Bury 500 bones");
		player.getAchievements().incr(player, "Bury 5,000 bones");
	}

	public static boolean useBonesOnAltar(Player player, int item, int object) {
		if (object == 2640 || object == 409) {
			BoneBurying bones = BoneBurying.forId(item);
			if (bones == null) {
				return false;
			}
			TaskQueue.queue(
					new Task(player, 3, true, StackType.NEVER_STACK, BreakType.ON_MOVE, TaskIdentifier.BONES_ON_ALTER) {
						@Override
						public void execute() {
							if (!player.getInventory().hasItemId(item)) {
								stop();
								return;
							}
							if (player.getSkill().locked()) {
								return;
							}
							player.getSkill().lock(2);
							player.getClient().queueOutgoingPacket(new SendSound(442, 1, 0));
							player.getClient().queueOutgoingPacket(new SendMessage("You sacrifice the "
									+ Item.getDefinition(bones.id).getName().toLowerCase() + " at the altar."));
							player.getUpdateFlags().sendAnimation(3705, 5);
							player.getInventory().remove(item);
							player.getSkill().addExperience(Skills.PRAYER, bones.experience * 4.0);
						}

						@Override
						public void onStop() {
						}
					});
			return true;
		}
		return false;
	}

}