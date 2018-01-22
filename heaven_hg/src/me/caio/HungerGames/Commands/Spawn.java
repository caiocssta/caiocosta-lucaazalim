package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
	private Main m;

	public Spawn(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("spawn")) {
			Player p = (Player) sender;
			if (this.m.stage != GameStage.PREGAME) {
				sender.sendMessage(ChatColor.RED + "Partida em andamento.");
			} else {
				this.m.sendToSpawnAnyway(p);
			}
			return true;
		}
		return false;
	}
}
