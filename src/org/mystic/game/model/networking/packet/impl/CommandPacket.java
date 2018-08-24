package org.mystic.game.model.networking.packet.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.mystic.game.GameConstants;
import org.mystic.game.World;
import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.Yelling;
import org.mystic.game.model.content.bank.BankSecurity;
import org.mystic.game.model.content.combat.formula.MagicFormulas;
import org.mystic.game.model.content.combat.formula.MeleeFormulas;
import org.mystic.game.model.content.combat.formula.RangeFormulas;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.impl.ChangePasswordDialogue;
import org.mystic.game.model.content.dialogue.impl.Tutorial;
import org.mystic.game.model.content.minigames.barrows.BarrowsRewardTable;
import org.mystic.game.model.content.randomevent.impl.Dwarf;
import org.mystic.game.model.content.skill.SkillManager;
import org.mystic.game.model.content.skill.prayer.PrayerBook;
import org.mystic.game.model.content.skill.prayer.PrayerBook.PrayerBookType;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.trivia.TriviaBot;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.drops.NPCDrops;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendSystemBan;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.game.model.networking.packet.command.CommandManager;
import org.mystic.game.model.player.io.PlayerSave;
import org.mystic.game.model.player.io.PlayerSaveUtil;
import org.mystic.utility.GameDefinitionLoader;

public class CommandPacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		String command = in.readString();
		String[] split = command.split(" ");
		String check = command.toLowerCase();
		if ((player.getController().equals(Tutorial.TUTORIAL_CONTROLLER)) && (player.getDialogue() != null)) {
			return;
		}
		if (check.startsWith("/")) {
			if (player.clan != null) {
				String message = command.substring(1);
				player.clan.sendChat(player, message);
				return;
			} else if (player.clan == null) {
				player.getClient().queueOutgoingPacket(new SendMessage("You can only do this while in a clan chat."));
				return;
			}
		}
		if (player.isSpeared()) {
			return;
		}
		if (player.isBusy()) {
			return;
		}
		if (check.startsWith("answer")) {
			String answer = command.substring(7);
			TriviaBot.answer(player, answer.trim());
		}
		if ((check.startsWith("yell")) && (!check.contains("yellmute"))) {
			try {
				Yelling.yell(player, command.substring(5, command.length()));
			} catch (Exception e) {
				player.getClient().queueOutgoingPacket(new SendMessage("Invalid yell format, syntax: ::yell messsage"));
			}
			return;
		}
		if (check.startsWith("changepass")) {
			try {
				if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
					BankSecurity.init(player, false);
					player.send(new SendMessage("You must enter your bank pin before changing your password."));
					return;
				}
				String password = command.substring("changepass".length()).trim();
				if ((password.length() > 4) && (password.length() < 15))
					player.start(new ChangePasswordDialogue(player, password.toLowerCase()));
				else
					DialogueManager.sendStatement(player,
							new String[] { "Your password must be between 4 and 15 characters." });
			} catch (Exception e) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("Invalid password format, syntax: ::changepass password here"));
			}
			return;
		}
		if (PlayerConstants.isOwner(player) || player.getRights().equal(Rights.OWNER)) {
			handleOwnerCommands(player, split[0].toLowerCase(), Arrays.copyOfRange(split, 1, split.length));
		}
		CommandManager.handleCommand(player, command);
	}

	public void handleOwnerCommands(final Player player, String keyword, String[] args) {
		try {
			if (PlayerConstants.isOwner(player) || player.getRights().equal(Rights.OWNER)) {
				if (keyword.equals("curses")) {
					player.send(new SendMessage("You switch to curses."));
					PrayerBook.setBook(player, PrayerBookType.CURSES);
					return;
				}
				if (keyword.equals("default")) {
					player.send(new SendMessage("You switch to defaults."));
					PrayerBook.setBook(player, PrayerBookType.DEFAULT);
					return;
				}
				if (keyword.equals("tele")) {
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					int z = 0;
					if (args.length > 2) {
						z = Integer.parseInt(args[2]);
					}
					player.teleport(new Location(x, y, z));
					return;
				}
				if (keyword.equals("random")) {
					final Dwarf dwarf = new Dwarf();
					dwarf.start(player);
				}
				if (keyword.startsWith("wipe")) {
					player.getBank().clear();
					player.getBank().update();
					player.send(new SendRemoveInterfaces());
					return;
				}
				if (keyword.startsWith("rollchest")) {
					BarrowsRewardTable.displayReward(player);
					return;
				}
				if (keyword.startsWith("resetbarrows")) {
					player.getMinigames().resetBarrows();
					return;
				}
				if (keyword.startsWith("packetlog")) {
					String name = args[0];
					Player p = World.getPlayerByName(name.replaceAll("_", " "));
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.getClient().setLogPlayer(true);
						player.getClient().queueOutgoingPacket(
								new SendMessage("Now logging incoming packets for: " + p.getUsername() + "."));
					}
					return;
				}
				if (keyword.startsWith("unpacketlog")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.getClient().setLogPlayer(false);
						player.getClient().queueOutgoingPacket(
								new SendMessage("No longer logging incoming packets for: " + p.getUsername() + "."));
					}
					return;
				}
				if (keyword.startsWith("kick")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.logout(true);
						player.getClient().queueOutgoingPacket(new SendMessage("Kicked."));
					}
					return;
				}
				if (keyword.equals("ipban")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("Success."));
						new SendSystemBan().execute(p.getClient());
						PlayerSaveUtil.setIPBanned(p);
						p.logout(true);
					}
				}
				if (keyword.equals("ipmute")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						PlayerSaveUtil.setIPMuted(p);
						player.getClient().queueOutgoingPacket(new SendMessage("Success."));
						p.setMuted(true);
					}
				}
				if (keyword.startsWith("ecosearch")) {
					int id = Integer.parseInt(args[0]);
					long amount = 0L;
					for (Player p : World.getPlayers()) {
						if ((p != null) && (p.isActive())) {
							amount += p.getInventory().getItemAmount(id);
							amount += p.getBank().getItemAmount(id);
						}
					}
					player.getClient().queueOutgoingPacket(new SendMessage("There is currently @dre@" + amount
							+ "x @bla@of: " + Item.getDefinition(id).getName() + " in the game."));
					return;
				}
				if (keyword.equals("npc")) {
					Npc mob = new Npc(Integer.parseInt(args[0]), true, new Location(player.getLocation()));
					player.getClient().queueOutgoingPacket(new SendMessage("Spawned NPC index: " + mob.getIndex()));
				}
				if (keyword.equals("giveitem")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.getInventory().add(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
						p.getClient().queueOutgoingPacket(
								new SendMessage(player.getDisplay() + " slides an item into your inventory."));
						player.getClient().queueOutgoingPacket(new SendMessage("Item successfully given."));
					}
				}
				if (keyword.equals("interface")) {
					player.getClient().queueOutgoingPacket(new SendInterface(Integer.parseInt(args[0])));
				}
				if (keyword.equals("sound")) {
					SoundPlayer.play(player, Integer.parseInt(args[0]), 0, 0);
				}
				if (keyword.equals("headicon")) {
					player.getPrayer().setHeadIcon((byte) Integer.parseInt(args[0]));
					player.setAppearanceUpdateRequired(true);
				}
				if (keyword.equals("bank")) {
					player.getBank().openBank();
				}
				if (keyword.equals("resetlevels")) {
					for (int i = 0; i < 25; i++) {
						player.getSkill().reset(i);
					}
				}
				if (keyword.equals("update")) {
					int timer = Integer.parseInt(args[0]);
					World.initUpdate(timer);
				}
				if (keyword.equals("pnpc")) {
					player.setNpcAppearanceId((short) Integer.parseInt(args[0]));
					player.setAppearanceUpdateRequired(true);
				}
				if (keyword.equals("reloadobjectspawns")) {
					ObjectManager.declare();
				}
				if (keyword.equals("resetupdate")) {
					World.resetUpdate();
				}
				if (keyword.startsWith("meleemax")) {
					player.getClient().queueOutgoingPacket(new SendMessage(
							"melee max hit: " + MeleeFormulas.getMaxHit(player, player.getCombat().getAttacking())));
				}
				if (keyword.startsWith("rangedmax")) {
					player.getClient()
							.queueOutgoingPacket(new SendMessage("ranged max hit: " + RangeFormulas.getMaxHit(player)));
				}
				if (keyword.startsWith("magicmax")) {
					player.getClient()
							.queueOutgoingPacket(new SendMessage("magic max hit: " + MagicFormulas.getMaxHit(player)));
				}
				if (keyword.equals("master")) {
					for (int i = 0; i < 25; i++) {
						if (i == 22 || i == 21 || i == 24) {
							player.getLevels()[i] = 1;
							player.getMaxLevels()[i] = 1;
							player.getSkill().getExperience()[i] = 0;
						} else {
							player.getLevels()[i] = 99;
							player.getMaxLevels()[i] = 99;
							player.getSkill().getExperience()[i] = SkillManager.EXP_FOR_LEVEL[98];
						}
						player.getSkill().update(i);
					}
					player.getSkill().updateCombatLevel();
					player.setAppearanceUpdateRequired(true);
				}
				if (keyword.equals("pouch")) {
					player.getRunePouch().add(new Item(560, 600));
					player.getRunePouch().add(new Item(555, 600));
					player.getRunePouch().add(new Item(565, 600));
					player.getRunePouch().open();
				}
				if (keyword.equals("setlvl")) {
					int id = Integer.parseInt(args[0]);
					int lvl = Integer.parseInt(args[1]);
					player.getLevels()[id] = ((byte) lvl);
					player.getMaxLevels()[id] = ((byte) lvl);
					player.getSkill().getExperience()[id] = player.getSkill().getXPForLevel(id, lvl);
					player.getSkill().update(id);
					player.getSkill().updateCombatLevel();
				}
				if (keyword.equals("reloadshops")) {
					GameDefinitionLoader.loadShopDefinitions();
				}
				if (keyword.equals("reloaddrops")) {
					NPCDrops.parseDrops().load();
					player.send(new SendMessage("Reloading drops..."));
					return;
				}
				if (keyword.equals("drop")) {
					int id = Integer.parseInt(args[0]);
					int amount = Integer.parseInt(args[1]);
					for (int i = 0; i < amount; i++) {
						NPCDrops.test(player, id);
					}
					player.send(new SendMessage("Sending test drops for (" + amount + ") kills of NPC ID: " + id));
					player.getBank().openBank();
					return;
				}
				if (keyword.equals("setowner")) {
					player.setRights(Rights.OWNER);
					player.send(new SendMessage("You have set your rank to: OWNER"));
					return;
				}
				if (keyword.equals("setadmin")) {
					player.setRights(Rights.ADMINISTRATOR);
					player.send(new SendMessage("You have set your rank to: ADMINISTRATOR"));
					return;
				}
				if (keyword.equals("givemod")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (IronMan.isIronMan(p)) {
						player.send(new SendMessage("You can't give this rank to an Iron man."));
						return;
					}
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Rights.MODERATOR);
						p.logout(true);
						player.getClient().queueOutgoingPacket(new SendMessage("Set player to MODERATOR rank."));
					}
					return;
				}
				if (keyword.equals("giveiron")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (IronMan.isIronMan(p)) {
						player.send(new SendMessage("You can't give this rank to an Iron man."));
						return;
					}
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Rights.HARDCORE_IRON_MAN);
						p.logout(true);
						player.getClient()
								.queueOutgoingPacket(new SendMessage("Set player to HARDCORE_IRON_MAN rank."));
					}
					return;
				}
				if (keyword.equals("giveplayer")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (IronMan.isIronMan(p)) {
						player.send(new SendMessage("You can't give this rank to an Iron man."));
						return;
					}
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Rights.PLAYER);
						p.goldmember = false;
						player.getClient().queueOutgoingPacket(new SendMessage("Set player to PLAYER rank."));
					}
					return;
				}
				if (keyword.equals("giveadmin")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (IronMan.isIronMan(p)) {
						player.send(new SendMessage("You can't give this rank to an Iron man."));
						return;
					}
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Rights.ADMINISTRATOR);
						p.logout(true);
						player.getClient().queueOutgoingPacket(new SendMessage("Set player to ADMINISTRATOR rank."));
					}
					return;
				}
				if (keyword.equals("givelegendary")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (IronMan.isIronMan(p)) {
						player.send(new SendMessage("You can't give this rank to an Iron man."));
						return;
					}
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Rights.LEGENDARY_GOLD_MEMBER);
						p.logout(true);
						player.getClient()
								.queueOutgoingPacket(new SendMessage("Set player to LEGENDARY_GOLD_MEMBER rank."));
					}
					return;
				}
				if (keyword.equals("giveextreme")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (IronMan.isIronMan(p)) {
						player.send(new SendMessage("You can't give this rank to an Iron man."));
						return;
					}
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Rights.EXTREME_GOLD_MEMBER);
						p.logout(true);
						player.getClient()
								.queueOutgoingPacket(new SendMessage("Set player to EXTREME_GOLD_MEMBER rank."));
					}
					return;
				}
				if (keyword.equals("givegold")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (IronMan.isIronMan(p)) {
						player.send(new SendMessage("You can't give this rank to an Iron man."));
						return;
					}
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Rights.GOLD_MEMBER);
						p.logout(true);
						player.getClient().queueOutgoingPacket(new SendMessage("Set player to GOLD_MEMBER rank."));
					}
					return;
				}
				if (keyword.equals("giveyoutuber")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (IronMan.isIronMan(p)) {
						player.send(new SendMessage("You can't give this rank to an Iron man."));
						return;
					}
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Rights.YOUTUBER);
						p.logout(true);
						player.getClient().queueOutgoingPacket(new SendMessage("Set player to YOUTUBER rank."));
					}
					return;
				}
				if (keyword.equals("givevet")) {
					String name = args[0].replaceAll("_", " ");
					Player p = World.getPlayerByName(name);
					if (IronMan.isIronMan(p)) {
						player.send(new SendMessage("You can't give this rank to an Iron man."));
						return;
					}
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Rights.VETERAN);
						p.logout(true);
						player.getClient().queueOutgoingPacket(new SendMessage("Set player to VETERAN rank."));
					}
					return;
				}
				if (keyword.equals("setmod")) {
					player.setRights(Rights.MODERATOR);
					player.send(new SendMessage("You have set your rank to: MODERATOR"));
					return;
				}
				if (keyword.equals("setlegendary")) {
					player.setRights(Rights.LEGENDARY_GOLD_MEMBER);
					player.send(new SendMessage("You have set your rank to: LEGENDARY GOLD MEMBER"));
					return;
				}
				if (keyword.equals("setextreme")) {
					player.setRights(Rights.EXTREME_GOLD_MEMBER);
					player.send(new SendMessage("You have set your rank to: EXTREME GOLD MEMBER"));
					return;
				}
				if (keyword.equals("setgold")) {
					player.setRights(Rights.GOLD_MEMBER);
					player.send(new SendMessage("You have set your rank to: GOLD MEMBER"));
					return;
				}
				if (keyword.equals("setyou")) {
					player.setRights(Rights.YOUTUBER);
					player.send(new SendMessage("You have set your rank to: YOUTUBER"));
					return;
				}
				if (keyword.equals("setvet")) {
					player.setRights(Rights.VETERAN);
					player.send(new SendMessage("You have set your rank to: VETERAN"));
					return;
				}
				if (keyword.startsWith("gfx")) {
					player.getUpdateFlags().sendGraphic(new Graphic(Integer.parseInt(args[0]), 0, true));
				}
				if (keyword.startsWith("ipsearch")) {
					try {
						byte offsetY;
						File[] files = new File("./data/characters/details/").listFiles();
						String host = args[0];
						System.out.println("Now searching accounts for host: " + host);
						for (offsetY = 0; offsetY < files.length; offsetY++) {
							File i = files[offsetY];
							String name = i.getName().replace(".json", "");
							Player p = new Player();
							p.setUsername(name);
							try {
								PlayerSave.load(p);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				if (keyword.startsWith("item")) {
					if (args.length == 1) {
						int item = Integer.parseInt(args[0]);
						player.getInventory().add(item, 1);
					} else if (args.length == 2) {
						int item;
						int amount;
						if (Integer.parseInt(args[0]) > Integer.MAX_VALUE) {
							item = Integer.MAX_VALUE;
						} else {
							item = Integer.parseInt(args[0]);
						}
						if (Integer.parseInt(args[1]) > Integer.MAX_VALUE) {
							amount = Integer.MAX_VALUE;
						} else {
							amount = Integer.parseInt(args[1]);
						}
						player.getInventory().add(item, amount);
					}
				}
				if (keyword.equals("anim")) {
					player.getUpdateFlags().sendAnimation(Integer.parseInt(args[0]), 0);
				}
				if (keyword.equals("skull")) {
					player.getSkulling().skull(player, player);
				}
				if (keyword.equals("spawn")) {
					try {
						int npcId = Integer.parseInt(args[0]);
						World.register(new Npc(npcId, false, new Location(player.getLocation())));
						BufferedWriter bw = new BufferedWriter(
								new FileWriter(new File("./data/spawn command dump.txt"), true));
						bw.newLine();
						bw.write("\t<NpcSpawnDefinition>", 0, "\t<NpcSpawnDefinition>".length());
						bw.newLine();
						bw.write("\t<!-->" + GameDefinitionLoader.getNpcDefinition(npcId).getName() + "<-->", 0,
								("\t<!-->" + GameDefinitionLoader.getNpcDefinition(npcId).getName() + "<-->").length());
						bw.newLine();
						bw.write("\t\t<id>" + npcId + "</id>", 0, ("\t\t<id>" + npcId + "</id>").length());
						bw.newLine();
						bw.write("\t\t<location>", 0, "\t\t<location>".length());
						bw.newLine();
						bw.write("\t\t\t<x>" + player.getLocation().getX() + "</x>", 0,
								("\t\t\t<x>" + player.getLocation().getX() + "</x>").length());
						bw.newLine();
						bw.write("\t\t\t<y>" + player.getLocation().getY() + "</y>", 0,
								("\t\t\t<y>" + player.getLocation().getY() + "</y>").length());
						bw.newLine();
						bw.write("\t\t\t<z>" + player.getLocation().getZ() + "</z>", 0,
								("\t\t\t<z>" + player.getLocation().getZ() + "</z>").length());
						bw.newLine();
						bw.write("\t\t</location>", 0, "\t\t</location>".length());
						bw.newLine();
						bw.write("\t\t<walk>true</walk>", 0, "\t\t<walk>true</walk>".length());
						bw.newLine();
						bw.write("\t</NpcSpawnDefinition>", 0, "\t</NpcSpawnDefinition>".length());
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (keyword.startsWith("shop")) {
					player.getShopping().open(Integer.parseInt(args[0]));
				}
				if (keyword.equals("mypos")) {
					player.getClient().queueOutgoingPacket(new SendMessage("You are at: " + player.getLocation()));
				}
			}
		} catch (Exception e) {
			if (GameConstants.DEV_MODE) {
				e.printStackTrace();
			}
			player.getClient().queueOutgoingPacket(new SendMessage("Invalid syntax."));
		}
	}
}