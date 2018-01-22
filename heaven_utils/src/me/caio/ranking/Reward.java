package me.skater.ranking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

public enum Reward {
	LEVEL0(0, ChatColor.GREEN + "Recompensa para Unranked", Arrays.asList("§8Recompensas", " ",  "§6▸ §8+§100 XP", "UNRANKED")),
	LEVEL1(1, ChatColor.GREEN + "Recompensa para Primary", Arrays.asList("§8Recompensas", " ",  "§6▸ §8+§6100 XP", "§6▸ §8+§6Titulo PRIMARY", "")),
	LEVEL2(2, ChatColor.GREEN + "Recompensa para Advanced", Arrays.asList("§8Recompensas", " ",  "§6▸ §8+§6200 XP", "§6▸ §8+§6Titulo ADVANCED", "")),
	LEVEL3(3, ChatColor.GREEN + "Recompensa para Expert", Arrays.asList("§8Recompensas", " ",  "§6▸ §8+§6400 XP", "§6▸ §8+§6Titulo EXPERT", "")),
	LEVEL4(4, ChatColor.GREEN + "Recompensa para Silver", Arrays.asList("§8Recompensas", " ",  "§6▸ §8+§6500 XP", "§6▸ §8+§6Titulo SILVER", "")),
	LEVEL5(5, ChatColor.GREEN + "Recompensa para Gold", Arrays.asList("§8Recompensas", " ",  "§6▸ §8+§6600 XP", "§6▸ §8+§6Titulo GOLD", "")),
	LEVEL6(6, ChatColor.GREEN + "Recompensa para Diamond", Arrays.asList("§8Recompensas", " ",  "§6▸ §8+§6700 XP", "§6▸ §8+§6Titulo DIAMOND", "")),
	LEVEL7(7, ChatColor.GREEN + "Recompensa para Saphire", Arrays.asList("§8Recompensas", " ",  "§6▸ §8+§6800 XP", "§6▸ §8+§6Titulo SAPHIRE", "")),
	LEVEL8(8, ChatColor.GREEN + "Recompensa para Emerald", Arrays.asList("§8Recompensas", " ",  "§6▸ §8+§6900 XP", "§6▸ §8+§6Titulo EMERALD", "")),
	LEVEL9(9, ChatColor.GREEN + "Recompensa para Crystal", Arrays.asList("§8Recompensas", " ", "§6▸ §8+§7,000 XP", "§6▸ §8+§6Titulo CRISTAL" , "")),
	LEVEL10(10, ChatColor.GREEN + "Recompensa para Elite", Arrays.asList("§8Recompensas", " ", "§6▸ §8+§7,000 XP", "§6▸ §8+§6Titulo ELITE", "")),
	LEVEL11(11, ChatColor.GREEN + "Recompensa para Master", Arrays.asList("§8Recompensas", " ", "§6▸ §8+§7,000 XP", "§6▸ §8+§6Titulo MASTER", "")),
	LEVEL12(12, ChatColor.GREEN + "Recompensa para Leggendary", Arrays.asList("§8Recompensas", " ", "§6▸ §8+§7,000 XP", "§6▸ §8+§6Titulo LEGENDARY", "")),
	LEVEL100(100, ChatColor.GREEN + "Recompensa para §c§lEm breve...", Arrays.asList("§8Recompensas", " ", "§cEm breve.", " "));


	private int id;
	private String prefix;
	private List<String> lore;

	private Reward(int id, String prefix, List<String> lore) {
		this.lore = new ArrayList<>();
		this.id = id;
		this.prefix = prefix;
		this.lore = lore;
	}

	public List<String> getLore() {
		return this.lore;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public int getID() {
		return this.id;
	}

	public static Reward getRewardByID(int id) {
		Reward ptitle = LEVEL0;
		for (Reward title : values()) {
			if (title.getID() == id) {
				ptitle = title;
			}
		}
		return ptitle;
	}
}
