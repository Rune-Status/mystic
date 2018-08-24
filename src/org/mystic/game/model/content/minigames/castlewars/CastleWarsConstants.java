package org.mystic.game.model.content.minigames.castlewars;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;

/**
 * Castle Wars minigame constants
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public final class CastleWarsConstants {

	/**
	 * Represents if the castle wars game is active or not
	 */
	public static boolean GAME_ACTIVE = false;

	/**
	 * The amount of time in game ticks per game
	 */
	public static final int GAME_TIME = 1200;

	/**
	 * The amount of time a lobby is active before starting a new game
	 */
	public static final int LOBBY_TIME = 500;

	/**
	 * The lobby interface id
	 */
	public static final int LOBBY_INTERFACE_ID = 11479;

	/**
	 * The lobby text interface id
	 */
	public static final int LOBBY_TEXT_INTERFACE_ID = 11480;

	/**
	 * The saradomin team's waiting room location
	 */
	public static final Location SARADOMIN_LOBBY = new Location(2377, 9485, 0);

	/**
	 * The saradomin team's bease location
	 */
	public static final Location SARADOMIN_BASE = new Location(2377, 9485, 0);

	/**
	 * The saradomin team's waiting room location
	 */
	public static final Location ZAMORAK_LOBBY = new Location(2421, 9524, 0);

	/**
	 * The saradomin team's base location
	 */
	public static final Location ZAMORAK_BASE = new Location(2421, 9524, 0);

	/**
	 * The default location for castle wars
	 * 
	 * Players will be placed to this location after games are finished or when they
	 * are removed from games for whatever reason
	 */
	public static Location CASTLE_WARS_DEFAULT = new Location(2442, 3090, 0);

	/**
	 * The saradomins team hood item
	 */
	public static final Item SARADOMIN_HOOD = new Item(4513);

	/**
	 * The saradomins team cape item
	 */
	public static final Item SARADOMIN_CAPE = new Item(4514);

	/**
	 * The zamoraks team hood item
	 */
	public static final Item ZAMORAK_HOOD = new Item(4515);

	/**
	 * The zamoraks team cape item
	 */
	public static final Item ZAMORAK_CAPE = new Item(4516);

}
