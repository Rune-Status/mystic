package org.mystic.game.model.content;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendUpdateItems;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class WildernessChest {

	public static class Reward {

		private final int id;
		private final int min;
		private final int max;

		public Reward(int id, int min, int max) {
			this.id = id;
			this.min = min;
			this.max = max;
		}
	}

	private static final Reward[] COMMON = { new Reward(995, 100000, 200000), new Reward(558, 50, 250),
			new Reward(562, 50, 200), new Reward(560, 50, 200), new Reward(565, 50, 200), new Reward(4740, 30, 150) };

	private static final Reward[] RARE = { new Reward(4708, 1, 1), new Reward(4710, 1, 1), new Reward(4712, 1, 1),
			new Reward(4714, 1, 1), new Reward(4716, 1, 1), new Reward(4718, 1, 1), new Reward(4720, 1, 1),
			new Reward(4722, 1, 1), new Reward(4724, 1, 1), new Reward(4726, 1, 1), new Reward(4728, 1, 1),
			new Reward(4730, 1, 1), new Reward(4732, 1, 1), new Reward(4734, 1, 1), new Reward(4736, 1, 1),
			new Reward(4738, 1, 1), new Reward(4745, 1, 1), new Reward(4747, 1, 1), new Reward(4749, 1, 1),
			new Reward(4751, 1, 1), new Reward(4753, 1, 1), new Reward(4755, 1, 1), new Reward(4757, 1, 1),
			new Reward(4759, 1, 1) };

	public static void displayReward(Player p) {
		if (!p.getInventory().hasItemId(1543)) {
			DialogueManager.sendStatement(p, "The chest is locked shut.");
			return;
		}
		p.getUpdateFlags().sendAnimation(881, 0);
		p.getInventory().remove(1543);
		Item[] reward = WildernessChest.roll();
		int total = 0;
		for (Item i : reward) {
			total += GameDefinitionLoader.getStoreSellToValue(i.getId()) * i.getAmount();
			p.getInventory().addOrCreateGroundItem(i.getId(), i.getAmount(), true);
			p.send(new SendUpdateItems(6963, reward));
		}
		p.send(new SendInterface(6960));
		p.send(new SendMessage("You find approximately @dre@" + Misc.formatCoins2(total)
				+ " @bla@worth of loot from within the chest!"));
	}

	public static Item[] roll() {
		int amount = 2 + (Misc.randomNumber(2) == 0 ? 1 : 0);
		Item[] rewards = new Item[amount];
		int chance = 0;
		if (Misc.randomNumber(9) == 0) {
			Reward r = RARE[Misc.randomNumber(RARE.length)];
			rewards[chance] = new Item(r.id, r.max > r.min ? r.min + Misc.randomNumber(r.max - r.min) : r.min);
			chance++;
			if (Misc.randomNumber(18) == 0) {
				r = RARE[Misc.randomNumber(RARE.length)];
				rewards[chance] = new Item(r.id, r.max > r.min ? r.min + Misc.randomNumber(r.max - r.min) : r.min);
				chance++;
			}
		}
		while (chance < rewards.length) {
			Reward r = COMMON[Misc.randomNumber(COMMON.length)];
			rewards[chance] = new Item(r.id, r.max > r.min ? r.min + Misc.randomNumber(r.max - r.min) : r.min);
			chance++;
		}
		return rewards;
	}
}