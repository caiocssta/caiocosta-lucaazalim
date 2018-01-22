package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Admin implements CommandExecutor {
	private Main m;

	public Admin(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("admin")) {
			if (!this.m.perm.isTrial(p)) {
				p.sendMessage(ChatColor.RED + "Ops... Você não pode fazer isso.");
				return true;
			}
			if (this.m.adm.isAdmin(p)) {
				this.m.adm.setPlayer(p);
			} else {
				this.m.adm.setAdmin(p);
			}
			return true;
		}
		return false;
	}
}
