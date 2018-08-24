package org.mystic.game.model.content.randomevent;

import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;

/*
 * @author Reece
 */
public interface RandomEvent {

	/**
	 * Is the user able to fail the event?
	 * 
	 * @return
	 */
	public boolean canFail();

	/**
	 * Is the user in a state that he/she can start an event?
	 * 
	 * @return
	 */
	public boolean canStart(Player player);

	/**
	 * The process that acquires at the beginning of an event.
	 */
	public void start(Player player);

	/**
	 * The process that acquires at the point of failure of an event.
	 */
	public void fail(Player player, Npc npc);

	/**
	 * The process that acquires at the end of an event.
	 */
	public void end(Player player, Npc npc);

	/**
	 * The reward that you obtain upon completion of an event
	 */
	public void reward(Player player);

}
