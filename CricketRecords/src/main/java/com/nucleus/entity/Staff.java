package com.nucleus.entity;

import javax.persistence.Entity;

@Entity
public class Staff {
	
	private String staffName;
	
	private String staffTYpe;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffTYpe() {
		return staffTYpe;
	}

	public void setStaffTYpe(String staffTYpe) {
		this.staffTYpe = staffTYpe;
	}

	
}
