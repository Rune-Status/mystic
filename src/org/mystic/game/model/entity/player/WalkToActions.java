package org.mystic.game.model.entity.player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mystic.cache.map.RSObject;
import org.mystic.cache.map.Region;
import org.mystic.game.GameConstants;
import org.mystic.game.World;
import org.mystic.game.model.content.CrystalChest;
import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.MobTeleportation;
import org.mystic.game.model.content.SheepShearing;
import org.mystic.game.model.content.TeleportationInterface;
import org.mystic.game.model.content.TeleportationInterface.TeleportType;
import org.mystic.game.model.content.WildernessChest;
import org.mystic.game.model.content.bank.BankSecurity;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.content.dialogue.OneLineDialogue;
import org.mystic.game.model.content.dialogue.impl.AltarSwap;
import org.mystic.game.model.content.dialogue.impl.Banker;
import org.mystic.game.model.content.dialogue.impl.CombatMaster;
import org.mystic.game.model.content.dialogue.impl.DonationClaimer;
import org.mystic.game.model.content.dialogue.impl.Donator;
import org.mystic.game.model.content.dialogue.impl.DonatorShopSelect;
import org.mystic.game.model.content.dialogue.impl.Lanthus;
import org.mystic.game.model.content.dialogue.impl.Nastroth;
import org.mystic.game.model.content.dialogue.impl.Scavvo;
import org.mystic.game.model.content.dialogue.impl.Shantay;
import org.mystic.game.model.content.dialogue.impl.ShopExchangeDialogue;
import org.mystic.game.model.content.dialogue.impl.SkillcapeShop;
import org.mystic.game.model.content.dialogue.impl.StarterShop;
import org.mystic.game.model.content.dialogue.impl.StudyLectern;
import org.mystic.game.model.content.dialogue.impl.TradingPost;
import org.mystic.game.model.content.dialogue.impl.VoteManager;
import org.mystic.game.model.content.dialogue.impl.WildernessExpert;
import org.mystic.game.model.content.dialogue.impl.ZamorakMage;
import org.mystic.game.model.content.dialogue.impl.teleport.SpiritTree;
import org.mystic.game.model.content.dialogue.impl.teleport.WildernessLever;
import org.mystic.game.model.content.minigames.barrows.Barrows;
import org.mystic.game.model.content.minigames.clanwars.SafeWars;
import org.mystic.game.model.content.minigames.fightcave.FightCave;
import org.mystic.game.model.content.minigames.fightpits.FightPits;
import org.mystic.game.model.content.minigames.godwars.GodWars;
import org.mystic.game.model.content.minigames.pestcontrol.PestControl;
import org.mystic.game.model.content.minigames.warriorsguild.ArmorAnimator;
import org.mystic.game.model.content.minigames.warriorsguild.CyclopsRoom;
import org.mystic.game.model.content.pets.PetDialogue;
import org.mystic.game.model.content.pets.Pets;
import org.mystic.game.model.content.quest.QuestConstants;
import org.mystic.game.model.content.quest.impl.disater.GypsyDialogue;
import org.mystic.game.model.content.randomevent.impl.Dwarf;
import org.mystic.game.model.content.shopping.ShopConstants;
import org.mystic.game.model.content.shopping.impl.PestShop;
import org.mystic.game.model.content.shopping.impl.RFDChestShop;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.agility.Agility;
import org.mystic.game.model.content.skill.cooking.CookingTask;
import org.mystic.game.model.content.skill.crafting.CraftingType;
import org.mystic.game.model.content.skill.crafting.Flax;
import org.mystic.game.model.content.skill.crafting.HideTanning;
import org.mystic.game.model.content.skill.crafting.JewelryCreationTask;
import org.mystic.game.model.content.skill.crafting.Spinnable;
import org.mystic.game.model.content.skill.herblore.PotionDecanting;
import org.mystic.game.model.content.skill.magic.MagicSkill;
import org.mystic.game.model.content.skill.magic.MagicSkill.SpellBookTypes;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.content.skill.mining.Mining;
import org.mystic.game.model.content.skill.mining.Mining.Ore;
import org.mystic.game.model.content.skill.mining.ProspectingTask;
import org.mystic.game.model.content.skill.prayer.BoneBurying;
import org.mystic.game.model.content.skill.runecrafting.AbyssObjects;
import org.mystic.game.model.content.skill.runecrafting.RunecraftingData;
import org.mystic.game.model.content.skill.runecrafting.RunecraftingTask;
import org.mystic.game.model.content.skill.slayer.SlayerMasterDialogue;
import org.mystic.game.model.content.skill.smithing.SmithingConstants;
import org.mystic.game.model.content.skill.summoning.SummoningXP;
import org.mystic.game.model.content.skill.thieving.StallTask;
import org.mystic.game.model.content.skill.thieving.TheivingTask;
import org.mystic.game.model.content.skill.woodcutting.WoodcuttingTask;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.content.wilderness.LockpickTask;
import org.mystic.game.model.content.wilderness.WildernessTargets;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectConstants;
import org.mystic.game.model.entity.pathfinding.StraightPathFinder;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendChatInterface;
import org.mystic.game.model.networking.outgoing.SendEnterString;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendItemOnInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendMoveComponent;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.FollowToEntityTask;
import org.mystic.game.task.impl.ForceMoveTask;
import org.mystic.game.task.impl.ObeliskTeleportation;
import org.mystic.game.task.impl.PullLeverTask;
import org.mystic.game.task.impl.WalkToTask;
import org.mystic.utility.GameDefinitionLoader;

public class WalkToActions {

	public static void interactNPC(Player player, int option, int slot) {
		if (player.getDueling().isDueling()) {
			return;
		}
		if (player.getMagic().isTeleporting()) {
			return;
		}
		if (player.getTrade().trading()) {
			player.getTrade().end(false);
		}
		if ((slot > World.getNpcs().length) || (slot < 0)) {
			return;
		}
		player.getMovementHandler().reset();
		Npc mob = World.getNpcs()[slot];
		if (mob == null) {
			return;
		}
		if (GameConstants.DEV_MODE) {
			player.getClient().queueOutgoingPacket(new SendMessage("NPC: " + mob.getId() + " - Option: " + option));
		}
		TaskQueue.queue(new FollowToEntityTask(player, mob) {

			@Override
			public void onDestination() {
				player.face(mob);
				if (mob.getSize() > 1) {
					player.getMovementHandler().reset();
				}
				if (player.getLocation().equals(mob.getLocation().copy())) {
					if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedSouth(
							player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
						player.getMovementHandler().walkTo(0, -1);
					} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedWest(
							player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
						player.getMovementHandler().walkTo(-1, 0);
					} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedEast(
							player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
						player.getMovementHandler().walkTo(1, 0);
					} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedNorth(
							player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
						player.getMovementHandler().walkTo(0, 1);
					}
				}
				if (!StraightPathFinder.isProjectilePathClear(player.getLocation(), mob.getLocation())) {
					player.send(new SendMessage("You can't reach that."));
					return;

				} else {
					WalkToActions.finishClickNpc(player, option, mob);
				}
				if (mob.face()) {
					mob.face(player);
				}
			}
		});

	}

	public static void clickObject(Player player, int option, int id, int x, int y) {
		if (player.getMagic().isTeleporting()) {
			return;
		}
		if (player.getTrade().trading()) {
			player.getTrade().end(false);
		}
		int z = player.getLocation().getZ();
		RSObject object = Region.getObject(x, y, z);
		if ((object == null) && (!PlayerConstants.isOverrideObjectExistance(player, id, x, y, z))) {
			return;
		}
		int[] length = ObjectConstants.getObjectLength(id, object == null ? 0 : object.getFace());
		TaskQueue.queue(new WalkToTask(player, x, y, length[0], length[1]) {
			@Override
			public void onDestination() {
				player.getUpdateFlags().sendFaceToDirection(length[0] >= 2 ? x + length[0] / 2 : x,
						length[1] >= 2 ? y + length[1] / 2 : y);
				WalkToActions.finishObjectClick(player, id, option, x, y);
			}
		});
	}

	public static void finishClickNpc(Player player, int option, Npc mob) {
		int id = mob.getId();
		if (player.getSummoning().interact(mob, option)) {
			return;
		}
		if (id == 836) {
			player.start(new Shantay(player));
			return;
		}
		switch (option) {
		case 1:
			if (player.getFishing().click(mob, id, 1)) {
				return;
			}
			if (GameDefinitionLoader.getNpcDefinition(mob.getId()).getName().toLowerCase().contains("banker")) {
				player.start(new Banker(player));
			}
			if (Pets.isMobPet(mob.getId())) {
				if (player.getPets().hasPet() && mob.getOwner().getUsername() == player.getUsername()) {
					player.getPets().remove();
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("This is not your pet."));
				}
				return;
			}
			switch (id) {
			case 956:
				if ((Boolean) player.getAttributes().get("randomEvent") == Boolean.valueOf(true)) {
					if (player.getAttributes().get("randomEventMob") != null) {
						if (player.getAttributes().get("randomEventMob") == mob) {
							Dwarf dwarf = new Dwarf();
							dwarf.end(player, mob);
						} else {
							player.send(new SendMessage("The dwarf doesn't want to talk to you."));
						}
					}
				} else {
					player.send(new SendMessage("The dwarf doesn't want to talk to you."));
				}
				break;
			case 3385:
				player.start(new GypsyDialogue(player));
				break;
			case 437:
				player.getShopping().open(82);
				break;
			case 594:
				player.getShopping().open(68);
				break;
			case 608: // Iron man shop
				player.getShopping().open(67);
				break;
			case 6531:// Exchange agent
				player.start(new ShopExchangeDialogue(player));
				break;
			case 4707: // Magic Shop
				player.getShopping().open(9);
				break;
			case 6540:
				player.start(new Nastroth(player));
				break;
			case 455:
				player.getShopping().open(13);
				break;
			case 1699:
				player.start(new Donator(player));
				break;
			case 949:
				player.start(new StarterShop(player));
				break;
			case 7959:
				player.start(new Scavvo(player));
				break;
			case 2259:
				player.start(new ZamorakMage(player, mob));
				break;
			case 43:
				SheepShearing.start(player, mob);
				break;
			case 590:
				player.start(new DonationClaimer(player));
				break;
			case 659:
				player.start(new VoteManager(player));
				break;
			case 1263:
			case 1304:
				TeleportationInterface.open(player, TeleportType.MONSTERS);
				break;
			case 1526:
				player.start(new Lanthus(player, mob));
				break;
			case 933:
				player.getShopping().open(28);
				break;
			case 1040:
				player.getShopping().open(29);
				break;
			case 219:
				player.getShopping().open(17);
				break;
			case 4906:
				player.start(new SkillcapeShop(player));
				break;
			case 3789:
			case 3788:
				player.getShopping().open(PestShop.SHOP_ID);
				break;
			case 519:
				player.start(new Scavvo(player));
				break;
			case 216:
				player.start(new AltarSwap(player));
				break;
			case 1273:
				player.start(new WildernessExpert(player));
				break;
			case 528:
				player.setEnterXInterfaceId(55777);
				player.getClient().queueOutgoingPacket(new SendEnterString());
				break;
			case 545: // Dommik
				player.getShopping().open(22);
				break;
			case 2536: // Niles
				player.getShopping().open(27);
				break;
			case 6538:
				player.start(new ShopExchangeDialogue(player));
				break;
			case 1597:
				if (player.getSkill().getCombatLevel() >= 40) {
					player.start(new SlayerMasterDialogue(player, mob.getId()));
				} else {
					DialogueManager.sendStatement(player, "You need a combat level of atleast 40 to get a task",
							"from this master.");
				}
				break;
			case 8275:
				if (player.getSkill().getCombatLevel() >= 80) {
					player.start(new SlayerMasterDialogue(player, mob.getId()));
				} else {
					DialogueManager.sendStatement(player, "You need a combat level of atleast 80 to get a task",
							"from this master.");
				}
				break;
			case 8274:
				if (player.getSkill().getLevels()[Skills.SLAYER] >= 85) {
					player.start(new SlayerMasterDialogue(player, mob.getId()));
				} else {
					DialogueManager.sendStatement(player, "You need a slayer level of 85 to request a Boss Task.");
				}
				break;

			case 8273:
				player.start(new SlayerMasterDialogue(player, mob.getId()));
				break;
			case 705:
				if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
					BankSecurity.init(player, false);
					player.send(new SendMessage("You must enter your bank pin before talking to this NPC."));
					return;
				}
				player.start(new CombatMaster(player));
				break;
			case 373:
				if (player.isGoldMember()) {
					player.resetCombatStats();
					if (player.isPoisoned()) {
						player.curePoison(0);
					}
					DialogueManager.sendNpcChat(player, 373, Emotion.HAPPY_TALK, new String[] {
							"Your skills have been replenished", "and you've been cured of any posions." });
				} else {
					DialogueManager.sendNpcChat(player, 373, Emotion.HAPPY_TALK,
							new String[] { "I only offer my skills in first aid to those worthy.",
									"@dre@You need to be atleast Gold member" });
				}
				break;
			case 598:
				PlayerConstants.setAppearance(player);
				break;
			default:
				if (OneLineDialogue.doOneLineChat(player, id)) {
					return;
				}
				break;
			}
			break;
		case 2:
			for (int i = 0; i < ShopConstants.SHOP_NPC_DATA.length; i++) {
				if (ShopConstants.SHOP_NPC_DATA[i][0] == id) {
					player.getShopping().open(ShopConstants.SHOP_NPC_DATA[i][1]);
					return;
				}
			}
			if (TheivingTask.attemptThieving(player, mob)) {
				return;
			}
			if (player.getFishing().click(mob, id, 2)) {
				return;
			}
			if (GameDefinitionLoader.getNpcDefinition(mob.getId()).getName().toLowerCase().contains("banker")) {
				player.getBank().openBank();
			}
			switch (id) {
			case 437:
				player.getShopping().open(82);
				break;
			case 594:
				player.getShopping().open(68);
				break;
			case 608: // Iron man shop
				player.getShopping().open(67);
				break;
			case 545: // Dommik
				player.getShopping().open(22);
				break;
			case 8273:// slayer shop
			case 8274:
			case 1597:// slayer shop
			case 8275:// slayer shop
				player.getShopping().open(53);
				break;
			case 2536: // Niles
				player.getShopping().open(27);
				break;
			case 4707: // Magic Shop
				player.getShopping().open(9);
				break;
			case 902:
				player.getBank().openBank();
				break;
			case 1861: // Melee Shop
				player.getShopping().open(8);
				break;
			case 7950: // Ranged Shop
				player.getShopping().open(7);
				break;
			case 590:
				player.start(new DonatorShopSelect(player));
				break;
			case 1699:
				player.start(new DonationClaimer(player));
				break;
			case 1263:
			case 1304:
				TeleportationInterface.open(player, TeleportType.MONSTERS);
				break;
			case 598:
				PlayerConstants.setAppearance(player);
				break;
			case 3789:
			case 3788:
				player.getShopping().open(PestShop.SHOP_ID);
				break;
			case 6524:
				DialogueManager.sendNpcChat(player, 6524, Emotion.CALM, "I can decant your potions for 300 gold each.");
				break;
			case 6970:
				player.getShopping().open(50);
				break;
			case 528:
				player.getShopping().open(player);
				break;
			case 4906:
				player.start(new SkillcapeShop(player));
				break;
			case 804:
				HideTanning.sendTanningInterface(player);
				break;
			default:
				if (Pets.isMobPet(mob.getId())) {
					player.start(new PetDialogue(id));
					return;
				}
				break;
			}
			break;
		case 3:
			switch (id) {
			case 1263:
				if (player.getPrevious() == null) {
					DialogueManager.sendStatement(player, "You do not currently have a previous teleport",
							"option set. Teleport anywhere to set one.");
				} else {
					player.getMagic().teleport(player.getPrevious().getX(), player.getPrevious().getY(),
							player.getPrevious().getZ(), TeleportTypes.WIZARD);
				}
				break;
			case 1526:
				player.getShopping().open(52);
				break;
			case 2259:
				MobTeleportation.teleportZamorak(player, mob);
				break;
			case 6524:// potion decanting
				PotionDecanting.decantAll(player);
				break;
			case 8273:// slayer shop
			case 1597:// slayer shop
			case 8274:
			case 8275:// slayer shop
				player.getShopping().open(93);
				break;
			case 6970:
				player.getShopping().open(51);
				break;
			case 6531:// Exchange agent
				if (IronMan.isIronMan(player)) {
					player.send(new SendMessage("You are an Iron Man. You stand alone."));
					return;
				}
				player.getClient().queueOutgoingPacket(new SendInterface(53500));
				player.getClient()
						.queueOutgoingPacket(new SendString("Mystic Shop Exchange | @red@0</col> Active Shops", 53505));
				List<Player> available = Arrays.stream(World.getPlayers())
						.filter(p -> p != null && p.isActive() && p.getPlayerShop().hasAnyItems())
						.collect(Collectors.toList());
				for (int i = 53516; i < 53716; i++) {
					Player p = null;
					if (i - 53516 < available.size()) {
						p = available.get(i - 53516);
						player.getClient()
								.queueOutgoingPacket(new SendString("Shops Exchange | @gre@" + available.size()
										+ "</col> Active " + (available.size() == 1 ? "Shop" : "Shops"), 53505));
						player.getClient().queueOutgoingPacket(new SendString(p.getUsername(), i));
						if (player.getShopMotto() != null) {
							player.getClient().queueOutgoingPacket(new SendString(p.getShopMotto(), i + 200));
						} else {
							player.getClient().queueOutgoingPacket(new SendString("No shop description set.", i + 200));
						}
					} else {
						player.getClient().queueOutgoingPacket(new SendString("", i));
						player.getClient().queueOutgoingPacket(new SendString("", i + 200));
					}
				}
				break;
			}
			break;
		}
	}

	public static void finishItemOnNpc(Player player, int item, Npc mob) {
		switch (mob.getId()) {
		case 519:
			player.start(new Scavvo(player, item));
			break;
		case 7959:
			player.start(new Scavvo(player, item));
			break;
		}
	}

	public static void finishObjectClick(Player player, int id, int option, int x, int y) {
		int z = player.getLocation().getZ();
		GameObject object = new GameObject(id, x, y, player.getLocation().getZ(), 10, 0);
		int[] length = ObjectConstants.getObjectLength(id, object == null ? 0 : object.getFace());
		if (GameConstants.DEV_MODE && (PlayerConstants.isOwner(player))) {
			RSObject o = Region.getObject(x, y, player.getLocation().getZ());
			if (o != null) {
				player.getClient().queueOutgoingPacket(new SendMessage("Object option: " + option + " ID: " + id
						+ " X: " + x + " Y: " + y + " DIR: " + o.getFace() + " TYPE: " + o.getType() + "."));
			} else {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("Clicking object with no reference: " + id + ""));
			}
		}
		if (player.getQuesting().isClickQuestObject(option, id)) {
			return;
		}
		if (QuestConstants.clickObject(player, id)) {
			return;
		}
		if ((id == 1815) && x == 3097 && y == 3499) {
			player.start(new WildernessLever(player));
			return;
		} else if ((id == 1815) && x == 3090 && y == 3474) {
			player.start(new WildernessLever(player));
			return;
		}
		if (id == 1816 && x == 3067 && y == 10252) {
			player.getMagic().teleportNoWildernessRequirement(2273, 4680, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			return;
		}
		if (id == 1817 && x == 2273 && y == 4680) {
			TaskQueue.queue(new PullLeverTask(player, x, y, length[0], length[1]) {
				@Override
				public void onDestination() {
					player.getMagic().teleportNoWildernessRequirement(PlayerConstants.HOME.getX(),
							PlayerConstants.HOME.getY(), PlayerConstants.HOME.getZ(),
							MagicSkill.TeleportTypes.SPELL_BOOK);
				}
			});
			return;
		}
		if (id == 1815) {
			TaskQueue.queue(new PullLeverTask(player, x, y, length[0], length[1]) {

				@Override
				public void onDestination() {
					player.getMagic().teleportNoWildernessRequirement(2561, 3311, 0,
							MagicSkill.TeleportTypes.SPELL_BOOK);
				}
			});
			return;
		} else if (id == 1814) {
			TaskQueue.queue(new PullLeverTask(player, x, y, length[0], length[1]) {
				@Override
				public void onDestination() {
					player.getMagic().teleportNoWildernessRequirement(3153, 3923, 0,
							MagicSkill.TeleportTypes.SPELL_BOOK);
				}
			});
			return;
		} else if (id == 5960 || id == 5959) {
			TaskQueue.queue(new PullLeverTask(player, x, y, length[0], length[1]) {
				@Override
				public void onDestination() {
					player.getMagic().teleportNoWildernessRequirement(id == 5960 ? 3090 : 2539,
							id == 5960 ? 3956 : 4712, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}
			});
			return;
		}
		if (SmithingConstants.clickAnvil(player, id)) {
			return;
		}
		if (player.getFarming().click(player, x, y, option)) {
			return;
		}
		if ((id == 9706) && (x == 3104) && (y == 3956)) {
			player.getMagic().teleportNoWildernessRequirement(3105, 3951, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			return;
		}
		if ((id == 9707) && (x == 3105) && (y == 3952)) {
			player.getMagic().teleportNoWildernessRequirement(3105, 3956, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			return;
		}
		if ((id == 1738) && (x == 2839) && (y == 3537)) {
			player.teleport(new Location(2840, 3539, 2));
			return;
		}
		if ((id == 1742) && (x == 2445) && (y == 3434)) {
			player.teleport(new Location(2445, 3433, 1));
			return;
		}
		if ((id == 1742) && (x == 2444) && (y == 3414)) {
			player.teleport(new Location(2445, 3416, 1));
			return;
		}
		if ((id == 1744) && (x == 2445) && (y == 3434)) {
			player.teleport(new Location(2446, 3436));
			return;
		}

		if ((id == 1744) && (x == 2445) && (y == 3415)) {
			player.teleport(new Location(2444, 3413));
			return;
		}
		if ((id == 2878) && (x == 2541) && (y == 4719)) {
			player.teleport(new Location(2509, 4689));
			return;
		}
		if ((id == 2879) && (x == 2508) && (y == 4686)) {
			player.teleport(new Location(2541, 4718));
			return;
		}

		if ((id == 15638) && (x == 2840) && (y == 3538)) {
			player.teleport(new Location(2841, 3538));
			return;
		}
		if (((id == 15644) || (id == 15641)) && (x == 2847) && (z == 2) && ((y == 3541) || (y == 3540))) {
			CyclopsRoom.handleDoor(player);
			return;
		}
		if (id == 9357) {
			FightCave.finish(player, false);
			return;
		}
		if ((id == 2873) && (x == 2500) && (y == 4721)) {
			DialogueManager.sendStatement(player, "You pray to the gods..");
			player.getUpdateFlags().sendAnimation(645, 0);
			GroundItemHandler.add(new Item(2412, 1), player.getLocation(), player);
			return;
		}
		if ((id == 2875) && (x == 2507) && (y == 4724)) {
			DialogueManager.sendStatement(player, "You pray to the gods..");
			player.getUpdateFlags().sendAnimation(645, 0);
			GroundItemHandler.add(new Item(2413, 1), player.getLocation(), player);
			return;
		}
		if ((id == 2874) && (x == 2516) && (y == 4721)) {
			DialogueManager.sendStatement(player, "You pray to the gods..");
			player.getUpdateFlags().sendAnimation(645, 0);
			GroundItemHandler.add(new Item(2414, 1), player.getLocation(), player);
			return;
		}
		if (PestControl.clickObject(player, id)) {
			return;
		}
		if (Agility.execute(player, object)) {
			return;
		}
		switch (option) {
		case 1:
			if (player.getDueling().clickForfeitTrapDoor(id)) {
				return;
			}
			if (GodWars.clickObject(player, id, x, y, z)) {
				return;
			}
			if (FightPits.clickObject(player, id)) {
				return;
			}
			if (Barrows.clickObject(player, id, x, y, z)) {
				return;
			}
			if (PestControl.clickObject(player, id)) {
				return;
			}
			if (id == 882) {
				if (x == 3089 && y == 3489) {// to essence mine
					player.teleport(new Location(2899, 4449));
					return;
				}
				if (x == 3331 && y == 3653) {// to strykeworms in wilderness
					if (player.getMaxLevels()[Skills.SLAYER] < 80) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("You need a Slayer level of 80 to climb down this manhole!"));
						return;
					}
					player.teleport(new Location(1990, 5189));
					return;
				}
				if (x == 3404 && y == 3089) {// to corp
					if (player.getSkill().getTotalLevel() < 800) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("You need a total level of 800 to climb down this manhole!"));
						return;
					}
					player.teleport(new Location(2885, 4372));
					return;
				}
				if (x == 2600 && y == 3096) {
					player.teleport(new Location(3161, 4237));
					return;
				}
			}
			if ((id == 5264) && (x == 3161) && (y == 4236)) {
				player.teleport(new Location(3085, 3493));
				return;
			}
			if (ObeliskTeleportation.clickObelisk(id)) {
				player.send(new SendMessage("You activate the obelisk."));
				return;
			}
			if (AbyssObjects.clickObject(player, id)) {
				return;
			}
			if (ObjectConstants.isSummoningObelisk(id)) {
				if (player.getLevels()[23] == player.getMaxLevels()[23]) {
					player.getClient().queueOutgoingPacket(new SendMessage("You already have full Summoning points."));
				} else {
					SoundPlayer.play(player, Sounds.RESTORE_PRAYER);
					player.getClient()
							.queueOutgoingPacket(new SendMessage("You recharge your Summoning points at the obelisk."));
					player.getUpdateFlags().sendAnimation(8502, 0);
					player.getUpdateFlags().sendGraphic(new Graphic(1308, 0, false));
					player.getLevels()[23] = player.getMaxLevels()[23];
					player.getSkill().update(23);
				}
				return;
			}
			StallTask.attemptStealFromStall(player, id, new Location(x, y, z));
			if (!player.getSkill().locked()) {
				if (RunecraftingData.forId(id) != null) {
					if (player.getInventory().playerHasItem(new Item(7936))) {
						player.getSkill().lock(5);
						TaskQueue.queue(new RunecraftingTask(player, RunecraftingData.forId(id)));
					} else {
						player.send(new SendMessage("You need rune essence to do this."));
					}
				}
				if (Mining.clickRock(player, object)) {
					player.getSkill().lock(1);
					return;
				}
			}
			WoodcuttingTask.attemptWoodcutting(player, object);
			switch (id) {
			case 10230:// Dagganoth Kings
				player.teleport(new Location(2900, 4449, 0));
				break;

			case 1765:
				player.getUpdateFlags().sendAnimation(new Animation(828));
				player.teleport(new Location(3069, 10257, 0));
				break;

			case 32015:
				player.getUpdateFlags().sendAnimation(new Animation(828));
				player.teleport(new Location(3017, 3850, 0));
				break;

			case 10091:
				player.send(new SendMessage("I'm sure Harry wouldn't want me messing around in his shop."));
				break;

			case 11557:
				DialogueManager.sendStatement(player, "There is no ore left in this rock.");
				break;

			case 1317:
				player.start(new SpiritTree(player));
				break;

			case 14964:
				player.send(new SendMessage("These aren't yours to take!"));
				break;

			case 28089:
				player.start(new TradingPost(player));
				break;

			case 4483:
				player.getBank().openBank();
				break;

			case 9312:
				if (object.getLocation().getX() == 3143 && object.getLocation().getY() == 3514) {
					player.teleport(new Location(3138, 3516));
				}
				break;
			case 9311:
				if (object.getLocation().getX() == 3139 && object.getLocation().getY() == 3516) {
					player.teleport(new Location(3144, 3514));
				}
				break;
			case 15653:
				if (player.getX() >= 2877) {
					if (player.getSkill().getLevels()[Skills.STRENGTH]
							+ player.getSkill().getLevels()[Skills.ATTACK] >= 130) {
						player.teleport(new Location(2876, 3546));
					} else {
						DialogueManager.sendStatement(player, "You are not a strong enough warrior to enter the guild.",
								"Your attack and strength levels must add up to ", "be equal or greater than 130.");
					}
				} else {
					player.teleport(new Location(2877, 3546));
				}
				break;

			case 28213:
				SafeWars.interactPortal(player, false);
				break;

			case 38700:
				SafeWars.interactPortal(player, true);
				break;

			case 5932:
				SafeWars.interactPortal(player, true);
				break;

			case 1747:
				if (player.getController().equals(ControllerManager.BARROWS_CONTROLLER)) {
					player.teleport(new Location(3565, 3308, 0));
				}
				break;

			case 6775:
				Barrows.search_chest(player, player.getMinigames().getTunnel());
				break;

			case 170:
				WildernessChest.displayReward(player);
				return;

			case 2073:
				if (player.getInventory().hasSpaceFor(new Item(1963))) {
					player.getInventory().add(new Item(1963));
					player.getClient().queueOutgoingPacket(new SendMessage("You pick the banana from the tree."));
				}
				break;

			case 2557:
			case 2558:
				if (player.getSkill().getLevels()[17] < 52) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You need a thieving level of 52 to pick the lock on this door."));
					return;
				}
				if (!player.getInventory().hasItemId(1523)) {
					DialogueManager.sendStatement(player, "You need a lockpick to be able to open this door.");
					return;
				}
				player.getUpdateFlags().sendAnimation(new Animation(2246));
				player.getClient().queueOutgoingPacket(new SendMessage("You attempt to pick the lock..."));
				Task task = new LockpickTask(player, (byte) 2, x, y, z);
				player.getAttributes().set("lockPick", task);
				TaskQueue.queue(task);

				break;
			case 1733: // To magebank dungeon
				if (x == 3044 && y == 3924) {
					player.teleport(new Location(3044, 10323));
				}
				break;

			case 32048: // From magebank dungeon
				if (x == 3043 && y == 10328) {
					player.teleport(new Location(3044, 3927));
				}
				break;

			case 10229: // From Dag Kings
				if (x == 2899 && y == 4449) {
					player.teleport(new Location(1912, 4367));
				}
				break;

			case 1568:
				if (x == 3097 && y == 3468) { // To Edgeville Dungeon
					player.teleport(new Location(3096, 9867));
				}
				break;

			case 9295:
				if (x == 3150 && y == 9906) { // To Moss Giants in Edgeville
												// Dungeon
					player.teleport(new Location(3155, 9906));
				}
				if (x == 3153 && y == 9906) { // From Moss Giants in Edgeville
												// Dungeon
					player.teleport(new Location(3149, 9906));
				}
				break;

			case 881:
				if (x == 3237 && y == 3458) { // To Varrock Sewers
					player.teleport(new Location(3237, 9859));
				}
				break;

			case 1764:
				if (x == 2856 && y == 9569) {
					player.teleport(new Location(2856, 3170));
				}
				break;

			case 31284:
				if (x == 2867 && y == 9570) {
					player.teleport(new Location(2480, 5175));
				}
				break;

			// tzharr exit
			case 9359:
				if (x == 2479 && y == 5176) {
					player.teleport(new Location(2866, 9571));
				}
				break;

			case 31015:
				if (x == 2884 && y == 9797) { // From Taverly Dungeon
					player.teleport(new Location(2318, 3807));
				}
				break;

			case 1755:
				if (x == 3237 && y == 9858) { // From Varrock Sewers
					player.teleport(new Location(3237, 3459));
				}
				if (x == 3008 && y == 9550) { // From Asgarnian Dungeon
					player.teleport(new Location(3087, 3490));
				}
				if (x == 3097 && y == 9867) { // From Edgeville Dungeon
					player.teleport(new Location(3096, 3468));
				}
				break;

			case 29728:
				if (x == 3076 && y == 3463) { // To Slayer Cave
					player.teleport(new Location(3168, 9572));
				}
				break;

			case 5946:
				if (x == 3169 && y == 9572) { // From Slayer Cave
					player.teleport(new Location(3078, 3463));
				}
				break;

			case 26933: // To Edgeville Dungeon
				player.teleport(new Location(3096, 9867));
				break;
			case 5084: // To Brimhaven Dungeon
				player.teleport(new Location(2401, 3888));
				break;
			case 25338:// Ancient Cavern (Down)
				player.teleport(new Location(1772, 5366, 0));
				break;
			case 25336:// Ancient Cavern (Up)
				player.teleport(new Location(1768, 5366, 1));
				break;
			case 10596: // To Skeletal Wyvrns
				player.teleport(new Location(3056, 9555));
				break;
			case 10595: // From Skeletal Wyverns
				player.teleport(new Location(3056, 9562));
				break;
			case 13643:
				player.start(new StudyLectern(player));
				break;
			case 21311:
				player.teleport(new Location(2314, 3839));
				break;
			case 21310:
				player.teleport(new Location(2314, 3848));
				break;
			case 21585:
				player.setController(ControllerManager.GOD_WARS_CONTROLLER);
				player.teleport(new Location(2882, 5310, 2));
				break;
			case 21586:
				player.teleport(new Location(3007, 9550));
				break;
			case 5083:
				player.teleport(new Location(2712, 9564));
				break;
			case 10041:
				DialogueManager.sendNpcChat(player, 496, Emotion.ANGRY_1, "Woah Woah! Watch out!");
				break;
			case 21309:
				player.getMovementHandler().addToPath(new Location(2343, 3820));
				player.getClient().queueOutgoingPacket(new SendMessage("You run across the bridge."));
				break;
			case 21308:
				player.getMovementHandler().addToPath(new Location(2343, 3829));
				player.getClient().queueOutgoingPacket(new SendMessage("You run across the bridge."));
				break;
			case 21306:
				player.getMovementHandler().addToPath(new Location(2317, 3832));
				player.getClient().queueOutgoingPacket(new SendMessage("You run across the bridge."));
				break;
			case 21307:
				player.getMovementHandler().addToPath(new Location(2317, 3823));
				player.getClient().queueOutgoingPacket(new SendMessage("You run across the bridge."));
				break;
			case 272:
				player.getUpdateFlags().sendAnimation(new Animation(828));
				player.teleport(new Location(3018, 3956, 1));
				break;
			case 26293:
				player.getUpdateFlags().sendAnimation(new Animation(828));
				player.teleport(new Location(3087, 3489, 0));
				break;
			case 273:
				player.getUpdateFlags().sendAnimation(new Animation(828));
				player.teleport(new Location(3018, 3958, 0));
				break;

			case 245:
				if (x == 3017 && y == 3959) {
					player.teleport(new Location(3017, 3960, 2));
				} else if (x == 3019 && y == 3959) {
					player.teleport(new Location(3019, 3960, 2));
				}
				break;

			case 246:
				player.teleport(new Location(player.getX(), player.getY() - 2, 1));
				break;
			case 9358:
				player.teleport(new Location(2480, 5175));
				break;
			case 492:
				player.teleport(new Location(2856, 9570));
				break;

			case 25339: // To Mith Drags
				player.teleport(new Location(1778, 5343, 1));
				break;

			case 25340:// From Mith Drags
				player.teleport(new Location(1778, 5346, 0));
				break;

			case 8929:// To Dagannoths
				player.teleport(new Location(2442, 10146));
				break;

			case 8966:// From Dagannoths
				player.teleport(new Location(2523, 3739));
				break;

			case 3832:// To Kalphite Queen
				player.teleport(new Location(3509, 9499, 2));
				break;

			case 3829:// From Kalphites
				player.teleport(new Location(3228, 3110));
				break;

			case 6552:// ancient altar
				player.getMagic().setSpellBookType(
						player.getMagic().getSpellBookType() == SpellBookTypes.MODERN ? SpellBookTypes.ANCIENT
								: SpellBookTypes.MODERN);
				player.getMagic()
						.setMagicBook(player.getMagic().getSpellBookType() == SpellBookTypes.MODERN ? 1151 : 12855);
				player.getUpdateFlags().sendAnimation(new Animation(645));
				break;
			case 410:// lunar altar
				player.getMagic().setSpellBookType(
						player.getMagic().getSpellBookType() == SpellBookTypes.MODERN ? SpellBookTypes.LUNAR
								: SpellBookTypes.MODERN);
				player.getMagic()
						.setMagicBook(player.getMagic().getSpellBookType() == SpellBookTypes.MODERN ? 1151 : 29999);
				player.getUpdateFlags().sendAnimation(new Animation(645));
				break;
			case 409:
				if (player.getSkill().locked()) {
					return;
				}
				player.getSkill().lock(4);
				player.getSkill().setLevel(Skills.PRAYER, player.getMaxLevels()[Skills.PRAYER]);
				SoundPlayer.play(player, Sounds.RESTORE_PRAYER);
				player.getClient().queueOutgoingPacket(new SendMessage("You have recharged your prayer points."));
				player.getUpdateFlags().sendAnimation(new Animation(645));
				break;

			case 2113:// to fally mine
				player.teleport(new Location(3021, 9739));
				break;
			case 30941:// from fally mine
				player.teleport(new Location(3019, 3337));
				break;

			case 7257:// to rogue den
				player.teleport(new Location(3061, 4985, 1));
				break;
			case 7258:// from rogue den
				player.teleport(new Location(2906, 3537));
				break;

			case 5008:// to rellekka slayer dungeon
				player.teleport(new Location(3206, 9379, 0));
				break;
			case 4499:// to rellekka slayer dungeon alt
				player.teleport(new Location(3206, 9379, 0));
				player.getClient().queueOutgoingPacket(new SendMessage(
						"This is an alternative entrance, use the rope to find the shorter cave entrance."));
				break;
			case 6439:
				player.teleport(new Location(2730, 3713, 0));
				break;

			case 36687:
				player.teleport(new Location(3210, 9616, 0));
				break;
			case 29355:
				player.teleport(new Location(3210, 3216, 0));
				break;
			case 2492:
				player.getMagic().teleport(3087, 3490, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 882:
				if (x == 3090 && y == 3489) {
					// player.teleport(new Location(2891, 4813));
					player.teleport(new Location(3114, 5528));
				}
				break;
			case 2807:
				if (x == 3187 && y == 3683) {
					player.teleport(new Location(2885, 4373));
				}
				break;
			case 37928:
				if (x == 2883 && y == 4370) {
					player.teleport(new Location(3188, 3682));
				}
				break;
			case 2191:
				CrystalChest.click(player);
				break;
			case 1767:// ladder down lava maze
				if (x == 3069 && y == 3856) {
					player.teleport(new Location(3017, 10248));
				}
				break;
			case 1768:// ladder up lava maze dungeon
				if (x == 3017 && y == 10249) {
					player.teleport(new Location(3069, 3857));
				}
				break;
			case 9319:
				player.changeZ(1);
				break;
			case 9320:
				player.changeZ(0);
				break;
			case 11402:
			case 21301:
			case 2213:
			case 6084:
			case 11758:
			case 12309:
			case 25808:
			case 27663:
			case 49018:
			case 14369:
			case 24914:
			case 2693:
			case 35647:
			case 14367:
			case 3193:
				player.getBank().openBank();
				break;
			case 26972:
				player.getBank().openBank();
				break;
			case 11601:
			case 2643:
				JewelryCreationTask.sendInterface(player);
				break;
			case 9390:
			case 3044:
			case 11666:
				SmithingConstants.sendSmeltSelectionInterface(player);
				break;
			case 2806:
				player.teleport(new Location(2885, 4372));
				break;
			case 37929:
				if (player.getLocation().getX() < 2926) {
					if (player.getLocation().getX() <= 2918)
						player.teleport(new Location(2921, 4384));
					else {
						player.teleport(new Location(2917, 4384));
					}
				} else if (player.getLocation().getX() <= 2971)
					player.teleport(new Location(2974, 3484));
				else {
					player.teleport(new Location(2970, 4384));
				}
				break;
			case 1293:
				player.start(new SpiritTree(player));
				break;
			case 2640:
			case 4859:
			case 27661:
				if (player.getLevels()[5] == player.getMaxLevels()[5]) {
					player.getClient().queueOutgoingPacket(new SendMessage("You already have full Prayer."));
				} else {
					SoundPlayer.play(player, Sounds.RESTORE_PRAYER);
					player.getClient()
							.queueOutgoingPacket(new SendMessage("You recharge your Prayer points at the altar."));
					player.getUpdateFlags().sendAnimation(645, 5);
					player.getLevels()[5] = player.getMaxLevels()[5];
					player.getSkill().update(5);
				}
				break;
			case 23271:
				Location location = new Location(0, player.getLocation().getY() < 3522 ? 3 : -3);
				TaskQueue.queue(new ForceMoveTask(player, 3, player.getLocation(), location, 6132, 33, 66,
						location.getY() == 3 ? 0 : 2));
				if (player.isTeleblocked()) {
					player.setTeleblockTime(0);
				}
				if (Objects.nonNull(player.getTarget())) {
					WildernessTargets.resetTarget(player, player.getTarget(), false);
				}
				break;
			case 9356:
				FightCave.init(player);
				break;
			case 4493:
				player.teleport(new Location(3433, 3537, 1));
				break;
			case 4494:
				player.teleport(new Location(3438, 3538));
				break;
			case 3556:
			case 10529:
				if (player.getLocation().getY() >= 3556)
					player.teleport(new Location(3427, 3555, 1));
				else {
					player.teleport(new Location(3427, 3556, 1));
				}
				break;
			case 4495:
				player.teleport(new Location(3417, 3541, 2));
				break;
			case 4496:
				player.teleport(new Location(3412, 3541, 1));
				break;
			case 5126:
				if (player.getLocation().getY() <= 3554)
					player.teleport(new Location(3445, 3555, 2));
				else {
					player.teleport(new Location(3445, 3554, 2));
				}
				break;
			case 1596:
			case 1597:
				if (player.getLocation().getY() <= 9917)
					player.teleport(new Location(3131, 9918, 0));
				else {
					player.teleport(new Location(3131, 9917, 0));
				}
				break;
			case 1557:
			case 1558:
				if (player.getLocation().getY() == 9944)
					player.teleport(new Location(3105, 9945, 0));
				else if (player.getLocation().getY() == 9945)
					player.teleport(new Location(3105, 9944, 0));
				else if (player.getLocation().getX() == 3146)
					player.teleport(new Location(3145, 9871, 0));
				else if (player.getLocation().getX() == 3145) {
					player.teleport(new Location(3146, 9871, 0));
				}
				break;
			case 2623:
				if (player.getLocation().getX() >= 2924)
					player.teleport(new Location(2923, 9803, 0));
				else {
					player.teleport(new Location(2924, 9803, 0));
				}
				break;
			case 8960:
				if (player.getLocation().getX() <= 2490)
					player.teleport(new Location(2491, 10131, 0));
				else {
					player.teleport(new Location(2490, 10131, 0));
				}
				break;
			case 8959:
				if (player.getLocation().getX() <= 2490)
					player.teleport(new Location(2491, 10146, 0));
				else {
					player.teleport(new Location(2490, 10146, 0));
				}
				break;
			case 8958:
				if (player.getLocation().getX() <= 2490)
					player.teleport(new Location(2491, 10163, 0));
				else {
					player.teleport(new Location(2490, 10163, 0));
				}
				break;
			case 5103:
				if (player.getLocation().getX() >= 2691)
					player.teleport(new Location(2689, 9564, 0));
				else {
					player.teleport(new Location(2691, 9564, 0));
				}
				break;
			case 5104:
				if (player.getLocation().getY() <= 9568)
					player.teleport(new Location(2683, 9570, 0));
				else {
					player.teleport(new Location(2683, 9568, 0));
				}
				break;
			case 5110:
				player.teleport(new Location(2647, 9557, 0));
				break;
			case 5111:
				player.teleport(new Location(2649, 9562, 0));
				break;
			case 5106:
				if (player.getLocation().getX() <= 2674)
					player.teleport(new Location(2676, 9479, 0));
				else {
					player.teleport(new Location(2674, 9479, 0));
				}
				break;
			case 5107:
				if (player.getLocation().getX() <= 2693)
					player.teleport(new Location(2695, 9482, 0));
				else {
					player.teleport(new Location(2693, 9482, 0));
				}
				break;
			case 5105:
				if (player.getLocation().getX() <= 2672)
					player.teleport(new Location(2674, 9499, 0));
				else {
					player.teleport(new Location(2672, 9499, 0));
				}
				break;
			case 5088:
				player.teleport(new Location(2687, 9506, 0));
				break;
			case 5090:
				player.teleport(new Location(2682, 9506, 0));
				break;
			case 5097:
				player.teleport(new Location(2636, 9510, 2));
				break;
			case 5098:
				player.teleport(new Location(2636, 9517, 0));
				break;
			case 5096:
				player.teleport(new Location(2649, 9591, 0));
				break;
			case 5094:
				player.teleport(new Location(2643, 9594, 2));
				break;
			default:
				player.sendClientRightClickRemoval();
				break;
			}
			break;
		case 2:
			Location location = new Location(x, y, z);
			StallTask.attemptStealFromStall(player, id, location);
			Ore ore = Ore.get(object.getId());
			if (ore != null) {
				TaskQueue.queue(new ProspectingTask(player, object));
			}
			switch (id) {
			case 2646:
				Flax.pickFlax(player, x, y);
				break;
			case 12309:
				player.getShopping().open(10);
				break;
			case 9390:
			case 2781:
			case 3044:
			case 45310:
			case 21303:
			case 26814:
			case 11666:
				SmithingConstants.sendSmeltSelectionInterface(player);
				break;
			case 2557:
			case 2558:
				if (player.getSkill().getLevels()[17] < 52) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You need a thieving level of 52 to pick the lock on this door."));
					return;
				}
				if (!player.getInventory().hasItemId(1523)) {
					DialogueManager.sendStatement(player, "You need a lockpick to be able to open this door.");
					return;
				}
				player.getClient().queueOutgoingPacket(new SendMessage("You attempt to pick the lock.S."));
				Task task = new LockpickTask(player, (byte) 2, x, y, z);
				player.getAttributes().set("lockPick", task);
				TaskQueue.queue(task);
				break;
			case 35543:
				if (player.getY() >= 3117) {
					if (player.getInventory().hasItemAmount(1854, 1)) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("You use your shantay pass to enter the desert.."));
						player.teleport(new Location(3304, 3117 - 2));
						player.getInventory().remove(1854);
					} else {
						player.start(new Shantay(player));
					}
				} else {
					player.teleport(new Location(3304, 3117));
				}
				break;

			case 4309:
				for (Item i : player.getInventory().getItems()) {
					if (i != null && Spinnable.forId(i.getId()) != null) {
						Spinnable spinnable = Spinnable.forId(i.getId());
						player.getAttributes().set("craftingType", CraftingType.WHEEL_SPINNING);
						player.getAttributes().set("spinnable", spinnable);
						player.getClient().queueOutgoingPacket(new SendString(
								"\\n \\n \\n \\n" + spinnable.getOutcome().getDefinition().getName(), 2799));
						player.getClient().queueOutgoingPacket(new SendChatInterface(4429));
						player.getClient().queueOutgoingPacket(new SendMoveComponent(0, 0, 1746));
						player.getClient().queueOutgoingPacket(
								new SendItemOnInterface(1746, 170, spinnable.getOutcome().getId()));
						return;
					}
				}
				player.getClient().queueOutgoingPacket(new SendMessage("You do not have anything to spin!"));
				break;
			case 1293:
				player.teleport(new Location(2461, 3434, 0));
				break;

			case 11402:
			case 21301:
			case 2213:
			case 6084:
			case 11758:
			case 25808:
			case 27663:
			case 49018:
			case 14369:
			case 24914:
			case 35647:
			case 14367:
				player.getBank().openBank();
				break;

			case 26972:
				player.start(new Banker(player));
				break;

			case 28089:
				player.start(new ShopExchangeDialogue(player));
				break;

			default:
				player.sendClientRightClickRemoval();
				break;
			}
			break;
		case 3:
			switch (id) {

			case 28089:
				player.getBank().openBank();
				break;

			case 12309:
				player.getShopping().open(RFDChestShop.RFD_CHEST_SHOP_ID);
				break;

			default:
				player.sendClientRightClickRemoval();
				return;
			}
			break;
		case 4:
			break;
		}
	}

	public static void itemOnObject(Player player, int itemId, int objectId, int x, int y) {
		if (GameConstants.DEV_MODE) {
			player.getClient().queueOutgoingPacket(new SendMessage("Item " + itemId + " on object " + objectId));
		}
		if (player.getMagic().isTeleporting() || player.isSpeared()) {
			return;
		}
		RSObject object = Region.getObject(x, y, player.getLocation().getZ());
		int z = player.getLocation().getZ();
		if ((object == null) && (!PlayerConstants.isOverrideObjectExistance(player, objectId, x, y, z))) {
			return;
		}
		int[] length = ObjectConstants.getObjectLength(objectId, object != null ? object.getFace() : 0);
		TaskQueue.queue(new WalkToTask(player, x, y, length[0], length[1]) {
			@Override
			public void onDestination() {
				player.getUpdateFlags().sendFaceToDirection(length[0] >= 2 ? x + length[0] / 2 : x,
						length[1] >= 2 ? y + length[1] / 2 : y);
				if (SmithingConstants.useBarOnAnvil(player, objectId, itemId)) {
					return;
				}
				if (objectId == 29953) {
					if (itemId == 12158 || itemId == 12159 || itemId == 12160 || itemId == 12163) {
						SummoningXP.process(player, itemId);
						return;
					}
				}
				if (GodWars.useItemOnObject(player, itemId, objectId)) {
					return;
				}
				if (objectId == 3044 || objectId == 45310 || objectId == 11666 || objectId == 2643) {
					SmithingConstants.sendSmeltSelectionInterface(player);
					return;
				}
				if (BoneBurying.useBonesOnAltar(player, itemId, objectId)) {
					World.sendStillGraphic(624, 0, new Location(
							player.getX() < x ? x : (player.getX() > x + 1 ? x + 1 : player.getX()), y, z));
					return;
				}
				if (player.getFarming().plant(itemId, objectId, x, y)) {
					return;
				}
				if (player.getFarming().useItemOnPlant(itemId, x, y)) {
					return;
				}
				if (objectId == 3827 && itemId == 954) { // To Kalphites
					player.teleport(new Location(3484, 9510, 2));
					return;
				}
				if (objectId == 23609 && itemId == 954) { // To Kalphite Queen
					player.teleport(new Location(3507, 9494));
					return;
				}
				if (objectId == 3044 || objectId == 45310 || objectId == 11666) {
					SmithingConstants.sendSmeltSelectionInterface(player);
					return;
				}
				if (objectId == 4309) {
					if (Spinnable.forId(itemId) != null) {
						Spinnable spinnable = Spinnable.forId(itemId);
						player.getAttributes().set("craftingType", CraftingType.WHEEL_SPINNING);
						player.getAttributes().set("spinnable", spinnable);
						player.getClient().queueOutgoingPacket(new SendString(
								"\\n \\n \\n \\n" + spinnable.getOutcome().getDefinition().getName(), 2799));
						player.getClient().queueOutgoingPacket(new SendChatInterface(4429));
						player.getClient().queueOutgoingPacket(new SendMoveComponent(0, 0, 1746));
						player.getClient().queueOutgoingPacket(
								new SendItemOnInterface(1746, 170, spinnable.getOutcome().getId()));
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("You cant spin this item."));
					}
					return;
				}
				GameObject object = new GameObject(objectId, x, y, player.getLocation().getZ(), 10, 0);
				if (CookingTask.isCookableObject(object)) {
					CookingTask.showInterface(player, object, new Item(itemId, 1));
					return;
				}
				if (objectId == 15621) {
					ArmorAnimator.armorOnAnimator(player, itemId, object, x, y);
					return;
				}
				player.send(new SendMessage("Nothing interesting happens."));
			}
		});
	}

	public static void useItemOnNpc(Player player, int item, int slot) {
		if (player.getMagic().isTeleporting()) {
			return;
		}
		if ((slot > World.getNpcs().length) || (slot < 0)) {
			return;
		}
		Npc mob = World.getNpcs()[slot];
		if (mob == null) {
			player.getMovementHandler().reset();
			return;
		}
		TaskQueue.queue(new FollowToEntityTask(player, mob) {
			@Override
			public void onDestination() {
				if (mob.face()) {
					mob.face(player);
				}
				player.face(mob);
				if (mob.getSize() > 1) {
					player.getMovementHandler().reset();
				}
				WalkToActions.finishItemOnNpc(player, item, mob);
			}
		});
	}
}
