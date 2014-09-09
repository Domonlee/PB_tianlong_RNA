package org.tianlong.rna.pojo;

public class Instrument {
	private int flux;
	private float blend;
	private float magnet;
	private float hole;
	
	public Instrument(int flux, float blend, float magnet, float hole) {
		super();
		this.flux = flux;
		this.blend = blend;
		this.magnet = magnet;
		this.hole = hole;
	}
	
	public Instrument() {
		super();
	}
	
	public int getFlux() {
		return flux;
	}
	public void setFlux(int flux) {
		this.flux = flux;
	}
	public float getBlend() {
		return blend;
	}
	public void setBlend(float blend) {
		this.blend = blend;
	}
	public float getMagnet() {
		return magnet;
	}
	public void setMagnet(float magnet) {
		this.magnet = magnet;
	}
	public float getHole() {
		return hole;
	}
	public void setHole(float hole) {
		this.hole = hole;
	}
}
