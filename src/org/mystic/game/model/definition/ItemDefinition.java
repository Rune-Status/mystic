package org.mystic.game.model.definition;

public class ItemDefinition {

	private String name;
	private short id;
	private int value;
	private boolean members;
	private boolean tradable;
	private boolean stackable;
	private boolean note;
	private short noteId;

	public boolean canNote() {
		return noteId != -1;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getNoteId() {
		return noteId;
	}

	public int getValue() {
		return value;
	}

	public boolean isMembers() {
		return members;
	}

	public boolean isNote() {
		return note;
	}

	public boolean isStackable() {
		return stackable;
	}

	public boolean isTradable() {
		return tradable;
	}

	public void setNoteId(int noteId) {
		this.noteId = (short) noteId;
	}

	public void setUntradable() {
		tradable = false;
	}
}
