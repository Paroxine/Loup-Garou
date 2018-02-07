package com.paroxine.lg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

	private LGPlayerList lgPlayerList = new LGPlayerList();
	
	@Override
	public void onEnable() {
		getLogger().info("Loup-Garou plugin has been enabled.");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Loup-Garou plugin has been disabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String lowerCmd = cmd.getName().toLowerCase();
		Player player = (Player)sender;
		
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
					player.sendMessage("Veuillez préciser le joeueur.");
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
			lg(args);
			return true;
		}
		return true;
	}
	
	public void sendRoleDescription(Player player, String roleName) {
		player.sendMessage(roleName);
		Role role = Role.getRoleByName(roleName);
		player.sendMessage(roleName);
		switch (role) {
		case VILLAGEOIS:
			player.sendMessage(roleVillageois());
			return;
		case LOUPGAROU:
			player.sendMessage(roleLoupGarou());
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

	public String roleVillageois() {
		return ("L'objectif du " + ChatColor.GRAY + "Villageois" + ChatColor.WHITE + " est de venger votre village en tuant tous les loups-garous. Il n'a aucun pouvoir.");
	}
	
	public String roleLoupGarou() {
		return ("Le rôle du " + ChatColor.RED + " Loup-Garou " + ChatColor.WHITE + " est de tuer tout les villageois et de rester en vie. Mais prenez garde au loup blanc ! Il se trouve dans votre équipe de loup-garou, mais il vous tuera pour être le dernier survivant ! Chaque nuit, vous disposerez d'un effet de force. Après chaque victime que vous tuerez, vous disposerez d'un effet de force et d'absorption pendant 1 minute.");
	}
	
	public void start() {
		if (Bukkit.getOnlinePlayers().size() < 3) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage("Il doit y avoir au moins 3 joueur pour jouer.");
			}
		}
		for (Player p : Bukkit.getOnlinePlayers()){
			this.lgPlayerList.addPlayer(p.getName());
		}
	}
	
	public void lg(String[] args) {
		switch (args[0]) {
		case "start":
			start();
		}
	}
}