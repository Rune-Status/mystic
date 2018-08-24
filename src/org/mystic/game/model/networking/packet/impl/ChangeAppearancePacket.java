package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.IncomingPacket;

public class ChangeAppearancePacket extends IncomingPacket {

	private static final int[][] MALE_VALUES = {

			{ 0, 97 }, // head
			{ 10, 104 }, // jaw
			{ 18, 116 }, // torso
			{ 26, 110 }, // arms
			{ 33, 126 }, // hands
			{ 36, 90 }, // legs
			{ 42, 43 }, // feet

	};

	private static final int[][] FEMALE_VALUES = {

			{ 45, 146 }, // head
			{ -1, -1 }, // jaw
			{ 56, 158 }, // torso
			{ 61, 152 }, // arms
			{ 67, 168 }, // hands
			{ 70, 134 }, // legs
			{ 79, 80 }, // feet

	};

	private static final int[][] IDK_COLORS = {

			{ 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, -31839, 22433, 2983, -11343, 8, 5281, 10438, 3650, -27322,
					-21845, 200, 571, 908, 21830, 28946, -15701, -14010 },

			{ 8741, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533, 25239,
					8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010 },

			{ 25238, 8742, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533,
					8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010 },

			{ 4626, 11146, 6439, 12, 4758, 10270, 6850, 350, 660, 36133, 43297 },

			{ 4550, 4574, 4537, 5681, 5673, 5790, 6806, 8076, 16800, 43720, 8416 }

	};

	private static final int[][] ALLOWED_COLORS = {

			{ 0, IDK_COLORS[0].length }, // hair color
			{ 0, IDK_COLORS[1].length }, // torso color
			{ 0, IDK_COLORS[2].length }, // legs color
			{ 0, IDK_COLORS[3].length }, // feet color
			{ 0, IDK_COLORS[4].length } // skin color

	};

	public static void setToDefault(Player p) {
		p.setGender((byte) 0);
		p.getAppearance()[0] = 3;
		p.getAppearance()[1] = 18;
		p.getAppearance()[2] = 30;
		p.getAppearance()[3] = 33;
		p.getAppearance()[4] = 39;
		p.getAppearance()[5] = 42;
		p.getAppearance()[6] = 14;
		byte[] col = { (byte) 7, (byte) 1, (byte) 8, (byte) 4, (byte) 0 };
		p.setColors(col);
	}

	public static boolean validate(Player player, int[] app, byte[] col, byte gender) {
		if (col[4] > 7 && !player.isGoldMember()) {
			player.send(new SendMessage("@dre@You must be a gold member to use the selected skin color."));
			return false;
		}
		if (gender == 0) {
			for (int i = 0; i < app.length; i++)
				if ((app[i] < MALE_VALUES[i][0]) || (app[i] > MALE_VALUES[i][1]))
					return false;
		} else if (gender == 1) {
			for (int i = 0; i < app.length; i++)
				if ((app[i] < FEMALE_VALUES[i][0]) || (app[i] > FEMALE_VALUES[i][1]))
					return false;
		} else {
			return false;
		}
		for (int i = 0; i < col.length; i++) {
			if ((col[i] < ALLOWED_COLORS[i][0]) || (col[i] > ALLOWED_COLORS[i][1])) {
				return false;
			}
		}
		return true;
	}

	public static boolean validate(Player p) {
		int[] a = p.getAppearance();
		return validate(p, new int[] { a[0], a[6], a[1], a[2], a[3], a[4], a[5] }, p.getColors(), p.getGender());
	}

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		byte gender = (byte) in.readByte();
		int head = in.readByte();
		int jaw = in.readByte();
		int torso = in.readByte();
		int arms = in.readByte();
		int hands = in.readByte();
		int legs = in.readByte();
		int feet = in.readByte();
		byte[] col = { (byte) in.readByte(), (byte) in.readByte(), (byte) in.readByte(), (byte) in.readByte(),
				(byte) in.readByte() };
		int[] app = new int[player.getAppearance().length];
		if (0 > head) {
			head += 256;
		}
		if (0 > torso) {
			torso += 256;
		}
		if (0 > arms) {
			arms += 256;
		}
		if (0 > hands) {
			hands += 256;
		}
		if (0 > legs) {
			legs += 256;
		}
		if (0 > feet) {
			feet += 256;
		}
		app[0] = head;
		app[1] = torso;
		app[2] = arms;
		app[3] = hands;
		app[4] = legs;
		app[5] = feet;
		app[6] = jaw;
		if (!validate(player, new int[] { head, jaw, torso, arms, hands, legs, feet }, col, gender)) {
			return;
		}
		player.setGender(gender);
		player.setAppearance(app);
		player.setColors(col);
		player.setAppearanceUpdateRequired(true);
	}
}
