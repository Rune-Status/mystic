package org.mystic.game.task.impl;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

public class AntifireTask extends Task {

	/**
	 * The cycles before it ends
	 */
	private int cycles;

	/**
	 * The player who drank the antifire potion
	 */
	private final Player player;

	/**
	 * The potion is a super potion
	 */
	private final boolean isSuper;

	/**
	 * Constructs a new AntiFireTask for the player
	 * 
	 * @param player
	 *            The player who drank the antifire potion
	 * @param isSuper
	 *            If the potion is a super potion or not
	 */
	public AntifireTask(Player player, boolean isSuper) {
		super(player, 1, false, StackType.STACK, BreakType.NEVER, TaskIdentifier.CURRENT_ACTION);
		this.cycles = 600;
		this.player = player;
		this.isSuper = isSuper;
		if (player.getAttributes().get("fire_potion_task") != null) {
			((AntifireTask) player.getAttributes().get("fire_potion_task")).stop();
		}
		player.getAttributes().set("fire_potion_task", this);
		player.getAttributes().set(isSuper ? "super_fire_resist" : "fire_resist", Boolean.TRUE);
		player.getClient()
				.queueOutgoingPacket(new SendMessage("<col=802DA0>You are now more resistant to dragonfire.</col>"));
	}

	@Override
	public void execute() {
		if (player.isDead()) {
			this.stop();
			return;
		}
		if ((!isSuper && !player.getAttributes().is("fire_resist"))
				|| (isSuper && !player.getAttributes().is("super_fire_resist"))) {
			this.stop();
			return;
		}
		if (cycles > 0) {
			cycles--;
			if (cycles == 100) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("<col=802DA0>Your resistance to dragonfire is about to run out.</col>"));
			}
			if (cycles == 0) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("<col=802DA0>Your resistance to dragonfire has run out.</col>"));
				this.stop();
				return;
			}
		}
	}

	@Override
	public void onStop() {
		player.getAttributes().set(isSuper ? "super_fire_resist" : "fire_resist", Boolean.FALSE);
		player.getAttributes().remove("fire_potion_task");
	}

}
