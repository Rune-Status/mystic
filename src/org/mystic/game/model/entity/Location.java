package org.mystic.game.model.entity;

import org.mystic.game.model.entity.Directions.NormalDirection;
import org.mystic.utility.Misc;

/**
 * Represents a single location
 * 
 * @author Michael Sasse
 * 
 */
public class Location {

	/**
	 * Creates a location.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param z
	 *            The z coordinate.
	 * @return The location.
	 */
	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}

	private short x = 0;

	private short y = 0;

	private short z = 0;

	/**
	 * Constructs an empty location
	 */
	public Location() {
	}

	/**
	 * Creates a new location with an x and y coordinate
	 * 
	 * @param x
	 * @param y
	 */
	public Location(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * Constructs a new location
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @param z
	 *            The z coordinate
	 */
	public Location(int x, int y, int z) {
		this.x = ((short) x);
		this.y = ((short) y);
		this.z = ((short) z);
	}

	/**
	 * Constructs a new location based on another location
	 * 
	 * @param other
	 *            The other location
	 * @param z
	 *            The z coordinate
	 */
	public Location(Location other) {
		this.x = ((short) other.getX());
		this.y = ((short) other.getY());
		this.z = ((short) other.getZ());
	}

	/**
	 * Constructs a new location based on another location
	 * 
	 * @param other
	 *            The other location
	 * @param z
	 *            The z coordinate
	 */
	public Location(Location other, int z) {
		this.x = ((short) other.getX());
		this.y = ((short) other.getY());
		this.z = ((short) z);
	}

	public Location copy() {
		return new Location(x, y, z);
	}

	public NormalDirection direction(Location next) {
		return Directions.directionFor(this, next);
	}

	@Override
	public boolean equals(Object other) {
		if ((other instanceof Location)) {
			Location p = (Location) other;
			return (x == p.x) && (y == p.y) && (z == p.z);
		}
		return false;
	}

	/**
	 * Gets the location x for this location
	 * 
	 * @return
	 */
	public int getLocalX() {
		return getLocalX(this);
	};

	/**
	 * Gets the location x for the provided location
	 * 
	 * @param base
	 *            The location to get the local x for
	 * @return
	 */
	public int getLocalX(Location base) {
		return x - 8 * base.getRegionX();
	}

	/**
	 * Gets the local y for this location
	 * 
	 * @return
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the location y for the provided location
	 * 
	 * @param base
	 *            The location to get the local y for
	 * @return
	 */
	public int getLocalY(Location base) {
		return y - 8 * base.getRegionY();
	}

	public int getRegionId() {
		return ((getRegionX() << 8) + getRegionY());
	}

	/**
	 * Gets the region x position
	 * 
	 * @return
	 */
	public int getRegionX() {
		return (x >> 3) - 6;
	}

	/**
	 * Gets the region y position
	 * 
	 * @return
	 */
	public int getRegionY() {
		return (y >> 3) - 6;
	}

	/**
	 * Gets the x coordinate
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	public int getXInChunk() {
		return x & 0x7;
	}

	public int getXInRegion() {
		return x & 0x3F;
	}

	/**
	 * Gets the y coordinate
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	public int getYInChunk() {
		return y & 0x7;
	}

	public int getYInRegion() {
		return y & 0x3F;
	}

	/**
	 * Gets the z coordinate
	 * 
	 * @return
	 */
	public int getZ() {
		return z;
	}

	/**
	 * The player is in the king black dragon area
	 * 
	 * @return
	 */
	public boolean inKingBlackDragonArea() {
		return x >= 2250 && x <= 2290 && y >= 4670 && y <= 4714;
	}

	/**
	 * This location is viewable from the other location
	 * 
	 * @param other
	 *            The other location
	 * @return The location is viewable from this location
	 */
	public boolean isViewableFrom(Location other) {
		Location p = Misc.delta(this, other);
		return (other.z == z) && (p.x <= 14) && (p.x >= -15) && (p.y <= 14) && (p.y >= -15);
	}

	/**
	 * Checks if this position is within distance of another position.
	 * 
	 * @param position
	 *            the position to check the distance for.
	 * @param distance
	 *            the distance to check.
	 * @return true if this position is within the distance of another position.
	 */
	public boolean withinDistance(Location position, int distance) {
		if (this.getZ() != position.getZ()) {
			return false;
		}
		return Math.abs(position.getX() - this.getX()) <= distance
				&& Math.abs(position.getY() - this.getY()) <= distance;
	}

	/**
	 * Moves the coordinate by the provided values
	 * 
	 * @param amountX
	 *            The amount of x coordinates to move
	 * @param amountY
	 *            The amount of y coordinates to move
	 */
	public void move(int amountX, int amountY) {
		x = ((short) (x + amountX));
		y = ((short) (y + amountY));
	}

	/**
	 * Sets this location as another location
	 * 
	 * @param other
	 */
	public void setAs(Location other) {
		x = other.x;
		y = other.y;
		z = other.z;
	}

	/**
	 * Sets the y coordinate
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = ((short) x);
	}

	/**
	 * Sets the y coordinate
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = ((short) y);
	}

	/**
	 * Sets the z coordinate
	 * 
	 * @param z
	 */
	public Location setZ(int z) {
		this.z = ((short) z);
		return this;
	}

	@Override
	public String toString() {
		return "Location(" + x + ", " + y + ", " + z + ")";
	}

}