package com.paroxine.lg;

import java.util.ConcurrentModificationException;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LGListener implements Listener {
	
	private LGPlayerList lgPlayerList;
	private Main plugin;
	
	public LGListener(LGPlayerList lgPlayerList, Main plugin) {
		this.plugin = plugin;
		this.lgPlayerList = lgPlayerList;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void setPlayerList(LGPlayerList lgPlayerList) {
		this.lgPlayerList = lgPlayerList;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent pde) {
		Player player = pde.getEntity();
		Player killer = null;
		try {
			killer = player.getKiller();
		} catch (Exception e) { }
		
		if (killer != null && lgPlayerList.getPlayerRole(killer.getName()) == Role.LOUPGAROU) {
			if (killer.hasPotionEffect(PotionEffectType.SPEED)) {
				killer.removePotionEffect(PotionEffectType.SPEED);
			}
			killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0));
			if (killer.hasPotionEffect(PotionEffectType.ABSORPTION)) {
				killer.removePotionEffect(PotionEffectType.ABSORPTION);
			}
			killer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 0));
		}

		player.teleport(plugin.spawnLoc);
		player.setGameMode(GameMode.SPECTATOR);
		plugin.ghosts.add(new Ghost(player));
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (plugin.ghosts.size() == 0) {
					return;
				}
				try {
					Ghost ghost = plugin.ghosts.get(0);
					long time = new Date().getTime();
					Bukkit.broadcastMessage(Long.toString(time - ghost.deathTime));
					if (time >= ghost.deathTime + 12000) {
						player.setGameMode(GameMode.SURVIVAL);
						plugin.ghosts.remove(ghost);
					}
				} catch (ConcurrentModificationException e) { }
			}
		}, 12*20);
	}	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		for(Ghost ghost : plugin.ghosts) {
			if (ghost.player == player) {
				if (plugin.spawnLoc == null || plugin.spawnLoc.getWorld() != player.getWorld()) {
					World world = player.getWorld();
					Location loc = world.getSpawnLocation();
					player.teleport(new Location(world, loc.getX(), loc.getY(), loc.getZ()));
				} else {
					player.teleport(plugin.spawnLoc);
				}
			}
		}
	}
}