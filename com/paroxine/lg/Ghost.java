package com.paroxine.lg;

import java.util.Date;

import org.bukkit.entity.Player;

public class Ghost {
	public Player player;
	public long deathTime;
	
	public Ghost(Player player) {
		this.player = player;
		this.deathTime = new Date().getTime();
	}
}
