package org.mystic.game.task.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.Hit.HitTypes;
import org.mystic.game.model.content.combat.impl.Killstreaks;
import org.mystic.game.model.content.combat.impl.PlayerDrops;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.prayer.PrayerBook.PrayerBookType;
import org.mystic.game.model.content.skill.prayer.PrayerConstants;
import org.mystic.game.model.content.skill.prayer.impl.CursesPrayerBook;
import org.mystic.game.model.entity.Area;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.mysql.Highscores;
import org.mystic.utility.Misc;

public class PlayerDeathTask extends Task {

	private final Player player;

	private final Entity killer;

	private final Controller controller;

	public PlayerDeathTask(final Player player) {
		super(player, 6, false, Task.StackType.NEVER_STACK, Task.BreakType.NEVER, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.killer = player.getCombat().getDamageTracker().getKiller();
		this.controller = player.getController();
		if (player.isDead()) {
			stop();
			return;
		}
		player.getUpdateFlags().faceEntity(65535);
		player.setDead(true);
		player.getMovementHandler().reset();
		TaskQueue.queue(
				new Task(player, 1, false, Task.StackType.STACK, Task.BreakType.NEVER, TaskIdentifier.CURRENT_ACTION) {
					@Override
					public void execute() {
						player.getUpdateFlags().sendAnimation(PlayerConstants.getDeathAnimation(), 0);
						if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT) {
							if (player.getPrayer().active(PrayerConstants.RETRIBUTION)) {
								player.getUpdateFlags().sendGraphic(new Graphic(437));
								if (Misc.getManhattanDistance(player.getLocation(), killer.getLocation()) < 4) {
									int damage = Misc.randomNumber((int) (player.getLevels()[Skills.PRAYER] * 0.11));
									killer.hit(new Hit(player, damage, HitTypes.NONE));
								}
							}
						} else if (player.getPrayer().getPrayerBookType() == PrayerBookType.CURSES) {
							if (player.getPrayer().active(CursesPrayerBook.WRATH)) {
								player.getUpdateFlags().sendGraphic(new Graphic(2259));
								Location location = new Location(player.getLocation());
								Area a = Area.areaFromCenter(location, 1);
								Location[] area = a.calculateAllLocations();
								for (Location loc : area) {
									World.sendStillGraphic(2260, 0, loc);
								}
								if (killer != null && !killer.isNpc() && player != null) {
									if (Misc.getManhattanDistance(player.getLocation(), killer.getLocation()) < 4) {
										int damage = Misc
												.randomNumber((int) (player.getLevels()[Skills.PRAYER] * 0.18));
										killer.getUpdateFlags().sendGraphic(new Graphic(2260));
										killer.hit(new Hit(player, damage, HitTypes.NONE));
									}
								}
							}
						}
						stop();
					}

					@Override
					public void onStop() {
					}
				});
	}

	@Override
	public void execute() {
		player.getClient().queueOutgoingPacket(new SendMessage("Oh dear, you are dead!"));
		if (!controller.isSafe(player)) {
			PlayerDrops.dropItemsOnDeath(player);
			player.getEarningPotential().onKilled(player.getCombat().getDamageTracker().getKiller());
			Killstreaks.reset(player, player.getCombat().getDamageTracker().getKiller());
			if (IronMan.isHardcoreIronMan(player)) {
				PlayerLogs.log(player.getUsername(), "Died as hardcore Iron man in controller: " + controller);
				player.getClient().queueOutgoingPacket(new SendMessage(
						"You have fallen as a Hardcore Iron Man, your Hardcore status has been revoked."));
				World.sendGlobalMessage(
						"@blu@News: @bla@Hardcore Iron Man (" + player.getDisplay()
								+ ") has fallen with a total level of " + player.getSkill().getTotalLevel() + ".",
						false);
				player.setRights(Rights.IRON_MAN);
				new Thread(new Highscores(player)).start();
				QuestTab.update(player);
			}
		}
		if (player.isPoisoned()) {
			player.curePoison(0);
		}
		if (player.getSkulling().isSkulled()) {
			player.getSkulling().unskull(player);
		}
		if (player.getMagic().isVengeanceActive()) {
			player.getMagic().setVengeanceActive(false);
		}
		if (player.getSummoning().hasFamiliar()) {
			player.getSummoning().onFamiliarDeath();
		}
		player.getAchievements().incr(player, "Die 50 times");
		player.getAchievements().incr(player, "Die 10 times");
		player.teleport(controller.getRespawnLocation(player));
		player.getSpecialAttack().setSpecialAmount(100);
		player.getSpecialAttack().update();
		player.getPrayer().disable();
		player.getRunEnergy().setEnergy(100);
		player.getRunEnergy().update();
		player.getEquipment().onLogin();
		player.unfreeze();
		player.resetSpear();
		player.setTeleblockTime(0);
		player.setAppearanceUpdateRequired(true);
		player.getCombat().forRespawn();
		controller.onDeath(player);
		stop();
	}

	@Override
	public void onStop() {
		player.getTasks().clear();
		player.getMovementHandler().reset();
		player.getUpdateFlags().sendAnimation(65535, 0);
	}
}