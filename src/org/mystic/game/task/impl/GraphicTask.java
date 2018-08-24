package org.mystic.game.task.impl;

import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

/**
 * A graphic to queue.
 * 
 * @author Michael Sasse
 * 
 */
public class GraphicTask extends Task {

	/**
	 * The graphic.
	 */
	private final Graphic graphic;
	/**
	 * The entity.
	 */
	private final Entity entity;

	/**
	 * Creates a new graphic to queue.
	 * 
	 * @param graphic
	 *            the graphic.
	 * @param delay
	 *            the action delay.
	 */
	public GraphicTask(int delay, boolean immediate, Graphic graphic, Entity entity) {
		super(entity, delay, immediate, StackType.STACK, BreakType.NEVER, TaskIdentifier.CURRENT_ACTION);
		this.graphic = graphic;
		this.entity = entity;
	}

	@Override
	public void execute() {
		entity.getUpdateFlags().sendGraphic(graphic);
		stop();
	}

	@Override
	public void onStop() {

	}
}
