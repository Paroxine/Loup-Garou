package com.paroxine.lg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private LGPlayerList lgPlayerList = new LGPlayerList();
	private LGListener lgListener;
	private Logger log;
	private Boolean onGame = false;
	
	public Location spawnLoc = null;
	public ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
	
	@Override
	public void onEnable() {
		log = getLogger();
		log.info("Loup-Garou plugin has been enabled.");
		lgListener = new LGListener(lgPlayerList, this);
	}
	
	@Override
	public void onDisable() {
		log.info("Loup-Garou plugin has been disabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String lowerCmd = cmd.getName().toLowerCase();
		Player player = null;
		try {
			player = (Player)sender;
		} catch (Exception e) { }
		
		switch (lowerCmd) {
		case "roles":
			/*
			player.sendMessage(roleVillageois());
			player.sendMessage(roleLoupGarou());
			*/
			String[] playerNameList = lgPlayerList.getPlayerList();
			for (int i = 0; i < playerNameList.length; i++) {
				String playerName = playerNameList[i];
				player.sendMessage(playerName + " : " + lgPlayerList.getPlayerRole(playerName));
			}
			return true;
		case "role":
			switch (args.length) {
			case 0:
				sendRoleDescription(player, lgPlayerList.getPlayerRole(player.getName()));
				return true;
			case 1:
				switch (args[0].toLowerCase()) {
				case "set":
					player.sendMessage("Veuillez préciser le rôle à attribuer.");
					return true;
				case "player":
					player.sendMessage("Veuillez préciser le joueur.");
					return true;
				default:
					sendRoleDescription(player, args[0]);
					return true;
				}
			case 2:
				switch (args[0].toLowerCase()) {
				case "set":
					this.lgPlayerList.setPlayerRole(player.getName(), args[1].toLowerCase());
					return true;
				case "player":
					sendRoleDescription(player, lgPlayerList.getPlayerRole(args[1]));
					return true;
				default:
					player.sendMessage("Commande inconnue.");
					return true;
				}
			case 3:
				switch (args[0].toLowerCase()) {
				case "set":
					this.lgPlayerList.setPlayerRole(args[2].toLowerCase(), args[1].toLowerCase());
					return true;
				default:
					player.sendMessage("Commande inconnue.");
					return true;
				}
			default:
				player.sendMessage("Nombre d'arguments incorrect.");
				return true;
			}		
		case "lg":
			lg(args, player);
			return true;
		}
		return true;
	}
	
	public void sendRoleDescription(Player player, String roleName) {
		Role role = Role.getRoleByName(roleName);
		switch (role) {
		case VILLAGEOIS:
			player.sendMessage("L'objectif du " + ChatColor.GRAY + "Villageois" + ChatColor.WHITE + " est de venger votre village en tuant tous les loups-garous. Il n'a aucun pouvoir.");
			return;
		case LOUPGAROU:
			player.sendMessage("Le rôle du " + ChatColor.RED + " Loup-Garou " + ChatColor.WHITE + " est de tuer tout les villageois et de rester en vie. Mais prenez garde au loup blanc ! Il se trouve dans votre équipe de loup-garou, mais il vous tuera pour être le dernier survivant ! Chaque nuit, vous disposerez d'un effet de force. Après chaque victime que vous tuerez, vous disposerez d'un effet de force et d'absorption pendant 1 minute.");
			return;
		case SORCIERE:
			player.sendMessage("L'objectif de la " + ChatColor.DARK_PURPLE + "sorcière" + ChatColor.WHITE + " est d\'éliminer les Loups-Garous. Pour ce faire, elle dispose de 3 potions splash d\'Instant Health I, d\'une potion splash de Regeneration I et de 3 potions splash d\'Instant Damage I. Elle a le pouvoir de ressusciter un joueur mort une fois dans la partie.");
			return;
		case NONE:
			player.sendMessage("Vous n'avez pas de role.");
			return;
		default:
			player.sendMessage("Role inconnu : " + ChatColor.YELLOW + roleName);
			return;
		}
	}
	
	public void sendRoleDescription(Player player, Role role) {
		sendRoleDescription(player, Role.getRoleName(role));
	}

	public void start() {
		if (Bukkit.getOnlinePlayers().size() < 3) {
			Bukkit.broadcastMessage("Il doit y avoir au moins 3 joueurs pour jouer.");
			return;
		}
		if (this.onGame) {
			this.stop();
		}
		for (Player p : Bukkit.getOnlinePlayers()){
			this.lgPlayerList.addPlayer(p.getName());
			this.lgListener.setPlayerList(lgPlayerList);
		}
		this.onGame = true;
	}
	
	public void stop() {
		this.lgPlayerList = new LGPlayerList();
		this.lgListener.setPlayerList(this.lgPlayerList);
		this.onGame = false;
	}
	
	public void lg(String[] args, Player player) {
		switch (args[0]) {
		case "start":
			Bukkit.broadcastMessage("[Loup-Garou]Lancement d'une partie...");
			start();
			break;
		case "stop":
			Bukkit.broadcastMessage("[Loup-Garou]Arrêt d'une partie...");
			stop();
			break;
		case "spawn":
			Bukkit.broadcastMessage("[Loup-Garou]Point de réapparition redéfini.");
			this.spawnLoc = player.getLocation();
			break;
		case "action":
			if (args.length < 1) {
				player.sendMessage("Nombre d'arguments incorrect.");
				return;
			} else if (this.lgPlayerList.getPlayerRole(player) == Role.SORCIERE) {
				if (args.length == 1) {
					revive();
				} else if (args.length == 2) {
					revive(args[1]);
				}
			} else {
				player.sendMessage("Vous n'êtes pas une sorcière.");
			}
			break;
		default:
			Bukkit.broadcastMessage("[Loup-Garou]Commande inconnue.");
			this.spawnLoc = player.getLocation();
			break;
		}
	}
	
	public void revive() {
		Iterator<Ghost> iter = this.ghosts.iterator();
		while (iter.hasNext()) {
			Ghost ghost = iter.next();
			ghost.player.setGameMode(GameMode.SURVIVAL);
			iter.remove();
		}
	}
	
	public void revive(String playerName) {
		ArrayList<Ghost> ghostsTemp = new ArrayList<Ghost>(this.ghosts);
		for (Ghost ghost : ghostsTemp) { 
			if (ghost.player.getName().equals(playerName)) {
				ghost.player.setGameMode(GameMode.SURVIVAL);
				this.ghosts.remove(ghost);
				Bukkit.broadcastMessage("Une sorcière a réssucité " + playerName + " !");
				return;
			}
		}
	}
}


//Commentaire inutile