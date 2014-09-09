package org.tianlong.rna.pojo;

public class Experiment {
	private int E_id;
	private String Ename;
	private String Cdate;
	private String Rdate;
	private String Eremark;
	private int Equick;
	private int EDE_id;
	private int U_id;
	
	public Experiment(int eId, String ename, String cdate, String rdate,
			String Eremark,int Equick,int ede_id,int U_id) {
		super();
		this.E_id = eId;
		this.Ename = ename;
		this.Cdate = cdate;
		this.Rdate = rdate;
		this.Eremark = Eremark;
		this.Equick = Equick;
		this.EDE_id = ede_id;
		this.U_id = U_id;
	}
	
	public Experiment() {
		super();
	}
	public int getEquick() {
		return Equick;
	}

	public void setEquick(int equick) {
		Equick = equick;
	}
	public int getE_id() {
		return E_id;
	}
	public void setE_id(int eId) {
		E_id = eId;
	}
	public String getEname() {
		return Ename;
	}
	public void setEname(String ename) {
		Ename = ename;
	}
	public String getCdate() {
		return Cdate;
	}
	public void setCdate(String cdate) {
		Cdate = cdate;
	}
	public String getRdate() {
		return Rdate;
	}
	public void setRdate(String rdate) {
		Rdate = rdate;
	}
	public String getEremark() {
		return Eremark;
	}
	public void setEremark(String Eremark) {
		this.Eremark = Eremark;
	}

	public int getEDE_id() {
		return EDE_id;
	}
	public void setEDE_id(int eDE_id) {
		EDE_id = eDE_id;
	}
	public int getU_id() {
		return U_id;
	}

	public void setU_id(int u_id) {
		U_id = u_id;
	}
}
