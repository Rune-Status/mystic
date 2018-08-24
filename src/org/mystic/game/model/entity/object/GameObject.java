package org.mystic.game.model.entity.object;

import org.mystic.game.model.entity.Location;

public class GameObject {

	private int id;

	private final Location loc;

	private final byte type;

	private final byte face;

	private final boolean overrideZ;

	public GameObject(final int x, final int y, final int z) {
		this(0, x, y, z, 0, 0);
	}

	public GameObject(final int id, final int x, final int y, final int z, final int type, final int face) {
		this(id, x, y, z, type, face, false);
	}

	public GameObject(final int id, final int x, final int y, final int z, final int type, final int face,
			final boolean overrideZ) {
		this.id = id;
		this.loc = new Location(x, y, z);
		this.type = ((byte) type);
		this.face = ((byte) face);
		this.overrideZ = overrideZ;
	}

	public GameObject(final int id, final Location location, final int type, final int face) {
		this.id = id;
		this.loc = location;
		this.type = ((byte) type);
		this.face = ((byte) face);
		overrideZ = true;
	}

	@Override
	public boolean equals(Object o) {
		if ((o instanceof GameObject)) {
			GameObject g = (GameObject) o;
			return g.getLocation().equals(this.loc);
		}
		return false;
	}

	public final int getFace() {
		return face;
	}

	public final int getId() {
		return id;
	}

	public final Location getLocation() {
		return this.loc;
	}

	public final int getType() {
		return type;
	}

	public final boolean isOverrideZ() {
		return overrideZ;
	}

	public final void setId(int id) {
		this.id = id;
		ObjectManager.send(this);
	}

}