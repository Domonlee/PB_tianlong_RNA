package org.tianlong.rna.pojo;

public class User {
	private int U_id;
	private String Uname;
	private String Upass;
	private String UnewPass;
	private String UnewSPass;
	private int Uadmin;
	private int Udefault;
	
	public User(int U_id, String Uname, String Upass, int Udefault, int Uadmin,
			String UnewPass, String UnewSPass) {
		super();
		this.U_id = U_id;
		this.Uname = Uname;
		this.Upass = Upass;
		this.Udefault = Udefault;
		this.UnewPass = UnewPass;
		this.UnewSPass = UnewSPass;
		this.Uadmin = Uadmin;
		
	}
	public User() {
		super();
	}
	public int getU_id() {
		return U_id;
	}
	public void setU_id(int uId) {
		U_id = uId;
	}
	public String getUname() {
		return Uname;
	}
	public void setUname(String uname) {
		Uname = uname;
	}
	public String getUpass() {
		return Upass;
	}
	public void setUpass(String upass) {
		Upass = upass;
	}
	public int getUdefault() {
		return Udefault;
	}
	public void setUdefault(int udefault) {
		Udefault = udefault;
	}
	public String getUnewPass() {
		return UnewPass;
	}
	public void setUnewPass(String unewPass) {
		UnewPass = unewPass;
	}
	public String getUnewSPass() {
		return UnewSPass;
	}
	public void setUnewSPass(String unewSPass) {
		UnewSPass = unewSPass;
	}
	public int getUadmin() {
		return Uadmin;
	}
	public void setUadmin(int uadmin) {
		Uadmin = uadmin;
	}
}
