package me.skater.Commands;

import java.util.UUID;

import me.skater.Main;
import me.skater.permissions.enums.Group;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Removekit implements CommandExecutor {
	private Main main;

	public Removekit(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("removekit")) {
			if (((sender instanceof Player)) && (!this.main.getPermissionManager().hasGroupPermission((Player) sender, Group.ADMIN))) {
				return true;
			}
			if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "Use: /removekit player kit");
				return true;
			}
			if (args[0].length() > 16) {
				sender.sendMessage(ChatColor.RED + "O nome do player nao pode ter mais que 16 letras.");
				return true;
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					Player target = Removekit.this.main.getServer().getPlayer(args[0]);
					UUID uuid = null;
					if (target != null) {
						uuid = target.getUniqueId();
					} else {
						try {
							uuid = main.name.makeUUID(main.name.getUUID(args[0]));
						} catch (Exception localException1) {
						}
					}
					if (uuid == null) {
						sender.sendMessage(ChatColor.RED + "O player não existe.");
						return;
					}
					String kit = args[1].toLowerCase();
					main.connect.SQLQuery("DELETE FROM wbc_hungergames.Kits WHERE Player='" + uuid.toString().replace("-", "") + "' AND Kits='" + kit + "';");
					sender.sendMessage(ChatColor.GREEN + "Player " + args[0] + " perdeu o kit " + kit + " com sucesso.");
				}
			}.runTaskAsynchronously(this.main);
			return true;
		}
		return false;
	}

}
