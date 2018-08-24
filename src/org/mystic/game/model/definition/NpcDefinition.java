package org.mystic.game.model.definition;

public class NpcDefinition {

	private String name;
	private short id;
	private short level;
	private byte size;
	private boolean attackable;

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public boolean isAttackable() {
		return attackable;
	}
}
