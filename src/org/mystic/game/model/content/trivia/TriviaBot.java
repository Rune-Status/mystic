package org.mystic.game.model.content.trivia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class TriviaBot {

	/**
	 * The logger for the class
	 */
	private static Logger logger = Logger.getLogger(TriviaBot.class.getSimpleName());

	/**
	 * Holds all the bot data
	 */
	private final static Set<Trivia> BOT_DATA = new HashSet<>();

	/**
	 * The current question/answer set
	 */
	private static Trivia current = null;

	/*
	 * Holds all the MysticBot attempted answers
	 */
	public final static ArrayList<String> attempts = new ArrayList<String>();

	/**
	 * Color of the MysticBot messages
	 */
	private static final String COLOR = "@blu@";

	/**
	 * Declares the Trivia data
	 */
	static {
		for (Trivia data : Trivia.values()) {
			BOT_DATA.add(data);
		}
		logger.info("Loaded " + BOT_DATA.size() + " Trivia questions.");
	}

	/**
	 * Initializes the MysticBot task
	 */
	public static void start() {
		TaskQueue.queue(new Task(800, false) {
			@Override
			public void execute() {
				if (current == null) {
					assign();
					return;
				}
				sendMessage("(" + COLOR + "Trivia</col>) " + current.getQuestion());
			}

			@Override
			public void onStop() {

			}
		});
	}

	/**
	 * Assigns a new question
	 */
	private static void assign() {
		current = Misc.randomElement(BOT_DATA);
		sendMessage("(" + COLOR + "Trivia</col>) " + current.getQuestion());
	}

	/**
	 * Handles player answering the question
	 * 
	 * @param player
	 * @param answer
	 */
	public static void answer(Player player, String answer) {
		if (current == null) {
			return;
		}
		for (int i = 0; i < current.getAnswers().length; i++) {
			if (current.getAnswers()[i].equalsIgnoreCase(answer)) {
				answered(player, answer);
				return;
			}
		}
		player.send(new SendMessage("(" + COLOR + "Trivia</col>) That answer is incorrect!"));
		attempts.add(answer);
	}

	/**
	 * Handles player answering the question successfully
	 * 
	 * @param player
	 * @param answer
	 */
	private static void answered(Player player, String answer) {

		sendMessage("(" + COLOR + "Trivia</col>) " + COLOR + player.getUsername()
				+ "</col> has answered the question correctly! Answer:" + COLOR + " "
				+ Misc.capitalizeFirstLetter(answer) + "</col>.");

		// TODO: Reward

		reset();
	}

	/**
	 * Resets the MysticBot
	 */
	private static final void reset() {
		current = null;
		attempts.clear();
	}

	/**
	 * Sends message to server
	 * 
	 * @param message
	 */
	public static void sendMessage(String message) {
		for (Player players : World.getPlayers()) {
			if (players != null) {
				players.send(new SendMessage(message));
			}
		}
	}

}