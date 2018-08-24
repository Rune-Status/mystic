package org.mystic.game.model.content.skill.summoning.impl;

import org.mystic.game.model.content.dialogue.impl.ConfirmDialogue;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class PackYak {

	private static final Graphic graphic = new Graphic(1316);

	private static final Animation animation = new Animation(7660);

	public static void winterStorage(Player player, Item usedWith, Item itemUsed) {
		if (player.getSkill().locked()) {
			return;
		}
		if (player.getCombat().inCombat()) {
			player.send(new SendMessage("You can not use this familiar's special attack whilst in combat."));
			return;
		}
		if (player.getWildernessLevel() > 20) {
			player.send(new SendMessage("You can not use this familiar's special attack above level 20 Wilderness."));
			return;
		}
		if (itemUsed.getId() == 995) {
			player.send(new SendMessage("You can't do this with coins."));
			return;
		}
		if (player.getSummoning().hasFamiliar()) {
			if (player.getSummoning().getFamiliar().getId() == 6873
					|| player.getSummoning().getFamiliar().getId() == 6874) {
				if (player.getBank().hasSpaceFor(itemUsed)) {
					player.start(
							new ConfirmDialogue(player, new String[] { "Are you sure you want to deposit this item?",
									"You will have to visit your bank to get it back." }) {
								@Override
								public void onConfirm() {
									player.getUpdateFlags().sendAnimation(animation);
									player.getUpdateFlags().sendGraphic(graphic);
									player.getBank().add(itemUsed);
									player.getInventory().remove(itemUsed);
									player.getInventory().remove(usedWith.getId(), 1);
									player.getInventory().update();
									player.getSkill().lock(6);
								}
							});
				}
			}
		}
	}
}
