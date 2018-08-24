package org.mystic.game.model.content.minigames.duelarena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mystic.game.World;
import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.ItemCheck;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendConfig;
import org.mystic.game.model.networking.outgoing.SendDuelEquipment;
import org.mystic.game.model.networking.outgoing.SendInventoryInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendPlayerHint;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendUpdateItems;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class Dueling {

	public enum DuelingStatuses {
		WAITING,

		SCREEN_1,

		SCREEN_1_ACCEPTED,

		SCREEN_2,

		SCREEN_2_ACCEPTED,

		DUELING;
	}

	public void beginDuel(final Player p1, final Player p2) {
		boolean obstacles = p1.getDueling().getRuleToggle()[8];
		boolean noMovement = p1.getDueling().getRuleToggle()[1];
		int arena = DuelingManager.getDuelArenaId(obstacles);

		p1.getDueling().setArenaId(arena);
		p2.getDueling().setArenaId(arena);

		p1.setController(ControllerManager.DUELING_CONTROLLER);
		p2.setController(ControllerManager.DUELING_CONTROLLER);

		p1.getDueling().setStatus(DuelingStatuses.DUELING);
		p2.getDueling().setStatus(DuelingStatuses.DUELING);

		p1.getDueling().removeEquipmentForDuel();
		p2.getDueling().removeEquipmentForDuel();

		p1.resetCombatStats();
		p2.resetCombatStats();

		p1.getPrayer().disable();
		p2.getPrayer().disable();

		p1.getSpecialAttack().setSpecialAmount(100);
		p2.getSpecialAttack().setSpecialAmount(100);

		p1.getSpecialAttack().update();
		p2.getSpecialAttack().update();

		p1.getRunEnergy().restoreAll();
		p2.getRunEnergy().restoreAll();

		p1.getMagic().setVengeanceActive(false);
		p2.getMagic().setVengeanceActive(false);

		if (p1.getSpecialAttack().isInitialized()) {
			p1.getSpecialAttack().toggleSpecial();
		}

		if (p2.getSpecialAttack().isInitialized()) {
			p2.getSpecialAttack().toggleSpecial();
		}

		if (noMovement) {
			Location p = DuelingManager.getCoordinates(arena, obstacles, false, true);
			p1.teleport(p);
			p.move(1, 0);
			p2.teleport(p);
		} else {
			p1.teleport(DuelingManager.getCoordinates(arena, obstacles, false, false));
			p2.teleport(DuelingManager.getCoordinates(arena, obstacles, true, false));
		}

		p1.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		p2.getClient().queueOutgoingPacket(new SendRemoveInterfaces());

		p1.getClient().queueOutgoingPacket(new SendPlayerHint(true, p2.getIndex()));
		p2.getClient().queueOutgoingPacket(new SendPlayerHint(true, p1.getIndex()));

		TaskQueue.queue(new Task(2) {

			int time = 5;

			@Override
			public void execute() {
				if ((--time > 0) && (time <= 3)) {
					p1.getUpdateFlags().sendForceMessage("" + time);
					p2.getUpdateFlags().sendForceMessage("" + time);
				} else if (time == 0) {
					p1.getUpdateFlags().sendForceMessage("FIGHT!");
					p2.getUpdateFlags().sendForceMessage("FIGHT!");
					p1.getDueling().setCanAttack(true);
					p2.getDueling().setCanAttack(true);
					stop();
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	public void beginStake(Player p1, Player p2) {

		p1.setController(ControllerManager.DUEL_STAKE_CONTROLLER);
		p2.setController(ControllerManager.DUEL_STAKE_CONTROLLER);

		final Dueling d1 = p1.getDueling();
		final Dueling d2 = p2.getDueling();

		d1.setDuelingWith(p2);
		d2.setDuelingWith(p1);
		d1.resetForStake();
		d2.resetForStake();
		d1.setStatus(DuelingStatuses.SCREEN_1);
		d2.setStatus(DuelingStatuses.SCREEN_1);

		p1.getClient().queueOutgoingPacket(new SendString("Whips only", 669));
		p1.getClient().queueOutgoingPacket(new SendString("Whip & Dagger only", 6696));

		p2.getClient().queueOutgoingPacket(new SendString("Whips only", 669));
		p2.getClient().queueOutgoingPacket(new SendString("Whips & Daggers only", 6696));

		sendDuelEquipment(p1);
		sendDuelEquipment(p2);

		p1.getClient().queueOutgoingPacket(new SendConfig(286, 0));
		p2.getClient().queueOutgoingPacket(new SendConfig(286, 0));

		p1.getClient()
				.queueOutgoingPacket(new SendString("Dueling with: " + p2.getUsername()
						+ "                                  Combat Level: @yel@" + p2.getSkill().getCombatLevel(),
						6671));
		p2.getClient()
				.queueOutgoingPacket(new SendString("Dueling with: " + p1.getUsername()
						+ "                                  Combat Level: @yel@" + p1.getSkill().getCombatLevel(),
						6671));

		p1.getClient().queueOutgoingPacket(new SendString("", 6684));
		p2.getClient().queueOutgoingPacket(new SendString("", 6684));

		p1.getClient().queueOutgoingPacket(new SendInventoryInterface(6575, 3321));
		p2.getClient().queueOutgoingPacket(new SendInventoryInterface(6575, 3321));

		d1.getContainer().update();
		d2.getContainer().update();
	}

	public Item[] buildItemArray(Player p1, Player p2, boolean[] toRemove) {
		final List<Item> items = new ArrayList<Item>();
		final Item[] equip = p1.getEquipment().getItems();
		final Item[] stake = p1.getDueling().getContainer().getItems();
		final Item[] oStake = p2.getDueling().getContainer().getItems();
		for (int i = 0; i < toRemove.length; i++) {
			if ((toRemove[i]) && (equip[i] != null)) {
				items.add(equip[i]);
			}
		}
		for (int i = 0; i < stake.length; i++) {
			if ((stake[i] == null) && (oStake[i] == null)) {
				break;
			}
			if (stake[i] != null) {
				items.add(stake[i]);
			}

			if (oStake[i] != null) {
				items.add(oStake[i]);
			}
		}
		final Item[] ret = new Item[items.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (items.get(i));
		}
		return ret;
	}

	public void onDecline(Player p1, Player p2) {
		if (p1 != null) {
			for (Item i : p1.getDueling().getContainer().getItems()) {
				if (i != null) {
					p1.getInventory().add(i);
				}
			}

			p1.getDueling().getContainer().clear();
			p1.getInventory().update();
		}

		if (p2 != null) {
			for (Item i : p2.getDueling().getContainer().getItems()) {
				if (i != null) {
					p2.getInventory().add(i);
				}
			}

			p2.getDueling().getContainer().clear();
			p2.getInventory().update();
		}

		if (p1 != null) {
			p1.setController(ControllerManager.DUEL_ARENA_CONTROLLER);
		}

		if (p2 != null) {
			p2.setController(ControllerManager.DUEL_ARENA_CONTROLLER);
		}
	}

	public void sendDuelEquipment(Player p) {
		Item[] equ = p.getEquipment().getItems();
		for (int i = 0; i < equ.length; i++)
			if (equ[i] == null)
				p.getClient().queueOutgoingPacket(new SendDuelEquipment(0, 0, i));
			else
				p.getClient().queueOutgoingPacket(new SendDuelEquipment(equ[i], i));
	}

	public void sendScreen2(Player p1, Player p2) {

		p1.getDueling().setStatus(DuelingStatuses.SCREEN_2);
		p2.getDueling().setStatus(DuelingStatuses.SCREEN_2);

		final String[] before = DuelingConstants.getBeforeDuelStringUpdates(p1.getDueling().getRuleToggle());
		final String[] during = DuelingConstants.getDuringDuelStringUpdates(p1.getDueling().getRuleToggle());

		final String stake1 = DuelingConstants.getStakedItemsToString(p1.getDueling().getContainer().getItems(),
				p1.getDueling().getContainer().getSize() - p1.getDueling().getContainer().getFreeSlots());
		final String stake2 = DuelingConstants.getStakedItemsToString(p2.getDueling().getContainer().getItems(),
				p2.getDueling().getContainer().getSize() - p2.getDueling().getContainer().getFreeSlots());

		for (int i = 0; i < DuelingConstants.BEFORE_THE_DUEL_STRING_IDS.length; i++) {
			String s = before[i] == null ? "" : before[i];
			p1.getClient().queueOutgoingPacket(new SendString(s, DuelingConstants.BEFORE_THE_DUEL_STRING_IDS[i]));
			p2.getClient().queueOutgoingPacket(new SendString(s, DuelingConstants.BEFORE_THE_DUEL_STRING_IDS[i]));
		}

		for (int i = 0; i < DuelingConstants.DURING_THE_DUEL_STRING_IDS.length; i++) {
			String s = during[i] == null ? "" : during[i];
			p1.getClient().queueOutgoingPacket(new SendString(s, DuelingConstants.DURING_THE_DUEL_STRING_IDS[i]));
			p2.getClient().queueOutgoingPacket(new SendString(s, DuelingConstants.DURING_THE_DUEL_STRING_IDS[i]));
		}

		p1.getClient().queueOutgoingPacket(new SendString(stake1, 6516));
		p1.getClient().queueOutgoingPacket(new SendString(stake2, 6517));

		p2.getClient().queueOutgoingPacket(new SendString(stake2, 6516));
		p2.getClient().queueOutgoingPacket(new SendString(stake1, 6517));

		p1.getClient().queueOutgoingPacket(new SendString("", 6571));
		p2.getClient().queueOutgoingPacket(new SendString("", 6571));

		p1.getClient().queueOutgoingPacket(new SendInventoryInterface(6412, 3321));
		p2.getClient().queueOutgoingPacket(new SendInventoryInterface(6412, 3321));
	}

	private final Player player;

	private Player lastRequest = null;

	private Player interacting = null;

	private int rules;

	private boolean[] ruleToggle = null;

	private boolean[] toRemove = null;

	private StakingContainer container;

	private DuelingStatuses s = DuelingStatuses.WAITING;

	private int arenaId = -1;

	private boolean attack = false;

	public Dueling(Player player) {
		this.player = player;
		container = new StakingContainer(player);
	}

	public void accept() {
		switch (s) {

		case SCREEN_1:
			if ((canAccept()) && (interacting != null)) {
				if (interacting.getDueling().hasAccepted()) {
					sendScreen2(player, interacting);
				} else {
					s = DuelingStatuses.SCREEN_1_ACCEPTED;
					player.getClient().queueOutgoingPacket(new SendString("Waiting for other player..", 6684));
					interacting.getClient().queueOutgoingPacket(new SendString("Other player has accepted.", 6684));
				}
			}

			break;

		case SCREEN_2:
			if (interacting != null) {
				if (interacting.getDueling().hasAccepted()) {
					beginDuel(player, interacting);
				} else {
					s = DuelingStatuses.SCREEN_2_ACCEPTED;
					player.getClient().queueOutgoingPacket(new SendString("Waiting for other player..", 6571));
					interacting.getClient().queueOutgoingPacket(new SendString("Other player has accepted.", 6571));
				}
			}
			break;

		default:
			break;
		}
	}

	public void appendToRemove(int slot, boolean remove) {
		toRemove[slot] = remove;
	}

	public boolean canAccept() {
		if (interacting == null) {
			return false;
		}
		if (player.getSummoning().hasFamiliar() || player.getPets().hasPet()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You must dismiss your familiar to duel."));
			return false;
		}
		if ((ruleToggle[3]) && (ruleToggle[2]) && (ruleToggle[4])) {
			player.getClient().queueOutgoingPacket(new SendMessage("You must enable one combat type to duel!"));
			return false;
		}
		if ((ruleToggle[1]) && (ruleToggle[8])) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You can only select either obstacles or no movement, not both."));
			return false;
		}
		if ((ruleToggle[0]) && (ruleToggle[9])) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't have both Whip combat options toggled."));
			return false;
		}
		if (!player.getSkill().locked()) {
			player.getSkill().lock(1);
		} else {
			return false;
		}
		if (!player.getInventory().hasSpaceFor(buildItemArray(player, interacting, toRemove))) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You do not have enough inventory space to accept this duel."));
			return false;
		}
		return true;
	}

	public boolean canAppendStake() {
		return (s == DuelingStatuses.SCREEN_1) || (s == DuelingStatuses.SCREEN_1_ACCEPTED);
	}

	public boolean canAttack() {
		return attack;
	}

	public boolean canUseWeapon() {
		final Item wep = player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT];
		if (wep != null) {
			if (ruleToggle[0]) {
				if ((wep.getId() != 12006) && (wep.getId() != 4151) && (!ItemCheck.isItemDyed(wep))
						&& (wep.getId() != 5698) && (wep.getId() != 1215)) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You can only use Whips and Dragon daggers during this duel."));
					return false;
				}
			} else if (ruleToggle[9]) {
				if (!wep.getDefinition().getName().toLowerCase().contains("whip")) {
					player.getClient()
							.queueOutgoingPacket(new SendMessage("You can only use a Whip during this duel."));
					return false;
				}
			}
		}
		return true;
	}

	public boolean clickForfeitTrapDoor(int id) {
		if (id == 3203) {
			if (ruleToggle == null) {
				return true;
			}
			if (interacting.getLevels()[3] <= 0 || player.getLevels()[3] <= 0 || interacting.isDead()
					|| player.isDead()) {
				return true;
			}
			if (ruleToggle[0]) {
				player.getClient().queueOutgoingPacket(new SendMessage("You are not allowed to forfeit this duel."));
				return true;
			}
			player.start(new DuelArenaForfeit(player));
		}
		return false;
	}

	public void decline() {
		onDecline(player, interacting);
		player.getClient().queueOutgoingPacket(new SendMessage("You decline the duel."));
		if (interacting != null) {
			interacting.getClient().queueOutgoingPacket(new SendMessage("The other player has declined the duel."));
			interacting.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			interacting.getDueling().reset();
		}
		reset();
	}

	public StakingContainer getContainer() {
		return container;
	}

	public Player getInteracting() {
		return interacting;
	}

	public int getRules() {
		return rules;
	}

	public boolean[] getRuleToggle() {
		return ruleToggle;
	}

	public boolean[] getToRemove() {
		return toRemove;
	}

	public boolean hasAccepted() {
		return (s == DuelingStatuses.SCREEN_1_ACCEPTED) || (s == DuelingStatuses.SCREEN_2_ACCEPTED);
	}

	public boolean isDueling() {
		return s == DuelingStatuses.DUELING;
	}

	public boolean isScreen() {
		return (s == DuelingStatuses.SCREEN_1)
				|| (s == DuelingStatuses.SCREEN_1_ACCEPTED || s == DuelingStatuses.SCREEN_2)
				|| (s == DuelingStatuses.SCREEN_2_ACCEPTED);
	}

	public boolean isStaking() {
		return (s != DuelingStatuses.WAITING) && (s != DuelingStatuses.DUELING);
	}

	public void onDuelEnd(boolean forfeit, boolean forceTie) {
		player.getClient().queueOutgoingPacket(new SendPlayerHint(true, -1));
		if (interacting != null) {
			interacting.getClient().queueOutgoingPacket(new SendPlayerHint(true, -1));
		}
		if (interacting != null && interacting.isDead() || interacting != null && forceTie) {
			interacting.teleport(interacting.getController().getRespawnLocation(interacting));
			onDecline(player, interacting);
			player.getClient().queueOutgoingPacket(new SendMessage("The duel was a tie."));
			interacting.getClient().queueOutgoingPacket(new SendMessage("The duel was a tie."));
			if (!player.isDead()) {
				player.teleport(player.getController().getRespawnLocation(player));
			}
		} else if (interacting != null) {
			interacting.teleport(interacting.getController().getRespawnLocation(interacting));
			interacting.getDueling().onVictory();
			interacting.getSkill().restore();
			if (interacting.isPoisoned()) {
				interacting.curePoison(0);
			}
		} else {
			decline();
		}
		if (forfeit) {
			player.teleport(player.getController().getRespawnLocation(player));
			if (player.isPoisoned()) {
				player.curePoison(0);
				player.getClient().queueOutgoingPacket(new SendMessage(":curepoison:"));
			}
			player.getPrayer().disable();
			player.resetCombatStats();
		}
		if (ruleToggle != null) {
			DuelingManager.onFinishDuel(arenaId, ruleToggle[8]);
		}
		if (interacting != null) {
			interacting.getPrayer().disable();
			interacting.resetCombatStats();
		}
		player.setController(ControllerManager.DUEL_ARENA_CONTROLLER);
		if (interacting != null) {
			interacting.setController(ControllerManager.DUEL_ARENA_CONTROLLER);
			interacting.getCombat().reset();
			interacting.getDueling().reset();
		}
		player.getCombat().reset();
		player.getDueling().reset();
	}

	public void onForceLogout() {
		onDuelEnd(false, true);
	}

	public void onStake() {
		this.s = DuelingStatuses.SCREEN_1;
		this.interacting.getDueling().setStatus(DuelingStatuses.SCREEN_1);
		this.interacting.getClient().queueOutgoingPacket(new SendString("", 6684));
		player.getClient().queueOutgoingPacket(new SendString("", 6684));
	}

	public void onVictory() {
		Item[] staked = null;
		Item[] won = null;
		if ((interacting != null) && (interacting.getDueling().getContainer().getItems() != null)) {
			won = interacting.getDueling().getContainer().getItems().clone();
		}
		if (container.getItems() != null) {
			staked = container.getItems().clone();
		}
		container.clear();
		interacting.getDueling().getContainer().clear();
		if (staked != null) {
			for (Item i : staked) {
				if (i != null) {
					player.getInventory().addOrCreateGroundItem(i.getId(), i.getAmount(), false);
				}
			}
		}
		if (won != null) {
			HashMap<Integer, Integer> recieved = new HashMap<>();
			for (Item i : won) {
				if (i != null) {
					player.getInventory().addOrCreateGroundItem(i.getId(), i.getAmount(), false);
					if (recieved.get(i.getId()) != null) {
						recieved.put(i.getId(), i.getAmount() + recieved.get(i.getId()));
					} else {
						recieved.put(i.getId(), i.getAmount());
					}
				}
			}
			for (int item : recieved.keySet()) {
				Item reward = new Item(item, recieved.get(item));
				PlayerLogs.log(player.getUsername(),
						String.format("%s has won %s %s from %s.", Misc.formatPlayerName(player.getUsername()),
								reward.getAmount(), reward.getDefinition().getName(),
								Misc.formatPlayerName(interacting.getUsername())));
			}
		}
		player.getInventory().update();
		if (won != null)
			player.getClient().queueOutgoingPacket(new SendUpdateItems(6822, won));
		else {
			player.getClient().queueOutgoingPacket(new SendUpdateItems(6822, new Item[0]));
		}
		player.duelWins++;
		if (player.getSkulling().isSkulled()) {
			player.getSkulling().unskull(player);
		}
		player.getSpecialAttack().setSpecialAmount(100);
		player.getUpdateFlags().sendAnimation(new Animation(862));
		player.send(new SendMessage(
				"@dre@You are victorious! Congratulations! You have now won: " + player.getDuelWins() + " duels."));
		QuestTab.update(player);
		player.getClient().queueOutgoingPacket(new SendString("" + interacting.getSkill().getCombatLevel(), 6839));
		player.getClient().queueOutgoingPacket(new SendString(interacting.getUsername(), 6840));
		player.getInventory().update();
	}

	public void removeEquipmentForDuel() {
		Item wep = player.getEquipment().getItems()[3];
		for (int i = 0; i < toRemove.length; i++)
			if (toRemove[i]) {
				if ((i == 5) && toRemove[3] && (wep != null) && (wep.getWeaponDefinition().isTwoHanded())) {
					player.getEquipment().unequip(3);
				}
				if (player.getEquipment().getItems()[i] != null) {
					player.getEquipment().unequip(i);
				}
			}
	}

	public void request(Player requested) {
		if (World.inUpdate()) {
			player.send(new SendMessage("You can't trade while the server is updating."));
			return;
		}
		if ((requested.isBusy()) || (player.isBusy())) {
			player.getClient().queueOutgoingPacket(new SendMessage("The other player is busy at the moment."));
			return;
		}
		if (IronMan.isIronMan(player)) {
			player.send(new SendMessage("You are an Iron Man. You stand alone."));
			return;
		}
		if (IronMan.isIronMan(requested)) {
			player.send(new SendMessage("This player is an iron man and can not duel."));
			return;
		}
		player.getClient().queueOutgoingPacket(new SendMessage("Sending duel offer.."));
		if (requested.getDueling().requested(player)) {
			beginStake(player, requested);
		} else if (!requested.getPrivateMessaging().ignored(player.getUsername())) {
			requested.getClient().queueOutgoingPacket(new SendMessage(player.getUsername() + ":duelreq:"));
			lastRequest = requested;
		}
	}

	public boolean requested(Player p) {
		return (lastRequest != null) && (lastRequest.equals(lastRequest));
	}

	public void reset() {
		this.rules = 0;
		this.ruleToggle = null;
		this.s = DuelingStatuses.WAITING;
		this.lastRequest = null;
		this.interacting = null;
		this.container.clear();
		this.toRemove = null;
		this.arenaId = -1;
		this.attack = false;
	}

	public void resetForStake() {
		this.rules = 0;
		this.ruleToggle = new boolean[DuelingConstants.DUEL_RULE_IDS.length];
		this.toRemove = new boolean[14];
		this.container.clear();
		this.lastRequest = null;
		this.arenaId = -1;
		this.attack = false;
	}

	public void setArenaId(int arenaId) {
		this.arenaId = arenaId;
	}

	public void setCanAttack(boolean attack) {
		this.attack = attack;
	}

	public void setDuelingWith(Player dueling) {
		this.interacting = dueling;
	}

	public void setStatus(DuelingStatuses s) {
		this.s = s;
	}

	public void toggle(int id) {
		ruleToggle[id] = (ruleToggle[id] ? false : true);
		rules += (ruleToggle[id] ? DuelingConstants.DUEL_RULE_IDS[id] : -DuelingConstants.DUEL_RULE_IDS[id]);
		if (id >= 11 && id <= 21) {
			DuelingConstants.updateToRemove(this, id, ruleToggle[id]);
		}
		if (id == 9) {
			for (int i = 11; i <= 20; i++) {
				if (i != 14) {
					DuelingConstants.updateToRemove(this, i, ruleToggle[9]);
				}
			}
		}
		if ((id == 0) && (ruleToggle[id])) {
			if (!ruleToggle[4]) {
				toggleRule(4);
			}
			if (!ruleToggle[2]) {
				toggleRule(2);
			}
		}
		if ((id == 4) && (!ruleToggle[4]) && (ruleToggle[0])) {
			toggleRule(0);
		}
		if ((id == 2) && (!ruleToggle[2]) && (ruleToggle[0])) {
			toggleRule(0);
		}
	}

	public void toggleRule(int id) {
		if (((s != DuelingStatuses.SCREEN_1) && (s != DuelingStatuses.SCREEN_1_ACCEPTED)) || (interacting == null)) {
			return;
		}
		toggle(id);
		interacting.getDueling().toggle(id);

		player.getClient().queueOutgoingPacket(new SendConfig(286, rules));
		interacting.getClient().queueOutgoingPacket(new SendConfig(286, interacting.getDueling().getRules()));

		player.getDueling().setStatus(DuelingStatuses.SCREEN_1);
		interacting.getDueling().setStatus(DuelingStatuses.SCREEN_1);

		player.getClient().queueOutgoingPacket(new SendString("", 6684));
		interacting.getClient().queueOutgoingPacket(new SendString("", 6684));
	}
}
