package br.com.wombocraft.lobby.gamer;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.admin.AdminManager;
import br.com.wombocraft.lobby.mysql.MysqlManager;
import br.com.wombocraft.lobby.nms.TabListManager;
import br.com.wombocraft.lobby.rank.enums.RankType;
import br.com.wombocraft.lobby.utils.ItemBuilder;
import br.com.wombocraft.lobby.utils.cd.Cooldown;

@SuppressWarnings("deprecation")
public class GamerListener implements Listener {

	private GamerManager gm;

	public GamerListener(GamerManager gm) {
		this.gm = gm;
	}

	@EventHandler
	public void prelogin(AsyncPlayerPreLoginEvent event) {
		MysqlManager mysqlm = Lobby.getLobbyManager().getMysqlManager();
		Gamer g = null;
		try {
			GamerLoginResult gr = mysqlm.getGamerQuerys().loadGamer(event.getName(), event.getUniqueId());
			g = gr.getGamer();
			if (g == null) {
				event.disallow(org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
						"§cErro ao carregar seu perfil!");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			event.disallow(org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
					"§cErro ao carregar seu perfil!");
		}
		if (event.getLoginResult() == org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.ALLOWED)
			if (g != null) {
				Lobby.getLobbyManager().getGamerManager().addGamer(g);
			}
	}

	@EventHandler
	public void quit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Gamer g = gm.getGamerByUUID(event.getPlayer().getUniqueId());
		gm.removeGamer(g);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void gamerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		LobbyManager lmmanager = Lobby.getLobbyManager();
		Player p = event.getPlayer();
		Gamer g = lmmanager.getGamerManager().getGamerByUUID(event.getPlayer().getUniqueId());
		if (g == null) {
			p.kickPlayer("§cErro ao carregar perfil");
			return;
		}
		g.setPlayer(event.getPlayer());
		lmmanager.getScoreboardManager().createScoreboardBase(g);
		lmmanager.getTagManager().addPlayerTag(g.getPlayer(), g.getRank().getTag());
		if (g.getRank().hasRank(RankType.TRIAL)) {
			lmmanager.getAdminManager().setAdmin(p);
		} else {
			p.setGameMode(GameMode.SURVIVAL);
			event.getPlayer().getInventory().setItem(0,
					new ItemBuilder(Material.COMPASS).nome("§aServers").construir());
		}
		if (!g.getRank().hasRank(RankType.TRIAL)) {
			lmmanager.getAdminManager().getVanish().updateVanished();
		}
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 10);
		tab(p);
		Location loc = new Location(Bukkit.getWorlds().get(0), 0, 93, 0, -80.22F, 0);
		p.teleport(loc);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().equals(new ItemStack(Material.MAGMA_CREAM))) {
			p.chat("/admin");
		}
	}

	public void tab(Player p) {
		StringBuilder headerBuilder = new StringBuilder();
		headerBuilder.append(" \n");
		headerBuilder.append("§f§lHEAVEN§6§lMC");
		headerBuilder.append("\n");
		headerBuilder.append("§r§6heavenmc.com.br");
		headerBuilder.append("\n");
		headerBuilder.append(" ");

		StringBuilder footerBuilder = new StringBuilder();
		footerBuilder.append(" ");
		footerBuilder.append("\n");
		footerBuilder.append("§e§lVIP, BETA E MAIS ACESSE");
		footerBuilder.append("\n");
		footerBuilder.append("§r§6heavenmc.com.br");
		footerBuilder.append("\n");
		TabListManager.setHeaderAndFooter(p, headerBuilder.toString(), footerBuilder.toString());
	}

	@EventHandler
	public void blockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void blockPlace(BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void damage(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.VOID) {
			event.getEntity().teleport(new Location(event.getEntity().getWorld(), -55.5, 107, 4.5));
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void levelfood(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void dropItemEvent(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void rain(WeatherChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void spawnMobs(EntitySpawnEvent e) {
		if (e.getEntity() instanceof Creature) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void chat(PlayerChatEvent event) {
		Gamer g = gm.getGamerByUUID(event.getPlayer().getUniqueId());
		AdminManager adm = Lobby.getLobbyManager().getAdminManager();
		if (adm._playerschat.contains(event.getPlayer().getUniqueId())) {
			String rawmsg = event.getMessage();
			event.setCancelled(true);
			String msg = ChatColor.translateAlternateColorCodes('&',
					"§7- §6[Staff] §4" + g.getPlayer().getName() + ": §f" + rawmsg + "");
			for (Gamer gs : Lobby.getLobbyManager().getGamerManager().getGamers()) {
				if (adm._playerschat.contains(gs.getPlayer().getUniqueId())) {
					gs.getPlayer().sendMessage(msg);
				} else {
					if (gs.getRank().hasRank(RankType.TRIAL)) {
						gs.getPlayer().sendMessage(msg);
					}
				}
			}
		}
		if (!g.getRank().hasRank(RankType.TRIAL)) {
			if (Cooldown.isInCooldown(event.getPlayer().getUniqueId(), "chat")) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cDigite mais devagar...");
				Cooldown cd = new Cooldown(event.getPlayer().getUniqueId(), "chat", 3);
				cd.start();
				return;
			}

			Cooldown cd = new Cooldown(event.getPlayer().getUniqueId(), "chat", 3);
			cd.start();
		}
		String tagname = g.getRank().getColor();
		event.setFormat(tagname + event.getPlayer().getName() + ChatColor.GRAY + " » " + ChatColor.WHITE + "%2$s");
	}
	
	@EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)  {
        if((event.getMessage().equals("/?") || event.getMessage().equals("/me")) && !event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

	@EventHandler
	public void pickup(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}

}
