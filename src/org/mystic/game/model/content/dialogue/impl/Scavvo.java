package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.Misc;

public class Scavvo extends Dialogue {

	public static final int[][] REPAIR = {

			{ 18349, 7000000, 18350 }, { 18351, 7000000, 18352 }, { 18353, 7000000, 18354 }, { 4708, 250000, 4860 },
			{ 4710, 150000, 4866 }, { 4712, 150000, 4872 }, { 4714, 150000, 4878 }, { 4716, 150000, 4884 },
			{ 4718, 150000, 4890 }, { 4720, 150000, 4896 }, { 4722, 150000, 4902 }, { 4724, 150000, 4908 },
			{ 4726, 150000, 4914 }, { 4728, 150000, 4920 }, { 4730, 150000, 4926 }, { 4732, 150000, 4932 },
			{ 4734, 150000, 4938 }, { 4736, 150000, 4944 }, { 4738, 150000, 4950 }, { 4745, 150000, 4956 },
			{ 4747, 150000, 4962 }, { 4749, 150000, 4968 }, { 4751, 150000, 4974 }, { 4753, 150000, 4980 },
			{ 4755, 150000, 4986 }, { 4757, 150000, 4992 }, { 4759, 150000, 4998 }

	};

	public static void doItemBreaking(Item item) {
		for (int i = 3; i < REPAIR.length; i++) {
			if (item.getId() == REPAIR[i][0]) {
				item.setId(REPAIR[i][2]);
				return;
			}
		}
	}

	private final int repair;

	public Scavvo(Player player) {
		repair = 0;
		this.player = player;
	}

	public Scavvo(Player player, int repair) {
		this.repair = repair;
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		return false;
	}

	@Override
	public void execute() {
		if (repair == 0) {
			DialogueManager.sendNpcChat(player, 7959, Emotion.CALM, new String[] {
					"Hi there, I can repair broken pieces of equipment.", "Use the item in need of repair on me." });
			end();
		} else {
			int[] data = getData(repair);
			if (data == null) {
				DialogueManager.sendNpcChat(player, 519, Emotion.CALM, new String[] { "I can't repair that." });
				end();
				return;
			}
			final int price = (int) (data[0] * (player.isGoldMember() ? 0.75D : 1.0D));
			final int repaired = data[1];
			player.start(new ConfirmDialogue(player, new String[] {
					"Repairs for this item will cost (" + Misc.formatCoins(price) + ") to repair.", "Are you sure?" }) {
				@Override
				public void onConfirm() {
					if (!player.getInventory().hasItemId(repair)) {
						return;
					}
					if (!player.getInventory().hasItemAmount(new Item(995, price))) {
						player.getClient().queueOutgoingPacket(new SendMessage(
								"You do not have enough coins, you need " + Misc.formatCoins(price) + "."));
						return;
					}
					player.getAchievements().incr(player, "Repair 25 equipment");
					player.getAchievements().incr(player, "Repair 75 equipment");
					player.getInventory().remove(repair, 1, false);
					player.getInventory().remove(new Item(995, price), false);
					player.getInventory().add(repaired, 1, true);
				}
			});
		}
	}

	public int[] getData(int id) {
		for (int[] i : REPAIR) {
			if (i[2] == id) {
				return new int[] { i[1], i[0] };
			}
		}
		return null;
	}
}
