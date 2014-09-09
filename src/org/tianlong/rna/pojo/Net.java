package org.tianlong.rna.pojo;

public class Net {
	private String Mname;
	private String Mip;
	private String Mmac;
	private String Mgateway;
	private String Mmask;
	
	public Net(String mname, String mip, String mmac, String mgateway,
			String mmask) {
		super();
		Mname = mname;
		Mip = mip;
		Mmac = mmac;
		Mgateway = mgateway;
		Mmask = mmask;
	}
	public Net() {
		super();
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
}
