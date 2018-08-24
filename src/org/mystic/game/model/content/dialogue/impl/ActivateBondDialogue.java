package org.mystic.game.model.content.dialogue.impl;

import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.MembershipBond;
import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueConstants;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

public class ActivateBondDialogue extends Dialogue {

	public ActivateBondDialogue(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case DialogueConstants.OPTIONS_2_1:
			next = 3;
			execute();
			return true;
		case DialogueConstants.OPTIONS_2_2:
			next = 2;
			execute();
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, 1699, Emotion.HAPPY_TALK,
					"Activating this bond will increase your membership status.", "Are you sure you want to do this?");
			next++;
			break;
		case 1:
			DialogueManager.sendOption(player, "Yes", "No");
			break;
		case 2:
			DialogueManager.sendPlayerChat(player, Emotion.CALM, "No thanks");
			player.getDialogue().end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		case 3:
			if (player.getInventory().playerHasItem(new Item(MembershipBond.MEMBERSHIP_BOND_ID, 1))) {
				player.getInventory().remove(20086, 1);
				if (player.getRights().greater(Rights.LEGENDARY_GOLD_MEMBER) || IronMan.isIronMan(player)) {
					player.setGoldMember(true);
				} else {
					player.setRights(Rights.GOLD_MEMBER);
				}
				QuestTab.update(player);
				player.setGoldMember(true);
				player.getUpdateFlags().sendAnimation(new Animation(10301));
				player.send(new SendMessage("You activate the bond and are now a Gold Member!"));
				player.getDialogue().end();
				player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			} else {
				player.getDialogue().end();
				player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			}
			break;
		}
	}
}