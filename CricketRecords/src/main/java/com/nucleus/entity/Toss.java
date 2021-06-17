package com.nucleus.entity;

import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
public class Toss {

	
	private TossResult tossResult;     // Head/Tails
	
	private String tossWinningTeam;
	
	private TossDecision tossDecision;   // Batting or Balling

	public TossResult getTossResult() {
		return tossResult;
	}

	public void setTossResult(TossResult tossResult) {
		this.tossResult = tossResult;
	}

	public String getTossWinningTeam() {
		return tossWinningTeam;
	}

	public void setTossWinningTeam(String tossWinningTeam) {
		this.tossWinningTeam = tossWinningTeam;
	}

	public TossDecision getTossDecision() {
		return tossDecision;
	}

	public void setTossDecision(TossDecision tossDecision) {
		this.tossDecision = tossDecision;
	}


}
