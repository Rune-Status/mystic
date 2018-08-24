package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.GameConstants;
import org.mystic.game.model.content.dialogue.impl.teleport.GloryDialogue;
import org.mystic.game.model.content.skill.magic.MagicSkill;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.IncomingPacket;

public class OperateItem extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isDead())
				|| (!player.getController().canClick() || player.isSpeared() || player.getMagic().isTeleporting())) {
			return;
		}
		switch (opcode) {

		case 232:
			final int slot = in.readShort(StreamBuffer.ValueType.A);
			final int itemId = in.readShort(StreamBuffer.ValueType.A);
			if (GameConstants.DEV_MODE) {
				player.send(new SendMessage("Packet: " + opcode + " Slot: " + slot));
			}
			switch (itemId) {
			case 10499:
			case 10498:
				if (slot == 4) {
					player.getClient().queueOutgoingPacket(new SendMessage("You have nothing to collect."));
					return;
				}
				break;
			case 11283:
				if (slot == 4) {
					player.getClient().queueOutgoingPacket(new SendMessage(
							"Your shield has " + player.getMagic().getDragonFireShieldCharges() + " charges."));
				}
				if (slot == 3) {
					player.getMagic().onOperateDragonFireShield();
				}
				break;
			case 1706:
			case 1708:
			case 1710:
			case 1712:
				if (slot == 1) {
					GloryDialogue.useGlory(player, new Location(3086, 3489, 0), itemId, true);
				}
				if (slot == 2) {
					GloryDialogue.useGlory(player, new Location(3093, 3244, 0), itemId, true);
				}
				if (slot == 3) {
					GloryDialogue.useGlory(player, new Location(2909, 3151, 0), itemId, true);
				}
				if (slot == 4) {
					GloryDialogue.useGlory(player, new Location(3097, 3499, 0), itemId, true);
				}
				break;
			case 2552:
			case 2554:
			case 2556:
			case 2558:
			case 2560:
			case 2562:
			case 2564:
			case 2566: // Ring of dueling
				if (slot == 2) {
					player.getPlayer().getMagic().teleport(3356, 3268, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}
				if (slot == 3) {
					player.getPlayer().getMagic().teleport(2442, 3090, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}
				if (slot == 4) {
					player.getPlayer().getMagic().teleport(3086, 3485, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}
				break;

			}
		}
	}

}