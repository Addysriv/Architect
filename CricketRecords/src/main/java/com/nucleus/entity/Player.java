package com.nucleus.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
public class Player {
	
	private Country country;
	
	private String fullName;
	
	private PlayerType playerType;   //all rounder, batsman [righty, lefty], bowler [type of bowler]
	
	private PlayerStatistics playerStatistics;
	
	@ManyToOne
	private List<Team> teams;    // present in number of teams
	
	@ManyToOne
	private List<Tournament> tournamentPlayed;
	
	

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}

	public PlayerStatistics getPlayerStatistics() {
		return playerStatistics;
	}

	public void setPlayerStatistics(PlayerStatistics playerStatistics) {
		this.playerStatistics = playerStatistics;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public List<Tournament> getTournamentPlayed() {
		return tournamentPlayed;
	}

	public void setTournamentPlayed(List<Tournament> tournamentPlayed) {
		this.tournamentPlayed = tournamentPlayed;
	}
	
	

}
