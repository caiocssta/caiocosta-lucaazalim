package me.caio.bungeecord;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import me.caio.bungeecord.clan.ClanManager;
import me.caio.bungeecord.commands.Aviso;
import me.caio.bungeecord.commands.Ban;
import me.caio.bungeecord.commands.Broadcast;
import me.caio.bungeecord.commands.Connect;
import me.caio.bungeecord.commands.Ip;
import me.caio.bungeecord.commands.Lobby;
import me.caio.bungeecord.commands.Motd;
import me.caio.bungeecord.commands.Pfind;
import me.caio.bungeecord.commands.Ping;
import me.caio.bungeecord.commands.Report;
import me.caio.bungeecord.commands.Track;
import me.caio.bungeecord.mysql.NameFetcher;
import me.caio.bungeecord.permissions.Permissions;
import me.caio.bungeecord.socket.ServerInfo;
import me.caio.bungeecord.socket.ServerListPing;
import me.caio.bungeecord.utils.BroadcastUtils;
import me.caio.bungeecord.utils.MotdUtils;
import me.caio.bungeecord.utils.StringUtils;
import me.caio.bungeecord.utils.json.JSONException;
import me.caio.bungeecord.utils.json.JSONObject;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener {
	public static ArrayList<ServerInfo> infos;
	private Permissions permissions;
	public me.caio.bungeecord.mysql.Connect connect;
	public Connection mainConnection;
	public NameFetcher name;
	private boolean running = true;
	public int multiplier = 1;
	// private List<String> hgmotd;
	// private List<String> teamhgmotd;
	// private List<String> mcmotd;
	public MotdUtils motd;
	public BroadcastUtils broadcast;
	public ClanManager clan;

	public void onEnable() {
		BungeeCord.getInstance().registerChannel("Clan");
		BungeeCord.getInstance().registerChannel("BungeeCord");
		this.connect = new me.caio.bungeecord.mysql.Connect(this);
		this.mainConnection = this.connect.trySQLConnection();
		this.motd = new MotdUtils(this);
		this.broadcast = new BroadcastUtils(this);
		this.broadcast.startBroadcast();
		this.permissions = new Permissions(this);
		this.name = new NameFetcher(this);
		this.clan = new ClanManager(this);
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerCommand(this, new Ban(this));
//		getProxy().getPluginManager().registerCommand(this, new Unban(this));
		getProxy().getPluginManager().registerCommand(this, new Connect());
		getProxy().getPluginManager().registerCommand(this, new Ip());
		getProxy().getPluginManager().registerCommand(this, new Ping());
		getProxy().getPluginManager().registerCommand(this, new Pfind(this));
		getProxy().getPluginManager().registerCommand(this, new Track(this));
		getProxy().getPluginManager().registerCommand(this, new Motd(this));
		getProxy().getPluginManager().registerCommand(this, new Broadcast(this));
		getProxy().getPluginManager().registerCommand(this, new Report(this));
		getProxy().getPluginManager().registerCommand(this, new Aviso(this));
		getProxy().getPluginManager().registerCommand(this, new Lobby(this));

		getProxy().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {
				System.gc();
				try {
					PreparedStatement stmt = mainConnection.prepareStatement("SELECT * FROM `multiplier`;");
					ResultSet result = stmt.executeQuery();
					if (result.next()) {
						multiplier = result.getInt("multiplier");
					}
					result.close();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro ao carregar multiplier");
				}
			}
		}, 0, 5, TimeUnit.MINUTES);
		getProxy().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {
				int hgplayers = 0;
				int thgplayer = 0;
				int lbplayers = 0;
				int pvpplayers = 0;
				int uhgplayers = 0;
				for (net.md_5.bungee.api.config.ServerInfo i : getProxy().getServers().values()) {
					if (i.getName().startsWith("pvp")) {
						pvpplayers = pvpplayers + i.getPlayers().size();
					} else if (i.getAddress().getPort() >= 25500) {
						lbplayers = lbplayers + i.getPlayers().size();
					} else if (i.getAddress().getPort() >= 2400) {
						hgplayers = hgplayers + i.getPlayers().size();
					} else if (i.getAddress().getPort() >= 3200) {
						uhgplayers = uhgplayers + i.getPlayers().size();
					} else if (i.getAddress().getPort() >= 2200) {
						thgplayer = thgplayer + i.getPlayers().size();
					}
				}
				try {
					JSONObject json = new JSONObject();
					json.put("HG", hgplayers);
					json.put("THG", thgplayer);
					json.put("LB", lbplayers);
					json.put("PVP", pvpplayers);
					json.put("UHG", uhgplayers);
					for (net.md_5.bungee.api.config.ServerInfo i : getProxy().getServers().values()) {
						if (i.getAddress().getPort() >= 25500) {
							sendToBukkit("BungeeCord", "players", json.toString(), i);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, 0, 10, TimeUnit.SECONDS);
		infos = new ArrayList<ServerInfo>();
		for (Map.Entry<String, net.md_5.bungee.api.config.ServerInfo> entrys : getProxy().getServers().entrySet()) {
			String serverName = (String) entrys.getKey();
			net.md_5.bungee.api.config.ServerInfo info = (net.md_5.bungee.api.config.ServerInfo) entrys.getValue();
			infos.add(new ServerInfo(serverName, info.getAddress()));
		}
		this.running = true;
		getProxy().getScheduler().runAsync(this, new Runnable() {
			public void run() {
				while (Main.this.running) {
					for (ServerInfo info : Main.infos) {
						ServerListPing ping = new ServerListPing();
						ping.setAddress(info.getAddress());
						ServerListPing.StatusResponse response = null;
						try {
							response = ping.fetchData();
							info.setOnline(true);
							info.setMotd(response.getDescription());
							info.setPlayersOnline(response.getPlayers().getOnline());
							info.setMaxPlayers(response.getPlayers().getMax());
						} catch (IOException e) {
							info.setMotd("Servidor §coffline§7...");
							info.setPlayersOnline(0);
							info.setMaxPlayers(0);
							info.setOnline(false);
						}
						if (info.isOnline()) {
						} else {
						}
					}
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void onDisable() {
		this.running = false;
		this.connect.SQLdisconnect();
	}

	public ServerInfo firstHG() {
		ServerInfo conexao = null;
		for (ServerInfo info : infos) {
			if (info.isOnline()) {
				if (info.getName().endsWith(".hg.heavenmc.com.br") || info.getName().endsWith(".heaven-mc.com")) {
					if (info.getMotd().contains("Aguardando") || (info.getMotd().contains("iniciando"))) {
						if (info.getPlayersOnline() >= info.getMaxPlayers()) {
						}
						conexao = info;
					}
				}
			}
		}
		return conexao;
	}

	public ServerInfo getPvP() {
		for (ServerInfo info : infos) {
			if (info.isOnline()) {
				if (info.getName().startsWith("pvp.")) {
					if (!(info.getPlayersOnline() >= info.getMaxPlayers())) {
						return info;
					}
				}
			}
		}
		return null;
	}

	public ServerInfo getCopa() {
		for (ServerInfo info : infos) {
			if (info.isOnline()) {
				if (info.getName().startsWith("copa.")) {
					if (!(info.getPlayersOnline() >= info.getMaxPlayers())) {
						return info;
					}
				}
			}
		}
		return null;
	}

	public ServerInfo getMiniCopa() {
		for (ServerInfo info : infos) {
			if (info.isOnline()) {
				if (info.getName().startsWith("minicopa.")) {
					if (!(info.getPlayersOnline() >= info.getMaxPlayers())) {
						return info;
					}
				}
			}
		}
		return null;
	}

	public ServerInfo getLobby() {
		ArrayList<ServerInfo> lobbys = new ArrayList<>();
		for (ServerInfo info : infos) {
			if (info.isOnline()) {
				if (info.getName().startsWith("l")) {
					if (!(info.getPlayersOnline() >= info.getMaxPlayers())) {
						lobbys.add(info);
					}
				}
			}
		}
		if (lobbys.size() == 0) {
			return null;
		}
		return lobbys.get(new Random().nextInt(lobbys.size()));
	}

	public ServerInfo firstCHG() {
		ServerInfo conexao = null;
		for (ServerInfo info : infos) {
			if (info.isOnline()) {
				if (info.getName().endsWith(".copa.heavenmc.com.br")) {
					if (info.getMotd().contains("Aguardando") || (info.getMotd().contains("iniciando"))) {
						if (info.getPlayersOnline() >= info.getMaxPlayers()) {
						}
						conexao = info;
					}
				}
			}
		}
		return conexao;
	}

	public ServerInfo firstTHG() {
		ServerInfo conexao = null;
		for (ServerInfo info : infos) {
			if (info.isOnline()) {
				if (info.getName().startsWith("b")) {
					if (info.getMotd().contains("Aguardando") || (info.getMotd().contains("iniciando"))) {
						if (info.getPlayersOnline() >= info.getMaxPlayers()) {
						}
						conexao = info;
					}
				}
			}
		}
		return conexao;
	}

	public ServerInfo firstUHG() {
		ServerInfo conexao = null;
		for (ServerInfo info : infos) {
			if (info.isOnline()) {
				if (info.getName().startsWith("c")) {
					if (info.getMotd().contains("Aguardando") || (info.getMotd().contains("iniciando"))) {
						if (info.getPlayersOnline() >= info.getMaxPlayers()) {
						}
						conexao = info;
					}
				}
			}
		}
		return conexao;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLogin(final ServerConnectEvent event) {
		final ProxiedPlayer p = event.getPlayer();
		if (p.getServer() != null) {
			return;
		}
		
		try {
			name.updateAPI(p);
		} catch (SQLException e2) {
			System.out.println("Erro ao atualizar NameFetcher do player " + p.getDisplayName());
		}
		
		String s = p.getPendingConnection().getVirtualHost().getHostName();
		getProxy().getScheduler().runAsync(this, new Runnable() {
			@Override
			public void run() {
				try {
					PreparedStatement stmt = mainConnection.prepareStatement("SELECT * FROM heaven_bans WHERE ban_uuid='"
							+ event.getPlayer().getUniqueId().toString().replace("-", "") + "'");
					ResultSet rs = stmt.executeQuery();
					if (rs.next()) {
						if (rs.getBoolean("tempban")) {
							if ((rs.getLong("ban_time") + 1000 * rs.getInt("ban_left")) > System.currentTimeMillis()) {
								String time = getStatsTime(Math.round(-((System.currentTimeMillis()
										- (rs.getLong("ban_time") + 1000 * rs.getInt("ban_left"))) / 1000L)));
								event.setCancelled(true);
								event.getPlayer().disconnect(getTempBanMessage(event.getPlayer().getUniqueId(),
										rs.getString("ban_reason"), time, rs.getString("banned_by")));
							} else {
								stmt.execute("DELETE FROM heaven_bans WHERE ban_uuid='"
										+ event.getPlayer().getUniqueId().toString().replace("-", "") + "'");
								return;
							}
						} else {
							event.getPlayer().disconnect(getBanMessage(event.getPlayer().getUniqueId(),
									rs.getString("banned_by"), rs.getString("ban_reason")));
							event.setCancelled(true);

							return;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				if (permissions.isLoaded(event.getPlayer())) {
					permissions.unloadPlayer(event.getPlayer());
					System.out.println("Grupo removido " + event.getPlayer().getName());
				}

				try {
					permissions.loadProxiedPlayerGroup(event.getPlayer());
				} catch (SQLException e1) {
					System.out.println("Falha ao carregar grupo de " + event.getPlayer().getName() + " " + e1);
					event.getPlayer().disconnect(
							TextComponent.fromLegacyText(ChatColor.RED + "Nao foi possivel carregar suas permissoes."));

				}

				try {
					clan.loadPlayerClan(p);
				} catch (SQLException e) {
					event.getPlayer().disconnect(
							TextComponent.fromLegacyText(ChatColor.RED + "Nao foi possivel carregar seu clan."));
				}
			}

		});

		if (s.equalsIgnoreCase("heavenmc.com.br")) {
			ServerInfo conexao = getLobby();
			if (conexao == null) {
				event.getPlayer().disconnect(ChatColor.RED + "Nenhum servidor disponivel.");
				return;
			}
			event.setTarget(getProxy().getServerInfo(conexao.getName()));
			return;
		}

		if (s.startsWith("a") || s.startsWith("b") || s.startsWith("c") || s.startsWith("pvp") || s.startsWith("mc")
				|| s.startsWith("hc") || s.startsWith("copa") || s.startsWith("minicopa") || s.endsWith(".copa.heavenmc.com.br")) {

			for (ServerInfo info : infos) {
				if (info.isOnline()) {
					if (info.getName().equalsIgnoreCase(s)) {
						if (!s.startsWith("heaven")) {
							event.setTarget(getProxy().getServerInfo(info.getName()));
						} else {
							net.md_5.bungee.api.config.ServerInfo se = getProxy().getServerInfo(info.getName());
							if (se == null) {
								ServerInfo conexao = null;
								boolean b = new Random().nextBoolean();
								if (b) {
									conexao = firstHG();
								} else {
									conexao = firstHG();
								}
								if (conexao == null) {
									event.setTarget(getProxy().getServerInfo(getLobby().getName()));
									return;
								}
								event.setTarget(getProxy().getServerInfo(conexao.getName()));
							} else {
								event.setTarget(getProxy().getServerInfo(info.getName()));
							}
						}
					}
				}
			}
		}

		if (s.equalsIgnoreCase("hg.heavenmc.com.br")) {
			ServerInfo conexao = firstHG();
			if (conexao == null) {
				conexao = getLobby();
				if (conexao == null) {
					event.getPlayer().disconnect(ChatColor.RED + "Nenhum servidor disponivel.");
					return;
				}
				event.setTarget(getProxy().getServerInfo(getLobby().getName()));
				return;
			}
			event.setTarget(getProxy().getServerInfo(conexao.getName()));
		} else if (s.equalsIgnoreCase("teamhg.heavenmc.com.br")) {
			ServerInfo conexao = firstTHG();
			if (conexao == null) {
				conexao = getLobby();
				if (conexao == null) {
					event.getPlayer().disconnect(ChatColor.RED + "Nenhum servidor disponivel.");
					return;
				}
				event.setTarget(getProxy().getServerInfo(getLobby().getName()));
				return;
			}
			event.setTarget(getProxy().getServerInfo(conexao.getName()));
		} else if (s.equalsIgnoreCase("ultrahg.HeavenMC.com.br")) {
			ServerInfo conexao = firstUHG();
			if (conexao == null) {
				conexao = getLobby();
				if (conexao == null) {
					event.getPlayer().disconnect(ChatColor.RED + "Nenhum servidor disponivel.");
					return;
				}
				event.setTarget(getProxy().getServerInfo(getLobby().getName()));
				return;
			}
			event.setTarget(getProxy().getServerInfo(conexao.getName()));
		} else if (s.equalsIgnoreCase("copa.heavenmc.com.br")) {
			ServerInfo conexao = firstCHG();
			if (conexao == null) {
				event.setTarget(getProxy().getServerInfo(getLobby().getName()));
				return;
			}
			event.setTarget(getProxy().getServerInfo(conexao.getName()));
		}

	}

	@EventHandler
	public void disconnect(ServerKickEvent event) {
		if (event.getKickedFrom().getName().equalsIgnoreCase("hc.heavenmc.com.br")
				|| event.getKickedFrom().getName().equalsIgnoreCase("copa.heavenmc.com.br")) {
			event.setCancelServer(null);
		}
	}

	@EventHandler
	public void onPing(ProxyPingEvent event) {
		if (event.getConnection() == null) {
			return;
		}
		if (event.getConnection().getVirtualHost() == null) {
			return;
		}
		String s = event.getConnection().getVirtualHost().getHostName();
		if (s.isEmpty()) {
			return;
		}
		ServerPing ping = event.getResponse();
		if (s.equalsIgnoreCase("heavenmc.com.br") || s.equalsIgnoreCase("heaven-mc.com")) {
			ping.setPlayers(new ServerPing.Players(getProxy().getOnlineCount() + 1, getProxy().getOnlineCount(), null));
			if (multiplier == 1) {
				ping.setDescription(StringUtils.makeCenteredMotd("§f§lHeaven§6§lMC") + "\n§r"
						+ StringUtils.makeCenteredMotd(motd.getRandomMotd(s.toLowerCase())));

			} else {
				ping.setDescription(StringUtils.makeCenteredMotd("§f§lHeaven§6§lMC") + "\n§r"
						+ StringUtils.makeCenteredMotd("§e§lSERVIDORES DE CARA NOVA"));
			}
			return;
		} else if (s.equalsIgnoreCase("hg.heavenmc.com.br")) {
			int n = 0;
			for (ServerInfo si : infos) {
				if (si.getName().endsWith(".hg.heavenmc.com.br")) {
					n = n + si.getPlayersOnline();
				}
			}
			ping.setPlayers(new ServerPing.Players(n + 1, n, null));
			if (multiplier == 1) {
				ping.setDescription(StringUtils.makeCenteredMotd("§fHeaven§6MC") + "\n§r"
						+ StringUtils.makeCenteredMotd(motd.getRandomMotd(s.toLowerCase())));

			} else {
				ping.setDescription(StringUtils.makeCenteredMotd("§fHeaven§6MC") + "\n§r"
						+ StringUtils.makeCenteredMotd("§e§lVEM VER NOSSO NOVO HG"));

			}
			return;
		} else if (s.equalsIgnoreCase("teamhg.heavenmc.com.br")) {
			int n = 0;
			for (ServerInfo si : infos) {
				if (si.getName().endsWith(".teamhg.heavenmc.com.br")) {
					n = n + si.getPlayersOnline();
				}
			}
			ping.setPlayers(new ServerPing.Players(n + 1, n, null));
			if (multiplier == 1) {
				ping.setDescription(StringUtils.makeCenteredMotd("§f§lHeaven§6§lMC §cTeam Hardcore Games")
						+ "\n§r" + StringUtils.makeCenteredMotd(motd.getRandomMotd(s.toLowerCase())));
			} else {
				ping.setDescription(StringUtils.makeCenteredMotd("§f§lHeaven§6§lMC §cTeam Hardcore Games")
						+ "\n§r"
						+ StringUtils.makeCenteredMotd("§e§lSERVIDORES DE CARA NOVA"));

			}
			return;
		} else if (s.equalsIgnoreCase("ultrahg.HeavenMC.com.br")) {
			int n = 0;
			for (ServerInfo si : infos) {
				if (si.getName().endsWith(".ultrahg.HeavenMC.com.br")) {
					n = n + si.getPlayersOnline();
				}
			}
			ping.setPlayers(new ServerPing.Players(n + 1, n, null));
			if (multiplier == 1) {
				ping.setDescription(StringUtils.makeCenteredMotd("§fHeavenMC Network§8: §cUltra Hardcore Games")
						+ "\n§r" + StringUtils.makeCenteredMotd(motd.getRandomMotd(s.toLowerCase())));
			} else {
				ping.setDescription(StringUtils.makeCenteredMotd("§fHeavenMC Network§8: §cUltra Hardcore Games")
						+ "\n§r"
						+ StringUtils.makeCenteredMotd("§e§lSERVIDORES EM §b§l" + multiplier + "X §e§lCOINS E XP"));

			}
			return;
		} else if (s.equalsIgnoreCase("pvp.HeavenMC.com.br")) {
			int n = 0;
			for (ServerInfo si : infos) {
				if (si.getName().endsWith("pvp.HeavenMC.com.br")) {
					n = n + si.getPlayersOnline();
				}
			}
			ping.setPlayers(new ServerPing.Players(n + 1, n, null));
			ping.setDescription(StringUtils.makeCenteredMotd("§fHeavenMC Network§8: §cPvP") + "\n§r"
					+ StringUtils.makeCenteredMotd(motd.getRandomMotd(s.toLowerCase())));

			return;
		}

		else if (s.equalsIgnoreCase("copa.HeavenMC.com.br")) {
			int n = 0;
			for (ServerInfo si : infos) {
				if (si.getName().endsWith("copa.HeavenMC.com.br")) {
					n = n + si.getPlayersOnline();
				}
			}
			ping.setPlayers(new ServerPing.Players(n + 1, n, null));
			ping.setDescription(StringUtils.makeCenteredMotd("§fHeavenMC Network§8: §cCopa TeamHG") + "\n§r"
					+ StringUtils.makeCenteredMotd(motd.getRandomMotd(s.toLowerCase())));

			return;
		} else if (s.equalsIgnoreCase("minicopa.HeavenMC.com.br")) {
			int n = 0;
			for (ServerInfo si : infos) {
				if (si.getName().endsWith("copa.HeavenMC.com.br")) {
					n = n + si.getPlayersOnline();
				}
			}
			ping.setPlayers(new ServerPing.Players(n + 1, n, null));
			ping.setDescription(StringUtils.makeCenteredMotd("§fHeavenMC Network§8: §cMiniCopa") + "\n§r"
					+ StringUtils.makeCenteredMotd(motd.getRandomMotd(s.toLowerCase())));

			return;
		}

		if (s.endsWith("Heavenmc.com.br")) {
			String server = event.getConnection().getVirtualHost().getHostName();
			if (s.endsWith(".Heavenmc.com.br")) {
				for (ServerInfo info : infos) {
					if (info.getName().equalsIgnoreCase(server)) {
						if (info.isOnline()) {
							ping.setPlayers(
									new ServerPing.Players(info.getMaxPlayers(), info.getPlayersOnline(), null));
							ping.setDescription(info.getMotd());
							break;
						}
						ping.setPlayers(new ServerPing.Players(0, 0, null));
						info.setMotd("§cServidor offline");
						break;
					}
				}
			}
		}
	}

	public Permissions getPermissions() {
		return this.permissions;
	}

	public String getMOTD(String ip, int port) {
		String returnString = null;
		try {
			Socket sock = new Socket();
			sock.setSoTimeout(100);
			sock.connect(new InetSocketAddress(ip, port), 100);

			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			DataInputStream in = new DataInputStream(sock.getInputStream());

			out.write(0xFE);

			int b;
			StringBuffer str = new StringBuffer();
			while ((b = in.read()) != -1) {
				if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
					str.append((char) b);
				}
			}

			String[] data = str.toString().split("§");
			String serverMotd = data[0];

			returnString = String.format(serverMotd);

			sock.close();

		} catch (ConnectException e) {

		} catch (UnknownHostException e) {

		} catch (IOException e) {

		}
		return returnString;
	}

	public void sendToBukkit(String a, String channel, String message, net.md_5.bungee.api.config.ServerInfo server) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		try {
			out.writeUTF(channel);
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.sendData(a, stream.toByteArray());

	}

	@EventHandler
	public void onPluginMessage(PluginMessageEvent ev) {
		if (!ev.getTag().equals("BungeeCord")) {
			return;
		}
		if (!(ev.getSender() instanceof Server)) {
			return;
		}
		if (!(ev.getReceiver() instanceof ProxiedPlayer)) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(ev.getData());
		String subchannel = in.readUTF();
		System.out.println("RECEBENDO " + subchannel);
		if (subchannel.equals("HardcoreGames")) {
			ev.setCancelled(true);
			ServerInfo connectServer = firstHG();
			if (connectServer == null) {
				((ProxiedPlayer) ev.getReceiver())
						.sendMessage(TextComponent.fromLegacyText("§cNenhum servidor disponível!\nAguarde um pouco."));
				return;
			}
			((ProxiedPlayer) ev.getReceiver()).connect(getProxy().getServerInfo(connectServer.getName()));
			return;
		}
		if (subchannel.equals("TeamHardcoreGames")) {
			ev.setCancelled(true);
			ServerInfo connectServer = firstTHG();
			if (connectServer == null) {
				((ProxiedPlayer) ev.getReceiver())
						.sendMessage(TextComponent.fromLegacyText("§cNenhum servidor disponivel!\nAguarde um pouco."));
				return;
			}
			((ProxiedPlayer) ev.getReceiver()).connect(getProxy().getServerInfo(connectServer.getName()));
			return;
		}
		if (subchannel.equalsIgnoreCase("PvP")) {
			ev.setCancelled(true);
			ServerInfo connectServer = getPvP();
			if (connectServer == null) {
				((ProxiedPlayer) ev.getReceiver())
						.sendMessage(TextComponent.fromLegacyText("§cServidor indisponivel!\nAguarde um pouco."));
				return;
			}
			((ProxiedPlayer) ev.getReceiver()).connect(getProxy().getServerInfo(connectServer.getName()));
			return;
		}
		if (subchannel.equalsIgnoreCase("UltraHardcoreGames")) {
			ev.setCancelled(true);
			ServerInfo connectServer = firstUHG();
			if (connectServer == null) {
				((ProxiedPlayer) ev.getReceiver())
						.sendMessage(TextComponent.fromLegacyText("§cServidor indisponivel!\nAguarde um pouco."));
				return;
			}
			((ProxiedPlayer) ev.getReceiver()).connect(getProxy().getServerInfo(connectServer.getName()));
			return;
		}
	}

	//
	// @EventHandler
	// public void onPluginMessage(PluginMessageEvent ev)
	// {
	// if (!ev.getTag().equals("BungeeCord")) {
	// return;
	// }
	// if (!(ev.getSender() instanceof Server)) {
	// return;
	// }
	// if (!(ev.getReceiver() instanceof ProxiedPlayer)) {
	// return;
	// }
	// ByteArrayDataInput in = ByteStreams.newDataInput(ev.getData());
	// String subchannel = in.readUTF();
	//
	// if (subchannel.equals("clanName"))
	// {
	// net.md_5.bungee.api.config.ServerInfo server = BungeeCord.getInstance()
	// .getPlayer(ev.getReceiver().toString()).getServer()
	// .getInfo();
	// String receiver = ev.getReceiver().toString();
	// if(getProxy().getPlayer(receiver) != null) {
	// ProxiedPlayer p = ProxyServer.getInstance().getPlayer(receiver);
	// sendToBukkit("clanName", clan.getPlayerClanName(p), server);
	// }
	// }
	//
	// if (subchannel.equals("clanTag"))
	// {
	// net.md_5.bungee.api.config.ServerInfo server = BungeeCord.getInstance()
	// .getPlayer(ev.getReceiver().toString()).getServer()
	// .getInfo();
	// String receiver = ev.getReceiver().toString();
	// if(getProxy().getPlayer(receiver) != null) {
	// ProxiedPlayer p = ProxyServer.getInstance().getPlayer(receiver);
	// sendToBukkit("clanTag", clan.getPlayerClanTag(p), server);
	// }
	// }
	// }

	public String getBanMessage(UUID uuid, String by, String reason) {
		return ChatColor.RED + "Sua conta foi suspensa permanentemente por " + by + "\n§aMotivo: " + reason
				+ "\n§6Compre seu unban em: §floja.heavenmc.com.br";
	}

	public String getTempBanMessage(UUID uuid, String reason, String time, String by) {
		return ChatColor.RED + "Sua conta foi suspensa temporariamente por " + by + "\n§cMotivo: " + reason
				+ "\n§cTempo restante:§c " + time;
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
			return mes + (days > 0 ? " " + dia : "") + (hours > 0 ? " " + hora : "") + (minutes > 0 ? " " + min : "")
					+ (seconds > 0 ? " e " + sec : "");
		} else if (days > 0) {
			return dia + (hours > 0 ? " " + hora : "") + (minutes > 0 ? " " + min : "")
					+ (seconds > 0 ? " e " + sec : "");
		} else if (hours > 0) {
			return hora + (minutes > 0 ? " " + min : "") + (seconds > 0 ? " e " + sec : "");
		} else if (minutes > 0) {
			return min + (seconds > 0 ? " e " + sec : "");
		} else {
			return sec;
		}
	}

	public ClanManager getClanManager() {
		return this.clan;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onKick(ServerKickEvent event) {
		if (event.getKickReason().contains("Sua conta")) {
			return;
		}
		ServerInfo lobby = getLobby();
		if (lobby == null) {
			event.getPlayer().disconnect(event.getKickReason());
			return;
		}
		ProxiedPlayer p = event.getPlayer();
		p.sendMessage(TextComponent
				.fromLegacyText(ChatColor.YELLOW + "Voce foi kickado do servidor: " + event.getKickReason()));
		event.setCancelled(true);
		p.connect(getProxy().getServerInfo(lobby.getName()));
	}

}
