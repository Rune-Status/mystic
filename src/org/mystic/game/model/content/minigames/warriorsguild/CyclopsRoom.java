package org.mystic.game.model.content.minigames.warriorsguild;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.entity.player.controllers.GenericMinigameController;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public final class CyclopsRoom extends WarriorsGuildConstants {

	public static class TokenRoomController extends GenericMinigameController {

		@Override
		public boolean allowMultiSpells() {
			return false;
		}

		@Override
		public boolean allowPvPCombat() {
			return false;
		}

		@Override
		public boolean canAttackNPC() {
			return true;
		}

		@Override
		public boolean canAttackPlayer(Player p, Player p2) {
			return false;
		}

		@Override
		public boolean canDrink(Player p) {
			return true;
		}

		@Override
		public boolean canEat(Player p) {
			return true;
		}

		@Override
		public boolean canEquip(Player p, int id, int slot) {
			return true;
		}

		@Override
		public boolean canTeleport() {
			return true;
		}

		@Override
		public boolean canUseCombatType(Player p, CombatTypes type) {
			return true;
		}

		@Override
		public boolean canUsePrayer(Player p, int id) {
			return true;
		}

		@Override
		public boolean canUseSpecialAttack(Player p) {
			return true;
		}

		@Override
		public Location getRespawnLocation(Player player) {
			return new Location(PlayerConstants.RESPAWN);
		}

		@Override
		public boolean isSafe(Player player) {
			return false;
		}

		@Override
		public void onControllerInit(Player p) {
		}

		@Override
		public void onDeath(Player p) {
			if (p.getAttributes().get("warrguildtokentask") != null) {
				((Task) p.getAttributes().get("warrguildtokentask")).stop();
			}
			p.getAttributes().remove("cyclopsdefenderdrop");
			p.getAttributes().remove("warrguildtokentask");
			p.setController(ControllerManager.DEFAULT_CONTROLLER);
		}

		@Override
		public void onDisconnect(Player p) {
			if (p.getAttributes().get("warrguildtokentask") != null) {
				((Task) p.getAttributes().get("warrguildtokentask")).stop();
			}
			p.getAttributes().remove("cyclopsdefenderdrop");
			p.getAttributes().remove("warrguildtokentask");
			p.setController(ControllerManager.DEFAULT_CONTROLLER);
			p.teleport(new Location(2846, 3540, 2));
		}

		@Override
		public void onTeleport(Player p) {
			if (p.getAttributes().get("warrguildtokentask") != null) {
				((Task) p.getAttributes().get("warrguildtokentask")).stop();
			}
			p.getAttributes().remove("cyclopsdefenderdrop");
			p.getAttributes().remove("warrguildtokentask");
			p.setController(ControllerManager.DEFAULT_CONTROLLER);
		}

		@Override
		public void tick(Player p) {
		}

		@Override
		public String toString() {
			return "Cyclops WG Room";
		}

		@Override
		public boolean canLogOut() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean canMove(Player p) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean canSave() {
			return true;
		}

		@Override
		public boolean canTalk() {
			return true;
		}

		@Override
		public boolean canTrade() {
			return false;
		}

		@Override
		public boolean canUnequip(Player player) {
			return true;
		}

		@Override
		public boolean canDrop(Player player) {
			return true;
		}
	}

	public static final TokenRoomController TOKEN_ROOM_CONTROLLER = new TokenRoomController();

	public static void dropDefender(Player player, Npc mob) {
		if (Misc.randomNumber(12) != 1) {
			return;
		}
		if (player.getAttributes().get("cyclopsdefenderdrop") == null) {
			return;
		}
		int defender = ((Integer) player.getAttributes().get("cyclopsdefenderdrop")).intValue();
		GroundItemHandler.add(new Item(defender), mob.getLocation(), player);
		if (defender != 20072) {
			if (defender == 8850) {
				player.getAttributes().set("cyclopsdefenderdrop", Integer.valueOf(20072));
				defender = ((Integer) player.getAttributes().get("cyclopsdefenderdrop")).intValue();
			} else {
				player.getAttributes().set("cyclopsdefenderdrop", Integer.valueOf(defender + 1));
				defender = ((Integer) player.getAttributes().get("cyclopsdefenderdrop")).intValue();
			}
			if (defender == 20072) {
				player.getClient().queueOutgoingPacket(new SendMessage(
						"@dre@You have recieved a defender. The cyclops are now set to drop: Dragon defender."));
			} else {
				player.getClient().queueOutgoingPacket(
						new SendMessage("@dre@You have recieved a defender. The cyclops are now set to drop: "
								+ Item.getDefinition(defender).getName() + "."));
			}
		} else if (defender == 20072) {
			player.getClient().queueOutgoingPacket(new SendMessage("@dre@You have recieved a Dragon defender."));
		}
	}

	public static boolean handleDoor(Player player) {
		if (player.getLocation().getX() <= 2846) {
			if (player.getInventory().getItemAmount(8851) >= 150) {
				player.getInventory().remove(8851, 100);
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You pay 100 tokens and enter the cyclops arena."));
				player.teleport(new Location(2847, 3541, 2));
				executeTimer(player);
				int defender = getDefender(player);
				player.getAttributes().set("cyclopsdefenderdrop", Integer.valueOf(defender));
				player.getClient().queueOutgoingPacket(new SendMessage(
						"@dre@The cyclops will now drop a: " + Item.getDefinition(defender).getName() + "."));
				player.setController(TOKEN_ROOM_CONTROLLER);
				return false;
			}
			DialogueManager.sendStatement(player, "You need at least 150 Warriors Guild Tokens to enter the arena.");
			return true;
		} else {
			if (player.getAttributes().get("warrguildtokentask") != null) {
				((Task) player.getAttributes().get("warrguildtokentask")).stop();
			}
			player.getAttributes().remove("cyclopsdefenderdrop");
			player.getAttributes().remove("warrguildtokentask");
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			player.teleport(new Location(2846, 3540, 2));
		}
		return true;
	}

	public static void executeTimer(Player player) {
		Task task = new ConsumeTokens(player, (byte) 100);
		player.getAttributes().set("warrguildtokentask", task);
		TaskQueue.queue(task);
	}

	public static int getDefender(Player player) {
		int currentDefender = -1;
		Item shield = player.getEquipment().getItems()[5];
		for (int i = 0; i < DEFENDERS.length; i++) {
			if ((player.getInventory().hasItemId(DEFENDERS[i]))
					|| ((shield != null) && (shield.getId() == DEFENDERS[i]))) {
				currentDefender = i;
			}
		}
		if ((currentDefender >= -1) && (currentDefender != 7)) {
			currentDefender++;
		}
		return DEFENDERS[currentDefender];
	}
}