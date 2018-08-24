package org.mystic.game.model.player.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.mystic.game.model.content.ItemDegrading;
import org.mystic.game.model.content.bank.Banking;
import org.mystic.game.model.content.skill.farming.GrassyPatch;
import org.mystic.game.model.content.skill.farming.Plant;
import org.mystic.game.model.content.skill.prayer.impl.CursesPrayerBook;
import org.mystic.game.model.content.skill.prayer.impl.DefaultPrayerBook;
import org.mystic.game.model.content.skill.slayer.SlayerDifficulty;
import org.mystic.game.model.entity.Entity.AttackType;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Equipment;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.utility.Misc;
import org.mystic.utility.NameUtil;
import org.mystic.utility.TimeStamp;

import com.google.gson.Gson;

public final class PlayerSave {

	public static final class PlayerContainer {

		public static boolean loadDetails(Player player) throws Exception {
			File file = new File("./data/characters/containers/" + player.getUsername() + ".json");
			if (!file.exists()) {
				return false;
			}
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				PlayerContainer details = PlayerSave.GSON.fromJson(reader, PlayerContainer.class);
				if (details.shopItems != null) {
					player.getPlayerShop().setItems(details.shopItems);
				}
				if (details.shopPrices != null) {
					player.getPlayerShop().setPrices(details.shopPrices);
				}
				if (details.tabAmounts != null) {
					player.getBank().setTabAmounts(details.tabAmounts);
				}
				if (details.bank != null) {
					int tabs = Arrays.stream(player.getBank().getTabAmounts()).sum();
					int total = 0;
					for (int i = 0, slot = 0; i < Banking.SIZE; i++) {
						if (i >= Banking.SIZE) {
							break;
						}
						if (i >= details.bank.length) {
							break;
						}
						Item check = ItemCheck.check(player, details.bank[i]);
						player.getBank().getItems()[slot++] = check;
						if (check != null) {
							total++;
						}
					}
					if (total != tabs) {
						player.getBank().setTabAmounts(new int[] { total, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
					}
				}
				if (details.equipment != null) {
					for (int i = 0; i < details.equipment.length; i++) {
						player.getEquipment().getItems()[i] = ItemCheck.check(player, details.equipment[i]);
					}
				}
				if (details.inventory != null) {
					for (int i = 0; i < details.inventory.length; i++) {
						player.getInventory().getItems()[i] = ItemCheck.check(player, details.inventory[i]);
					}
				}
				if (details.runepouch != null) {
					for (int i = 0; i < details.runepouch.length; i++) {
						player.getRunePouch().getItems()[i] = ItemCheck.check(player, details.runepouch[i]);
					}
				}
				if (details.bobInventory != null) {
					player.getAttributes().set("summoningbobinventory", details.bobInventory);
				}
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
			return true;
		}

		private final Item[] bank;
		private final int[] tabAmounts;
		private final Item[] runepouch;
		private final Item[] inventory;
		private final Item[] equipment;
		private final Item[] bobInventory;
		private final Item[] shopItems;
		private final int[] shopPrices;

		public PlayerContainer(Player player) {
			this.bank = player.getBank().getItems();
			this.tabAmounts = player.getBank().getTabAmounts();
			this.runepouch = player.getRunePouch().getItems();
			this.inventory = player.getInventory().getItems();
			this.equipment = player.getEquipment().getItems();
			this.bobInventory = (player.getSummoning().isFamilarBOB() ? player.getSummoning().getContainer().getItems()
					: null);
			this.shopItems = player.getPlayerShop().getItems();
			this.shopPrices = player.getPlayerShop().getPrices();
		}

		public void parseDetails(Player player) throws IOException {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter("./data/characters/containers/" + player.getUsername() + ".json", false));
			try {
				writer.write(PlayerSave.GSON.toJson(this));
				writer.flush();
			} finally {
				writer.close();
			}
		}
	}

	public static final class PlayerDetails {

		public static boolean loadDetails(Player player) throws Exception {
			BufferedReader reader = null;
			try {
				File file = new File(
						"./data/characters/details/" + NameUtil.uppercaseFirstLetter(player.getUsername()) + ".json");
				if (!file.exists()) {
					return false;
				}
				reader = new BufferedReader(new FileReader(file));
				PlayerDetails details = PlayerSave.GSON.fromJson(reader, PlayerDetails.class);
				player.setUsername(details.username);
				player.setPassword(details.password);
				player.getLocation().setAs(new Location(details.x, details.y, details.z));
				player.setRights(details.rights);
				player.setRetaliate(details.retaliate);
				player.getSkill().setExpLock(details.expLock);
				player.getEarningPotential().setPkp(details.pkp);
				player.setScreenBrightness(details.screenBrightness);
				player.getSlayer().setAmount(details.slayerAmount);
				player.getSlayer().setTask(details.task);
				player.setPoisonDamage(details.poisonDmg);
				player.getSpecialAttack().setSpecialAmount(details.spec);
				player.getRunEnergy().setEnergy(details.energy);
				player.setTotalVotes(details.totalVotes);
				player.getSummoning().setAttack(details.summoningAttack);
				player.getSummoning().setSpecial(details.summoningSpecialAmount);
				player.getSummoning().setTime(details.summoningTime);
				player.setPestPoints(details.pestPoints);
				player.getBankPinAttributes().setBankPin(details.bankPin);
				player.getBankPinAttributes().setHasBankPin(details.hasPin);
				player.getBankPinAttributes().setLastAttempt(details.lastPinAttempt);
				player.getBankPinAttributes().setLastAttempt(details.pinAttempts);
				if (details.killOrKilledBy != null) {
					player.getEarningPotential().setKillOrKilledBy(details.killOrKilledBy);
				}
				if (details.summoningFamiliar != -1) {
					player.getAttributes().set("summoningfamsave", Integer.valueOf(details.summoningFamiliar));
				}
				player.getClient().setLogPlayer(details.logPackets);
				player.getRunEnergy().setRunning(details.running);
				player.setSkillPoints(details.skillPoints);
				player.setTeleblockTime(details.teleblockTime);
				if (details.achievements != null) {
					player.getAchievements().setData(details.achievements);
				}
				if (details.lastVote != null) {
					player.setVoteTime(details.lastVote);
				}
				player.setKills((short) details.kills);
				player.setDeaths((short) details.deaths);
				player.setMusicVolume((byte) details.musicVolume);
				player.setEP((byte) details.EP);
				player.setSoundVolume((byte) details.soundVolume);
				player.setVotePoints(details.votePoints);
				player.setSlayerPoints(details.slayerPoints);
				player.setRunecraftingPoints(details.runecraftingPoints);
				player.setDonatorPoints(details.donatorPoints);
				player.displayKDR = details.displayKDR;
				player.getMagic().setDragonFireShieldCharges(details.dragonFireShieldCharges);
				player.duelWins = details.duelWins;
				player.duelLosses = details.duelLosses;
				if (details.degrading != null) {
					player.getItemDegrading().setDegrading(details.degrading);
				}
				if (details.savedArrows != null) {
					player.getRanged().getSavedArrows().setItems(details.savedArrows);
				}
				if (details.attackStyle != null) {
					player.getEquipment().setAttackStyle(details.attackStyle);
				}
				if (details.attackType != null) {
					player.setAttackType(details.attackType);
				}
				if (details.recoilStage != -1) {
					player.getAttributes().set("recoilhits", Integer.valueOf(details.recoilStage));
				}
				if (details.plants != null) {
					player.getFarming().setPlants(details.plants);
				}
				if (details.patches != null) {
					player.getFarming().setPatches(details.patches);
				}
				player.getSkulling().setLeft(details.left);
				if (player.getSkulling().isSkulled()) {
					player.getSkulling().setSkullIcon(player, details.skullIcon);
				}
				if (details.host != null) {
					player.getClient().setHost(details.host);
				}
				if (details.slayerDifficulty != null) {
					player.getSlayer().setCurrent(details.slayerDifficulty);
				}
				player.setMultipleMouseButtons(details.multipleMouse);
				player.setChatEffects(details.chatEffects);
				player.setSplitPrivateChat(details.splitPrivate);
				player.setAcceptAid(details.acceptAid);
				player.getJadDetails().setStage(details.fightCavesWave);
				player.setDungPoints(details.dungPoints);
				if (details.questStages != null) {
					for (int i = 0; i < details.questStages.length; i++) {
						player.getQuesting().getQuestStages()[i] = details.questStages[i];
					}
				}
				if (details.activeQuests != null) {
					for (byte i : details.activeQuests) {
						player.getQuesting().setQuestActive(
								org.mystic.game.model.content.quest.QuestConstants.QUESTS_BY_ID[i], true);
					}

				}
				if (details.friends != null) {
					for (String i : details.friends) {
						player.getPrivateMessaging().getFriends().add(i);
					}
				}
				if (details.ignores != null) {
					for (String i : details.ignores) {
						player.getPrivateMessaging().getIgnores().add(i);
					}
				}
				if ((details.poisonDmg > 0) && (details.poisoned)) {
					player.poison(details.poisonDmg);
				}
				player.setGender(details.gender);
				if (details.appearance != null) {
					for (int i = 0; i < details.appearance.length; i++)
						player.getAppearance()[i] = details.appearance[i];
				}
				if (details.colours != null) {
					for (int i = 0; i < details.colours.length; i++)
						player.getColors()[i] = details.colours[i];
				}
				if (details.experience != null) {
					for (int i = 0; i < details.experience.length; i++) {
						player.getSkill().getExperience()[i] = details.experience[i];
					}
				}
				if (details.skillsLevel != null) {
					for (int i = 0; i < details.skillsLevel.length; i++) {
						player.getLevels()[i] = details.skillsLevel[i];
					}
				}
				if (details.experience != null) {
					for (int i = 0; i < details.experience.length; i++) {
						player.getMaxLevels()[i] = player.getSkill().getLevelForExperience(i, details.experience[i]);
					}
				}
				if (details.gwkc != null) {
					player.getMinigames().setGWKC(details.gwkc);
				}
				boolean banned = details.banned;
				boolean muted = details.muted;
				if ((banned) && (!Misc.isExpired(details.banDay, details.banYear, details.banLength))) {
					player.setBanned(true);
				}
				if ((muted) && (!Misc.isExpired(details.muteDay, details.muteYear, details.muteLength))) {
					player.setMuted(true);
					player.setMuteDay(details.muteDay);
					player.setMuteYear(details.muteYear);
					player.setMuteLength(details.muteLength);
					player.setRemainingMute(details.muteLength - Misc.getElapsed(details.muteDay, details.muteYear));
				}
				if (details.magicBook > 0) {
					player.getMagic().setMagicBook(details.magicBook);
				}
				if (details.prayerBook > 0) {
					player.setPrayerInterface(details.prayerBook);
				}
				if (details.barrowsKilled != null) {
					for (int i = 0; i < details.barrowsKilled.length; i++) {
						if (details.barrowsKilled[i]) {
							player.getMinigames().setBarrowKilled(i);
						}
					}
				}
				if (details.tunnel > 0) {
					player.getMinigames().setTunnel(details.tunnel);
				}
				if (details.killstreak > 0) {
					player.setKillstreak(details.killstreak);
				}
				if (details.epTimer > 0) {
					player.epTimer = details.epTimer;
				}
				if (details.targetTimer > 0) {
					player.targetTimer = details.targetTimer;
				}
				if (details.goldmember == true) {
					player.setGoldMember(true);
				}
				if (details.achievementpoints > 0) {
					player.achievementpoints = details.achievementpoints;
				}
				if (details.prayerBook == 0) {
					player.setPrayerInterface(5608);
					player.setPrayer(new DefaultPrayerBook(player));
				} else if (details.prayerBook == 5608) {
					player.setPrayer(new DefaultPrayerBook(player));
				} else if (details.prayerBook == 21356) {
					player.setPrayer(new CursesPrayerBook(player));
				}
				if (details.quickPrayersDefault != null) {
					player.setQuickPrayersDefault(details.quickPrayersDefault);
				}
				if (details.quickPrayersCurses != null) {
					player.setQuickPrayersCurses(details.quickPrayersCurses);
				}
				return true;
			} finally {
				if (reader != null)
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}

		private final String username;
		private final String password;
		private final int x;
		private final int y;
		private final int z;
		private final Rights rights;
		private final boolean banned;
		private final int banLength;
		private final int banDay;
		private final int banYear;
		private final boolean muted;
		private final int muteLength;
		private final int muteDay;
		private final int muteYear;
		private final int fightCavesWave;
		private final int magicBook;
		private final int prayerBook;
		private final boolean retaliate;
		private final boolean expLock;
		private final boolean[] barrowsKilled;
		private final int EP;
		private final int pkp;
		private final int screenBrightness;
		private final short[] gwkc;
		private final boolean poisoned;
		private final int poisonDmg;
		private final int task;
		private final int slayerAmount;
		private final SlayerDifficulty slayerDifficulty;
		private final short[] skillsLevel;
		private final double[] experience;
		private final byte gender;
		private final int[] appearance;
		private final byte[] colours;
		private final long left;
		private final int skullIcon;
		private final byte multipleMouse;
		private final byte chatEffects;
		private final byte splitPrivate;
		private final byte acceptAid;
		private final String[] friends;
		private final String[] ignores;
		private final byte[] questStages;
		private final byte[] activeQuests;
		private final int dungPoints;
		private final int recoilStage;
		private final int spec;
		private final Equipment.AttackStyles attackStyle;
		private final AttackType attackType;
		private final double energy;
		private final String host;
		private final int votePoints;
		private final ItemDegrading.DegradedItem[] degrading;
		private final byte dragonFireShieldCharges;
		private final int slayerPoints;
		private final int musicVolume;
		private final int soundVolume;
		private final TimeStamp lastVote;
		private final Item[] savedArrows;
		private final int kills;
		private final int deaths;
		private final Plant[] plants;
		private final GrassyPatch[] patches;
		private final int skillPoints;
		private final boolean running;
		private final boolean logPackets;
		private final String[][] achievements;
		private final int summoningTime;
		private final int summoningSpecialAmount;
		private final int summoningFamiliar;
		private final boolean summoningAttack;
		private final int pestPoints;
		private final int totalVotes;
		private final int teleblockTime;
		private final long[] killOrKilledBy;
		private final byte[] quickPrayersDefault;
		private final byte[] quickPrayersCurses;
		private final int[] bankPin;
		private final boolean hasPin;
		private final long lastPinAttempt;
		private final int pinAttempts;
		private final int tunnel;
		private final int killstreak;
		private final int epTimer;
		private final int targetTimer;
		private final boolean goldmember;
		private final int achievementpoints;
		private final int runecraftingPoints;
		private final int donatorPoints;
		private final int duelWins;
		private final int duelLosses;
		private final boolean displayKDR;

		public PlayerDetails(Player player) {
			username = player.getUsername();
			password = player.getPassword();
			x = player.getLocation().getX();
			y = player.getLocation().getY();
			z = player.getLocation().getZ();
			rights = player.getRights();
			banned = player.isBanned();
			banLength = player.getBanLength();
			banDay = player.getBanDay();
			banYear = player.getBanYear();
			muted = player.isMuted();
			muteLength = player.getMuteLength();
			muteDay = player.getMuteDay();
			muteYear = player.getMuteYear();
			fightCavesWave = player.getJadDetails().getStage();
			magicBook = player.getMagic().getMagicBook();
			prayerBook = player.getPrayerInterface();
			retaliate = player.isRetaliate();
			expLock = player.getSkill().isExpLocked();
			barrowsKilled = player.getMinigames().getBarrowsKilled();
			pkp = player.getEarningPotential().getPkp();
			screenBrightness = player.getScreenBrightness();
			gwkc = player.getMinigames().getGWKC();
			quickPrayersCurses = player.getQuickPrayersCurses();
			quickPrayersDefault = player.getQuickPrayersDefault();
			killOrKilledBy = player.getEarningPotential().getKillOrKilledBy();
			poisoned = player.isPoisoned();
			poisonDmg = player.getPoisonDamage();
			task = player.getSlayer().getTask();
			slayerAmount = player.getSlayer().getAmount();
			experience = player.getSkill().getExperience();
			skillsLevel = player.getSkill().getLevels();
			gender = player.getGender();
			appearance = (player.getAppearance().clone());
			colours = (player.getColors().clone());
			left = player.getSkulling().getLeft();
			skullIcon = player.getSkulling().getSkullIcon();
			spec = player.getSpecialAttack().getAmount();
			attackStyle = player.getEquipment().getAttackStyle();
			attackType = player.getAttackType();
			energy = player.getRunEnergy().getEnergy();
			votePoints = player.getVotePoints();
			totalVotes = player.getTotalVotes();
			teleblockTime = player.getTeleblockTime();
			summoningAttack = player.getSummoning().isAttack();
			summoningTime = player.getSummoning().getTime();
			summoningSpecialAmount = player.getSummoning().getSpecialAmount();
			summoningFamiliar = (player.getSummoning().getFamiliarData() != null
					? player.getSummoning().getFamiliarData().mob
					: -1);
			bankPin = player.getBankPinAttributes().getBankPin();
			hasPin = player.getBankPinAttributes().hasBankPin();
			lastPinAttempt = player.getBankPinAttributes().getLastAttempt();
			pinAttempts = player.getBankPinAttributes().getInvalidAttempts();
			logPackets = player.getClient().isLogPlayer();

			running = player.getRunEnergy().isRunning();

			achievements = player.getAchievements().getData();

			pestPoints = player.getPestPoints();

			skillPoints = player.getSkillPoints();

			soundVolume = player.getSoundVolume();

			patches = player.getFarming().getPatches();

			plants = player.getFarming().getPlants();

			lastVote = player.getVoteTime();

			kills = player.getKills();
			deaths = player.getDeaths();

			musicVolume = player.getMusicVolume();

			EP = player.getEP();

			dragonFireShieldCharges = player.getMagic().getDragonFireShieldCharges();

			duelWins = player.getDuelWins();

			duelLosses = player.getDuelLosses();

			host = player.getClient().getHost();

			degrading = player.getItemDegrading().getDegrading();

			slayerPoints = player.getSlayerPoints();

			slayerDifficulty = player.getSlayer().getDifficulty();

			runecraftingPoints = player.getRunecraftingPoints();

			donatorPoints = player.getDonatorPoints();

			displayKDR = player.displayKDR;

			if (player.getAttributes().get("recoilhits") != null)
				recoilStage = player.getAttributes().getInt("recoilhits");
			else {
				recoilStage = -1;
			}
			multipleMouse = player.getMultipleMouseButtons();
			chatEffects = player.getChatEffectsEnabled();
			splitPrivate = player.getSplitPrivateChat();
			acceptAid = player.getAcceptAid();
			dungPoints = player.getDungPoints();
			savedArrows = player.getRanged().getSavedArrows().getItems();
			questStages = player.getQuesting().getQuestStages();
			if (player.getQuesting().getActiveQuests().size() > 0) {
				activeQuests = new byte[player.getQuesting().getActiveQuests().size()];

				for (int i = 0; i < player.getQuesting().getActiveQuests().size(); i++)
					activeQuests[i] = ((byte) player.getQuesting().getActiveQuests().get(i).getId());
			} else {
				activeQuests = null;
			}
			tunnel = player.getMinigames().getTunnel();
			killstreak = player.getKillstreak();
			epTimer = player.epTimer;
			targetTimer = player.targetTimer;
			goldmember = player.goldmember;
			achievementpoints = player.achievementpoints;
			int k = 0;
			friends = new String[player.getPrivateMessaging().getFriends().size()];
			for (String i : player.getPrivateMessaging().getFriends()) {
				friends[k] = i;
				k++;
			}
			int l = 0;
			ignores = new String[player.getPrivateMessaging().getIgnores().size()];
			for (String i : player.getPrivateMessaging().getIgnores()) {
				ignores[l] = i;
				l++;
			}
		}

		public void parseDetails() throws Exception {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter("./data/characters/details/" + username + ".json", false));
				writer.write(PlayerSave.GSON.toJson(this));
				writer.flush();
			} finally {
				if (writer != null)
					writer.close();
			}
		}
	}

	public static final Gson GSON = new Gson();

	public static boolean load(Player p) throws Exception {
		if (!PlayerDetails.loadDetails(p)) {
			return false;
		}
		if (!PlayerContainer.loadDetails(p)) {
			return false;
		}
		return true;
	}

	public static final void save(Player p) {
		try {
			if (p.getController().equals(ControllerManager.TUTORIAL_CONTROLLER)) {
				return;
			} else {
				new PlayerDetails(p).parseDetails();
				new PlayerContainer(p).parseDetails(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
