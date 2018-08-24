package org.mystic.game.task.impl;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.utility.Misc;

public class PlayerBroadcasts extends Task {

	private final Player player;

	public PlayerBroadcasts(final Player player, final int delay) {
		super(player, delay);
		this.player = player;
	}

	private final String presuffix = "@cr6@ @dre@";

	private final String announcements[] =

			{

					"Did you know the experience rate for Iron men is 50% slower?",

					"Interested in supporting Mystic? Visit the store: www.Mystic-PS.com/store/",

					"Join our community forums at www.community.mystic-ps.com",

					"Create your own shop by speaking to the Grand Exchange clerk in edgeville!",

					"Found a bug? Report it on the forums.",

					"Did you know you can reset combat stats for a small fee in edgeville?",

					"Mystic staff will never ask for your password. Do not share it.",

					"Don't know where to go? Speak to the Teleport Wizard at Edgeville.",

					"Want to change how the client looks? Navigate to the settings tab.",

					"If you do not have a bank PIN you should consider setting one.",

					"Fight caves consists of 24 waves, only 12 for gold members!",

					"If you do not have a bank PIN you should consider setting one.",

					"Did you know that to answer the MysticBot you have to type ::answer",

					"Did you know there is a rare chance to steal an Onyx from the gem stall?"

			};

	@Override
	public void execute() {
		final int random = Misc.randomNumber(announcements.length);
		player.send(new SendMessage(presuffix + announcements[random]));
	}

	@Override
	public void onStop() {

	}

}