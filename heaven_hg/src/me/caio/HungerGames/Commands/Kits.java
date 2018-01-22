package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.KitRotation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kits implements CommandExecutor {
	private Main m;

	public Kits(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("kits")) {
			if (!this.m.perm.isAdmin(p)) {
				p.sendMessage(ChatColor.RED + "Você não possui permissão.");
				return true;
			}
			if (args.length != 1) {
				p.sendMessage(ChatColor.RED + "Use: /kits aceitar/gerar.");
				return true;
			}
			String comando = args[0];
			if (comando.equalsIgnoreCase("gerar")) {
				KitRotation rot = new KitRotation(this.m);
				p.sendMessage(rot.getMessage());
				Main.rotationRequest.put(p.getUniqueId(), rot);
				return true;
			}
			if (comando.equalsIgnoreCase("aceitar")) {
				KitRotation rot = (KitRotation) Main.rotationRequest.get(p.getUniqueId());
				if (rot == null) {
					p.sendMessage(ChatColor.RED + "Não ha nenhuma rotação pendente.");
					return true;
				}
				rot.aplicarRotacao();
				p.sendMessage(rot.getMessage());
				p.sendMessage(ChatColor.GREEN + "Rotação aplicada.");
				return true;
			}
			p.sendMessage(ChatColor.RED + "Use: /kits aceitar/gerar.");

			return true;
		}
		return false;
	}
}
