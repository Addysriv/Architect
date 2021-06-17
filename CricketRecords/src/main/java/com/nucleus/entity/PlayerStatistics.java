package com.nucleus.entity;

import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
public class PlayerStatistics {
	
	private Integer numberOfRunsScored;
	
	private Integer numberOfWickets;
	
	private Integer numberOfMatchesPlayed;

	public Integer getNumberOfRunsScored() {
		return numberOfRunsScored;
	}

	public void setNumberOfRunsScored(Integer numberOfRunsScored) {
		this.numberOfRunsScored = numberOfRunsScored;
	}

	public Integer getNumberOfWickets() {
		return numberOfWickets;
	}

	public void setNumberOfWickets(Integer numberOfWickets) {
		this.numberOfWickets = numberOfWickets;
	}

	public Integer getNumberOfMatchesPlayed() {
		return numberOfMatchesPlayed;
	}

	public void setNumberOfMatchesPlayed(Integer numberOfMatchesPlayed) {
		this.numberOfMatchesPlayed = numberOfMatchesPlayed;
	}
	
	

}
