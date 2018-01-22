package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help implements CommandExecutor {
	public Main m;

	public Help(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.RED.toString() + this.m.getTime(Integer.valueOf(this.m.PreGameTimer)) + ChatColor.GRAY.toString() + " para começar a partida.");
			int size = 0;
			for (Player p : this.m.getServer().getOnlinePlayers()) {
				if (!this.m.adm.isSpectating(p)) {
					size++;
				}
			}
			sender.sendMessage(ChatColor.RED.toString() + size + ChatColor.GRAY + " players online.");
			return true;
		}
		return false;
	}
}
