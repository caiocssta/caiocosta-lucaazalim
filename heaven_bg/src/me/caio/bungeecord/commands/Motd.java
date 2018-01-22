package me.caio.bungeecord.commands;

import me.caio.bungeecord.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Motd extends Command {
	public Main m;

	public Motd(Main m) {
		super("motd");
		this.m = m;
	}

	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (!m.getPermissions().isOwner(player)) {
			player.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Você não possui permissão."));
			return;
		}
		StringBuilder str = new StringBuilder();
		for (int i = 2; i < args.length; i++) {
			str.append(args[i]).append(" ");
		}
		if (args[0].equals("add")) {
			String server = args[1] + ".heavenmc.com.br";
			m.motd.addMotd(server, ChatColor.translateAlternateColorCodes('&', str.toString()));
			m.motd.reloadMotds();
		}

		if (args[0].equals("remove")) {
			m.motd.removeMotd(Integer.parseInt(args[1]));
			m.motd.reloadMotds();
		}

		if (args[0].equals("get")) {
			m.motd.getMotds(player);
		}

	}

	public static String message(String[] value, String join) {
		String message = "";
		String[] arrayOfString = value;
		int j = value.length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString[i];

			message = message + join + s;
		}
		return message.substring(join.length());
	}
}
