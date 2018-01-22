package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Invsee implements CommandExecutor {
	public Main m;

	public Invsee(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("invsee")) {
			Player p = (Player) sender;
			if (!this.m.perm.isTrial(p)) {
				p.sendMessage(ChatColor.RED + "Você não possui permissão.");
				return true;
			}
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "Use /invsee player");
			} else if (args.length == 1) {
				Player targetPlayer = player.getServer().getPlayer(args[0]);
				Inventory targetPlayerInventory = targetPlayer.getInventory();
				player.openInventory(targetPlayerInventory);
			}
		}
		return false;
	}
}
