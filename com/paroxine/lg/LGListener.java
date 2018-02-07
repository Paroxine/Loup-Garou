package com.paroxine.lg;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LGListener implements Listener {
	
	private LGPlayerList lgPlayerList;
	
	public LGListener(LGPlayerList lgPlayerList) {
		this.lgPlayerList = lgPlayerList;
	}
	
	public void setPlayerList(LGPlayerList lgPlayerList) {
		this.lgPlayerList = lgPlayerList;
	}
	
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent pde) {
		Player player = pde.getEntity();
		Player killer = player.getKiller();
		if (lgPlayerList.getPlayerRole(killer.getName()) == Role.LOUPGAROU) {
			killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10, 1));
		}
	}
	
	
}
