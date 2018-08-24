package org.mystic.game.task;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mystic.game.model.entity.Entity;
import org.mystic.game.task.Task.BreakType;
import org.mystic.game.task.Task.StackType;
import org.mystic.game.task.impl.HitTask;

public class TaskQueue {

	private static final Queue<Task> adding = new ConcurrentLinkedQueue<Task>();

	private static final List<Task> tasks = new LinkedList<Task>();

	public static void cancelHitsOnEntity(Entity e) {
		for (Task i : tasks) {
			if (i instanceof HitTask) {
				HitTask hit = (HitTask) i;
				if (hit.getEntity() != null && hit.getEntity().equals(e)) {
					hit.stop();
				}
			}
		}
	}

	public static void onMovement(Entity e) {
		LinkedList<Task> active = e.getTasks();
		if (active != null) {
			for (Iterator<Task> i = active.iterator(); i.hasNext();) {
				Task t = i.next();
				if (t.getBreakType() == BreakType.ON_MOVE) {
					t.stop();
					i.remove();
				}
			}
		}
	}

	/**
	 * Adds new tasks and then processes all tasks.
	 */
	public static void process() throws Exception {
		Task t;
		while ((t = adding.poll()) != null) {
			tasks.add(t);
		}
		for (Iterator<Task> i = tasks.iterator(); i.hasNext();) {
			Task task = i.next();
			try {
				if (task.isAssociated()) {
					if (!task.getEntity().isActive() || !task.getEntity().getTasks().contains(task)) {
						task.onStop();
						i.remove();
						continue;
					}
				}
				if (task.stopped()) {
					if (task.isAssociated()) {
						task.getEntity().getTasks().remove(task);
					}
					task.onStop();
					i.remove();
					continue;
				}
				task.run();
			} catch (Exception e) {
				e.printStackTrace();
				i.remove();
			}
		}
	}

	/**
	 * Queues a task.
	 * 
	 * @param task
	 *            the task
	 * @return The task for chaining.
	 */
	public static Task queue(Task task) {
		if (task.stopped()) {
			return task;
		}
		if (task.isAssociated()) {
			Entity e = task.getEntity();
			if (task.getStackType() == StackType.NEVER_STACK) {
				for (Iterator<Task> i = e.getTasks().iterator(); i.hasNext();) {
					Task t = i.next();
					if (t.getStackType() == StackType.NEVER_STACK && t.getTaskId() == task.getTaskId()) {
						i.remove();
					}
				}
			}
			e.getTasks().add(task);
		}
		task.onStart();
		if (task.immediate()) {
			task.execute();
		}
		adding.add(task);
		return task;
	}

}
