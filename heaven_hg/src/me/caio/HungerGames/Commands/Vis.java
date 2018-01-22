package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vis implements CommandExecutor {
	public Main m;

	public Vis(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("vis")) {
			Player p = (Player) sender;
			if (!this.m.perm.isTrial(p)) {
				p.sendMessage(ChatColor.RED + "Voc� n�o possui permiss�o.");
				return true;
			}
			if (this.m.vanish.isVanished(p)) {
				this.m.vanish.makeVisible(p);
				this.m.vanish.updateVanished();
				p.sendMessage(ChatColor.GREEN + "Voc� ficou visivel agora.");
			} else {
				p.sendMessage(ChatColor.RED + "Voc� j� esta visivel.");
			}
		}
		return false;
	}
}
