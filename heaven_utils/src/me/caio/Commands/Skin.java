package me.skater.Commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.skater.Main;
import me.skater.Utils.FakePlayerUtils;
import me.skater.permissions.enums.Group;

public class Skin implements CommandExecutor {

	private Main m;

	public Skin(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("skin")) {
			if ((sender instanceof Player)) {
				final Player p = (Player) sender;
				if (!m.getPermissionManager().hasGroupPermission(p, Group.YOUTUBER)) {
					p.sendMessage(ChatColor.RED + "Você não possui permissão.");
					return true;
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Use /skin <nick>");
					return true;
				}
				final String playerName = args[0];
				if (!FakePlayerUtils.validateName(playerName)) {
					p.sendMessage(ChatColor.RED + "Nick invalido.");
					return true;
				}
				String id = m.name.getUUID(playerName);
				if (id == null) {
					p.sendMessage(ChatColor.RED + "O nick " + playerName + " não existe.");
					return true;
				}

				final UUID uuid = m.name.makeUUID(id);

				new BukkitRunnable() {
					@Override
					public void run() {
						FakePlayerUtils.changePlayerSkin(p, playerName, uuid);
						p.sendMessage(ChatColor.YELLOW + "Você mudou para a skin de " + ChatColor.AQUA + playerName + ChatColor.YELLOW + " com sucesso!");
					}
				}.runTask(m);

			}

		}
		return false;
	}

}
