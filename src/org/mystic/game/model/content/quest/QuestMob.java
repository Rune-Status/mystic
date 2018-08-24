package org.mystic.game.model.content.quest;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;

public abstract class QuestMob extends Npc {

	public QuestMob(int npcId, Location location) {
		super(npcId, true, location);
	}

	public abstract void onClick(Player paramPlayer, int paramInt);
}
