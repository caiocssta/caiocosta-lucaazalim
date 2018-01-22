package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Utils.Feast;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FFeast implements CommandExecutor {
	private Main m;

	public FFeast(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce tem que ser um Player");
		}
		Player p = (Player) sender;
		if (!this.m.perm.isMod(p)) {
			sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
			return true;
		}
		if (label.equalsIgnoreCase("ffeast")) {
			Feast f = new Feast(1,  true);
			f.startCountdown();
		//	this.m.getServer().broadcastMessage(ChatColor.RED + "O feast spawnou em (" + f.central.getX() + ", " + f.central.getY() + ", " + f.central.getZ() + ").");
			return true;
		}
		return false;
	}
}
