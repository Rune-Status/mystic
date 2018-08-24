package org.mystic.game.model.entity.player;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.mystic.Engine;
import org.mystic.cache.map.Region;
import org.mystic.game.World;
import org.mystic.game.model.content.Achievements;
import org.mystic.game.model.content.Emotes;
import org.mystic.game.model.content.ItemDegrading;
import org.mystic.game.model.content.PrivateMessaging;
import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.RunePouch;
import org.mystic.game.model.content.TeleportationInterface.TeleportType;
import org.mystic.game.model.content.WelcomeScreen;
import org.mystic.game.model.content.bank.BankSecurity.BankPinAttributes;
import org.mystic.game.model.content.bank.Banking;
import org.mystic.game.model.content.book.Book;
import org.mystic.game.model.content.book.Page;
import org.mystic.game.model.content.clanchat.Clan;
import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.CombatInterface;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.PlayerCombatInterface;
import org.mystic.game.model.content.combat.impl.EarningPotential;
import org.mystic.game.model.content.combat.impl.Skulling;
import org.mystic.game.model.content.combat.impl.SpecialAttack;
import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.impl.Tutorial;
import org.mystic.game.model.content.minigames.PlayerMinigames;
import org.mystic.game.model.content.minigames.barrows.Barrows;
import org.mystic.game.model.content.minigames.castlewars.CastleWarsTeam;
import org.mystic.game.model.content.minigames.duelarena.Dueling;
import org.mystic.game.model.content.minigames.fightcave.FightCave;
import org.mystic.game.model.content.minigames.fightcave.WaveInformation;
import org.mystic.game.model.content.minigames.godwars.GodWarsData;
import org.mystic.game.model.content.minigames.godwars.GodWarsData.GodWarsNpc;
import org.mystic.game.model.content.pets.Pets;
import org.mystic.game.model.content.quest.Questing;
import org.mystic.game.model.content.shopping.PlayerOwnedShops;
import org.mystic.game.model.content.shopping.Shopping;
import org.mystic.game.model.content.skill.SkillManager;
import org.mystic.game.model.content.skill.agility.RunEnergy;
import org.mystic.game.model.content.skill.farming.Farming;
import org.mystic.game.model.content.skill.fishing.FishingAction;
import org.mystic.game.model.content.skill.magic.Autocast;
import org.mystic.game.model.content.skill.magic.MagicSkill;
import org.mystic.game.model.content.skill.melee.Melee;
import org.mystic.game.model.content.skill.prayer.PrayerBook;
import org.mystic.game.model.content.skill.prayer.PrayerConstants;
import org.mystic.game.model.content.skill.prayer.impl.CursesPrayerBook;
import org.mystic.game.model.content.skill.prayer.impl.DefaultPrayerBook;
import org.mystic.game.model.content.skill.ranged.RangedSkill;
import org.mystic.game.model.content.skill.slayer.Slayer;
import org.mystic.game.model.content.skill.summoning.Summoning;
import org.mystic.game.model.content.trading.Trade;
import org.mystic.game.model.content.wilderness.WildernessTargets;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.following.Following;
import org.mystic.game.model.entity.following.impl.PlayerFollowing;
import org.mystic.game.model.entity.item.impl.LocalGroundItems;
import org.mystic.game.model.entity.movement.MovementHandler;
import org.mystic.game.model.entity.movement.impl.PlayerMovementHandler;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.NpcConstants;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.LocalObjects;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;
import org.mystic.game.model.networking.outgoing.SendConfig;
import org.mystic.game.model.networking.outgoing.SendInventory;
import org.mystic.game.model.networking.outgoing.SendLoginResponse;
import org.mystic.game.model.networking.outgoing.SendLogout;
import org.mystic.game.model.networking.outgoing.SendMapRegion;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendPlayerOption;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendSidebarInterface;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.packet.impl.ChangeAppearancePacket;
import org.mystic.game.model.player.io.PlayerSave;
import org.mystic.game.model.player.io.PlayerSaveUtil;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.PlayerBroadcasts;
import org.mystic.mysql.Highscores;
import org.mystic.utility.Misc;
import org.mystic.utility.Misc.Stopwatch;
import org.mystic.utility.NameUtil;
import org.mystic.utility.TimeStamp;
import org.mystic.utility.TraversableList;

public class Player extends Entity {

	private final Client client;

	private Location currentRegion = new Location(0, 0, 0);

	private Book book;

	private TraversableList<Page> pages;

	private final List<Player> players = new LinkedList<Player>();

	private PlayerAnimations playerAnimations = new PlayerAnimations();

	private RunEnergy runEnergy = new RunEnergy(this);

	private MovementHandler movementHandler = new PlayerMovementHandler(this);

	private final CombatInterface combatInterface = new PlayerCombatInterface(this);

	private Following following = new PlayerFollowing(this);

	private PrivateMessaging privateMessaging = new PrivateMessaging(this);

	private Inventory inventory = new Inventory(this);

	private Banking bank = new Banking(this);

	private Trade trade = new Trade(this);

	private Shopping shopping = new Shopping(this);

	private Equipment equipment = new Equipment(this);

	private SpecialAttack specialAttack = new SpecialAttack(this);

	private LocalGroundItems groundItems = new LocalGroundItems(this);

	private final ItemDegrading degrading = new ItemDegrading();

	private SkillManager skill = new SkillManager(this);

	private MagicSkill magic = new MagicSkill(this);

	private RangedSkill ranged = new RangedSkill(this);

	private Melee melee = new Melee();

	private PrayerBook prayer;

	private FishingAction fishing = new FishingAction(this);

	private Slayer slayer = new Slayer(this);

	private Farming farming = new Farming(this);

	private final Summoning summoning = new Summoning(this);

	private final RunePouch runepouch = new RunePouch(this, 3);

	private Pets pets = new Pets(this);

	private Questing questing = new Questing(this);

	private PlayerOwnedShops playerShop = new PlayerOwnedShops(this);

	private final Achievements achievements = new Achievements();

	private Dialogue dialogue = null;

	private Skulling skulling = new Skulling();

	private LocalObjects objects = new LocalObjects(this);

	private Controller controller = ControllerManager.DEFAULT_CONTROLLER;

	private InterfaceManager interfaceManager = new InterfaceManager();

	private PlayerMinigames minigames = new PlayerMinigames(this);

	private Dueling dueling = new Dueling(this);

	private WaveInformation jadDetails = new WaveInformation();

	private EarningPotential earningPotential = new EarningPotential(this);

	private final BankPinAttributes bankPinAttributes = new BankPinAttributes();

	private Rights rights = Rights.PLAYER;

	private byte[] quickPrayersDefault = new byte[PrayerConstants.DEFAULT_NAMES.length];
	private byte[] quickPrayersCurses = new byte[CursesPrayerBook.CURSE_NAMES.length];

	private boolean starter = false;

	private String username;
	private String password;
	private long usernameToLong;

	public String playerTitle = getRights().getTitle();

	public boolean updateItems = false;

	public boolean teleporting = false;

	public boolean doingCape;

	private boolean visible = true;

	private int currentSongId = -1;

	private int chatColor;

	private int chatEffects;

	public int achievementpoints;

	private boolean openBank;

	private byte[] chatText;

	public TeleportType type;

	public TeleportType setType(TeleportType type2) {
		return type = type2;
	}

	public TeleportType getType() {
		return type;
	}

	public int duelWins;

	public int getDuelWins() {
		return duelWins;
	}

	public int duelLosses;

	public int getDuelLosses() {
		return duelLosses;
	}

	private final Stopwatch karambwanTimer = new Stopwatch();
	private final Stopwatch foodTimer = new Stopwatch();
	private final Stopwatch potionTimer = new Stopwatch();

	public Stopwatch getFoodTimer() {
		return foodTimer;
	}

	public Stopwatch getKarambwanTimer() {
		return karambwanTimer;
	}

	public Stopwatch getPotionTimer() {
		return potionTimer;
	}

	public boolean displayKDR = true;

	private DecimalFormat df = new DecimalFormat("#.#");

	public String getKDR() {
		double ratio = (double) getKills() / (double) getDeaths();
		if (getKills() == 0 && getDeaths() == 0) {
			return "0.0";
		} else if (getKills() == 1 && getDeaths() == 0) {
			return "1.0";
		} else if (getKills() == 0 && getDeaths() == 1) {
			return "0.0";
		}
		return df.format(ratio);
	}

	private Location previous;

	public Location getPrevious() {
		return previous;
	}

	public Location setPrevious(Location set) {
		return previous = set;
	}

	private TeleportType teleportation_type;

	public TeleportType getTeleportationType() {
		return teleportation_type;
	}

	public TeleportType setPreviousType(TeleportType type) {
		return teleportation_type = type;
	}

	private byte gender = 0;
	private int[] appearance = new int[7];
	private byte[] colors = new byte[5];
	private short npcAppearanceId = -1;
	private boolean appearanceUpdateRequired = false;
	private boolean chatUpdateRequired = false;
	private boolean needsPlacement = false;
	private boolean resetMovementQueue = false;

	private int screenBrightness = 4;

	private byte multipleMouseButtons = 0;

	private byte chatEffectsEnabled = 0;

	private int killstreak;

	private byte splitPrivateChat = 0;

	private byte acceptAid = 1;

	private long currentStunDelay;

	private GameObject interacting;

	public GameObject getInteractingObject() {
		return interacting;
	}

	private int donatorPoints;

	public int getDonatorPoints() {
		return donatorPoints;
	}

	public int setDonatorPoints(int set) {
		return donatorPoints = set;
	}

	private int runecraftingPoints;

	public int getRunecraftingPoints() {
		return runecraftingPoints;
	}

	public int setRunecraftingPoints(int set) {
		return runecraftingPoints += set;
	}

	/* Shopping variables */
	private String shopMotto;
	private String shopColor;
	private long shopCollection;

	public CastleWarsTeam team;

	public CastleWarsTeam getTeam() {
		return team;
	}

	public CastleWarsTeam setTeam(CastleWarsTeam team_set) {
		return team = team_set;
	}

	public GameObject setInteractingObject(GameObject acting) {
		return interacting = acting;
	}

	private long setStunDelay;
	private long lastAction = System.currentTimeMillis();
	private int enterXSlot = -1;
	private int enterXInterfaceId = -1;
	private int enterXItemId = 1;
	private boolean banned = false;
	private int banLength = 0;
	private int banDay = 0;

	/** Clan Chat */
	public Clan clan;
	public String lastClanChat = "";

	private int banYear = 0;
	private boolean muted = false;
	private int muteLength = 0;
	private int muteDay = 0;

	private int muteYear = 0;

	private int remainingMute = 0;

	private boolean yellMuted = false;

	public long timeout = 0L;

	public long aggressionDelay = System.currentTimeMillis();

	private int kills = 0;
	private int deaths = 0;

	private int prayerInterface;
	private int dungPoints = 0;

	private byte musicVolume = 3;

	private byte soundVolume = 3;
	private TimeStamp voteTime = null;

	private int totalVotes = 0;
	private int votePoints = 0;
	private int slayerPoints = 0;
	private int skillPoints = 0;
	private int pestPoints = 0;

	private byte[] pouches = new byte[4];

	public Player target;

	public int EP;

	public int targetTimer;

	public int epTimer;

	public boolean homeTeleporting;

	public String display;

	public boolean goldmember = false;

	public Player() {
		ChangeAppearancePacket.setToDefault(this);
		client = new Client(null);
		usernameToLong = 0L;
	}

	public Player(Client client) {
		this.client = client;
		this.getLocation().setAs(new Location(PlayerConstants.START));
		this.setNpc(false);
	}

	/**
	 * Adds the connecting user the the default clan chat channel
	 */
	public void addDefaultChannel() {
		if (clan == null) {
			Clan localClan = Engine.clanManager.getClan("Mystic");
			if (localClan != null) {
				localClan.addMember(this);
			}
		}
		return;
	}

	public void addDungPoints(int add) {
		dungPoints += add;
	}

	public void addSlayerPoints(int amount) {
		slayerPoints += amount;
	}

	public void addDonatorPoints(int amount) {
		donatorPoints += amount;
	}

	@Override
	public void afterCombatProcess(Entity attack) {
		combatInterface.afterCombatProcess(attack);
	}

	@Override
	public boolean canAttack() {
		return combatInterface.canAttack();
	}

	public boolean canSave() {
		return controller.canSave();
	}

	public void changeZ(int z) {
		getLocation().setZ(z);
		needsPlacement = true;
		objects.onRegionChange();
		groundItems.onRegionChange();
		getMovementHandler().reset();
		send(new SendMapRegion(this));
	}

	/**
	 * Access to the current page.
	 * 
	 * @return
	 */
	public Page currentPage() {
		return pages.current();
	}

	/**
	 * Access to the next page.
	 * 
	 * @return
	 */
	public Page nextPage() {
		return pages.next();
	}

	/**
	 * Access to the previous page.
	 * 
	 * @return
	 */
	public Page previousPage() {
		return pages.previous();
	}

	/**
	 * Flips the page of a book.
	 * 
	 * @param forward
	 */
	public void flip(boolean forward) {
		Page page = forward ? nextPage() : previousPage();
		if (page == null) {
			this.book = null;
			send(new SendRemoveInterfaces());
			return;
		}
		this.book.update(this, page);
	}

	/**
	 * When a player reads a book
	 * 
	 * @param book
	 */
	public void read(Book book) {
		book.open(this);
		this.pages = book.getPages();
		this.book = book;
	}

	@Override
	public void checkForDeath() {
		combatInterface.checkForDeath();
	}

	public void checkForRegionChange() {
		int deltaX = getLocation().getX() - getCurrentRegion().getRegionX() * 8;
		int deltaY = getLocation().getY() - getCurrentRegion().getRegionY() * 8;
		if ((deltaX < 16) || (deltaX >= 88) || (deltaY < 16) || (deltaY > 88)) {
			getClient().queueOutgoingPacket(new SendMapRegion(this));
		}
	}

	public void clearClanChat() {
		getClient().queueOutgoingPacket(new SendString("Talking in: ", 18139));
		getClient().queueOutgoingPacket(new SendString("Owner: ", 18140));
		for (int j = 18144; j < 18244; j++) {
			getClient().queueOutgoingPacket(new SendString("", j));
		}
	}

	public void doAgressionCheck() {
		if (!controller.canAttackNPC()) {
			return;
		}
		short[] override = new short[3];
		if ((getCombat().inCombat()) && (!inMultiArea())) {
			return;
		}
		if ((getCombat().inCombat()) && (getCombat().getLastAttackedBy().isNpc())) {
			Npc m = World.getNpcs()[getCombat().getLastAttackedBy().getIndex()];
			if (m != null) {
				if (m.getId() == 6260) {
					override[0] = 6261;
					override[1] = 6263;
					override[2] = 6265;
				} else if (m.getId() == 6203) {
					override[0] = 6204;
					override[1] = 6206;
					override[2] = 6208;
				} else if (m.getId() == 6222) {
					override[0] = 6227;
					override[1] = 6225;
					override[2] = 6223;
				} else if (m.getId() == 6247) {
					override[0] = 6250;
					override[1] = 6248;
					override[2] = 6252;
				}
			}
		}
		for (Npc i : getClient().getNpcs())
			if ((i.getCombat().getAttacking() == null) && (i.getCombatDefinition() != null)) {
				boolean overr = false;
				for (short j : override) {
					if ((short) i.getId() == j) {
						overr = true;
						break;
					}
				}
				if (overr && i.inWilderness()) {
					continue;
				}
				if (!overr && GodWarsData.forId(i.getId()) == null) {
					if (System.currentTimeMillis() - aggressionDelay >= 60000 * 8) {
						continue;
					}
				}
				if ((i.getLocation().getZ() == getLocation().getZ()) && (!i.isWalkToHome())) {
					if (getController().equals(ControllerManager.GOD_WARS_CONTROLLER)) {
						GodWarsNpc npc = GodWarsData.forId(i.getId());
						if (npc != null) {
							if (!GodWarsData.isProtected(this, npc) && !i.getCombat().inCombat()) {
								if (Math.abs(getLocation().getX() - i.getLocation().getX())
										+ Math.abs(getLocation().getY() - i.getLocation().getY()) <= 30) {
									i.getCombat().setAttack(this);
									i.getFollowing().setFollow(this, Following.FollowType.COMBAT);
								}
							}
						}

						continue;
					}
					if (NpcConstants.isAggressive(i.getId()) && (!i.getCombat().inCombat() || i.inMultiArea())) {
						if ((NpcConstants.isAgressiveFor(i, this))) {
							if ((overr) || (Math.abs(getLocation().getX() - i.getLocation().getX())
									+ Math.abs(getLocation().getY() - i.getLocation().getY()) <= i.getSize() * 2))
								i.getCombat().setAttack(this);
						}
					}
				}
			}
	}

	@Override
	public boolean equals(Object o) {
		if ((o instanceof Player)) {
			return ((Player) o).getUsernameToLong() == getUsernameToLong();
		}
		return false;
	}

	public byte getAcceptAid() {
		return acceptAid;
	}

	@Override
	public int getAccuracy(Entity attack, CombatTypes type) {
		return combatInterface.getAccuracy(attack, type);
	}

	public int getAchievementPoints() {
		return achievementpoints;
	}

	public Achievements getAchievements() {
		return achievements;
	}

	public PlayerAnimations getAnimations() {
		return playerAnimations;
	}

	public int[] getAppearance() {
		return appearance;
	}

	public int getBanDay() {
		return banDay;
	}

	public Banking getBank() {
		return bank;
	}

	public int getBanLength() {
		return banLength;
	}

	public int getBanYear() {
		return banYear;
	}

	public int getChatColor() {
		return chatColor;
	}

	public int getChatEffects() {
		return chatEffects;
	}

	public byte getChatEffectsEnabled() {
		return chatEffectsEnabled;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public Clan getClan() {
		if (Engine.clanManager.clanExists(getUsername())) {
			return Engine.clanManager.getClan(getUsername());
		}
		return null;
	}

	public Client getClient() {
		return client;
	}

	public byte[] getColors() {
		return colors;
	}

	public Controller getController() {
		if (controller == null) {
			setController(ControllerManager.DEFAULT_CONTROLLER);
		}
		return controller;
	}

	@Override
	public int getCorrectedDamage(int damage) {
		return combatInterface.getCorrectedDamage(damage);
	}

	public Location getCurrentRegion() {
		return currentRegion;
	}

	public int getCurrentSongId() {
		return currentSongId;
	}

	public long getCurrentStunDelay() {
		return currentStunDelay;
	}

	public int getDeaths() {
		return deaths;
	}

	public ItemDegrading getDegrading() {
		return degrading;
	}

	public Dialogue getDialogue() {
		return dialogue;
	}

	public String getDisplay() {
		return Misc.capitalizeFirstLetter(display);
	}

	public Dueling getDueling() {
		return dueling;
	}

	public int getDungPoints() {
		return dungPoints;
	}

	public EarningPotential getEarningPotential() {
		return earningPotential;
	}

	public int getEnterXInterfaceId() {
		return enterXInterfaceId;
	}

	public int getEnterXItemId() {
		return enterXItemId;
	}

	public int getEnterXSlot() {
		return enterXSlot;
	}

	public int getEP() {
		return EP;
	}

	public String getEPColor() {
		if (EP > 55) {
			return "@gre@";
		} else if (55 > EP) {
			return "@yel@";
		} else {
			return "@dre";
		}
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Questing getQuesting() {
		return questing;
	}

	public Farming getFarming() {
		return farming;
	}

	public FishingAction getFishing() {
		return fishing;
	}

	@Override
	public Following getFollowing() {
		return following;
	}

	public byte getGender() {
		return gender;
	}

	public LocalGroundItems getGroundItems() {
		return groundItems;
	}

	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public ItemDegrading getItemDegrading() {
		return degrading;
	}

	public WaveInformation getJadDetails() {
		return jadDetails;
	}

	public String getShopColor() {
		return shopColor;
	}

	public void setShopColor(String shopColor) {
		this.shopColor = shopColor;
	}

	public String getShopMotto() {
		return shopMotto;
	}

	public void setShopMotto(String shopMotto) {
		this.shopMotto = shopMotto;
	}

	public long getShopCollection() {
		return shopCollection;
	}

	public void setShopCollection(long shopCollection) {
		this.shopCollection = shopCollection;
	}

	public int getKills() {
		return kills;
	}

	public int getKillstreak() {
		return killstreak;
	}

	public long getLastAction() {
		return lastAction;
	}

	public MagicSkill getMagic() {
		return magic;
	}

	@Override
	public int getMaxHit(CombatTypes type) {
		return combatInterface.getMaxHit(type);
	}

	public Melee getMelee() {
		return melee;
	}

	public PlayerMinigames getMinigames() {
		return minigames;
	}

	@Override
	public MovementHandler getMovementHandler() {
		return movementHandler;
	}

	public byte getMultipleMouseButtons() {
		return multipleMouseButtons;
	}

	public byte getMusicVolume() {
		return musicVolume;
	}

	public int getMuteDay() {
		return muteDay;
	}

	public int getMuteLength() {
		return muteLength;
	}

	public int getMuteYear() {
		return muteYear;
	}

	public int getNpcAppearanceId() {
		return npcAppearanceId;
	}

	public String getPassword() {
		return password;
	}

	public int getPestPoints() {
		return pestPoints;
	}

	public Pets getPets() {
		return pets;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public PlayerOwnedShops getPlayerShop() {
		return playerShop;
	}

	public byte[] getPouches() {
		return pouches;
	}

	public PrayerBook getPrayer() {
		return prayer;
	}

	public int getPrayerInterface() {
		return prayerInterface;
	}

	public PrivateMessaging getPrivateMessaging() {
		return privateMessaging;
	}

	public byte[] getQuickPrayersCurses() {
		return quickPrayersCurses;
	}

	public byte[] getQuickPrayersDefault() {
		return quickPrayersDefault;
	}

	public RangedSkill getRanged() {
		return ranged;
	}

	public int getRemainingMute() {
		return remainingMute;
	}

	/**
	 * Gets the amount of authority this player has over others.
	 *
	 * @return the authority this player has.
	 */
	public Rights getRights() {
		return rights;
	}

	/**
	 * Sets the value for {@link Player#rights}.
	 *
	 * @param rights
	 *            the new value to set.
	 */
	public void setRights(Rights rights) {
		this.rights = rights;
	}

	public RunEnergy getRunEnergy() {
		return runEnergy;
	}

	public int getScreenBrightness() {
		return screenBrightness;
	}

	public long getSetStunDelay() {
		return setStunDelay;
	}

	public Shopping getShopping() {
		return shopping;
	}

	public SkillManager getSkill() {
		return skill;
	}

	public int getSkillPoints() {
		return skillPoints;
	}

	public Skulling getSkulling() {
		return skulling;
	}

	public Slayer getSlayer() {
		return slayer;
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}

	public byte getSoundVolume() {
		return soundVolume;
	}

	public SpecialAttack getSpecialAttack() {
		return specialAttack;
	}

	public byte getSplitPrivateChat() {
		return splitPrivateChat;
	}

	public Summoning getSummoning() {
		return summoning;
	}

	public Player getTarget() {
		return this.target;
	}

	public int getTotalVotes() {
		return totalVotes;
	}

	public Trade getTrade() {
		return trade;
	}

	public RunePouch getRunePouch() {
		return runepouch;
	}

	public LocalObjects getObjects() {
		return objects;
	}

	public String getUsername() {
		return username;
	}

	public long getUsernameToLong() {
		return usernameToLong;
	}

	public int getVotePoints() {
		return votePoints;
	}

	public TimeStamp getVoteTime() {
		return voteTime;
	}

	@Override
	public void hit(Hit hit) {
		combatInterface.hit(hit);
	}

	public boolean inBarrows() {
		if (this.getLocation().getX() > 3543 && this.getLocation().getX() < 3584 && this.getLocation().getY() > 3265
				&& this.getLocation().getY() < 3311) {

			return true;
		} else if (this.getLocation().getX() > 3529 && this.getLocation().getX() < 3581
				&& this.getLocation().getY() > 9673 && this.getLocation().getY() < 9722) {
			return true;
		}
		return false;
	}

	public boolean inBarrows2() {
		if (this.getLocation().getX() > 3529 && this.getLocation().getX() < 3581 && this.getLocation().getY() > 9673
				&& this.getLocation().getY() < 9722) {
			return true;
		}
		return false;
	}

	public void incrDeaths() {
		this.setDeaths(deaths + 1);
		QuestTab.update(this);
	}

	public void incrKills() {
		this.setKills(kills + 1);
		QuestTab.update(this);
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public boolean isBanned() {
		return banned;
	}

	public boolean isBusy() {
		return (interfaceManager.hasBankOpen()) || interfaceManager.hasInterfaceOpen() || (trade.trading())
				|| (dueling.isStaking());
	}

	public boolean isBusyNoInterfaceCheck() {
		return (interfaceManager.hasBankOpen()) || (trade.trading()) || (dueling.isStaking());
	}

	public boolean isChatUpdateRequired() {
		return chatUpdateRequired;
	}

	public boolean isDoingCape() {
		return doingCape;
	}

	public boolean isGoldMember() {
		return goldmember || this.getRights().equals(Rights.GOLD_MEMBER);
	}

	@Override
	public boolean isIgnoreHitSuccess() {
		return combatInterface.isIgnoreHitSuccess();
	}

	public boolean isMuted() {
		return muted;
	}

	public boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	public boolean isStarter() {
		return starter;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isYellMuted() {
		return yellMuted;
	}

	/**
	 * The main method of logging a player into the game
	 * 
	 * @param starter
	 *            determines if the player needs to do the starter or not
	 * @return if the login is successful or not
	 * @throws Exception
	 */
	public boolean login(boolean starter) throws Exception {
		this.starter = starter;
		username = NameUtil.uppercaseFirstLetter(username);
		usernameToLong = Misc.nameToLong(username.toLowerCase());
		int response = 2;
		if ((password.length() == 0) || (username.length() == 0) || (username.length() > 12)) {
			response = 3;
		} else if ((banned) || (PlayerSaveUtil.isIPBanned(this))) {
			response = 4;
		} else if ((password != null) && (!password.equals(client.getEnteredPassword()))) {
			response = 3;
		} else if (World.isUpdating()) {
			response = 14;
		} else if (World.getPlayerByName(username) != null) {
			response = 5;
		} else if (World.register(this) == -1) {
			response = 7;
		}
		if (response != 2) {
			StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
			resp.writeByte(response);
			resp.writeByte(rights.getProtocolValue());
			resp.writeByte(0);
			client.send(resp.getBuffer());
			return false;
		}
		new SendLoginResponse(response, this.getRights()).execute(client);
		if (PlayerSaveUtil.isIPMuted(this)) {
			setMuted(true);
			setRemainingMute(99999);
		}
		ControllerManager.setControllerOnWalk(this);
		if (Region.getRegion(getLocation().getX(), getLocation().getY()) == null) {
			teleport(new Location(PlayerConstants.HOME));
		}
		movementHandler.getLastLocation()
				.setAs(new Location(getLocation().getX(), getLocation().getY() + 1, getLocation().getZ()));
		for (int i = 0; i < PlayerConstants.SIDEBAR_INTERFACE_IDS.length; i++) {
			if (i != PlayerConstants.PRAYER_TAB && i != PlayerConstants.MAGIC_TAB) {
				client.queueOutgoingPacket(new SendSidebarInterface(i, PlayerConstants.SIDEBAR_INTERFACE_IDS[i]));
			}
		}
		if (magic.getMagicBook() == 0) {
			magic.setMagicBook(1151);
		}
		prayerInterface = 5608;
		prayer = new DefaultPrayerBook(this);
		client.queueOutgoingPacket(new SendSidebarInterface(PlayerConstants.PRAYER_TAB, prayerInterface));
		if (!ChangeAppearancePacket.validate(this)) {
			ChangeAppearancePacket.setToDefault(this);
		}

		equipment.onLogin();
		skill.onLogin();
		magic.onLogin();
		setScreenBrightness(getScreenBrightness());
		privateMessaging.connect();
		runEnergy.update();
		prayer.disable();
		this.getRunEnergy().setRunning(true);
		this.getClient().queueOutgoingPacket(new SendConfig(173, 1));
		Autocast.resetAutoCastInterface(this);
		achievements.onLogin(this);
		Emotes.onLogin(this);
		QuestTab.update(this);
		inventory.update();

		client.queueOutgoingPacket(new SendPlayerOption("Follow", 1));
		client.queueOutgoingPacket(new SendPlayerOption("Trade with", 2));
		client.queueOutgoingPacket(new SendConfig(166, screenBrightness));
		client.queueOutgoingPacket(new SendConfig(171, multipleMouseButtons));
		client.queueOutgoingPacket(new SendConfig(172, chatEffectsEnabled));
		client.queueOutgoingPacket(new SendConfig(287, splitPrivateChat));
		client.queueOutgoingPacket(new SendConfig(427, acceptAid));
		client.queueOutgoingPacket(new SendConfig(172, isRetaliate() ? 1 : 0));
		client.queueOutgoingPacket(new SendConfig(429, runEnergy.isRunning() ? 1 : 0));
		client.queueOutgoingPacket(new SendConfig(168, musicVolume));
		client.queueOutgoingPacket(new SendConfig(169, soundVolume));
		client.queueOutgoingPacket(new SendConfig(876, 0));
		client.queueOutgoingPacket(new SendConfig(77, 0));

		// TODO: remove after fixed
		getRunEnergy().setEnergy(100);

		getUpdateFlags().setUpdateRequired(true);
		appearanceUpdateRequired = true;
		needsPlacement = true;

		if (getMinigames().getTunnel() < 1) {
			Barrows.assignTunnel(this);
		}
		questing.onLogin();
		controller.onControllerInit(this);
		playerShop.setName(username + "'s Personal Shop");
		if (prayer instanceof DefaultPrayerBook) {
			for (int i = 0; i < getQuickPrayersDefault().length; i++) {
				send(new SendConfig(630 + i, getQuickPrayersDefault()[i]));
			}
		} else {
			for (int i = 0; i < getQuickPrayersCurses().length; i++) {
				send(new SendConfig(630 + i, getQuickPrayersCurses()[i]));
			}
		}
		client.queueOutgoingPacket(new SendMessage(PlayerConstants.LOGIN_MESSAGE));
		clearClanChat();
		setClanData();
		if (lastClanChat != null && lastClanChat.length() > 0) {
			Clan clan = Engine.clanManager.getClan(lastClanChat);
			if (clan != null) {
				clan.addMember(this);
			}
		} else {
			addDefaultChannel();
		}
		if (starter || !this.getAchievements().isCompleted("Learning the ropes")) {
			this.start(new Tutorial(this));
		} else {
			WelcomeScreen.send(this);
		}
		QuestTab.update(this);
		if (!this.getBankPinAttributes().hasBankPin() && !starter) {
			client.queueOutgoingPacket(
					new SendMessage("@dre@You don't currently have a bank pin, please consider setting one!"));
		}
		return true;
	}

	public void logout(boolean force) {
		if (isActive()) {
			if ((pets.hasPet()) && (!pets.remove()) && (!force)) {
				return;
			}
			if (force) {
				ControllerManager.onForceLogout(this);
			} else if ((controller != null) && (!controller.canLogOut())) {
				return;
			}
			World.remove(client.getNpcs());
			if (controller != null) {
				controller.onDisconnect(this);
			}
			if (trade.trading()) {
				trade.end(false);
			}
			if (clan != null) {
				clan.removeMember(getUsername());
			}
			if (getTarget() != null) {
				WildernessTargets.resetTarget(this, getTarget(), true);
			}
			if (dueling.isStaking()) {
				dueling.decline();
			}
			if (summoning.hasFamiliar()) {
				summoning.removeForLogout();
			}
			new Thread(new Highscores(this)).start();
			PlayerSave.save(this);
		}
		System.out.println("Player (" + getUsername() + ")" + " has logged out.");
		World.unregister(this);
		client.setStage(Client.Stages.LOGGED_OUT);
		setActive(false);
		new SendLogout().execute(client);
		client.disconnect();
	}

	public boolean needsPlacement() {
		return needsPlacement;
	}

	@Override
	public void onAttack(Entity attack, int hit, CombatTypes type, boolean success) {
		combatInterface.onAttack(attack, hit, type, success);
	}

	@Override
	public void onCombatProcess(Entity attack) {
		combatInterface.onCombatProcess(attack);
	}

	public void onControllerFinish() {
		controller = ControllerManager.DEFAULT_CONTROLLER;
	}

	@Override
	public void onHit(Entity e, Hit hit) {
		combatInterface.onHit(e, hit);
	}

	@Override
	public void poison(int start) {
		if (isPoisoned()) {
			return;
		}
		super.poison(start);
		if (isActive()) {
			client.queueOutgoingPacket(new SendMessage("You have been poisoned!"));
		}
	}

	@Override
	public void process() throws Exception {
		if (Math.abs(World.getCycles() - client.getLastPacketTime()) >= 9) {
			if (getCombat().inCombat() && !getCombat().getLastAttackedBy().isNpc()) {
				if (timeout == 0) {
					timeout = System.currentTimeMillis() + 180000;
				} else if (timeout <= System.currentTimeMillis() || !getCombat().inCombat()) {
					logout(false);
				}
			} else {
				logout(false);
			}
		}
		if (updateItems) {
			this.getClient().queueOutgoingPacket(new SendInventory(this.getInventory().getItems()));
		}
		if (controller != null) {
			controller.tick(this);
		}
		shopping.update();
		prayer.drain();
		following.process();
		getCombat().process();
		doAgressionCheck();
		if (getRunEnergy().getEnergy() <= 0) {
			runEnergy.tick();
		}
	}

	@Override
	public void reset() {
		following.updateWaypoint();
		appearanceUpdateRequired = false;
		chatUpdateRequired = false;
		resetMovementQueue = false;
		needsPlacement = false;
		getMovementHandler().resetMoveDirections();
		getUpdateFlags().setUpdateRequired(false);
		getUpdateFlags().reset();
	}

	public void resetAggression() {
		aggressionDelay = System.currentTimeMillis();
	}

	@Override
	public void retaliate(Entity attacked) {
		if (attacked != null) {
			if (isRetaliate() && getCombat().getAttacking() == null && !getMovementHandler().moving()) {
				getCombat().setAttack(attacked);
			}
		}
	}

	public void send(OutgoingPacket o) {
		client.queueOutgoingPacket(o);
	}

	public void sendClientRightClickRemoval() {
		send(new SendString("[CLOSEMENU]", 0));
	}

	public void setAcceptAid(byte acceptAid) {
		this.acceptAid = acceptAid;
	}

	public void setAppearance(int[] appearance) {
		this.appearance = appearance;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		if (appearanceUpdateRequired) {
			getUpdateFlags().setUpdateRequired(true);
		}
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public void setBanDay(int banDay) {
		this.banDay = banDay;
	}

	public void setBanLength(int banLength) {
		this.banLength = banLength;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public void setBanYear(int banYear) {
		this.banYear = banYear;
	}

	public void setChatColor(int chatColor) {
		this.chatColor = chatColor;
	}

	public void setChatEffects(int chatEffects) {
		this.chatEffects = chatEffects;
	}

	public void setChatEffectsEnabled(byte chatEffectsEnabled) {
		this.chatEffectsEnabled = chatEffectsEnabled;
	}

	public void setChatText(byte[] chatText) {
		this.chatText = chatText;
	}

	public void setChatUpdateRequired(boolean chatUpdateRequired) {
		if (chatUpdateRequired) {
			getUpdateFlags().setUpdateRequired(true);
		}
		this.chatUpdateRequired = chatUpdateRequired;
	}

	public void setClanData() {
		boolean exists = Engine.clanManager.clanExists(getUsername());
		if (!exists || clan == null) {
			getClient().queueOutgoingPacket(new SendString("Join chat", 18135));
			getClient().queueOutgoingPacket(new SendString("Talking in: Not in chat", 18139));
			getClient().queueOutgoingPacket(new SendString("Owner: None", 18140));
		}
		if (!exists) {
			getClient().queueOutgoingPacket(new SendString("Chat Disabled", 18306));
			String title = "";
			for (int id = 18307; id < 18317; id += 3) {
				if (id == 18307) {
					title = "Anyone";
				} else if (id == 18310) {
					title = "Anyone";
				} else if (id == 18313) {
					title = "General+";
				} else if (id == 18316) {
					title = "Only Me";
				}
				getClient().queueOutgoingPacket(new SendString(title, id + 2));
			}
			for (int index = 0; index < 100; index++) {
				getClient().queueOutgoingPacket(new SendString("", 18323 + index));
			}
			for (int index = 0; index < 100; index++) {
				getClient().queueOutgoingPacket(new SendString("", 18424 + index));
			}
			return;
		}
		Clan clan = Engine.clanManager.getClan(getUsername());
		getClient().queueOutgoingPacket(new SendString(clan.getTitle(), 18306));
		String title = "";
		for (int id = 18307; id < 18317; id += 3) {
			if (id == 18307) {
				title = clan.getRankTitle(clan.whoCanJoin)
						+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18310) {
				title = clan.getRankTitle(clan.whoCanTalk)
						+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18313) {
				title = clan.getRankTitle(clan.whoCanKick)
						+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18316) {
				title = clan.getRankTitle(clan.whoCanBan)
						+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
			}
			getClient().queueOutgoingPacket(new SendString(title, id + 2));
		}
		if (clan.rankedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.rankedMembers.size()) {
					getClient().queueOutgoingPacket(new SendString(
							"<clan=" + clan.ranks.get(index) + ">" + clan.rankedMembers.get(index), 18323 + index));
				} else {
					getClient().queueOutgoingPacket(new SendString("", 18323 + index));
				}
			}
		}
		if (clan.bannedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.bannedMembers.size()) {
					getClient().queueOutgoingPacket(new SendString(clan.bannedMembers.get(index), 18424 + index));
				} else {
					getClient().queueOutgoingPacket(new SendString("", 18424 + index));
				}
			}
		}
	}

	public void setColors(byte[] colors) {
		this.colors = colors;
	}

	public boolean setController(Controller controller) {
		this.controller = controller;
		controller.onControllerInit(this);
		return true;
	}

	public boolean setControllerNoInit(Controller controller) {
		this.controller = controller;
		return true;
	}

	public void setCurrentRegion(Location currentRegion) {
		this.currentRegion = currentRegion;
	}

	public void setCurrentSongId(int currentSongId) {
		this.currentSongId = currentSongId;
	}

	public void setCurrentStunDelay(long delay) {
		currentStunDelay = delay;
	}

	public int setDeaths(int deaths) {
		return this.deaths = deaths;
	}

	public void setDialogue(Dialogue d) {
		dialogue = d;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public boolean setDoingCape(boolean cape) {
		return doingCape = cape;
	}

	public void setDungPoints(int dungPoints) {
		this.dungPoints = dungPoints;
	}

	public void setEnterXInterfaceId(int enterXInterfaceId) {
		this.enterXInterfaceId = enterXInterfaceId;
	}

	public void setEnterXItemId(int enterXItemId) {
		this.enterXItemId = enterXItemId;
	}

	public void setEnterXSlot(int enterXSlot) {
		this.enterXSlot = enterXSlot;
	}

	public int setEP(int value) {
		return EP = value;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public boolean setGoldMember(boolean bool) {
		return goldmember = bool;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int setKillstreak(int value) {
		return killstreak = value;
	}

	public void setLastAction(long lastAction) {
		this.lastAction = lastAction;
	}

	public void setMultipleMouseButtons(byte multipleMouseButtons) {
		this.multipleMouseButtons = multipleMouseButtons;
	}

	public void setMusicVolume(byte musicVolume) {
		this.musicVolume = musicVolume;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public void setMuteDay(int muteDay) {
		this.muteDay = muteDay;
	}

	public void setMuteLength(int muteLength) {
		this.muteLength = muteLength;
	}

	public void setMuteYear(int muteYear) {
		this.muteYear = muteYear;
	}

	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	public void setNpcAppearanceId(short npcAppearanceId) {
		this.npcAppearanceId = npcAppearanceId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}

	public void setPouches(byte[] pouches) {
		this.pouches = pouches;
	}

	public void setPrayer(PrayerBook prayer) {
		this.prayer = prayer;
	}

	public void setPrayerInterface(int prayerInterface) {
		this.prayerInterface = prayerInterface;
	}

	public void setQuickPrayersCurses(byte[] quickPrayersCurses) {
		this.quickPrayersCurses = quickPrayersCurses;
	}

	public void setQuickPrayersDefault(byte[] quickPrayersDefault) {
		this.quickPrayersDefault = quickPrayersDefault;
	}

	public void setRemainingMute(int remainingMute) {
		this.remainingMute = remainingMute;
	}

	public void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	public void setScreenBrightness(int screenBrightness) {
		this.screenBrightness = screenBrightness;
	}

	public void setSetStunDelay(long delay) {
		setStunDelay = delay;
	}

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
	}

	public void setSlayerPoints(int slayerPoints) {
		this.slayerPoints = slayerPoints;
	}

	public void setSoundVolume(byte soundVolume) {
		this.soundVolume = soundVolume;
	}

	public void setSplitPrivateChat(byte splitPrivateChat) {
		this.splitPrivateChat = splitPrivateChat;
	}

	public void setStarter(boolean starter) {
		this.starter = starter;
	}

	public void setTarget(Player target) {
		this.target = target;
	}

	public void setTotalVotes(int totalVotes) {
		this.totalVotes = totalVotes;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setVotePoints(int votePoints) {
		this.votePoints = votePoints;
	}

	public void setVoteTime(TimeStamp voteTime) {
		this.voteTime = voteTime;
	}

	public BankPinAttributes getBankPinAttributes() {
		return bankPinAttributes;
	}

	public boolean openBank() {
		return openBank;
	}

	public void setOpenBank(boolean openBank) {
		this.openBank = openBank;
	}

	public void setYellMuted(boolean yellMuted) {
		this.yellMuted = yellMuted;
	}

	public void start() {
		runEnergy.tick();
		startRegeneration();
		specialAttack.tick();
		farming.init();
		summoning.onLogin();
		skulling.tick(this);
		if (jadDetails.getStage() != 0) {
			FightCave.loadGame(this);
		}
		final Player player = this;
		TaskQueue.queue(new Task(player, 500) {
			@Override
			public void execute() {
				if (canSave()) {
					PlayerSave.save(player);
				}
			}

			@Override
			public void onStop() {
			}
		});
		TaskQueue.queue(new PlayerBroadcasts(player, 400));
		if (getTeleblockTime() > 0) {
			tickTeleblock();
		}
	}

	public void start(Dialogue dialogue) {
		this.dialogue = dialogue;
		if (dialogue != null) {
			dialogue.setNext(0);
			dialogue.setPlayer(this);
			dialogue.execute();
		} else if (getAttributes().get("pauserandom") != null) {
			getAttributes().remove("pauserandom");
		}
	}

	@Override
	public void teleblock(int i) {
		achievements.incr(this, "Get tele-blocked");
		super.teleblock(i);
	}

	public void teleport(Location location) {
		boolean zChange = location.getZ() != getLocation().getZ();
		getLocation().setAs(location);
		setResetMovementQueue(true);
		setNeedsPlacement(true);
		movementHandler.getLastLocation().setAs(new Location(getLocation().getX(), getLocation().getY() + 1));
		send(new SendRemoveInterfaces());
		if (!controller.equals(ControllerManager.DUEL_STAKE_CONTROLLER)) {
			client.queueOutgoingPacket(new SendPlayerOption("null", 4));
		}
		ControllerManager.setControllerOnWalk(this);
		TaskQueue.cancelHitsOnEntity(this);
		movementHandler.reset();
		if (zChange) {
			client.queueOutgoingPacket(new SendMapRegion(this));
		} else {
			checkForRegionChange();
		}
		if (trade.trading()) {
			trade.end(false);
		} else if (dueling.isStaking()) {
			dueling.decline();
		}
		TaskQueue.onMovement(this);
	}

	public void teleportWithDamage(Location location) {
		boolean zChange = location.getZ() != getLocation().getZ();
		getLocation().setAs(location);
		setResetMovementQueue(true);
		setNeedsPlacement(true);
		movementHandler.getLastLocation().setAs(new Location(getLocation().getX(), getLocation().getY() + 1));
		movementHandler.reset();
		if (zChange) {
			client.queueOutgoingPacket(new SendMapRegion(this));
		} else {
			checkForRegionChange();
		}
		client.queueOutgoingPacket(new SendRemoveInterfaces());
	}

	@Override
	public String toString() {
		return "Player(" + getUsername() + ":" + getPassword() + " - " + client.getHost() + ")";
	}

	@Override
	public void updateCombatType() {
		final CombatTypes type;
		if (magic.getSpellCasting().isCastingSpell())
			type = CombatTypes.MAGIC;
		else {
			type = EquipmentConstants.getCombatTypeForWeapon(this);
		}
		if (type != CombatTypes.MAGIC || !magic.getSpellCasting().isAutocasting()) {
			send(new SendConfig(333, 0));
		}
		getCombat().setCombatType(type);
		switch (type) {
		case MELEE:
			equipment.updateMeleeDataForCombat();
			break;
		case RANGED:
			equipment.updateRangedDataForCombat();
			break;
		default:
			break;
		}
	}

	public boolean withinRegion(Location other) {
		int deltaX = other.getX() - currentRegion.getRegionX() * 8;
		int deltaY = other.getY() - currentRegion.getRegionY() * 8;
		if ((deltaX < 2) || (deltaX > 110) || (deltaY < 2) || (deltaY > 110)) {
			return false;
		}
		return true;
	}

}