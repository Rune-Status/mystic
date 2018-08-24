package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.GameConstants;
import org.mystic.game.World;
import org.mystic.game.model.content.Emotes;
import org.mystic.game.model.content.ItemsOnDeath;
import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.TeleportationInterface;
import org.mystic.game.model.content.TeleportationInterface.TeleportType;
import org.mystic.game.model.content.bank.BankSecurity;
import org.mystic.game.model.content.combat.formula.MeleeFormulas;
import org.mystic.game.model.content.dialogue.impl.OptionDialogue;
import org.mystic.game.model.content.dialogue.impl.Tutorial;
import org.mystic.game.model.content.minigames.duelarena.DuelingConstants;
import org.mystic.game.model.content.skill.cooking.CookingTask;
import org.mystic.game.model.content.skill.crafting.Crafting;
import org.mystic.game.model.content.skill.crafting.HideCrafting;
import org.mystic.game.model.content.skill.crafting.HideTanning;
import org.mystic.game.model.content.skill.fletching.Fletching;
import org.mystic.game.model.content.skill.magic.Autocast;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.content.skill.magic.spells.BoltEnchanting;
import org.mystic.game.model.content.skill.smithing.SmithingConstants;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendConfig;
import org.mystic.game.model.networking.outgoing.SendEnterString;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendSidebarInterface;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.utility.Misc;

public class ClickButtonPacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 5;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		int button = in.readShort();
		in.reset();
		button = Misc.hexToInt(in.readBytes(2));
		if ((player.isDead()) || (player.getMagic().isTeleporting()) || (!player.getController().canClick())) {
			return;
		}
		if (player.isSpeared()) {
			return;
		}
		if ((player.getController().equals(Tutorial.TUTORIAL_CONTROLLER)) && (player.getDialogue() != null)) {
			player.getDialogue().clickButton(button);
			return;
		}
		if (GameConstants.DEV_MODE) {
			player.getClient().queueOutgoingPacket(new SendMessage("Button: " + button));
		}
		if (button == 14067) {
			player.setAppearanceUpdateRequired(true);
			player.getAttributes().remove("setapp");
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return;
		}
		if (player.getInterfaceManager().getMain() == 53500) {
			if (button <= 209161 && button >= 209012) {
				int c = 0;
				for (Player p : World.getPlayers()) {
					if (p != null && p.isActive() && p.getPlayerShop().hasAnyItems()) {
						if (209012 + c == button) {
							player.getShopping().open(p);
							break;
						}
						c++;
					}
				}
				return;
			}
		}
		if (player.getInterfaceManager().getMain() == 51000) {
			if (button >= 180181 && !(button > 180239)) {
				TeleportationInterface.click(player, player.getType(), button);
				return;
			}
		}
		switch (button) {

		// Previous
		case 156065:
			if (player.getPrevious() == null) {
				player.send(new SendMessage("You do not currently have a previous teleport option set."));
			} else {
				player.getMagic().teleport(player.getPrevious().getX(), player.getPrevious().getY(),
						player.getPrevious().getZ(), TeleportTypes.WIZARD);
			}
			break;

		// Monsters
		case 161033:
			TeleportationInterface.open(player, TeleportType.MONSTERS);
			break;

		// Dungeons
		case 161036:
			TeleportationInterface.open(player, TeleportType.DUNGEONS);
			break;

		// Bosses
		case 165015:
			TeleportationInterface.open(player, TeleportType.BOSSES);
			break;

		// Wilderness
		case 165018:
			TeleportationInterface.open(player, TeleportType.WILDERNESS);
			break;

		// Minigames
		case 165021:
			TeleportationInterface.open(player, TeleportType.MINIGAMES);
			break;

		// Areas
		case 165024:
			TeleportationInterface.open(player, TeleportType.AREAS);
			break;

		// Guilds
		case 165027:
			TeleportationInterface.open(player, TeleportType.GUILDS);
			break;

		/// book buttons
		case 49121:
			player.send(new SendRemoveInterfaces());
			break;

		case 49169:
			player.flip(true);
			break;

		case 49167:
			player.flip(false);
			break;

		case 209002:
			player.start(new OptionDialogue("Search name", p -> {
				player.setEnterXInterfaceId(55777);
				player.getClient().queueOutgoingPacket(new SendEnterString());
			}, "Search item", p -> {
				player.setEnterXInterfaceId(55778);
				player.getClient().queueOutgoingPacket(new SendEnterString());
			}));
			break;

		// send xp counter interface
		case 129:
			player.send(new SendInterface(32500));
			break;

		// close teleport interface
		case 130051:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;

		// close rune-pouch
		case 162235:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;

		// make-over
		case 14067:
			player.setAppearanceUpdateRequired(true);
			player.getAttributes().remove("setapp");
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;

		// closes the custom settings interface
		case 121027:
			if (player.isBusy()) {
				player.send(new SendMessage("Please finish what you're doing before doing this."));
			} else {
				player.send(new SendSidebarInterface(11, 904));
			}
			break;

		// opens the custom settings interface
		case 74212:
			if (player.isBusy()) {
				player.send(new SendMessage("Please finish what you're doing before doing this."));
			} else {
				player.send(new SendSidebarInterface(11, 31002));
			}
			break;

		case 58073:
			player.send(
					new SendMessage("If you do not know your pin you may request a manual removal via the forums."));
			break;

		case 58074:
			player.send(new SendRemoveInterfaces());
			break;

		case 75007:
			BoltEnchanting.open(player);
			break;

		case 58025:
		case 58026:
		case 58027:
		case 58028:
		case 58029:
		case 58030:
		case 58031:
		case 58032:
		case 58033:
		case 58034:
			BankSecurity.clickedButton(player, button);
			break;

		// equipment screen
		case 59097:
		case 83093:
			player.send(new SendString(
					"Melee Max Hit: " + MeleeFormulas.getMaxHit(player, player.getCombat().getAttacking()), 15155));
			player.send(new SendInterface(21172));
			break;

		// defensive autocasting
		case 94047:
			player.send(new SendMessage("This feature will be available soon."));
			break;

		// items kept on death
		case 59098:
			ItemsOnDeath.display(player);
			break;

		case 58242:
			QuestTab.update(player);
			player.send(new SendSidebarInterface(2, 638));
			break;

		case 113228:
			player.getAchievements().onLogin(player);
			player.send(new SendSidebarInterface(2, 15001));
			break;

		case 29213:
		case 29238:
		case 30108:
		case 29124:
		case 29049:
		case 29199:
		case 29138:
		case 48034:
			player.getSpecialAttack().clickSpecialButton(button);
			break;

		case 155026:
			player.getClient().queueOutgoingPacket(new SendInterface(38700));
			break;

		case 151045:
			player.getClient().queueOutgoingPacket(new SendInterface(39700));
			break;

		case 9118:
		case 83051:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;

		case 3162:
			player.setMusicVolume((byte) 4);
			player.getClient().queueOutgoingPacket(new SendConfig(168, 4));
			break;

		case 70209:
			player.setEnterXInterfaceId(6969);
			break;

		case 3163:
		case 3164:
		case 3165:
		case 3166:
			player.setMusicVolume((byte) (3166 - button));
			player.getClient().queueOutgoingPacket(new SendConfig(168, player.getMusicVolume()));
			break;

		case 3173:
			player.setSoundVolume((byte) 4);
			player.getClient().queueOutgoingPacket(new SendConfig(169, 4));
			break;

		case 3174:
		case 3175:
		case 3176:
		case 3177:
			player.setSoundVolume((byte) (3177 - button));
			player.getClient().queueOutgoingPacket(new SendConfig(169, player.getSoundVolume()));
			break;

		case 24125:
			player.getAttributes().remove("manual");
			break;

		case 24126:
			player.getAttributes().set("manual", Byte.valueOf((byte) 1));
			break;

		case 108005:
			player.getClient().queueOutgoingPacket(new SendInterface(19148));
			break;

		case 9154:
			if (player.isBusy()) {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("Please finish what you're doing before logging out."));
			} else if (player.getCombat().inCombat()) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You can't log out until 10 seconds after the end of combat."));
			} else {
				if (player.getClient().getStage() == Client.Stages.LOGGED_IN) {
					player.logout(false);
				}
			}
			break;

		case 153:
			if (player.isGoldMember()) {
				player.getRunEnergy().toggleResting(true);
			} else {
				player.send(new SendMessage("You must be a gold member to use the resting feature."));
			}
			break;

		case 74:
		case 152:
		case 33230:
		case 74214:
			player.getRunEnergy().setRunning(!player.getRunEnergy().isRunning());
			player.getClient().queueOutgoingPacket(new SendConfig(149, player.getRunEnergy().isRunning() ? 1 : 0));
			player.getClient().queueOutgoingPacket(new SendConfig(429, player.getRunEnergy().isRunning() ? 1 : 0));
			break;

		case 3138:
			player.setScreenBrightness((byte) 1);
			break;

		case 3140:
			player.setScreenBrightness((byte) 2);
			break;

		case 3142:
			player.setScreenBrightness((byte) 3);
			break;

		case 3144:
			player.setScreenBrightness((byte) 4);
			break;

		case 3146:
			player.setMultipleMouseButtons((byte) (player.getMultipleMouseButtons() == 0 ? 1 : 0));
			break;

		case 3147:
			player.setChatEffectsEnabled((byte) (player.getChatEffectsEnabled() == 0 ? 1 : 0));
			break;

		case 3189:
			player.setSplitPrivateChat((byte) (player.getSplitPrivateChat() == 0 ? 1 : 0));
			player.getClient().queueOutgoingPacket(new SendConfig(287, player.getSplitPrivateChat()));
			break;

		case 48176:
			player.setAcceptAid((byte) (player.getAcceptAid() == 0 ? 1 : 0));
			break;

		case 150:
		case 89061:
		case 93202:
		case 93209:
		case 93217:
		case 93225:
		case 94051:
		case 93233:
			player.setRetaliate(!player.isRetaliate());
			player.send(new SendConfig(172, player.isRetaliate() ? 1 : 0));
			break;

		default:
			if (player.getBank().clickButton(button)) {
				break;
			}
			if (player.getSummoning().click(button)) {
				break;
			}
			if (Fletching.SINGLETON.clickButton(player, button)) {
				break;
			}
			if (HideCrafting.SINGLETON.clickButton(player, button)) {
				break;
			}
			if (Crafting.handleCraftingByButtons(player, button)) {
				break;
			}
			if (HideTanning.clickButton(player, button)) {
				break;
			}
			if (player.getPrayer().clickButton(button)) {
				break;
			}
			if (CookingTask.handleCookingByAmount(player, button)) {
				break;
			}
			if ((player.getDialogue() != null) && (player.getDialogue().clickButton(button))) {
				break;
			}
			if (Autocast.clickButton(player, button)) {
				break;
			}
			if (Emotes.clickButton(player, button)) {
				break;
			}
			if (DuelingConstants.clickDuelButton(player, button)) {
				break;
			}
			if (player.getTrade().clickTradeButton(button)) {
				break;
			}
			if (player.getMagic().clickMagicButtons(button)) {
				break;
			}
			if (EquipmentConstants.clickAttackStyleButtons(player, button)) {
				break;
			}
			if (SmithingConstants.clickSmeltSelection(player, button)) {
				break;
			}
			break;
		}
	}
}
