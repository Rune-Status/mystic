package org.mystic.game.model.entity;

public class Projectile {

	private int id;
	private int size;
	private int delay;
	private int duration;
	private int startHeight;
	private int endHeight;
	private int curve;

	public Projectile(int id) {
		this.id = ((short) id);
		this.size = 1;
		this.delay = 50;
		this.duration = 75;
		this.startHeight = 43;
		this.endHeight = 31;
		this.curve = 16;
	}

	public Projectile(int id, boolean ranged) {
		this(id);
		this.curve = ((byte) (ranged ? 10 : 16));
	}

	public Projectile(int id, int size, int delay, int duration, int startHeight, int endHeight, int curve) {
		this.id = ((short) id);
		this.size = ((byte) size);
		this.delay = ((byte) delay);
		this.duration = ((byte) duration);
		this.startHeight = ((byte) startHeight);
		this.endHeight = ((byte) endHeight);
		this.curve = ((byte) curve);
	}

	public Projectile(Projectile p) {
		this.id = p.id;
		this.size = p.size;
		this.delay = p.delay;
		this.duration = p.duration;
		this.startHeight = p.startHeight;
		this.endHeight = p.endHeight;
		this.curve = p.curve;
	}

	public final int getCurve() {
		return curve;
	}

	public final int getDelay() {
		return delay;
	}

	public final int getDuration() {
		return duration;
	}

	public final int getEndHeight() {
		return endHeight;
	}

	public final int getId() {
		return id;
	}

	public final int getSize() {
		return size;
	}

	public final int getStartHeight() {
		return startHeight;
	}

	public void setCurve(int curve) {
		this.curve = ((byte) curve);
	}

	public void setDelay(int delay) {
		this.delay = ((byte) delay);
	}

	public void setDuration(int duration) {
		this.duration = ((byte) duration);
	}

	public void setEndHeight(int endHeight) {
		this.endHeight = ((byte) endHeight);
	}

	public void setId(int id) {
		this.id = ((short) id);
	}

	public void setSize(int size) {
		this.size = ((byte) size);
	}

	public void setStartHeight(int startHeight) {
		this.startHeight = ((byte) startHeight);
	}

}