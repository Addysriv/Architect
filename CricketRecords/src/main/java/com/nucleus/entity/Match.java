package com.nucleus.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
public class Match {
	
	private Team firstTeam;
	
	private Team secondTeam;
	
	private LocalDateTime dateTime;
	
	private MatchStage matchStage;   // Final, Semi-Final, Quarter-Final
	
	private MatchType matchType; // T20, OneDay, Test
	
	private Toss tossStats;
	
	private List<Umpire> umpires;
	
	private List<String> commentators;
	
	private Integer numberOfOvers;       //Total number of overs in match
	
	private MatchFormat matchFormat;   //Day, DayNight
	
	private MatchDecision matchDecision;    // Win/Cancel/Draw
	
	private Score firstTeamScore;
	
	private Score secondTeamScore;
	
	private Venue venue;
	
	private String winningTeam;
	
	private Statistics stats;

	public Team getFirstTeam() {
		return firstTeam;
	}

	public void setFirstTeam(Team firstTeam) {
		this.firstTeam = firstTeam;
	}

	public Team getSecondTeam() {
		return secondTeam;
	}

	public void setSecondTeam(Team secondTeam) {
		this.secondTeam = secondTeam;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public MatchStage getMatchStage() {
		return matchStage;
	}

	public void setMatchStage(MatchStage matchStage) {
		this.matchStage = matchStage;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	public List<Umpire> getUmpires() {
		return umpires;
	}

	public void setUmpires(List<Umpire> umpires) {
		this.umpires = umpires;
	}

	public List<String> getCommentators() {
		return commentators;
	}

	public void setCommentators(List<String> commentators) {
		this.commentators = commentators;
	}

	public Integer getNumberOfOvers() {
		return numberOfOvers;
	}

	public void setNumberOfOvers(Integer numberOfOvers) {
		this.numberOfOvers = numberOfOvers;
	}

	public MatchFormat getMatchFormat() {
		return matchFormat;
	}

	public void setMatchFormat(MatchFormat matchFormat) {
		this.matchFormat = matchFormat;
	}

	public MatchDecision getMatchDecision() {
		return matchDecision;
	}

	public void setMatchDecision(MatchDecision matchDecision) {
		this.matchDecision = matchDecision;
	}

	public Score getFirstTeamScore() {
		return firstTeamScore;
	}

	public void setFirstTeamScore(Score firstTeamScore) {
		this.firstTeamScore = firstTeamScore;
	}

	public Score getSecondTeamScore() {
		return secondTeamScore;
	}

	public void setSecondTeamScore(Score secondTeamScore) {
		this.secondTeamScore = secondTeamScore;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public String getWinningTeam() {
		return winningTeam;
	}

	public void setWinningTeam(String winningTeam) {
		this.winningTeam = winningTeam;
	}

	public Toss getTossStats() {
		return tossStats;
	}

	public void setTossStats(Toss tossStats) {
		this.tossStats = tossStats;
	}

	public Statistics getStats() {
		return stats;
	}

	public void setStats(Statistics stats) {
		this.stats = stats;
	}

	

}
