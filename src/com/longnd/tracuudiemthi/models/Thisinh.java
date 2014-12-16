package com.longnd.tracuudiemthi.models;

public class Thisinh {
	private String sbd, hoten, diem;
	private float d1, d2, d3;
	

	public Thisinh() {

	}

	public Thisinh(String sbd, String hoten, float d1, float d2, float d3) {
		super();
		this.sbd = sbd;
		this.hoten = hoten;
		this.d1 = d1;
		this.d2 = d2;
		this.d3 = d3;
	}

	public String getSbd() {
		return sbd;
	}

	public void setSbd(String sbd) {
		this.sbd = sbd;
	}

	public String getHoten() {
		return hoten;
	}

	public void setHoten(String hoten) {
		this.hoten = hoten;
	}

	public float getD1() {
		return d1;
	}

	public void setD1(float d1) {
		this.d1 = d1;
	}

	public float getD2() {
		return d2;
	}

	public void setD2(float d2) {
		this.d2 = d2;
	}

	public float getD3() {
		return d3;
	}

	public void setD3(float d3) {
		this.d3 = d3;
	}

	public String getDiem() {
		return diem;
	}

	public void setDiem(String diem) {
		this.diem = diem;
	}

}
