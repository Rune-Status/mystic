package org.mystic.game.model.content.skill.magic.spells;

import org.mystic.game.model.content.skill.magic.Spell;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.ItemCheck;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendOpenTab;
import org.mystic.utility.GameDefinitionLoader;

public class HighAlchemy extends Spell {

	@Override
	public boolean execute(Player player) {
		if (player.getSkill().locked()) {
			return false;
		}
		if (player.getAttributes().get("magicitem") == null) {
			return false;
		}
		int item = player.getAttributes().getInt("magicitem");
		if (item == 995 || item == 15098 || item == 20086 || item == 608 || item == 6758 || item == 607) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot cast alchemy on this item."));
			return false;
		}
		Item coins = new Item(995, GameDefinitionLoader.getHighAlchemyValue(item));
		if (!player.getInventory().hasSpaceFor(coins)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You do not have enough inventory space to do this."));
			return false;
		}
		player.getInventory().remove(item);
		player.getInventory().add(coins);
		if (player.getEquipment().getItems()[3] != null
				&& player.getEquipment().getItems()[3].getDefinition().getName().toLowerCase().contains("staff")) {
			player.getUpdateFlags().sendAnimation(9633, 0);
		} else {
			player.getUpdateFlags().sendAnimation(713, 0);
		}
		player.getClient().queueOutgoingPacket(new SendOpenTab(PlayerConstants.MAGIC_TAB));
		player.getSkill().lock(4);
		SoundPlayer.play(player, Sounds.HIGH_ALCHEMY);
		if ((item == 4151) || (item == 4152) || (ItemCheck.isItemDyed(new Item(item)))) {
			player.getAchievements().incr(player, "Alch an Abyssal whip");
		}
		player.getAchievements().incr(player, "Alch 10,000,000gp", coins.getAmount());
		player.getAchievements().incr(player, "Alch 50,000,000gp", coins.getAmount());
		return true;
	}

	@Override
	public double getExperience() {
		return 32500;
	}

	@Override
	public int getLevel() {
		return 55;
	}

	@Override
	public String getName() {
		return "High alchemy";
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(554, 5), new Item(561, 1) };
	}
}
