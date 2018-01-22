package me.skater.Commands;

import java.util.UUID;

import me.skater.Main;
import me.skater.Enums.ServerType;
import me.skater.permissions.enums.Group;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Givekit implements CommandExecutor {
	private Main main;

	public Givekit(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("givekit")) {
			if (((sender instanceof Player)) && (!this.main.getPermissionManager().hasGroupPermission((Player) sender, Group.ADMIN))) {
				return true;
			}
			if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "Use: /givekit player kit");
				return true;
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					Player target = Givekit.this.main.getServer().getPlayer(args[0]);
					UUID uuid = null;
					String uuidString = args[0];
					if (target != null) {
						uuid = target.getUniqueId();
					} else {
						try {
							uuidString = uuidString.replace("-", "");
							uuid = UUID.fromString(uuidString.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
						} catch (Exception localException1) {
							sender.sendMessage("§cErro ao construir uuid " + uuidString);
							return;
						}
					}
					if (uuid == null) {
						sender.sendMessage(ChatColor.RED + "O player não existe.");
						return;
					}
					String kit = args[1].toLowerCase();
					if (!(main.type == ServerType.HG)) {
						main.connect.SQLQuery("INSERT INTO wbc_hungergames.Kits (`Player`, `Kits`) VALUES ('" + uuid.toString().replace("-", "") + "','" + kit + "');");
					} else {
						me.caio.HungerGames.Main.getInstance().kit.giveKit(uuid, kit);
					}
					sender.sendMessage(ChatColor.GREEN + "Player " + args[0] + " recebeu o kit " + kit + " com sucesso.");
				}
			}.runTaskAsynchronously(this.main);
			return true;
		}
		return false;
	}

}
