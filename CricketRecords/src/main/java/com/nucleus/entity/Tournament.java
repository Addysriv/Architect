package com.nucleus.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
public class Tournament {

	private String tournamentName;
	
	private Date tournamentYear;
	
	@OneToMany
	private List<Match> matches;
	
	private Integer numberOfTeams;
	
	private Team tournamentWinner;
	
	private Team runnerUp;

	public String getTournamentName() {
		return tournamentName;
	}

	public void setTournamentName(String tournamentName) {
		this.tournamentName = tournamentName;
	}

	public Date getTournamentYear() {
		return tournamentYear;
	}

	public void setTournamentYear(Date tournamentYear) {
		this.tournamentYear = tournamentYear;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public Integer getNumberOfTeams() {
		return numberOfTeams;
	}

	public void setNumberOfTeams(Integer numberOfTeams) {
		this.numberOfTeams = numberOfTeams;
	}

	public Team getTournamentWinner() {
		return tournamentWinner;
	}

	public void setTournamentWinner(Team tournamentWinner) {
		this.tournamentWinner = tournamentWinner;
	}

	public Team getRunnerUp() {
		return runnerUp;
	}

	public void setRunnerUp(Team runnerUp) {
		this.runnerUp = runnerUp;
	}
	
	
	
}
