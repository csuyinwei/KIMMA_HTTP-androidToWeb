package com.example.modle;

public class iBeacon {
	
	private String address;
	private String uuid;
	private int major;
	private int minor;
	private int RSSI;
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}
	public int getMinor() {
		return minor;
	}
	public void setMinor(int minor) {
		this.minor = minor;
	}
	public int getRSSI() {
		return RSSI;
	}
	public void setRSSI(int rSSI) {
		RSSI = rSSI;
	}


}
