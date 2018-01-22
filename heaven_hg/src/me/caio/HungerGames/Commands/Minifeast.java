package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Minifeast implements CommandExecutor {
	private Main m;

	public Minifeast(Main m) {
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
		if (label.equalsIgnoreCase("minifeast")) {
			me.caio.HungerGames.Utils.Minifeast mf = new me.caio.HungerGames.Utils.Minifeast(true);
			mf.spawnFloor();
			mf.spawnChests();
			return true;
		}
		return false;
	}
}
