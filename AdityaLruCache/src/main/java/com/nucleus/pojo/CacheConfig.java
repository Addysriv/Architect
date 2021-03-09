package com.nucleus.pojo;


public class CacheConfig {

	private int capacity;
	private long expiryTime;
	private int thresholdSize;
	
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public long getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;
	}
	public int getThresholdSize() {
		return thresholdSize;
	}
	public void setThresholdSize(int thresholdSize) {
		this.thresholdSize = thresholdSize;
	}
	
	
	
}
