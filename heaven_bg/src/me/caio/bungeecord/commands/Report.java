package me.caio.bungeecord.commands;

import java.util.HashMap;
import java.util.UUID;

import me.caio.bungeecord.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Report extends Command {
	public Main m;
	private HashMap<UUID, String> reports;

	public Report(Main m) {
		super("report");
		this.m = m;
		this.reports = new HashMap<>();
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (args.length < 2 || args.length == 1) {
			player.sendMessage(ChatColor.RED + "Use: /report <nick> <motivo>");
			return;
		}
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
		if (target == null) {
			player.sendMessage(ChatColor.RED + args[0] + " esta offline.");
			return;
		}
		if (reports.containsKey(player.getUniqueId())) {
			if (reports.get(player.getUniqueId()).contains(target.getName())) {
				player.sendMessage(ChatColor.RED + target.getName() + " já foi reportado por você.");
				return;
			}
		}
		reports.put(player.getUniqueId(), target.getName());
		StringBuilder str = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			str.append(args[i]).append(" ");
		}
		String server = target.getServer().getInfo().getName().startsWith("l") ? "Lobby" : target.getServer().getInfo().getName().substring(0, 3).toUpperCase().replace(".", "");
		for (ProxiedPlayer staffers : ProxyServer.getInstance().getPlayers()) {
			if (m.getPermissions().isTrial(staffers)) {
				staffers.sendMessage("");
				staffers.sendMessage("   §c§lReport");
				staffers.sendMessage("");
				staffers.sendMessage(" §cPlayer: " + target.getName());
				staffers.sendMessage(" §cMotivo: " + str.toString());
				staffers.sendMessage(" §cServidor: " + server);
				staffers.sendMessage(" §cEnviado por: " + player.getName());
				staffers.sendMessage("");
			}
		}

	}

}
