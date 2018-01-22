package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpall implements CommandExecutor {
	public Main m;

	public Tpall(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("tpall")) {
			if (!this.m.perm.isMod(p)) {
				p.sendMessage(ChatColor.RED + "Você não possui permissão.");
				return true;
			}
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.teleport(p);
			}
			p.sendMessage(ChatColor.RED + "Você puxou " + ChatColor.GRAY + Bukkit.getOnlinePlayers().length + " players.");
			Bukkit.broadcastMessage(ChatColor.YELLOW + p.getName() + " puxou todos os jogadores.");
		}
		return false;
	}
}
