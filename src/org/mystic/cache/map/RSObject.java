package org.mystic.cache.map;

public class RSObject {

	private final int x, y, z;

	private int id, type, direction;

	public RSObject(int x, int y, int z) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) z;
	}

	public RSObject(int x, int y, int z, int id, int type, int direction) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) z;
		this.id = id;
		this.type = (byte) type;
		this.direction = (byte) direction;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RSObject) {
			RSObject l = (RSObject) o;
			return l.getX() == getX() && l.getY() == getY() && l.getZ() == getZ();
		}
		return false;
	}

	public final int getFace() {
		return direction;
	}

	public final int getId() {
		return id;
	}

	public final int getType() {
		return type;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final int getZ() {
		return z;
	}

	public final void setId(int id) {
		this.id = id;
	}

}