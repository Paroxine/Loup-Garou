package com.paroxine.lg;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

public class LGPlayerList {
	
	private Map<String, LGPlayer> playerList = new HashMap<String, LGPlayer>(); 
	
	public LGPlayerList() {
		
	}
	
	public void addPlayer(String playerName) {
		if (!playerList.containsKey(playerName)) {
			LGPlayer lgPlayer = new LGPlayer();
			lgPlayer.setRole(Role.randomRole());
			playerList.put(playerName, lgPlayer);	
		}
	}
	
	public Role getPlayerRole(Player player) {
		return getPlayerRole(player.getName());
	}
	
	public Role getPlayerRole(String name) {
		if (playerList.containsKey(name)) {
			return playerList.get(name).getRole();
		}
		else {
			return Role.NONE;
		}
	}
	
	public String[] getPlayerList(Player player) {
		player.sendMessage("In getList");
		player.sendMessage(String.valueOf(this.playerList.size()));
		Set<String> keyset = this.playerList.keySet();
		player.sendMessage("Got the keys.");
		return (String[]) keyset.toArray();
	}
}
