package org.mystic.game.model.entity.player;

public class InterfaceManager {

	private int[] tabs = new int[PlayerConstants.SIDEBAR_INTERFACE_IDS.length];

	public int main = -1;

	public int sub = -1;

	public int chat = -1;

	public int[] getTabs() {
		return tabs;
	}

	public boolean interfaceOpen(int interfaceId) {
		return (main == interfaceId);
	}

	public boolean hasBankOpen() {
		return (main == 5292) && (sub == 5063);
	}

	public boolean hasBOBInventoryOpen() {
		return (main == 2700) && (sub == 5063);
	}

	public boolean hasInterfaceOpen() {
		return main != -1 || sub != -1 || chat != -1;
	}

	public void reset() {
		main = -1;
		sub = -1;
		chat = -1;
	}

	public void setActive(int main, int sub) {
		this.main = main;
		this.sub = sub;
	}

	public void setChat(int chat) {
		this.chat = chat;
	}

	public void setTabId(int slot, int id) {
		tabs[slot] = id;
	}

	public int getMain() {
		return main;
	}

}