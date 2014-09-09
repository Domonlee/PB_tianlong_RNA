package org.tianlong.rna.pojo;

public class DefaultStep {
	private int DS_id;
	private String DSname;
	private int DShole;
	private int DSspeed;
	private int DSvol;
	private String DSwait;
	private String DSblend;
	private String DSmagnet;
	private int DSswitch;
	private int DStemp;
	private int DE_id;
	public DefaultStep(int dS_id, String dSname, int dShole, int dSspeed,
			int dSvol, String dSwait, String dSblend, String dSmagnet,
			int dSswitch, int dStemp, int dE_id) {
		super();
		DS_id = dS_id;
		DSname = dSname;
		DShole = dShole;
		DSspeed = dSspeed;
		DSvol = dSvol;
		DSwait = dSwait;
		DSblend = dSblend;
		DSmagnet = dSmagnet;
		DSswitch = dSswitch;
		DStemp = dStemp;
		DE_id = dE_id;
	}
	public DefaultStep() {
		super();
	}
	public int getDS_id() {
		return DS_id;
	}
	public void setDS_id(int dS_id) {
		DS_id = dS_id;
	}
	public String getDSname() {
		return DSname;
	}
	public void setDSname(String dSname) {
		DSname = dSname;
	}
	public int getDShole() {
		return DShole;
	}
	public void setDShole(int dShole) {
		DShole = dShole;
	}
	public int getDSspeed() {
		return DSspeed;
	}
	public void setDSspeed(int dSspeed) {
		DSspeed = dSspeed;
	}
	public int getDSvol() {
		return DSvol;
	}
	public void setDSvol(int dSvol) {
		DSvol = dSvol;
	}
	public String getDSwait() {
		return DSwait;
	}
	public void setDSwait(String dSwait) {
		DSwait = dSwait;
	}
	public String getDSblend() {
		return DSblend;
	}
	public void setDSblend(String dSblend) {
		DSblend = dSblend;
	}
	public String getDSmagnet() {
		return DSmagnet;
	}
	public void setDSmagnet(String dSmagnet) {
		DSmagnet = dSmagnet;
	}
	public int getDSswitch() {
		return DSswitch;
	}
	public void setDSswitch(int dSswitch) {
		DSswitch = dSswitch;
	}
	public int getDStemp() {
		return DStemp;
	}
	public void setDStemp(int dStemp) {
		DStemp = dStemp;
	}
	public int getDE_id() {
		return DE_id;
	}
	public void setDE_id(int dE_id) {
		DE_id = dE_id;
	}
}
