package com.nucleus.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
public class Venue {
	
	private String stadiumName;
	
	private Location location;
	
	private Long seatingCapacity;
	
	@OneToMany
	private List<String> facilities;
	
	private Pitch pitchType;

	public String getStadiumName() {
		return stadiumName;
	}

	public void setStadiumName(String stadiumName) {
		this.stadiumName = stadiumName;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Long getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(Long seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public List<String> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<String> facilities) {
		this.facilities = facilities;
	}

	public Pitch getPitchType() {
		return pitchType;
	}

	public void setPitchType(Pitch pitchType) {
		this.pitchType = pitchType;
	}
	
	

}
