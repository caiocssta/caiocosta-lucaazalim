package me.skater.permissions.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.skater.Main;
import me.skater.permissions.PermissionManager;
import me.skater.permissions.enums.Group;

public class GroupSet implements CommandExecutor, TabCompleter {
	private PermissionManager manager;
	private Main m;

	public GroupSet(PermissionManager manager, Main main) {
		this.manager = manager;
		this.m = main;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("group")) {
			if (this.manager.hasGroupPermission(player, Group.DONO)) {
				if (args.length == 2) {
					List<String> str = new ArrayList<String>();
					for (Group group : Group.values()) {
						str.add(group.toString().toLowerCase());
					}
					return str;
				}
			} else if ((this.manager.hasGroupPermission(player, Group.ADMIN)) && (args.length == 2)) {
				List<String> str = new ArrayList<String>();
				str.add(Group.NORMAL.toString().toLowerCase());
				str.add(Group.VIP.toString().toLowerCase());
				str.add(Group.MVP.toString().toLowerCase());
				str.add(Group.PRO.toString().toLowerCase());
				str.add(Group.BETA.toString().toLowerCase());
				str.add(Group.YOUTUBER.toString().toLowerCase());
				str.add(Group.TRIAL.toString().toLowerCase());
				str.add(Group.MOD.toString().toLowerCase());
				return str;
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("group")) {
			if ((sender instanceof Player)) {
				Player player = (Player) sender;
				if (!this.manager.hasGroupPermission(player, Group.ADMIN)) {
					player.sendMessage(ChatColor.RED + "Voce nao possui permissao para usar este comando!");
					return true;
				}
			}
		/*	if (args.length != 3) {
				sender.sendMessage(ChatColor.RED + "Use: /group player grupo");
				return true;
			}
		*///	final Player target = this.manager.getServer().getPlayer(args[0]);
			Group groupo = null;
			try {
				groupo = Group.valueOf(args[1].toUpperCase());
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Este grupo nao existe!");
				return true;
			}

			final Group group = groupo;
			if ((sender instanceof Player)) {
				Player player = (Player) sender;
				if ((this.manager.getPlayerGroup(player) == Group.ADMIN)
						&& ((group == Group.ADMIN) || (group == Group.DONO))) {
					player.sendMessage(ChatColor.RED + "Desculpe, mas voce nao pode manipular estes grupos!");
					return true;
				}
			}
			
			final String[] argss = args;
			final CommandSender senderr = sender;
			
			new BukkitRunnable() {
				@Override
				public void run() {
					String uuid = argss[0];
					if (isValidMD5(uuid)) {
						if (args.length == 2) {
							try {
								GroupSet.this.manager.savePlayerGroup(uuid, group);
							} catch (SQLException e) {
								senderr.sendMessage("§cErro ao setar grupo de " + argss[0]);
								e.printStackTrace();
								return;
							}
							senderr.sendMessage(ChatColor.GREEN + "Player " + argss[0] + " setado como "
									+ group.toString() + " permanentemente!");
							return;
						} else if (args.length > 2) {
							Integer time = Integer.parseInt(args[2]);
							try {
								GroupSet.this.manager.savePlayerGroup(uuid, group, time);
							} catch (SQLException e) {
								senderr.sendMessage("§cErro ao setar grupo de " + argss[0]);
								e.printStackTrace();
								return;
							}
							senderr.sendMessage(ChatColor.GREEN + "Player " + argss[0] + " setado como "
									+ group.toString() + " com sucesso, por " + getStatsTime(time) +  " !");
							return;
						} else {
							senderr.sendMessage("§cSintaxe incorreta");
						}
					}
					Player target = Bukkit.getPlayer(uuid);
				//	if (target == null) {
				//		senderr.sendMessage("§cJogador offline");
						
				//	}
				/*	if ((senderr instanceof Player)) {
						Player player = (Player) senderr;
						if ((GroupSet.this.manager.getPlayerGroup(target) == Group.DONO)
								&& (GroupSet.this.manager.getPlayerGroup(player) == Group.ADMIN)) {
							senderr.sendMessage(ChatColor.RED + "Voce nao pode mudar o grupo de um dono.");
							return;
						}
					}
				*/	try {
						if (args.length > 2) {
							Integer time = Integer.parseInt(args[2]);
							GroupSet.this.manager.setPlayerGroup(m.getUUIDFetcher().getUUIDFromString(m.name.getUUID(args[0])), group);
							GroupSet.this.manager.savePlayerGroup(m.getUUIDFetcher().getUUID(m.name.getUUID(args[0])).toString(), group, time);
						} else {
							GroupSet.this.manager.setPlayerGroup(m.getUUIDFetcher().getUUIDFromString(m.name.getUUID(args[0])), group);
							GroupSet.this.manager.savePlayerGroup(m.getUUIDFetcher().getUUID(m.name.getUUID(args[0])).toString(), group);
						}
					} catch (SQLException e) {
						senderr.sendMessage("§cErro ao setar grupo de " + target.getUniqueId());
						e.printStackTrace();

					}
					senderr.sendMessage(ChatColor.GREEN + "Player " + argss[0] + " setado como " + group.toString()
							+ " com sucesso!");

				}

			}.runTask(JavaPlugin.getPlugin(Main.class));
		}
		return true;
	}

	public String getStatsTime(Integer input) {
		int month = (int) Math.floor(input / 2592000);
		int days = (int) Math.floor((input % 2592000) / 86400);
		int hours = (int) Math.floor(((input % 2592000) % 3600) / 3600);
		int minutes = (int) Math.floor(((input % 2592000) % 3600) / 60);
		int seconds = (int) Math.floor(((input % 2592000) % 3600) % 60);

		String sec = seconds + "s" + (seconds > 1 ? "" : "");
		String min = minutes + "m" + (minutes > 1 ? "" : "");
		String hora = hours + "h" + (hours > 1 ? "" : "");
		String dia = days + "d" + (days > 1 ? "" : "");
		String mes = month + "m" + (month > 1 ? "" : "");

		if (month > 0) {
			return mes + (days > 0 ? " " + dia : "") + (hours > 0 ? " " + hora : "") + (minutes > 0 ? " " + min : "") + (seconds > 0 ? " e " + sec : "");
		} else if (days > 0) {
			return dia + (hours > 0 ? " " + hora : "") + (minutes > 0 ? " " + min : "") + (seconds > 0 ? " e " + sec : "");
		} else if (hours > 0) {
			return hora + (minutes > 0 ? " " + min : "") + (seconds > 0 ? " e " + sec : "");
		} else if (minutes > 0) {
			return min + (seconds > 0 ? " e " + sec : "");
		} else {
			return sec;
		}
	}

	public boolean isValidMD5(String s) {
		return s.matches("[a-fA-F0-9]{32}");
	}
}
