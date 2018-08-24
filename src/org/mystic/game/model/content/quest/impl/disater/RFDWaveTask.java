package org.mystic.game.model.content.quest.impl.disater;

import org.mystic.game.model.content.quest.QuestConstants;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;

public class RFDWaveTask extends Task {

	private final Player player;

	private byte wave = 0;

	private Npc mob = null;

	public RFDWaveTask(final Player player) {
		super(player, 3);
		this.player = player;
		player.getPrayer().disable();
	}

	@Override
	public void execute() {
		if ((player.isDead()) || (!player.getController().equals(RecipeForDisaster.RFD_CONTROLLER))) {
			if (mob != null) {
				mob.remove();
			}
			stop();
			return;
		}
		if ((mob == null) || ((mob.isDead()) && (!mob.isVisible()))) {
			if (wave == RFDConstants.WAVES.length) {
				QuestConstants.RECIPE_FOR_DISASTER.doAction(player, wave);
				stop();
				return;
			} else {
				QuestConstants.RECIPE_FOR_DISASTER.doAction(player, wave);
				mob = ((Npc) player.getAttributes().get("currentRFDMob"));
				mob.getCombat().setAttack(player);
				wave++;
				if (wave == RFDConstants.WAVES.length) {
					this.setTaskDelay(1);
					mob.getUpdateFlags().sendForceMessage("Now you will die!");
				}
			}
		}
	}

	@Override
	public void onStop() {
		if (mob != null) {
			mob.remove();
		}
		if (player != null) {
			if (wave != RFDConstants.WAVES.length) {
				player.getQuesting().setQuestStage(QuestConstants.RECIPE_FOR_DISASTER, 0);
			}
		}
	}

}