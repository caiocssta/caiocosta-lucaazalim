package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {
	public Main m;

	public Fly(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Apenas jogadores.");
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("fly")) {
			if (!this.m.perm.isMvp(p)) {
				p.sendMessage(ChatColor.RED + "Você não possui permissão.");
				return true;
			}
			if (this.m.stage != GameStage.PREGAME) {
				p.sendMessage(ChatColor.RED + "Partida em andamento.");
				return true;
			}
			if (this.m.PreGameTimer <= 10) {
				p.sendMessage(ChatColor.RED + "A partida já vai iniciar!");
				return true;
			}
			if (args.length == 0) {
				if (!p.getAllowFlight()) {
					p.sendMessage(ChatColor.GREEN + "Fly habilitado.");
					p.setAllowFlight(true);
					p.setFlying(true);
				} else {
					sender.sendMessage(ChatColor.RED + "Fly desabilitado.");
					p.setAllowFlight(false);
					p.setFlying(false);
				}
			} else if (args.length == 1) {
				if (!this.m.perm.isAdmin(p)) {
					p.sendMessage(ChatColor.RED + "Você não possui permissão.");
					return true;
				}
				Player p2 = Bukkit.getPlayer(args[0]);
				if (p2 == null) {
					sender.sendMessage(ChatColor.RED + "Jogador offline.");
					return true;
				}
				if (!p2.getAllowFlight()) {
					sender.sendMessage(ChatColor.GREEN + "Você habilitou o fly de " + p2.getName());
					p2.sendMessage(ChatColor.GREEN + "Você recebeu fly de " + sender.getName());
					p2.setAllowFlight(true);
					p2.setFlying(true);
				} else {
					sender.sendMessage(ChatColor.RED + "Você desabilitou o fly de " + p2.getName());
					p2.sendMessage(ChatColor.RED + "Seu fly foi desabilitado por " + sender.getName());
					p2.setAllowFlight(false);
					p2.setFlying(false);
				}
			}
		}
		return false;
	}
}
