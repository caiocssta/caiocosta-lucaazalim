package me.skater.Commands;

import java.util.ArrayList;

import me.skater.Main;
import me.skater.Utils.StringUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chat implements CommandExecutor {
	private Main m;

	public Chat(Main m) {
		this.m = m;
	}

	public static ArrayList<String> chatStaff = new ArrayList<String>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("chat")) {
			if ((sender instanceof Player)) {
				Player player = (Player) sender;
				if (!m.perm.isMod(player)) {
					player.sendMessage(ChatColor.RED + "Você não possui permissão.");
					return false;
				}
			}

			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("clear")) {
					for (int i = 0; i < 255; i++) {
						Bukkit.broadcastMessage("");
					}
					Bukkit.broadcastMessage(StringUtils.makeCenteredMessage(""));
					Bukkit.broadcastMessage(StringUtils.makeCenteredMessage("§a§lO chat foi limpo!"));
					Bukkit.broadcastMessage(StringUtils.makeCenteredMessage(""));
				}
				if (args[0].equalsIgnoreCase("off")) {
					if (args.length >= 0) {
						if (m.globalmute) {
							sender.sendMessage("§cO chat já está pausado.");
							return false;
						}
						this.m.globalmute = true;
						Bukkit.broadcastMessage(StringUtils.makeCenteredMessage(""));
						Bukkit.broadcastMessage(StringUtils.makeCenteredMessage("§c§lO chat foi pausado!"));
						Bukkit.broadcastMessage(StringUtils.makeCenteredMessage(""));
					}
				}
				if (args[0].equalsIgnoreCase("on")) {
					if (args.length >= 0) {
						if (!m.globalmute) {
							sender.sendMessage("§cO chat já está habilitado.");
							return false;
						}
						this.m.globalmute = false;
						Bukkit.broadcastMessage(StringUtils.makeCenteredMessage(""));
						Bukkit.broadcastMessage(StringUtils.makeCenteredMessage("§a§lO chat foi habilitado!"));
						Bukkit.broadcastMessage(StringUtils.makeCenteredMessage(""));
					}
				}
			} else {
				sender.sendMessage("§cTente §7- §c/chat off §7| §cPara desativar o chat!");
				sender.sendMessage("§cTente §7- §c/chat on §7| §cPara ativar o chat!");
			}
		}
		return false;
	}
}