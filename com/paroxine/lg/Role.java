package com.paroxine.lg;

import java.util.Random;

public enum Role {
	VILLAGEOIS,
	LOUPGAROU,
	NONE,
	UNDEFINED;
	
	private static Random random = new Random();;
	
	public static Role randomRole() {
		return values()[random.nextInt(values().length)];
	}
	
	public static Role getRoleByName(String name) {
		switch (name.toLowerCase()) {
			case "villageois":
				return VILLAGEOIS;
			case "loup-garou":
				return LOUPGAROU;
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
			case NONE:
				return "Aucun";
			default:
				return "";
		}
	}
}
