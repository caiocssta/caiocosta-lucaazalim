package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Specs implements CommandExecutor {
	public Main m;

	public Specs(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("specs")) {
			Player p = (Player) sender;
			if (this.m.perm.isTrial(p)) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("on")) {
						this.m.vanish.setSpectatorEnabled(p, true);
						this.m.vanish.updateVanished();
						p.sendMessage(ChatColor.GREEN + "Você ativou os espectadores para você.");
					} else if (args[0].equalsIgnoreCase("off")) {
						this.m.vanish.setSpectatorEnabled(p, false);
						this.m.vanish.updateVanished();
						p.sendMessage(ChatColor.RED + "Você desativou os espectadores para você.");
					} else {
						sender.sendMessage(ChatColor.RED + "Use: /specs on/off");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Use: /specs on/off");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
			}
			return true;
		}
		return false;
	}
}
