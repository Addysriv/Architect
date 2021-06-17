package com.nucleus.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
public class Team {
	
	private String teamName;
	
	@OneToMany
	private List<Player> playingPlayers;
	
	@OneToMany
	private List<Player> reservedPlayers;
	
	@OneToMany
	private List<Staff>  staff;

	private Player captain;
	
	private Player wicketKeeper;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<Player> getPlayingPlayers() {
		return playingPlayers;
	}

	public void setPlayingPlayers(List<Player> playingPlayers) {
		this.playingPlayers = playingPlayers;
	}

	public List<Player> getReservedPlayers() {
		return reservedPlayers;
	}

	public void setReservedPlayers(List<Player> reservedPlayers) {
		this.reservedPlayers = reservedPlayers;
	}

	public List<Staff> getStaff() {
		return staff;
	}

	public void setStaff(List<Staff> staff) {
		this.staff = staff;
	}

	public Player getCaptain() {
		return captain;
	}

	public void setCaptain(Player captain) {
		this.captain = captain;
	}

	public Player getWicketKeeper() {
		return wicketKeeper;
	}

	public void setWicketKeeper(Player wicketKeeper) {
		this.wicketKeeper = wicketKeeper;
	}
	
	
}
