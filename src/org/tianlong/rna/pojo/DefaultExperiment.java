package org.tianlong.rna.pojo;

public class DefaultExperiment {
	private int E_id;
	private String Ename;
	private int Enum;
	
	public DefaultExperiment() {
		super();
	}

	public DefaultExperiment(int e_id, String ename, int num) {
		super();
		E_id = e_id;
		Ename = ename;
		Enum = num;
	}

	public int getE_id() {
		return E_id;
	}

	public void setE_id(int e_id) {
		E_id = e_id;
	}

	public String getEname() {
		return Ename;
	}

	public void setEname(String ename) {
		Ename = ename;
	}

	public int getEnum() {
		return Enum;
	}

	public void setEnum(int enum1) {
		Enum = enum1;
	}
}
