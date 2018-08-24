package org.mystic.game.model.content.bank;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.utility.Misc;

public class BankSecurity {

	public static void deletePin(Player player) {
		player.getBankPinAttributes().setHasBankPin(false).setHasEnteredBankPin(false).setInvalidAttempts(0)
				.setLastAttempt(System.currentTimeMillis());
		for (int i = 0; i < player.getBankPinAttributes().getBankPin().length; i++) {
			player.getBankPinAttributes().getBankPin()[i] = -1;
			player.getBankPinAttributes().getEnteredBankPin()[i] = -1;
		}
		DialogueManager.sendStatement(player, "@dre@Your pin has been deleted.");
		player.send(new SendRemoveInterfaces());
	}

	public static void init(Player player, boolean openBankAfter) {
		if (player.getBankPinAttributes().getInvalidAttempts() == 3) {
			if (System.currentTimeMillis() - player.getBankPinAttributes().getLastAttempt() < 400000) {
				DialogueManager.sendStatement(player, "You must wait " + (int) ((400
						- (System.currentTimeMillis() - player.getBankPinAttributes().getLastAttempt()) * 0.001))
						+ " seconds", "before attempting to enter your bank-pin again.");
				return;
			} else {
				player.getBankPinAttributes().setInvalidAttempts(0);
				player.send(new SendRemoveInterfaces());
			}
		}
		player.setOpenBank(openBankAfter);
		randomizeNumbers(player);
		player.send(new SendString("First click the FIRST digit", 15313));
		player.send(new SendString("", 14923));
		player.send(new SendString("?", 14913));
		player.send(new SendString("?", 14914));
		player.send(new SendString("?", 14915));
		player.send(new SendString("?", 14916));
		sendPins(player);
		player.send(new SendInterface(7424));
		for (int i = 0; i < player.getBankPinAttributes().getEnteredBankPin().length; i++)
			player.getBankPinAttributes().getEnteredBankPin()[i] = -1;
	}

	public static void clickedButton(Player player, int button) {
		sendPins(player);
		SoundPlayer.play(player, Sounds.CLICK_PIN_BUTTON);
		if (player.getBankPinAttributes().getEnteredBankPin()[0] == -1) {
			player.send(new SendString("Now click the SECOND digit", 15313));
			player.send(new SendString("*", 14913));
			for (int i = 0; i < actionButtons.length; i++)
				if (actionButtons[i] == button)
					player.getBankPinAttributes().getEnteredBankPin()[0] = player.getBankPinAttributes()
							.getBankPins()[i];
		} else if (player.getBankPinAttributes().getEnteredBankPin()[1] == -1) {
			player.send(new SendString("Now click the THIRD digit", 15313));
			player.send(new SendString("*", 14914));
			for (int i = 0; i < actionButtons.length; i++)
				if (actionButtons[i] == button)
					player.getBankPinAttributes().getEnteredBankPin()[1] = player.getBankPinAttributes()
							.getBankPins()[i];
		} else if (player.getBankPinAttributes().getEnteredBankPin()[2] == -1) {
			player.send(new SendString("Now click the FINAL digit", 15313));
			player.send(new SendString("*", 14915));
			for (int i = 0; i < actionButtons.length; i++)
				if (actionButtons[i] == button)
					player.getBankPinAttributes().getEnteredBankPin()[2] = player.getBankPinAttributes()
							.getBankPins()[i];
		} else if (player.getBankPinAttributes().getEnteredBankPin()[3] == -1) {
			player.send(new SendString("*", 14916));
			for (int i = 0; i < actionButtons.length; i++)
				if (actionButtons[i] == button)
					player.getBankPinAttributes().getEnteredBankPin()[3] = player.getBankPinAttributes()
							.getBankPins()[i];
			if (!player.getBankPinAttributes().hasBankPin()) {
				player.getBankPinAttributes().setHasBankPin(true).setHasEnteredBankPin(true)
						.setBankPin(player.getBankPinAttributes().getEnteredBankPin());
				player.send(new SendMessage("Success! You have set your security pin as -> (@dre@"
						+ player.getBankPinAttributes().getEnteredBankPin()[0] + "-"
						+ player.getBankPinAttributes().getEnteredBankPin()[1] + "-"
						+ player.getBankPinAttributes().getEnteredBankPin()[2] + "-"
						+ player.getBankPinAttributes().getEnteredBankPin()[3] + "@bla@)."));
				player.send(new SendRemoveInterfaces());
				return;
			}
			for (int i = 0; i < player.getBankPinAttributes().getEnteredBankPin().length; i++) {
				if (player.getBankPinAttributes().getEnteredBankPin()[i] != player.getBankPinAttributes()
						.getBankPin()[i]) {
					player.send(new SendRemoveInterfaces());
					int invalidAttempts = player.getBankPinAttributes().getInvalidAttempts() + 1;
					if (invalidAttempts >= 3) {
						player.getBankPinAttributes().setLastAttempt(System.currentTimeMillis());
					}
					player.getBankPinAttributes().setInvalidAttempts(invalidAttempts);
					DialogueManager.sendStatement(player, "The PIN you have entered is incorrect.");
					return;
				}
			}
			player.getBankPinAttributes().setInvalidAttempts(0).setHasEnteredBankPin(true);
			player.send(new SendMessage("You have correctly entered your pin."));
			if (player.openBank()) {
				player.getBank().openBankNoBusy();
			} else {
				player.send(new SendRemoveInterfaces());
			}
		}
		randomizeNumbers(player);
	}

	private static void sendPins(Player player) {
		for (int i = 0; i < player.getBankPinAttributes().getBankPins().length; i++)
			player.send(new SendString("" + player.getBankPinAttributes().getBankPins()[i], stringIds[i]));
	}

	public static void randomizeNumbers(Player player) {
		int i = Misc.getRandom(5);
		switch (i) {
		case 0:
			player.getBankPinAttributes().getBankPins()[0] = 1;
			player.getBankPinAttributes().getBankPins()[1] = 7;
			player.getBankPinAttributes().getBankPins()[2] = 0;
			player.getBankPinAttributes().getBankPins()[3] = 8;
			player.getBankPinAttributes().getBankPins()[4] = 4;
			player.getBankPinAttributes().getBankPins()[5] = 6;
			player.getBankPinAttributes().getBankPins()[6] = 5;
			player.getBankPinAttributes().getBankPins()[7] = 9;
			player.getBankPinAttributes().getBankPins()[8] = 3;
			player.getBankPinAttributes().getBankPins()[9] = 2;
			break;

		case 1:
			player.getBankPinAttributes().getBankPins()[0] = 5;
			player.getBankPinAttributes().getBankPins()[1] = 4;
			player.getBankPinAttributes().getBankPins()[2] = 3;
			player.getBankPinAttributes().getBankPins()[3] = 7;
			player.getBankPinAttributes().getBankPins()[4] = 8;
			player.getBankPinAttributes().getBankPins()[5] = 6;
			player.getBankPinAttributes().getBankPins()[6] = 9;
			player.getBankPinAttributes().getBankPins()[7] = 2;
			player.getBankPinAttributes().getBankPins()[8] = 1;
			player.getBankPinAttributes().getBankPins()[9] = 0;
			break;

		case 2:
			player.getBankPinAttributes().getBankPins()[0] = 4;
			player.getBankPinAttributes().getBankPins()[1] = 7;
			player.getBankPinAttributes().getBankPins()[2] = 6;
			player.getBankPinAttributes().getBankPins()[3] = 5;
			player.getBankPinAttributes().getBankPins()[4] = 2;
			player.getBankPinAttributes().getBankPins()[5] = 3;
			player.getBankPinAttributes().getBankPins()[6] = 1;
			player.getBankPinAttributes().getBankPins()[7] = 8;
			player.getBankPinAttributes().getBankPins()[8] = 9;
			player.getBankPinAttributes().getBankPins()[9] = 0;
			break;

		case 3:
			player.getBankPinAttributes().getBankPins()[0] = 9;
			player.getBankPinAttributes().getBankPins()[1] = 4;
			player.getBankPinAttributes().getBankPins()[2] = 2;
			player.getBankPinAttributes().getBankPins()[3] = 7;
			player.getBankPinAttributes().getBankPins()[4] = 8;
			player.getBankPinAttributes().getBankPins()[5] = 6;
			player.getBankPinAttributes().getBankPins()[6] = 0;
			player.getBankPinAttributes().getBankPins()[7] = 3;
			player.getBankPinAttributes().getBankPins()[8] = 1;
			player.getBankPinAttributes().getBankPins()[9] = 5;
			break;

		case 4:
			player.getBankPinAttributes().getBankPins()[0] = 8;
			player.getBankPinAttributes().getBankPins()[1] = 7;
			player.getBankPinAttributes().getBankPins()[2] = 6;
			player.getBankPinAttributes().getBankPins()[3] = 2;
			player.getBankPinAttributes().getBankPins()[4] = 5;
			player.getBankPinAttributes().getBankPins()[5] = 4;
			player.getBankPinAttributes().getBankPins()[6] = 1;
			player.getBankPinAttributes().getBankPins()[7] = 0;
			player.getBankPinAttributes().getBankPins()[8] = 3;
			player.getBankPinAttributes().getBankPins()[9] = 9;
			break;
		}
		sendPins(player);
	}

	private static final int stringIds[] = { 14883, 14884, 14885, 14886, 14887, 14888, 14889, 14890, 14891, 14892 };

	private static final int actionButtons[] = { 58025, 58026, 58027, 58028, 58029, 58030, 58031, 58032, 58033, 58034 };

	public static class BankPinAttributes {

		public BankPinAttributes() {

		}

		private boolean hasBankPin;
		private boolean hasEnteredBankPin;
		private int[] bankPin = new int[4];
		private int[] enteredBankPin = new int[4];
		private int bankPins[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		private int invalidAttempts;
		private long lastAttempt;

		public boolean hasBankPin() {
			return hasBankPin;
		}

		public BankPinAttributes setHasBankPin(boolean hasBankPin) {
			this.hasBankPin = hasBankPin;
			return this;
		}

		public boolean hasEnteredBankPin() {
			return hasEnteredBankPin;
		}

		public BankPinAttributes setHasEnteredBankPin(boolean hasEnteredBankPin) {
			this.hasEnteredBankPin = hasEnteredBankPin;
			return this;
		}

		public int[] getBankPin() {
			return bankPin;
		}

		public BankPinAttributes setBankPin(int[] bankPin) {
			this.bankPin = bankPin;
			return this;
		}

		public int[] getEnteredBankPin() {
			return enteredBankPin;
		}

		public int[] getBankPins() {
			return bankPins;
		}

		public int getInvalidAttempts() {
			return invalidAttempts;
		}

		public BankPinAttributes setInvalidAttempts(int invalidAttempts) {
			this.invalidAttempts = invalidAttempts;
			return this;
		}

		public long getLastAttempt() {
			return lastAttempt;
		}

		public BankPinAttributes setLastAttempt(long lastAttempt) {
			this.lastAttempt = lastAttempt;
			return this;
		}
	}
}