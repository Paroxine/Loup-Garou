package com.paroxine.lg;

import java.util.Random;

public enum Role {
	VILLAGEOIS,
	LOUPGAROU,
	SORCIERE,
	NONE,
	UNDEFINED;
	
	private static Random random = new Random();
	
	public static Role randomRole() {
		return values()[random.nextInt(values().length-2)];
	}
	
	public static Role getRoleByName(String name) {
		switch (name.toLowerCase()) {
			case "villageois":
				return VILLAGEOIS;
			case "loup-garou":
				return LOUPGAROU;
			case "sorcière":
				return SORCIERE;
			case "aucun":
				return NONE;
			default:
				return UNDEFINED;
		}
	}
	
	public static String getRoleName(Role role) {
		switch  (role) {
			case VILLAGEOIS:
				return "Villageois";
			case LOUPGAROU:
				return "Loup-Garou";
			case SORCIERE:
				return "Sorcière";
			case NONE:
				return "Aucun";
			default:
				return "";
		}
	}
}
