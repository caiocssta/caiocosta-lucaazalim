package me.caio.bungeecord.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Connect extends Command {
	public Connect() {
		super("connect");
	}

	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (args.length != 1) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Use: /connect <ip>"));
			return;
		}
		String server = args[0];
		ServerInfo info = ProxyServer.getInstance().getServerInfo(server);
		if (info == null) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Servidor não encontrado."));
			return;
		}
		sender.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Conectando..."));
		player.connect(info);
	}
}
