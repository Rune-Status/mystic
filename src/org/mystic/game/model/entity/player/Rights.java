package org.mystic.game.model.entity.player;

/**
 * The enumerated type whose elements represent the types of authority a player
 * can have.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum Rights {

	PLAYER(0, 0, ""),

	IRON_MAN(8, 0, ""),

	ULTIMATE_IRON_MAN(9, 0, ""),

	HARDCORE_IRON_MAN(10, 0, ""),

	VETERAN(1, 0, ""),

	YOUTUBER(2, 0, ""),

	GOLD_MEMBER(3, 0, "@or1@[$] "),

	EXTREME_GOLD_MEMBER(4, 0, "@or1@[$$] "),

	LEGENDARY_GOLD_MEMBER(5, 0, "@or1@[$$$] "),

	MODERATOR(6, 1, "@or1@Mod "),

	ADMINISTRATOR(7, 2, "@or1@Admin "),

	OWNER(7, 3, "@or1@Owner ");

	/**
	 * The value of this rank as seen by the protocol. The only ranks the protocol
	 * sees by default are:
	 * <p>
	 * <p>
	 * <table BORDER CELLPADDING=3 CELLSPACING=1>
	 * <tr>
	 * <td></td>
	 * <td ALIGN=CENTER><em>Protocol Value</em></td>
	 * </tr>
	 * <tr>
	 * <td>Player</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>Moderator</td>
	 * <td>1</td>
	 * </tr>
	 * <tr>
	 * <td>Administrator</td>
	 * <td>2</td>
	 * </tr>
	 * </table>
	 */
	private final int protocolValue;

	/**
	 * The value of this rank as seen by the server. This value will be used to
	 * determine which of the elements are greater than each other.
	 */
	private final int value;

	/**
	 * The string in which represents the players title corresponding to their rank.
	 */
	private final String rights_title;

	/**
	 * Create a new {@link Rights}.
	 *
	 * @param protocolValue
	 *            the value of this rank as seen by the protocol.
	 * @param value
	 *            the value of this rank as seen by the server.
	 */
	private Rights(int protocolValue, int value, String rights_title) {
		this.protocolValue = protocolValue;
		this.value = value;
		this.rights_title = rights_title;
	}

	/**
	 * Determines if this right is greater than the argued right. Please note that
	 * this method <b>does not</b> compare the Objects themselves, but instead
	 * compares the value behind them as specified by {@code value} in the
	 * enumerated type.
	 *
	 * @param other
	 *            the argued right to compare.
	 * @return {@code true} if this right is greater, {@code false} otherwise.
	 */
	public final boolean greater(Rights other) {
		return value > other.value;
	}

	/**
	 * Determines if this right is lesser than the argued right. Please note that
	 * this method <b>does not</b> compare the Objects themselves, but instead
	 * compares the value behind them as specified by {@code value} in the
	 * enumerated type.
	 *
	 * @param other
	 *            the argued right to compare.
	 * @return {@code true} if this right is lesser, {@code false} otherwise.
	 */
	public final boolean less(Rights other) {
		return value < other.value;
	}

	/**
	 * Determines if this right is equal in power to the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but instead
	 * compares the value behind them as specified by {@code value} in the
	 * enumerated type.
	 *
	 * @param other
	 *            the argued right to compare.
	 * @return {@code true} if this right is equal, {@code false} otherwise.
	 */
	public final boolean equal(Rights other) {
		return value == other.value;
	}

	/**
	 * Gets the value of this rank as seen by the protocol.
	 *
	 * @return the protocol value of this rank.
	 */
	public final int getProtocolValue() {
		return protocolValue;
	}

	/**
	 * Gets the value of this rank as seen by the server.
	 *
	 * @return the server value of this rank.
	 */
	public final int getValue() {
		return value;
	}

	/**
	 * Gets the string of the players title corresponding to their rank
	 *
	 * @return the title for this rank.
	 */
	public String getTitle() {
		return rights_title;
	}
}