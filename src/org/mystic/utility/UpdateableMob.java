package org.mystic.utility;

import org.mystic.game.World;
import org.mystic.game.model.entity.npc.Npc;

/**
 * A updateable mob
 * 
 * @author Michael Sasse
 * 
 */
public class UpdateableMob {

	/**
	 * The amount of players viewing this mob
	 */
	protected int viewed = 1;

	/**
	 * The mob
	 */
	protected final short mob;

	/**
	 * Creates a new (@UpdateableMob)
	 * 
	 * @param mob
	 */
	public UpdateableMob(Npc mob) {
		this.mob = (short) mob.getIndex();
	}

	@Override
	public boolean equals(Object o) {
		return this.mob == ((UpdateableMob) o).mob;
	}

	/**
	 * Gets the mob
	 */
	public Npc getMob() {
		return World.getNpcs()[mob];
	}

}