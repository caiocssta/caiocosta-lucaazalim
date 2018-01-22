package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class Cleardrops implements CommandExecutor {
	public Main m;

	public Cleardrops(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Apenas jogadores.");
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("cleardrops")) {
			if (!this.m.perm.isMod(p)) {
				p.sendMessage(ChatColor.RED + "Você não possui permissão.");
				return true;
			}
			for (Entity e : p.getWorld().getEntities()) {
				if ((e instanceof Item)) {
					e.remove();
				}
			}
			p.sendMessage(ChatColor.GREEN + "Drops limpos!");
		}
		return false;
	}
}
