package me.skater;

import java.sql.Connection;

import org.bukkit.plugin.java.JavaPlugin;

import me.skater.CopaHG.CopaManager;
import me.skater.Enums.ServerType;
import me.skater.Events.UpdateScheduler;
import me.skater.Listeners.PlayerListeners;
import me.skater.Managers.BanManager;
import me.skater.Managers.CommandManager;
import me.skater.Managers.InventoryManager;
import me.skater.Managers.Permissions;
import me.skater.Managers.ReflectionManager;
import me.skater.MySQL.Connect;
import me.skater.Utils.BukkitUUIDFetcher;
import me.skater.Utils.Changelog;
import me.skater.Utils.NameFetcher;
import me.skater.Utils.UUIDFetcher;
import me.skater.clans.ClanManager;
import me.skater.multiplier.MultiplierManager;
import me.skater.permissions.PermissionManager;
import me.skater.ranking.RankingManager;
import me.skater.scoreboard.ScoreboardManager;
import me.skater.tags.TagManager;
import me.skater.titles.TitleManager;

public class Main extends JavaPlugin {

	private static Main static_instance;
	public NameFetcher name;
	public ReflectionManager rm;
	public Permissions perm;
	public BanManager ban;
	public ServerType type;
	private ScoreboardManager scoreboard;
	private TagManager tag;
	public static String[] onlineplayers;
	public boolean globalmute;
	private PermissionManager permissionManager;
	public boolean sql = true;
	public Connect connect;
	public Connection mainConnection;
	private RankingManager ranking;
	private TitleManager title;
	private MultiplierManager multiplier;
	private CopaManager copa;
	private ClanManager clan;
	private Changelog log;
	private UUIDFetcher uuidfetcher;
	private InventoryManager inventory;

	@Override
	public void onEnable() {
		static_instance = this;
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		loadServerType();
		System.out.println("Heaven_Utils listening " + type);
		this.connect = new Connect(this);
		this.mainConnection = this.connect.trySQLConnection();
		this.connect.prepareSQL(mainConnection);
		this.inventory = new InventoryManager();
		this.permissionManager = new PermissionManager(this);
		this.permissionManager.onEnable();
		this.log = new Changelog(this);
		this.name = new NameFetcher(this);
		this.rm = new ReflectionManager();
		this.perm = new Permissions(this);
		this.ban = new BanManager(this);
		this.scoreboard = new ScoreboardManager(this);
		this.scoreboard.onEnable();
		this.tag = new TagManager(this);
		this.tag.onEnable();
		this.ranking = new RankingManager(this);
		this.ranking.onEnable();
		this.title = new TitleManager(this);
		this.title.onEnable();
		this.multiplier = new MultiplierManager(this);
		this.multiplier.onEnable();
		this.copa = new CopaManager(this);
		this.copa.onEnable();
		this.clan = new ClanManager(this);
		this.clan.onEnable();
		this.uuidfetcher = new BukkitUUIDFetcher();
		new CommandManager(this);
		this.globalmute = false;
		getServer().getScheduler().runTaskTimer(this, new UpdateScheduler(), 1L, 1L);
		getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

	}

	@Override
	public void onDisable() {
		if (this.permissionManager != null) {
			this.permissionManager.onDisable();
		}
		if (this.scoreboard != null) {
			this.scoreboard.onDisable();
		}
		if (this.tag != null) {
			this.tag.onDisable();
		}
		if (this.ranking != null) {
			this.ranking.onDisable();
		}

		if (this.title != null) {
			this.title.onDisable();
		}

		if (this.copa != null) {
			this.copa.onDisable();
		}

	}

	public static Main getInstance() {
		return static_instance;
	}

	public BanManager getBanManager() {
		return ban;
	}

	public Permissions getPermissions() {
		return perm;
	}

	public NameFetcher getAPI() {
		return name;
	}

	public TagManager getTagManager() {
		return this.tag;
	}

	public ScoreboardManager getScoreboardManager() {
		return this.scoreboard;
	}

	public PermissionManager getPermissionManager() {
		return this.permissionManager;
	}

	public Connection getSQL() {
		return this.mainConnection;
	}

	public RankingManager getRankManager() {
		return this.ranking;
	}

	public TitleManager getTitleManager() {
		return this.title;
	}

	public MultiplierManager getMultiplierManager() {
		return this.multiplier;
	}

	public CopaManager getCopaManager() {
		return this.copa;
	}

	public ClanManager getClanManager() {
		return this.clan;
	}

	public Changelog getChangelog() {
		return this.log;
	}

	public UUIDFetcher getUUIDFetcher() {
		return this.uuidfetcher;
	}

	public InventoryManager getInventoryManager() {
		return this.inventory;
	}

	public void loadServerType() {
		if (getConfig().getString("serverType") != null) {
			if (getConfig().getString("serverType").equals("lobby")) {
				type = ServerType.LOBBY;
			}
			if (getConfig().getString("serverType").equals("hg")) {
				type = ServerType.HG;
			}
			if (getConfig().getString("serverType").equals("kitpvp")) {
				type = ServerType.KITPVP;
			}
			if (getConfig().getString("serverType").equals("hc")) {
				type = ServerType.HC;
			}
		} else {
			getConfig().set("serverType", "hg");
			type = ServerType.HG;
			saveConfig();
		}
	}

}
