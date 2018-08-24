package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.GameConstants;
import org.mystic.game.World;
import org.mystic.game.model.entity.following.Following;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.WalkToActions;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.packet.IncomingPacket;

public class NPCPacket extends IncomingPacket {

	public static final int FIRST_CLICK = 155;

	public static final int SECOND_CLICK = 17;

	public static final int THIRD_CLICK = 21;

	public static final int ATTACK = 72;

	public static final int MAGIC_ON_NPC = 131;

	public static final int ITEM_ON_NPC = 57;

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isDead()) || (player.getMagic().isTeleporting())
				|| (!player.getController().canClick() || player.isSpeared())) {
			return;
		}
		if (!player.getMagic().isDFireShieldEffect()) {
			player.getMagic().getSpellCasting().resetOnAttack();
		}
		player.getCombat().reset();
		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		switch (opcode) {

		case FIRST_CLICK:
			int slot = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);
			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}
			WalkToActions.interactNPC(player, 1, slot);
			break;

		case SECOND_CLICK:
			slot = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE) & 0xFFFF;
			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}
			WalkToActions.interactNPC(player, 2, slot);
			break;

		case THIRD_CLICK:
			slot = in.readShort(true);
			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}
			WalkToActions.interactNPC(player, 3, slot);
			break;

		case ATTACK:
			slot = in.readShort(StreamBuffer.ValueType.A);
			Npc mob = World.getNpcs()[slot];
			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}
			player.getMovementHandler().reset();
			player.getCombat().setAttacking(mob);
			player.getFollowing().setFollow(mob, Following.FollowType.COMBAT);
			if (GameConstants.DEV_MODE) {
				player.getClient().queueOutgoingPacket(new SendMessage("NPC ID: " + mob.getId()));
			}
			break;

		case MAGIC_ON_NPC:
			slot = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			int magicId = in.readShort(StreamBuffer.ValueType.A);
			player.getMovementHandler().reset();
			mob = World.getNpcs()[slot];
			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}
			if (GameConstants.DEV_MODE) {
				player.getClient().queueOutgoingPacket(new SendMessage("Magic id: " + magicId));
			}
			player.getMagic().getSpellCasting().castCombatSpell(magicId, mob);
			break;

		case ITEM_ON_NPC:
			int itemId = in.readShort(StreamBuffer.ValueType.A);
			slot = in.readShort(StreamBuffer.ValueType.A);
			int itemSlot = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}
			if (!player.getInventory().slotContainsItem(itemSlot, itemId)) {
				return;
			}
			WalkToActions.useItemOnNpc(player, itemId, slot);
			break;

		}
	}
}
