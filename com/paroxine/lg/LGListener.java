package com.paroxine.lg;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LGListener implements Listener {
	
	public LGListener() {
		
	}
	
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent pde) {
		Player player = pde.getEntity();
		Player killer = player.getKiller();
		if (lgPlayerList.getPlayerRole(killer.getName())) {
			
		}
	}
	
	
}
