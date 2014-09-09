package org.tianlong.rna.pojo;

public class Machine {
	private int M_id;
	private int Mlanguage;
	private int Mflux;
	private float Mblend;
	private float Mmagnet;
	private float Mhole;
	private String Mname;
	private String Mip;
	private String Mmac;
	private String Mgateway;
	private String Mmask;
	private String MDtime;

	public Machine() {
		super();
	}

	public Machine(int m_id, int mlanguage, int mflux, float mblend,
			float mmagnet, float mhole, String mname, String mip, String mmac,
			String mgateway, String mmask, String mDtime) {
		super();
		M_id = m_id;
		Mlanguage = mlanguage;
		Mflux = mflux;
		Mblend = mblend;
		Mmagnet = mmagnet;
		Mhole = mhole;
		Mname = mname;
		Mip = mip;
		Mmac = mmac;
		Mgateway = mgateway;
		Mmask = mmask;
		MDtime = mDtime;
	}

	public int getM_id() {
		return M_id;
	}

	public void setM_id(int m_id) {
		M_id = m_id;
	}

	public int getMlanguage() {
		return Mlanguage;
	}

	public void setMlanguage(int mlanguage) {
		Mlanguage = mlanguage;
	}

	public int getMflux() {
		return Mflux;
	}

	public void setMflux(int mflux) {
		Mflux = mflux;
	}

	public float getMblend() {
		return Mblend;
	}

	public void setMblend(float mblend) {
		Mblend = mblend;
	}

	public float getMmagnet() {
		return Mmagnet;
	}

	public void setMmagnet(float mmagnet) {
		Mmagnet = mmagnet;
	}

	public float getMhole() {
		return Mhole;
	}

	public void setMhole(float mhole) {
		Mhole = mhole;
	}

	public String getMname() {
		return Mname;
	}

	public void setMname(String mname) {
		Mname = mname;
	}

	public String getMip() {
		return Mip;
	}

	public void setMip(String mip) {
		Mip = mip;
	}

	public String getMmac() {
		return Mmac;
	}

	public void setMmac(String mmac) {
		Mmac = mmac;
	}

	public String getMgateway() {
		return Mgateway;
	}

	public void setMgateway(String mgateway) {
		Mgateway = mgateway;
	}

	public String getMmask() {
		return Mmask;
	}

	public void setMmask(String mmask) {
		Mmask = mmask;
	}

	public String getMDtime() {
		return MDtime;
	}

	public void setMDtime(String mDtime) {
		MDtime = mDtime;
	}
}
