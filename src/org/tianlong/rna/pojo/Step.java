package org.tianlong.rna.pojo;

public class Step {
	private int S_id;
	private String Sname;
	private int Shole;
	private int Sspeed;
	private int Svol;
	private String Swait;
	private String Sblend;
	private String Smagnet;
	private int Sswitch;
	private int Stemp;
	private int E_id;
	public Step(int s_id, String sname, int shole, int sspeed, int svol,
			String swait, String sblend, String smagnet, int sswitch, int stemp, int e_id) {
		super();
		S_id = s_id;
		Sname = sname;
		Shole = shole;
		Sspeed = sspeed;
		Svol = svol;
		Swait = swait;
		Sblend = sblend;
		Smagnet = smagnet;
		Sswitch = sswitch;
		Stemp = stemp;
		E_id = e_id;
	}
	public Step() {
		super();
	}
	public int getS_id() {
		return S_id;
	}
	public void setS_id(int s_id) {
		S_id = s_id;
	}
	public String getSname() {
		return Sname;
	}
	public void setSname(String sname) {
		Sname = sname;
	}
	public int getShole() {
		return Shole;
	}
	public void setShole(int shole) {
		Shole = shole;
	}
	public int getSspeed() {
		return Sspeed;
	}
	public void setSspeed(int sspeed) {
		Sspeed = sspeed;
	}
	public int getSvol() {
		return Svol;
	}
	public void setSvol(int svol) {
		Svol = svol;
	}
	public String getSwait() {
		return Swait;
	}
	public void setSwait(String swait) {
		Swait = swait;
	}
	public String getSblend() {
		return Sblend;
	}
	public void setSblend(String sblend) {
		Sblend = sblend;
	}
	public String getSmagnet() {
		return Smagnet;
	}
	public void setSmagnet(String smagnet) {
		Smagnet = smagnet;
	}
	public int getSswitch() {
		return Sswitch;
	}
	public void setSswitch(int sswitch) {
		Sswitch = sswitch;
	}
	public int getStemp() {
		return Stemp;
	}
	public void setStemp(int stemp) {
		Stemp = stemp;
	}
	public int getE_id() {
		return E_id;
	}
	public void setE_id(int e_id) {
		E_id = e_id;
	}
}
