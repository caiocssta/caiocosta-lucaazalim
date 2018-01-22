package me.caio.HungerGames.Admin;

import java.util.ArrayList;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Type;
import me.skater.tags.Tag;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Mode {
	public ArrayList<Player> admin = new ArrayList<Player>();
	public ArrayList<Player> youtuber = new ArrayList<Player>();
	public ArrayList<Player> pro = new ArrayList<Player>();
	private Main m;
	public ItemStack chest;

	public Mode(Main m) {
		this.m = m;
		this.chest = new ItemStack(Material.CHEST);
		ItemMeta ima = this.chest.getItemMeta();
		ima.setDisplayName(ChatColor.GOLD + "Jogadores vivos " + ChatColor.GRAY + "(Clique)");
		this.chest.setItemMeta(ima);
	}

	public void setAdmin(Player p) {
		if ((!this.admin.contains(p)) || (this.admin.isEmpty())) {
			this.admin.add(p);
		}
		p.setGameMode(GameMode.CREATIVE);
		this.m.vanish.makeVanished(p);
		this.m.vanish.updateVanished();
		p.sendMessage(ChatColor.LIGHT_PURPLE + "Você entrou no modo ADMIN!");
		p.sendMessage(ChatColor.LIGHT_PURPLE + "Você está invisível!");
		p.getInventory().clear();
		p.getInventory().setItem(4, m.listener.adminstack);
		p.updateInventory();
		this.m.getScoreboardManager().updatePlayers();
	}

	public void setYoutuber(Player p) {
		if (this.m.perm.isMvp(p)) {
			if ((!this.pro.contains(p)) || (this.pro.isEmpty())) {
				this.pro.add(p);
			}
		} else if ((this.m.perm.isYouTuber(p)) && ((!this.youtuber.contains(p)) || (this.youtuber.isEmpty()))) {
			this.youtuber.add(p);
		}
		if (!this.m.isNotPlaying(p)) {
			this.m.removeGamer(p);
		}
		p.getInventory().clear();
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(true);
		p.setFlying(true);
		this.m.vanish.makeVanished(p);
		this.m.vanish.updateVanished();
		p.getInventory().addItem(new ItemStack[] { this.chest });
		p.sendMessage(ChatColor.GREEN + "Você entrou no modo espectador!");
		this.m.getScoreboardManager().updatePlayers();
		m.kit.removePlayerKit(p);
		if (m.type != Type.TEAM) {
			me.skater.Main.getInstance().getTagManager().addPlayerTag(p, Tag.SPEC);
		}
	}

	public void setPlayer(Player p) {
		if (this.admin.contains(p)) {
			p.getInventory().clear();
			p.sendMessage(ChatColor.LIGHT_PURPLE + "Você saiu do modo ADMIN!");
			m.listener.sendItems(p);

		}
		if ((this.youtuber.contains(p)) || (this.pro.contains(p))) {
			p.sendMessage(ChatColor.RED + "Você saiu do modo espectador!");
		}
		p.sendMessage(ChatColor.LIGHT_PURPLE + "Você está visível para TODOS!");
		this.admin.remove(p);
		this.youtuber.remove(p);
		this.pro.remove(p);
		p.setGameMode(GameMode.SURVIVAL);
		this.m.vanish.makeVisible(p);
		this.m.vanish.updateVanished();
		this.m.getScoreboardManager().updatePlayers();
		p.updateInventory();
		// this.m.getScoreboardManager().updatePlayer(p);
	}

	public boolean isAdmin(Player p) {
		return this.admin.contains(p);
	}

	public boolean isSpectating(Player p) {
		return (this.admin.contains(p)) || (this.youtuber.contains(p)) || (this.pro.contains(p));
	}

	public boolean isYTPRO(Player p) {
		return (this.youtuber.contains(p)) || (this.pro.contains(p));
	}
}