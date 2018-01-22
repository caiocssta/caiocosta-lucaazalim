package br.com.wombocraft.lobby.nms;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import br.com.wombocraft.lobby.nms.packets.PacketPlayOutTitle;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PlayerConnection;

public class Title {
	private String title = "";
	private ChatColor titleColor = ChatColor.WHITE;
	private String subtitle = "";
	private ChatColor subtitleColor = ChatColor.WHITE;
	private int fadeInTime = -1;
	private int stayTime = -1;
	private int fadeOutTime = -1;
	private boolean ticks = false;

	public Title(String title) {
		this.title = title;
	}

	public Title(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
	}

	public Title(Title title) {
		this.title = title.title;
		this.subtitle = title.subtitle;
		this.titleColor = title.titleColor;
		this.subtitleColor = title.subtitleColor;
		this.fadeInTime = title.fadeInTime;
		this.fadeOutTime = title.fadeOutTime;
		this.stayTime = title.stayTime;
		this.ticks = title.ticks;
	}

	public Title(String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
		this.title = title;
		this.subtitle = subtitle;
		this.fadeInTime = fadeInTime;
		this.stayTime = stayTime;
		this.fadeOutTime = fadeOutTime;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public void setTitleColor(ChatColor color) {
		this.titleColor = color;
	}

	public void setSubtitleColor(ChatColor color) {
		this.subtitleColor = color;
	}

	public void setFadeInTime(int time) {
		this.fadeInTime = time;
	}

	public void setFadeOutTime(int time) {
		this.fadeOutTime = time;
	}

	public void setStayTime(int time) {
		this.stayTime = time;
	}

	public void setTimingsToTicks() {
		this.ticks = true;
	}

	public void setTimingsToSeconds() {
		this.ticks = false;
	}

	public void send(Player player) {
		resetTitle(player);
		EntityPlayer handle = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = handle.playerConnection;
		if (connection.networkManager.getVersion() < 47) {
			ArrayList<String> linhas = new ArrayList<String>();
			if (!this.title.isEmpty()) {
				linhas.add(this.title);
			}
			if (!this.subtitle.isEmpty()) {
				linhas.add(this.subtitle);
			}
			String[] array = new String[linhas.size()];
			linhas.toArray(array);

			return;
		}
		PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES,
				ChatSerializer.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', this.title) + "\",color:"
						+ this.titleColor.name().toLowerCase() + "}"),
				this.fadeInTime * (this.ticks ? 1 : 20), this.stayTime * (this.ticks ? 1 : 20),
				this.fadeOutTime * (this.ticks ? 1 : 20));
		if ((this.fadeInTime != -1) && (this.fadeOutTime != -1) && (this.stayTime != -1)) {
			connection.sendPacket(packetTitle);
		}
		IChatBaseComponent serializer = ChatSerializer
				.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', this.title) + "\",color:"
						+ this.titleColor.name().toLowerCase() + "}");
		packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, serializer);
		connection.sendPacket(packetTitle);
		if (this.subtitle != "") {
			packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
					ChatSerializer.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', this.subtitle)
							+ "\",color:" + this.subtitleColor.name().toLowerCase() + "}"));
			connection.sendPacket(packetTitle);
		}
	}

	@SuppressWarnings("deprecation")
	public void broadcast() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			send(p);
		}
	}

	public void clearTitle(Player player) {
		EntityPlayer handle = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = handle.playerConnection;
		if (connection.networkManager.getVersion() < 47) {
			return;
		}
		PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, null);
		connection.sendPacket(packetTitle);
	}

	public void resetTitle(Player player) {
		EntityPlayer handle = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = handle.playerConnection;
		if (connection.networkManager.getVersion() < 47) {
			return;
		}
		PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null);
		connection.sendPacket(packetTitle);
	}
}
