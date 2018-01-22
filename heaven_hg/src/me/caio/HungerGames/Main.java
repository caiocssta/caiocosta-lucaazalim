package me.caio.HungerGames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.caio.HungerGames.Admin.Mode;
import me.caio.HungerGames.Admin.Vanish;
import me.caio.HungerGames.BungeeCord.IncomingChannel;
import me.caio.HungerGames.Caixas.CaixaManager;
import me.caio.HungerGames.Coliseu.ColiseuManager;
import me.caio.HungerGames.Config.Config;
import me.caio.HungerGames.Config.ConfigEnum;
import me.caio.HungerGames.Constructors.BO3Blocks;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Constructors.KitRotation;
import me.caio.HungerGames.Events.GameStartEvent;
import me.caio.HungerGames.Events.PlayerGainKillEvent;
import me.caio.HungerGames.Events.PlayerStartGameEvent;
import me.caio.HungerGames.Listener.BorderListener;
import me.caio.HungerGames.Listener.CombatLogListener;
import me.caio.HungerGames.Listener.CopaGameListener;
import me.caio.HungerGames.Listener.DamagerFixer;
import me.caio.HungerGames.Listener.Espectador;
import me.caio.HungerGames.Listener.GameListener;
import me.caio.HungerGames.Listener.KnockbackListener;
import me.caio.HungerGames.Listener.PlayerListener;
import me.caio.HungerGames.Listener.TeamGameListener;
import me.caio.HungerGames.Managers.AbilityManager;
import me.caio.HungerGames.Managers.CombatLogManager;
import me.caio.HungerGames.Managers.CommandManager;
import me.caio.HungerGames.Managers.CooldownManager;
import me.caio.HungerGames.Managers.Permissions;
import me.caio.HungerGames.Managers.ReflectionManager;
import me.caio.HungerGames.MySQL.Connect;
import me.caio.HungerGames.MySQL.MySQLCommands;
import me.caio.HungerGames.NMS.TabListManager;
import me.caio.HungerGames.NMS.Title;
import me.caio.HungerGames.NMS.Packets.Injector;
import me.caio.HungerGames.NMS.barapi.BarAPI;
import me.caio.HungerGames.Scoreboard.ScoreboardListener;
import me.caio.HungerGames.Scoreboard.ScoreboardManager;
import me.caio.HungerGames.Team.TeamManager;
import me.caio.HungerGames.Team.Enums.TeamColors;
import me.caio.HungerGames.Utils.Files;
import me.caio.HungerGames.Utils.KitManager;
import me.caio.HungerGames.Utils.Name;
import me.caio.HungerGames.Utils.TerrainControlUpdater;
import me.caio.HungerGames.Utils.Enum.GameStage;
import me.skater.Utils.StringUtils;
import net.minecraft.server.v1_7_R4.WorldServer;

public class Main extends JavaPlugin {
	public Type type;
	public int PreGameTimer = 300;
	public int GameTimer = 0;
	public Integer Invenci;
	public GameStage stage = GameStage.PREGAME;
	public Files files;
	public KitManager kit;
	public Name name;
	public Permissions perm;
	public PlayerListener listener;
	public Espectador espectador;
	public int TotalPlayers;
	public static Connection con;
	public boolean sql = true;
	public UUID winner = null;
	public ArrayList<BO3Blocks> structure;
	public ArrayList<BO3Blocks> cheststructure;
	public ArrayList<BO3Blocks> cake;
	public ArrayList<BO3Blocks> ministructure;
	public ArrayList<BO3Blocks> glad1;
	public ReflectionManager rm = new ReflectionManager();
	private CombatLogManager combatLog;
	public Connect connection = new Connect(this);
	public MySQLCommands sqlcmd = new MySQLCommands(this);
	public Config config = new Config(this);
	public Vanish vanish = new Vanish(this);
	public Mode adm = new Mode(this);
	public String site = "heavenmc.com.br";
	public ArrayList<UUID> gamers = new ArrayList<UUID>();
	public boolean countStarted = false;
	private static Main instance;
	public String ipAddress = "Carregando...";
	private HashMap<UUID, Integer> playerKills;
	public static HashMap<UUID, KitRotation> rotationRequest;
	public ColiseuManager coliseumManager;
	private ScoreboardManager scoreboardManager;
	private CooldownManager cooldownManager;
	public boolean timeChanged = false;
	public boolean timeStop = false;
	public boolean evento = false;
	public boolean dano = true;
	public boolean placeBlocks = true;
	public boolean breakBlocks = true;
	public boolean pvp = true;
	public boolean drops = true;
	public boolean pickupItems = true;
	public CaixaManager caixa;
	public ArrayList<UUID> teste;
	public TeamManager team;

	public static Main getInstance() {
		return instance;
	}

	public void onLoad() {
		new TerrainControlUpdater(this).run();
		this.config.loadConfig();
		if (getServer().getPort() != 25566) {
			String world;
			if (this.config.fileExists(ConfigEnum.CONFIG)) {
				world = this.config.getConfig(ConfigEnum.CONFIG).getString("world-name");
			} else {
				world = "world";
			}
			getServer().unloadWorld(world, false);
			deleteDir(new File(world));
		}
		getLogger().info("Apagando mundo...");
		Injector.inject();
	}

	public void onEnable() {
		loadHungerGamesType();
		getLogger().info("HungerGames type: " + type);
	//	generateBorder();
		instance = this;
		rotationRequest = new HashMap<UUID, KitRotation>();
		this.connection.trySQLConnection();
		this.connection.prepareSQL();
		this.name = new Name();
		this.combatLog = new CombatLogManager();
		this.kit = new KitManager(this);
		this.perm = new Permissions(this);
		this.coliseumManager = new ColiseuManager(this);
		this.listener = new PlayerListener(this);
		this.espectador = new Espectador(this);
		this.playerKills = new HashMap<UUID, Integer>();
		this.scoreboardManager = new ScoreboardManager();
		this.cooldownManager = new CooldownManager(this);
	//	this.caixa = new CaixaManager(this);
		this.teste = new ArrayList<>();
		getServer().getPluginManager().registerEvents(this.listener, this);
		getServer().getPluginManager().registerEvents(this.espectador, this);
		getServer().getPluginManager().registerEvents(new BarAPI(this), this);
		getServer().getPluginManager().registerEvents(new BorderListener(this), this);
		getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);
		getServer().getPluginManager().registerEvents(new KnockbackListener(), this);
		getServer().getPluginManager().registerEvents(new CombatLogListener(this.combatLog), this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new IncomingChannel(this));
		getServer().getPluginManager().registerEvents(new DamagerFixer(), this);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				World world = Main.this.getServer().getWorld(Main.this.config.getConfig(ConfigEnum.CONFIG).getString("world-name"));
				world.setSpawnLocation(0, ((World) Main.this.getServer().getWorlds().get(0)).getHighestBlockYAt(0, 0), 0);
				for (int x = -5; x <= 5; x++) {
					for (int z = -5; z <= 5; z++) {
						world.getSpawnLocation().clone().add(x * 16, 0.0D, z * 16).getChunk().load();
					}
				}
				world.setDifficulty(Difficulty.NORMAL);
				if (world.hasStorm()) {
					world.setStorm(false);
				}
				world.setTime(6000);
				world.setWeatherDuration(999999999);
				world.setGameRuleValue("doDaylightCycle", "false");
				for (Entity e : world.getEntities()) {
					if ((e instanceof Item)) {
						e.remove();
					}
				}
			}
		});
		new CommandManager(this);
		AbilityManager hm = new AbilityManager(this);
		hm.registerAbilityListeners();
		this.kit.loadFreeKits();
		this.structure = loadBO3("feast");
		this.cheststructure = loadBO3("feastchest");
		this.ministructure = loadBO3("minifeast");
		this.cake = loadBO3("cake");
		this.glad1 = loadBO3("glad1");
	//	this.coliseumManager.spawnColiseum();
		getLogger().info("Estagio do Jogo: Aguardando Jogadores");
		changeViewDistance(3);
		// new BukkitRunnable() {
		// @Override
		// public void run() {
		// try {
		// performTransfer();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// }
		// }.runTaskAsynchronously(this);
	}

	public void startCounter() {
		this.countStarted = true;
	}

	public void onDisable() {
		if (getServer().getPort() != 25566) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.kickPlayer("Servidor reiniciando");
			}
		//	if (coliseumManager.isConstructed()) {
		//		coliseumManager.destroyColiseum();
		//	}
		}
		getLogger().info("Desconectando MySQL");
		Connect.SQLdisconnect();
		getLogger().info("HeavenMC_HungerGames desabilitado!");
	}

	public void checkWinner() {
		if (this.winner != null) {
			return;
		}
		Iterator<UUID> iterator = this.gamers.iterator();
		while (iterator.hasNext()) {
			UUID uuid = (UUID) iterator.next();
			if (!PlayerListener.relog.contains(uuid)) {
				if (getServer().getPlayer(uuid) == null) {
					iterator.remove();
				}
			}
		}
		if (this.gamers.size() > 1) {
			return;
		}
		if (this.gamers.size() < 1) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			return;
		}
		getServer().getScheduler().cancelAllTasks();
		this.dano = false;
		final Player p = getServer().getPlayer((UUID) this.gamers.get(0));
		if (p == null) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			return;
		}
		this.winner = p.getUniqueId();
		getLogger().info("Vencedor: " + this.winner + "!");
		this.stage = GameStage.WINNER;
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				Main.this.giveWinnerItems(p);
			}
		}, 50L);
	}

	public void checkTeamWinner() {
		if (this.winner != null) {
			return;
		}
		Iterator<UUID> iterator = this.gamers.iterator();
		while (iterator.hasNext()) {
			UUID uuid = (UUID) iterator.next();
			if (!PlayerListener.relog.contains(uuid)) {
				if (getServer().getPlayer(uuid) == null) {
					iterator.remove();
				}
			}
		}

		List<TeamColors> tcs = team.timesRestantes();

		if (tcs.size() > 1) {
			return;
		}
		if (tcs.size() < 1) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			return;
		}
		getServer().getScheduler().cancelAllTasks();
		this.dano = false;
		List<Player> players = team.getPlayersTeamOnline(tcs.get(0));
		if (players.isEmpty()) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			return;
		}
		getLogger().info("Vencedor: " + team.teamNomes.get(tcs));
		this.stage = GameStage.WINNER;
		for (final Player player : players) {
			getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {

					Main.this.giveWinnerItems(player);
				}
			}, 50L);
		}
	}

	@SuppressWarnings("deprecation")
	public void giveWinnerItems(final Player p) {
		Location l = p.getLocation();
		l.setY(120.0D);
		Random r = new Random();
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0.0D, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0.0D, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0.0D, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0.0D, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0.0D, -10 + r.nextInt(20)));
		spawnRandomFirework(l.add(-10 + r.nextInt(20), 0.0D, -10 + r.nextInt(20)));
		new BukkitRunnable() {
			public void run() {
				Main.this.spawnRandomFirework(p.getLocation().add(-10.0D, 0.0D, -10.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(-10.0D, 0.0D, 10.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(10.0D, 0.0D, -10.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(10.0D, 0.0D, 10.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(-5.0D, 0.0D, -5.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(-5.0D, 0.0D, 5.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(5.0D, 0.0D, -5.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(5.0D, 0.0D, 5.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(-4.0D, 0.0D, -3.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(-3.0D, 0.0D, 4.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(2.0D, 0.0D, -6.0D));
				Main.this.spawnRandomFirework(p.getLocation().add(1.0D, 0.0D, 9.0D));

			}
		}.runTaskTimer(this, 10L, 30L);
		for (BO3Blocks bo3 : this.cake) {
			Block b = new Location(l.getWorld(), l.getX() + bo3.getX(), l.getY() + bo3.getY(), l.getZ() + bo3.getZ()).getBlock();
			b.setType(bo3.getType());
			b.setData(bo3.getData());
		}
		p.teleport(l.add(0.0D, 10.0D, 0.0D));
		p.getWorld().setTime(19000L);
		p.getInventory().clear();
		p.setItemInHand(new ItemStack(Material.MAP));
		p.getInventory().setItem(8, new ItemStack(Material.WATER_BUCKET));
		StringUtils.sendCenteredMessage(p, "");
		StringUtils.sendCenteredMessage(p, ChatColor.GRAY + StringUtils.line);
		StringUtils.sendCenteredMessage(p, "§7§lParabéns por vencer no §f§lHeavenHG!");
		StringUtils.sendCenteredMessage(p, "§7• Tempo:§b " + getTime(GameTimer));
		StringUtils.sendCenteredMessage(p, "§7• Kit 1:§b " + kit.getFirstKit(p));
		if (type != Type.ULTRA) {
			StringUtils.sendCenteredMessage(p, "§7• Kit 2:§b " + kit.getSecondKit(p));
		}
		StringUtils.sendCenteredMessage(p, "§7Para mais informaçoes acesse:");
		StringUtils.sendCenteredMessage(p, "§aHeavenMC.com.br");
		StringUtils.sendCenteredMessage(p, ChatColor.GRAY + StringUtils.line);
		StringUtils.sendCenteredMessage(p, "");
		me.skater.Main.getInstance().getRankManager().getPlayerRank(p).addXp(500);
		me.skater.Main.getInstance().getClanManager().clanWinnerReward(p);
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				TabListManager.setHeaderAndFooter(p, "", "");
				p.kickPlayer("§aParabéns §e" + p.getName() + " §avocê venceu o torneio!\n§6Acesse: §bheavenmc.com.br\n§cServidor reiniciando");
				p.kickPlayer("§6Parabens §6§l" + p.getName() + ",\n§aVoce venceu a partida!\n§bAcesse o site:§f " + Main.this.site);
				for (Player player : Main.this.getServer().getOnlinePlayers()) {
					if (player != p) {
						player.kickPlayer("§b" + p.getName() + " ganhou!");
					}
				}
			}
		}, 600L);
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			}
		}, 30 * 20);

	}

	public void spawnRandomFirework(Location loc) {
		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		Random r = new Random();
		int rt = r.nextInt(4) + 1;
		FireworkEffect.Type type = FireworkEffect.Type.BALL;
		if (rt == 1) {
			type = FireworkEffect.Type.BALL;
		}
		if (rt == 2) {
			type = FireworkEffect.Type.BALL_LARGE;
		}
		if (rt == 3) {
			type = FireworkEffect.Type.BURST;
		}
		if (rt == 4) {
			type = FireworkEffect.Type.STAR;
		}
		Color c1 = Color.WHITE;
		Color c2 = Color.YELLOW;
		Color c3 = Color.ORANGE;
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withColor(c2).withFade(c3).with(type).trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}

	public void startGame() {
		if (type == Type.TEAM) {
			for (TeamColors tc : TeamColors.values()) {
				for (UUID uuid : team.teamPlayers.get(tc)) {
					if (Bukkit.getPlayer(uuid) != null) {
						scoreboardManager.teamplayers.put(uuid, Bukkit.getPlayer(uuid).getName());
					}
				}
			}
		}
		me.skater.Main.getInstance().globalmute = false;
	//	coliseumManager.openDoors();
		World world = getServer().getWorld(this.config.getConfig(ConfigEnum.CONFIG).getString("world-name"));
		world.setTime(0L);
		world.setGameRuleValue("doDaylightCycle", "true");
		world.setThundering(false);
		world.setWeatherDuration(1000000);
		Title title = new Title(ChatColor.YELLOW + "Torneio começou");
		if (type != Type.NORMAL) {
			title.setSubtitle("§bVocê esta invencivel por §c5§b minutos!");
		} else {
			title.setSubtitle("§bVocê esta invencivel por §c2§b minutos!");
		}
		title.setStayTime(2);
		title.setFadeInTime(1);
		title.setFadeOutTime(1);
		title.setTimingsToSeconds();
		title.broadcast();
		getServer().broadcastMessage("§cO torneio começou! Não decepcione nos campos de batalha!");
		getServer().getPluginManager().callEvent(new GameStartEvent());
		for (Player p : getServer().getOnlinePlayers()) {
			p.removePotionEffect(PotionEffectType.JUMP);
			p.removePotionEffect(PotionEffectType.SLOW);
			p.setWalkSpeed(0.2F);
			p.setHealth(20.0D);
			p.setFoodLevel(20);
		}
		for (Player p : getServer().getOnlinePlayers()) {
			if (this.adm.isSpectating(p)) {
				this.vanish.makeVanished(p);
				p.getInventory().clear();
				p.getInventory().addItem(new ItemStack[] { this.adm.chest });
			} else {
				p.setAllowFlight(false);
				p.setFlying(false);
				this.gamers.add(p.getUniqueId());
			}
		}
		new BukkitRunnable() {
			public void run() {
				for (Player p : Main.this.getServer().getOnlinePlayers()) {
					if (!Main.this.adm.isSpectating(p)) {
						Main.this.kit.giveItem(p);

						Main.this.getServer().getPluginManager().callEvent(new PlayerStartGameEvent(p));
					}
				}
			}
		}.runTaskAsynchronously(this);
		this.TotalPlayers = this.gamers.size();
		if (type == Type.TEAM) {
			for (Player p : getServer().getOnlinePlayers()) {
				if (!isNotPlaying(p)) {
					if (!team.hasTeam(p)) {
						for (TeamColors t : team.teamPlayers.keySet()) {
							if (team.teamPlayers.get(t).size() < 4) {
								team.addPlayerToTeam(p.getUniqueId(), t);
								team.setPlayerArmadura(p, t);
							}
						}
					}
					TeamColors t = team.getTeam(p);
					if (t != null) {
						team.setPlayerArmadura(p, t);
					}
				}
			}
			// for(Player p : getServer().getOnlinePlayers()) {
			// if(!isNotPlaying(p)) {
			// scoreboardManager.createSecondScoreboardBase(p);
			// }
			// }
			// scoreboardManager.updateSecondScoreboard();
		}
		this.stage = GameStage.INVENCIBILITY;
		((ScoreboardManager) this.scoreboardManager).updateState();
		getLogger().info("Iniciando Partida!");
		getLogger().info("Estagio: Invencibilidade");
	}

	public Location getSpawn() {
		return ((World) getServer().getWorlds().get(0)).getSpawnLocation().add(0.0D, 5.0D, 0.0D);
	}

	public String getTime(Integer i) {
		if (i.intValue() >= 60) {
			Integer time = Integer.valueOf(i.intValue() / 60);
			String add = "";
			if (time.intValue() > 1) {
				add = "s";
			}
			return time + " minuto" + add;
		}
		Integer time = i;
		String add = "";
		if (time.intValue() > 1) {
			add = "s";
		}
		return time + " segundo" + add;
	}

	public void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addPlayerKit(UUID uuid, String kit) {
		if (this.kit.playerKit.containsKey(uuid)) {
			ArrayList<String> kits = (ArrayList<String>) this.kit.playerKit.get(uuid);
			if (!kits.contains(kit)) {
				kits.add(kit);
				this.kit.playerKit.put(uuid, kits);
			}
		} else {
			ArrayList<String> kits = new ArrayList<String>();
			kits.add(kit);
			this.kit.playerKit.put(uuid, kits);
		}
	}

	public String getShort(String name) {
		if (name.length() == 16) {
			String shorts = name.substring(0, name.length() - 2);
			return shorts;
		}
		if (name.length() == 15) {
			String shorts = name.substring(0, name.length() - 1);
			return shorts;
		}
		return name;
	}

	public void dropItems(Player p, Location l) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack item : p.getPlayer().getInventory().getContents()) {
			if ((item != null) && (item.getType() != Material.AIR)) {
				items.add(item.clone());
			}
		}
		for (ItemStack item : p.getPlayer().getInventory().getArmorContents()) {
			if ((item != null) && (item.getType() != Material.AIR)) {
				items.add(item.clone());
			}
		}
		if ((p.getPlayer().getItemOnCursor() != null) && (p.getPlayer().getItemOnCursor().getType() != Material.AIR)) {
			items.add(p.getPlayer().getItemOnCursor().clone());
		}
		dropItems(p, items, l);
	}

	@SuppressWarnings("deprecation")
	public void dropItems(Player p, List<ItemStack> items, Location l) {
		if (this.stage != GameStage.PREGAME) {
			World world = l.getWorld();
			for (ItemStack item : items) {
				if ((item != null) && (item.getType() != Material.AIR)) {
					if ((item.getType() != Material.POTION) || (item.getDurability() != 8201)) {
						if (item.getType() != Material.SKULL_ITEM) {
							boolean isItemKit = false;
							if (this.kit.KITSS.get(p.getUniqueId()) != null) {
								List<String> kitList = (List<String>) this.kit.KITSS.get(p.getUniqueId());
								for (String kitName : kitList) {
									kitName = kitName.toLowerCase();
									if (this.kit.items.get(kitName) != null) {
										for (ItemStack i : ((Kit) this.kit.items.get(kitName)).items) {
											if (item.getType() == i.getType()) {
												isItemKit = true;
												break;
											}
										}
									}
								}
							}
							if (!isItemKit) {
								if (item.hasItemMeta()) {
									world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
								} else {
									world.dropItemNaturally(l, item);
								}
							}
						}
					}
				}
			}
			p.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
			p.getPlayer().getInventory().clear();
			p.getPlayer().setItemOnCursor(new ItemStack(0));
			for (PotionEffect pot : p.getActivePotionEffects()) {
				p.removePotionEffect(pot.getType());
			}
		}
	}

	public String getMod(String name) {
		if (name.length() == 16) {
			String shorts = name.substring(0, name.length() - 4);
			return shorts;
		}
		if (name.length() == 15) {
			String shorts = name.substring(0, name.length() - 3);
			return shorts;
		}
		if (name.length() == 14) {
			String shorts = name.substring(0, name.length() - 2);
			return shorts;
		}
		if (name.length() == 13) {
			String shorts = name.substring(0, name.length() - 1);
			return shorts;
		}
		return name;
	}

	public Location getRespawnLocation() {
		Random r = new Random();
		int x = 200 + r.nextInt(200);
		int z = 200 + r.nextInt(200);
		if (r.nextBoolean()) {
			x = -x;
		}
		if (r.nextBoolean()) {
			z = -z;
		}
		World world = (World) getServer().getWorlds().get(0);
		int y = world.getHighestBlockYAt(x, z) + 3;
		Location loc = new Location(world, x, y, z);
		if (!loc.getChunk().isLoaded()) {
			loc.getChunk().load();
		}
		return loc;
	}

	public Location getSpawnLocation() {
		Random r = new Random();
		int x = r.nextInt(490);
		int z = r.nextInt(490);
		if (r.nextBoolean()) {
			x = -x;
		}
		if (r.nextBoolean()) {
			z = -z;
		}
		World world = (World) getServer().getWorlds().get(0);
		int y = world.getHighestBlockYAt(x, z);
		Location loc = new Location(world, x, y, z);
		if (!loc.getChunk().isLoaded()) {
			loc.getChunk().load();
		}
		return loc;
	}

	public boolean isNotPlaying(Player p) {
		return !this.gamers.contains(p.getUniqueId());
	}

	public void removeGamer(Player p) {
		this.gamers.remove(p.getUniqueId());
		if (type == Type.TEAM) {
			team.removerPlayerTeam(p.getUniqueId());
		}
	}

	public ArrayList<BO3Blocks> loadBO3(String path) {
		File file = new File(getDataFolder(), "/BO3/" + path + ".bo3");
		if (!file.exists()) {
			getLogger().log(Level.SEVERE, "Nao foi possivel encontrar o arquivo " + path + ".bo3");
			return new ArrayList<BO3Blocks>();
		}
		ArrayList<BO3Blocks> blocks = new ArrayList<BO3Blocks>();
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (line.startsWith("Block")) {
					String[] bo3 = line.replace("Block(", "").replace(")", "").split(",");
					int x = Integer.valueOf(bo3[0]).intValue();
					int y = Integer.valueOf(bo3[1]).intValue();
					int z = Integer.valueOf(bo3[2]).intValue();
					String mat = bo3[3];
					byte data = 0;
					if (bo3[3].contains(":")) {
						String[] material = bo3[3].split(":");
						mat = material[0];
						data = Byte.valueOf(material[1]).byteValue();
					}
					blocks.add(new BO3Blocks(x, y, z, Material.valueOf(mat), data));
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			getLogger().log(Level.INFO, "Carregado arquivo " + path + ".bo3");
		}
		return blocks;
	}

	public void sendTabToPlayer(Player player) {
		if (this.ipAddress.equals("Carregando...")) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("GetServer");
			player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
		}
		StringBuilder headerBuilder = new StringBuilder();
		int players = Bukkit.getOnlinePlayers().length - this.adm.admin.size() - this.adm.youtuber.size();
		int tempo = 0;
		int maxPlayer = Bukkit.getMaxPlayers();
		if (this.stage == GameStage.PREGAME) {
			tempo = this.PreGameTimer;
		} else {
			players = this.gamers.size();
			tempo = this.GameTimer;
		}
		headerBuilder.append(" ");
		headerBuilder.append("§6HeavenMC§8: §eHardcore Games");
		headerBuilder.append("\n");
		headerBuilder.append(" ");

		StringBuilder footerBuilder = new StringBuilder();
		footerBuilder.append(" ");
		footerBuilder.append("\n");
		footerBuilder.append("§7IP: §e" + ipAddress);
		footerBuilder.append("\n");
		footerBuilder.append("§7Tempo: §e" + getHourTime(Integer.valueOf(tempo)) + " §7Players: §e" + players + "/" + maxPlayer + " §7Kit: §e" + kit.getKitName(kit.getPlayerKit(player)));
		footerBuilder.append("\n");
		footerBuilder.append(ChatColor.GOLD + "Acesse: HeavenMC.com.br");
		TabListManager.setHeaderAndFooter(player, headerBuilder.toString(), footerBuilder.toString());
	}

	public String getHourTime(Integer i) {
		int minutes = i.intValue() / 60;
		int seconds = i.intValue() % 60;
		String disMinu = (minutes < 10 ? "" : "") + minutes;
		String disSec = (seconds < 10 ? "0" : "") + seconds;
		String formattedTime = disMinu + ":" + disSec;
		return formattedTime;
	}

	public int getKills(Player p) {
		if (this.playerKills.containsKey(p.getUniqueId())) {
			return ((Integer) this.playerKills.get(p.getUniqueId())).intValue();
		}
		return 0;
	}

	public void addKill(Player p) {
		int kills = 0;
		if (this.playerKills.containsKey(p.getUniqueId())) {
			kills = ((Integer) this.playerKills.get(p.getUniqueId())).intValue();
		}
		kills++;
		this.playerKills.put(p.getUniqueId(), Integer.valueOf(kills));
		Bukkit.getServer().getPluginManager().callEvent(new PlayerGainKillEvent(p, Integer.valueOf(kills)));
	}

	public void removeKills(Player p) {
		this.playerKills.remove(p.getUniqueId());
	}

	public void sendToSpawnAnyway(Player p) {
		p.setFlying(false);
		p.teleport(p.getWorld().getSpawnLocation().clone().add(0.5D, 3.0D, 0.5D));
	}

	public void sendToSpawn(Player p) {
		p.setFlying(false);
		if (this.coliseumManager.isInsideColiseum(p)) {
			return;
		}
		p.teleport(p.getWorld().getSpawnLocation().clone().add(0.5D, 3.0D, 0.5D));
	}

	public void changeViewDistance(int distance) {
		((WorldServer) ((CraftServer) getServer()).getHandle().getServer().worlds.get(0)).getPlayerChunkMap().a(distance);
	}

	public ScoreboardManager getScoreboardManager() {
		return (ScoreboardManager) this.scoreboardManager;
	}

	public CooldownManager getCooldownManager() {
		return this.cooldownManager;
	}

	public String filterIp(String s) {
		if (s.equals("Carregando...")) {
			return new String("?");
		}
		return s.substring(0, 3).toUpperCase().replace(".", "");

	}

	public void deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				deleteDir(new File(dir, children[i]));
			}
		}
		dir.delete();
	}

	public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}
			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
			}
		} else {
			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	// public void finishTransfer(String uuid, String name) {
	// System.out.println(uuid + " " + name);
	// connection.SQLQuery("INSERT INTO wbc_utils.UuidFetcher(`uuid`, `name`)
	// VALUES ('" + uuid + "', '" + name + "');");
	// }
	//
	//
	// public void performTransfer() throws SQLException {
	// getLogger().info("Iniciando transferencia");
	// PreparedStatement stmt = null;
	// ResultSet result = null;
	// stmt = con.prepareStatement("SELECT * FROM hg_players;");
	// result = stmt.executeQuery();
	// while(result.next()) {
	// finishTransfer(result.getString("uuid").replace("-", ""),
	// result.getString("name"));
	// }
	// }
	//

	// public void finishTransfer(String uuid, String kit) {
	// System.out.println(uuid + " " + kit);
	// connection.SQLQuery("INSERT INTO Kits(`Player`, `Kits`) VALUES ('" + uuid
	// + "', '" + kit + "');");
	// }
	//
	//
	// public void performTransfer() throws SQLException {
	// getLogger().info("Iniciando transferencia");
	// PreparedStatement stmt = null;
	// ResultSet result = null;
	// stmt = con.prepareStatement("SELECT * FROM
	// wbc_utils.server_permissions;");
	// result = stmt.executeQuery();
	// while(result.next()) {
	// if(kit.isKit(result.getString("permission").replace("kit.",
	// "").replace("Kit.", "").toLowerCase())) {
	// finishTransfer(result.getString("name").replace("-", ""),
	// result.getString("permission").replace("kit.", "").replace("Kit.",
	// "").toLowerCase());
	// }
	// }
	// }

	@SuppressWarnings("deprecation")
	private void generateBorder() {
		for (int x = -500; x <= 500; x++) {
			if ((x == -500) || (x == 500)) {
				for (int z = -500; z <= 500; z++) {
					for (int y = 0; y <= 250; y++) {
						Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
						if (!loc.getChunk().isLoaded()) {
							loc.getChunk().load();
						}
						if (new Random().nextBoolean()) {
							loc.getBlock().setTypeId(20);
						} else {
							loc.getBlock().setTypeId(155);

						}
					}
				}
			}
		}
		for (int z = -500; z <= 500; z++) {
			if ((z == -500) || (z == 500)) {
				for (int x = -500; x <= 500; x++) {
					for (int y = 0; y <= 250; y++) {
						Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
						if (!loc.getChunk().isLoaded()) {
							loc.getChunk().load();
						}
						if (new Random().nextBoolean()) {
							loc.getBlock().setTypeId(20);
						} else {
							loc.getBlock().setTypeId(155);
						}
					}
				}
			}
		}
	}

	public Type getHungerGamesType() {
		return this.type;
	}

	public void loadHungerGamesType() {
		if (getConfig().getString("type") != null) {
			type = Type.valueOf(getConfig().getString("type").toUpperCase());
		} else {
			getConfig().set("type", "normal");
			type = Type.NORMAL;
			saveConfig();
		}
		if (getHungerGamesType() == Type.NORMAL) {
			getServer().getPluginManager().registerEvents(new GameListener(this), this);
			this.Invenci = Integer.valueOf(120);
		}
		if (getHungerGamesType() == Type.ULTRA) {
			getServer().getPluginManager().registerEvents(new GameListener(this), this);
			this.Invenci = Integer.valueOf(120);
		}
		if (getHungerGamesType() == Type.COPA) {
			getServer().getPluginManager().registerEvents(new CopaGameListener(this), this);
			this.Invenci = Integer.valueOf(300);

		}
		if (getHungerGamesType() == Type.TEAM) {
			getServer().getPluginManager().registerEvents(new TeamGameListener(this), this);
			this.team = new TeamManager(this);
			this.team.onEnable();
			this.Invenci = Integer.valueOf(300);
		}

	}

	public boolean hasVip(Player p) {
/*		if (perm.isElite(p)) {
			return true;
		}
*/		if (perm.isMvp(p) || (perm.isPro(p))) {
			return true;
		}
		return false;
	}

	public int getPlayerVipTime(Player p) {
	/*	if (perm.isElite(p)) {
			return 420;
		}
	*/	if (perm.isMvp(p) || (perm.isPro(p))) {
			return 300;
		}

		return 0;
	}

}
