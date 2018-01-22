package me.caio.bungeecord.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ip extends Command {
	public Ip() {
		super("ip");
	}

	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		player.sendMessage(TextComponent.fromLegacyText(ChatColor.GOLD + "Você esta conectado no servidor "
				+ ChatColor.YELLOW + player.getServer().getInfo().getName()));
	}
}
