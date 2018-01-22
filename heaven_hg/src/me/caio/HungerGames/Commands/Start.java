package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Start implements CommandExecutor {
	public Main m;

	public Start(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("start")) {
			if (!(sender instanceof Player)) {
				if (this.m.stage != GameStage.PREGAME) {
					sender.sendMessage(ChatColor.RED + "O torneio ja iniciou!");
					return true;
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!this.m.adm.isSpectating(p)) {
						p.setFlying(false);
						p.setAllowFlight(false);
						this.m.sendToSpawn(p);
					}
				}
				this.m.startGame();
				sender.sendMessage(ChatColor.RED + "Partida iniciada!");
				return true;
			}
			Player p = (Player) sender;
			if (!this.m.perm.isMod(p)) {
				sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
				return true;
			}
			if (this.m.stage != GameStage.PREGAME) {
				sender.sendMessage(ChatColor.RED + "Partida em andamento.");
				return true;
			}
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!this.m.adm.isSpectating(player)) {
					player.setFlying(false);
					player.setAllowFlight(false);
					this.m.sendToSpawn(player);
				}
			}
			this.m.startGame();
			sender.sendMessage(ChatColor.RED + "Partida iniciada");
			return true;
		}
		return false;
	}
}
