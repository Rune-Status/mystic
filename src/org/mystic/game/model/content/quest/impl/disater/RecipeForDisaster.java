package org.mystic.game.model.content.quest.impl.disater;

import org.mystic.game.model.content.quest.Quest;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.impl.GelatinnothMother;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendPlayerHint;
import org.mystic.game.task.TaskQueue;

public class RecipeForDisaster implements Quest {

	public static final RFDController RFD_CONTROLLER = new RFDController();

	@Override
	public void init(Player player) {
		if (!player.getSkill().locked()) {
			player.teleport(new Location(1909, 5356, player.getIndex() * 4 + 2));
			player.setController(RFD_CONTROLLER);
			player.getQuesting().setQuestActive(this, true);
			player.getSkill().lock(5);
			TaskQueue.queue(new RFDWaveTask(player));
		}
	}

	@Override
	public void doAction(Player player, int stage) {
		player.getQuesting().incrQuestStage(this);
		if (player.getQuesting().isQuestCompleted(this)) {
			reward(player);
			return;
		}
		final Location location = new Location(RFDConstants.SPAWN);
		location.setZ(player.getIndex() * 4 + 2);
		Npc mob = null;
		if (stage != 4)
			mob = new Npc(RFDConstants.WAVES[stage], false, location, player, false, false, null);
		else {
			mob = new GelatinnothMother(location, player);
		}
		player.send(new SendPlayerHint(false, mob.getIndex()));
		mob.getFollowing().setIgnoreDistance(true);
		player.getAttributes().set("currentRFDMob", mob);
	}

	@Override
	public void reward(Player player) {
		player.getQuesting().setCompleted(this);
		player.teleport(RFD_CONTROLLER.getRespawnLocation(player));
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
	}

	@Override
	public String getName() {
		return "Recipe for Disaster";
	}

	@Override
	public byte getFinalStage() {
		return 6;
	}

	@Override
	public boolean clickObject(Player player, int option, int id) {
		if (id == 12356) {
			player.getSkill().lock(5);
			player.teleport(new Location(3098, 3512, 0));
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			player.getSkill().restore();
			player.send(new SendMessage("You step through the portal and feel refreshed."));
		}
		return false;
	}

	@Override
	public boolean clickButton(Player player, int id) {
		return false;
	}

	@Override
	public short getId() {
		return 0;
	}

	@Override
	public boolean useItemOnObject(Player player, int item, int object) {
		return false;
	}

	@Override
	public byte getPoints() {
		return 1;
	}

	@Override
	public String[] getLinesForStage(byte stage) {
		if (stage == 0)
			return new String[] { "You can start this quest by talking to the Gypsy.",
					"She is located within Edgeville." };
		if (stage < 5) {
			return new String[] { "You must defeat all the monsters to complete this quest." };
		}
		return new String[] { "You have completed this mini-quest.", "You can now purchase from the chest." };
	}
}
