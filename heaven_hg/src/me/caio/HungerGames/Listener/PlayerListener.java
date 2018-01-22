package me.caio.HungerGames.Listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Type;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Events.TimeSecondEvent;
import me.caio.HungerGames.NMS.Title;
import me.caio.HungerGames.NMS.barapi.BarAPI;
import me.caio.HungerGames.Team.Enums.TeamColors;
import me.caio.HungerGames.Utils.Cooldown;
import me.caio.HungerGames.Utils.InventorySelector;
import me.caio.HungerGames.Utils.Map;
import me.caio.HungerGames.Utils.Enum.GameStage;
import me.caio.HungerGames.Utils.Enum.KitCategory;
import me.skater.Utils.StringUtils;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;

@SuppressWarnings("deprecation")
public class PlayerListener implements Listener {

	private me.caio.HungerGames.Main m;

	public HashMap<UUID, String> deathMessage = new HashMap<UUID, String>();
	public static ArrayList<UUID> relog = new ArrayList<UUID>();
	private ArrayList<UUID> reloged = new ArrayList<UUID>();
	private ArrayList<UUID> joined = new ArrayList<UUID>();
	public static ShapelessRecipe camelrec;
	private ItemStack chest;
	private ItemStack ender;
//	private ItemStack craft;
	private ItemStack emerald;
	public ItemStack adminstack;
	public ArrayList<UUID> desistiu = new ArrayList<>();
	private List<String> novidades;
	public ArrayList<UUID> chatTime = new ArrayList<UUID>();

	public PlayerListener(me.caio.HungerGames.Main main) {
		ItemStack cacto = new ItemStack(Material.MUSHROOM_SOUP);
		ItemStack cacau = new ItemStack(Material.MUSHROOM_SOUP);
		ItemStack abobora = new ItemStack(Material.MUSHROOM_SOUP);
		ItemStack camel = new ItemStack(Material.MUSHROOM_SOUP);
		ItemMeta cactometa = cacto.getItemMeta();
		ItemMeta cacaumeta = cacau.getItemMeta();
		ItemMeta aboborameta = abobora.getItemMeta();
		ItemMeta camelmeta = camel.getItemMeta();
		cactometa.setDisplayName("Sopa de Cactos");
		cacaumeta.setDisplayName("Sopa de Cocoa");
		aboborameta.setDisplayName("Sopa de Aboboras");
		camelmeta.setDisplayName("Sopa de Cactos");
		cacto.setItemMeta(cactometa);
		cacau.setItemMeta(cacaumeta);
		abobora.setItemMeta(aboborameta);
		camel.setItemMeta(camelmeta);
		ShapelessRecipe cactorec = new ShapelessRecipe(cacto);
		ShapelessRecipe cacaurec = new ShapelessRecipe(cacau);
		ShapelessRecipe aboborarec = new ShapelessRecipe(abobora);
		camelrec = new ShapelessRecipe(camel);
		cactorec.addIngredient(Material.BOWL);
		cactorec.addIngredient(Material.CACTUS);
		cacaurec.addIngredient(Material.BOWL);
		cacaurec.addIngredient(Material.INK_SACK, 3);
		aboborarec.addIngredient(Material.BOWL);
		aboborarec.addIngredient(Material.PUMPKIN_SEEDS);
		aboborarec.addIngredient(Material.PUMPKIN_SEEDS);
		camelrec.addIngredient(Material.SAND);
		camelrec.addIngredient(Material.CACTUS);
		main.getServer().addRecipe(cactorec);
		main.getServer().addRecipe(cacaurec);
		main.getServer().addRecipe(aboborarec);
		main.getServer().addRecipe(camelrec);
		this.m = main;

		if (m.type != Type.ULTRA) {
			this.chest = new ItemStack(Material.CHEST);
			ItemMeta im = this.chest.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "Escolha o primeiro Kit§7");
			this.chest.setItemMeta(im);

			this.ender = new ItemStack(Material.CHEST);
			ItemMeta im5 = this.ender.getItemMeta();
			im5.setDisplayName(ChatColor.YELLOW + "Escolha o segundo Kit§7");
			this.ender.setItemMeta(im5);

		} else {
			this.chest = new ItemStack(Material.CHEST);
			ItemMeta im = this.chest.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "Escolha seu Kit§7");
			this.chest.setItemMeta(im);
		}

	/*	this.craft = new ItemStack(Material.WORKBENCH);
		ItemMeta im2 = this.craft.getItemMeta();
		im2.setDisplayName("§cCustomização de Kits§7");
		this.craft.setItemMeta(im2);
	
		this.emerald = new ItemStack(Material.EMERALD);
		ItemMeta im3 = this.emerald.getItemMeta();
		im3.setDisplayName("§eLoja de Kits§7");
		this.emerald.setItemMeta(im3);
	*/
		this.adminstack = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta a = adminstack.getItemMeta();
		a.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Você esta no modo ADMIN!");
		this.adminstack.setItemMeta(a);
		this.novidades = new ArrayList<String>();
		this.novidades.add("§e§lHEAVENHG DE CARA NOVA!");
		this.novidades.add("§e§lNOVOS KITS");
		this.novidades.add("§e§lPVP EM BREVE!");
		this.novidades.add("§e§lNOVAS ATUALIZACOES");
		if (m.type == Type.TEAM) {
			this.novidades.add("§a§lTEAM HARDCORE GAMES");
		}
		if (m.type == Type.COPA) {
			this.novidades.add("§a§lCOPA HARDCORE GAMES");
		}
		if (m.type == Type.NORMAL) {
			this.novidades.add("§a§lHEAVENHG");
		}
	}

	@EventHandler
	public void onSetTabList(TimeSecondEvent event) {
		new BukkitRunnable() {
			public void run() {
				for (Player p : PlayerListener.this.m.getServer().getOnlinePlayers()) {
					PlayerListener.this.m.sendTabToPlayer(p);
				}
			}
		}.runTaskAsynchronously(this.m);
	}

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		event.setCancelled(true);
	}

/*	@EventHandler
	public void onCaixaDiaria(PlayerInteractEvent e) {
		if (e.getPlayer().getItemInHand().equals(m.caixa.disponivel) || e.getPlayer().getItemInHand().equals(m.caixa.usado)) {
			m.caixa.ClaimBox(e.getPlayer());
		}
	}
*/
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (p.getItemInHand().equals(adminstack)) {
			p.chat("/admin");
		}
		if (this.m.stage == GameStage.PREGAME) {
			if ((event.getItem() != null) && (event.getItem().getType() == Material.EMPTY_MAP)) {
				return;
			}
			event.setCancelled(true);
			return;
		}
		ItemStack item = event.getItem();
		if ((item == null) || (item.getType() == Material.AIR)) {
			return;
		}
		if (item.getType().name().contains("BUCKET")) {
			if (p.getGameMode() == GameMode.CREATIVE && !m.perm.isTrial(p)) {
				event.setCancelled(true);
			}
		}
		if (event.getAction().toString().startsWith("LEFT")) {
			if (item.getType() == Material.COMPASS) {
				Player target = null;
				double distance = 10000.0D;
				for (Player game : Bukkit.getOnlinePlayers()) {
					if (!this.m.isNotPlaying(game)) {
						double distOfPlayerToVictim = p.getLocation().distance(game.getPlayer().getLocation());
						if ((distOfPlayerToVictim < distance) && (distOfPlayerToVictim > 25.0D)) {
							distance = distOfPlayerToVictim;
							target = game;
						}
					}
				}
				if (target == null) {
					p.sendMessage(ChatColor.RED + "Nenhum jogador encontrado! Apontando para o spawn.");
					p.setCompassTarget(((org.bukkit.World) Bukkit.getWorlds().get(0)).getSpawnLocation());
				} else {
					p.setCompassTarget(target.getLocation());
					p.sendMessage(ChatColor.GREEN + "Bússola apontando para §7" + target.getName());
				}
			}
		} else if (event.getAction().toString().startsWith("RIGHT")) {
			if (item.getType() == Material.COMPASS) {
				if (m.type != Type.TEAM) {
					Player target = null;
					double distance = 10000.0D;
					for (Player game : Bukkit.getOnlinePlayers()) {
						if (!this.m.isNotPlaying(game)) {
							double distOfPlayerToVictim = p.getLocation().distance(game.getPlayer().getLocation());
							if ((distOfPlayerToVictim < distance) && (distOfPlayerToVictim > 25.0D)) {
								distance = distOfPlayerToVictim;
								target = game;
							}
						}
					}
					if (target == null) {
						p.sendMessage(ChatColor.RED + "Nenhum jogador encontrado! Apontando para o spawn.");
						p.setCompassTarget(((org.bukkit.World) Bukkit.getWorlds().get(0)).getSpawnLocation());
					} else {
						p.setCompassTarget(target.getLocation());
						p.sendMessage(ChatColor.GREEN + "Bússola apontando para §7" + target.getDisplayName());
					}
					return;
				}
				Player target = null;
				double distance = 10000.0D;
				if (m.team.getTeam(p) == null) {
					return;
				}
				List<Player> pls = m.team.getPlayersTeamOnline(m.team.getTeam(p));
				if (pls.isEmpty()) {
					p.sendMessage(ChatColor.RED + "Não há ninguém vivo do seu time! Apontando para o spawn.");
					p.setCompassTarget(((World) Bukkit.getWorlds().get(0)).getSpawnLocation());
					return;
				}
				for (Player game : pls) {
					if (p != game) {
						if (!this.m.isNotPlaying(game)) {
							double distOfPlayerToVictim = p.getLocation().distance(game.getPlayer().getLocation());
							if ((distOfPlayerToVictim < distance)) {
								distance = distOfPlayerToVictim;
								target = game;
							}
						}
					}
				}
				if (target == null) {
					p.sendMessage(ChatColor.RED + "Não há ninguém vivo do seu time! Apontando para o spawn.");
					p.setCompassTarget(((World) Bukkit.getWorlds().get(0)).getSpawnLocation());
				} else {
					p.setCompassTarget(target.getLocation());
					p.sendMessage(ChatColor.GREEN + "Bússola apontando para o amigo mais próximo: §7" + target.getDisplayName());
				}
			}
		}
		if ((item.getType() == Material.MUSHROOM_SOUP) && ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
				&& ((((Damageable) p).getHealth() < ((Damageable) p).getMaxHealth()) || (p.getFoodLevel() < 20))) {
			int restores = 7;
			event.setCancelled(true);
			if (((Damageable) p).getHealth() < ((Damageable) p).getMaxHealth()) {
				if (((Damageable) p).getHealth() + restores <= ((Damageable) p).getMaxHealth()) {
					p.setHealth(((Damageable) p).getHealth() + restores);
				} else {
					p.setHealth(((Damageable) p).getMaxHealth());
				}
			} else if (p.getFoodLevel() < 20) {
				if (p.getFoodLevel() + restores <= 20) {
					p.setFoodLevel(p.getFoodLevel() + restores);
					p.setSaturation(3.0F);
				} else {
					p.setFoodLevel(20);
					p.setSaturation(3.0F);
				}
			}
			item = new ItemStack(Material.BOWL);
			p.setItemInHand(item);
		}
		if ((item.getType() == Material.WOOL) && (item.getItemMeta() != null) && (item.getItemMeta().getDisplayName() != null)) {
			event.setCancelled(true);
			String kit = ChatColor.stripColor(item.getItemMeta().getDisplayName().replace("Presente para: ", "").toLowerCase());
			if (this.m.kit.hasAbility(p, kit)) {
				p.setItemInHand(null);
				this.m.kit.giveMini(p, kit);
				p.updateInventory();
				p.sendMessage(ChatColor.GREEN + "Voce recebeu seu kit novamente!");
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (m.stage == GameStage.PREGAME) {
			e.setCancelled(true);
		}
		if (e.getInventory().getTitle().equals("Boa sorte!!!")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onClickStore(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item == null) {
			return;
		}
		if ((item.getType() != Material.EMERALD)) {
			return;
		}
		if (!item.hasItemMeta()) {
			return;
		}
		if (!item.getItemMeta().hasDisplayName()) {
			return;
		}
		if (item.equals(emerald)) {
			e.setCancelled(true);
			new BukkitRunnable() {
				public void run() {
					InventorySelector loja = new InventorySelector(p, PlayerListener.this.m.kit);
					loja.setCategory(KitCategory.STORE);
					loja.open();

				}
			}.runTaskAsynchronously(this.m);
		}
	}

	@EventHandler
	public void onClickTeam(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item == null) {
			return;
		}
		if ((item.getType() != Material.LEATHER_CHESTPLATE)) {
			return;
		}
		if (!item.hasItemMeta()) {
			return;
		}
		if (!item.getItemMeta().hasDisplayName()) {
			return;
		}
		if (!item.getItemMeta().getDisplayName().equals("§eEscolha o seu Time§7 (Clique)")) {
			return;
		}
		if (m.type != Type.TEAM) {
			return;
		}
		e.setCancelled(true);
		m.team.getTeamInventory().open(p);
	}

	@EventHandler
	public void onClickFirstKit(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item == null) {
			return;
		}
		if ((item.getType() != Material.CHEST)) {
			return;
		}
		if (!item.hasItemMeta()) {
			return;
		}
		if (!item.getItemMeta().hasDisplayName()) {
			return;
		}
		if (item.equals(chest)) {
			if (Cooldown.isInCooldown(p.getUniqueId(), "inventory")) {
				p.sendMessage("§cClique devagar");
				return;
			}
			Cooldown cd = new Cooldown(p.getUniqueId(), "inventory", 1);
			cd.start();
			e.setCancelled(true);
			new BukkitRunnable() {
				public void run() {
					InventorySelector kits = new InventorySelector(p, PlayerListener.this.m.kit);
					if (m.type != Type.ULTRA) {
						kits.setCategory(KitCategory.TEAM_ALL);
					} else {
						kits.setCategory(KitCategory.TEAM_OWNED);
					}
					kits.open();

				}
			}.runTaskAsynchronously(this.m);
		}
	}

	@EventHandler
	public void onClickMenu(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item == null) {
			return;
		}
		if ((item.getType() != Material.SKULL_ITEM && item.getType() != Material.NETHER_STAR)) {
			return;
		}
		if (!item.hasItemMeta()) {
			return;
		}
		if (!item.getItemMeta().hasDisplayName()) {
			return;
		}
		if (item.getItemMeta().getDisplayName().contains(ChatColor.YELLOW + "HeavenMC Menu §7(Clique)")) {
			if (Cooldown.isInCooldown(p.getUniqueId(), "inventory")) {
				p.sendMessage("§cClique devagar");
				return;
			}
			Cooldown cd = new Cooldown(p.getUniqueId(), "inventory", 1);
			cd.start();
			e.setCancelled(true);
			me.skater.Main.getInstance().getRankManager().openWomboMenu(p);

		}
	}

	@EventHandler
	public void onClickSecondKit(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item == null) {
			return;
		}
		if ((item.getType() != Material.CHEST)) {
			return;
		}
		if (!item.hasItemMeta()) {
			return;
		}
		if (!item.getItemMeta().hasDisplayName()) {
			return;
		}
		if (item.equals(ender)) {
			if (Cooldown.isInCooldown(p.getUniqueId(), "inventory")) {
				p.sendMessage("§cClique devagar");
				return;
			}
			Cooldown cd = new Cooldown(p.getUniqueId(), "inventory", 1);
			cd.start();
			e.setCancelled(true);
			new BukkitRunnable() {
				public void run() {
					InventorySelector kits = new InventorySelector(p, PlayerListener.this.m.kit);
					kits.setCategory(KitCategory.TEAM_OWNED);
					kits.open();

				}
			}.runTask(this.m);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (Main.getInstance().stage == GameStage.PREGAME) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (this.m.stage != GameStage.GAMETIME) {
			event.setCancelled(true);
			return;
		}
		if ((event.getEntity() instanceof Player)) {
			((Player) event.getEntity()).setSaturation(5.0F);
		}
	}

	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		if ((event.getEntityType() == EntityType.GHAST) || (event.getEntityType() == EntityType.PIG_ZOMBIE)) {
			event.setCancelled(true);
			return;
		}
		if (this.m.stage == GameStage.PREGAME) {
			event.setCancelled(true);
			return;
		}
		if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
			return;
		}
		if (new Random().nextInt(5) != 0) {
			event.setCancelled(true);
		}
	}

	
/*	@EventHandler
	public void onUpdateCaixaDiaria(final PlayerLoginEvent e) {
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					m.caixa.refreshPlayer(e.getPlayer());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}.runTaskAsynchronously(m);
	}
*/
	
	@EventHandler
	public void onClanLogin(PlayerLoginEvent e) {
		if (m.type == Type.CLAN) {
			if (me.skater.Main.getInstance().getClanManager().getPlayerClan(e.getPlayer()) == null) {
				e.disallow(Result.KICK_OTHER, "\n§eVocê não possui os requisitos para jogar!\n§ePara jogar no modo de §bClans§e, você deve estar em um §bClan§e!\n§eUse §b/clan§e para mais informações.");
				return;
			}
		}
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		if (this.m.stage != GameStage.PREGAME) {
			if ((!this.m.isNotPlaying(p)) && (relog.contains(p.getUniqueId())) && (!this.reloged.contains(p.getUniqueId()))) {
				return;
			}
			if (this.m.GameTimer > m.getPlayerVipTime(p)) {
				if (!this.m.hasVip(p)) {
					if (this.deathMessage.containsKey(p.getUniqueId())) {
						event.disallow(PlayerLoginEvent.Result.KICK_OTHER, (String) this.deathMessage.get(p.getUniqueId()));
					} else {
						event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Torneio ja iniciou!");
					}
				}
			} else if ((this.m.perm.isMvp(p)) && (this.joined.contains(p.getUniqueId()))) {
				if (!this.m.perm.isYouTuber(p)) {
					if (this.deathMessage.containsKey(p.getUniqueId())) {
						event.disallow(PlayerLoginEvent.Result.KICK_OTHER, (String) this.deathMessage.get(p.getUniqueId()));
					} else {
						event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Torneio ja iniciou!");
					}
				}
			} else if (!this.m.perm.isMvp(p)) {
				if (this.deathMessage.containsKey(p.getUniqueId())) {
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, (String) this.deathMessage.get(p.getUniqueId()));
				} else {
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Torneio ja iniciou!");
				}
			}
		}
		if ((event.getResult() == PlayerLoginEvent.Result.KICK_FULL) || (this.m.getServer().getOnlinePlayers().length - this.m.adm.admin.size() - this.m.adm.youtuber.size() >= 80)) {
			if (this.m.perm.isVip(p)) {
				if (m.type == Type.TEAM) {
					event.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.GOLD + "Servidor esta lotado, todos os times estão cheios!");
					return;
				}
				event.allow();
			} else {
				event.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.GOLD + "Servidor esta lotado. Compre vip em: " + this.m.site + " para entrar em servidores lotados.");
			}
		}
	}

	@EventHandler
	public void onTeleport(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (this.m.stage == GameStage.PREGAME) {
			this.m.sendToSpawnAnyway(p);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onAsync(final PlayerLoginEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					m.sqlcmd.setPlayerRankAndKits(event.getPlayer().getUniqueId());
				} catch (SQLException e) {
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Houve um problema ao carregar suas informações.");
				} finally {
				}
			}
		}.runTaskAsynchronously(m);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		p.setAllowFlight(false);
		p.setFlying(false);
		p.setGameMode(GameMode.SURVIVAL);
		event.setJoinMessage(ChatColor.YELLOW + p.getName() + " entrou no servidor");
		m.vanish.updateVanished();
		if (this.m.stage == GameStage.PREGAME) {
			for (int i = 0; i < this.novidades.size(); i++) {
				final String message = (String) this.novidades.get(i);
				new BukkitRunnable() {
					public void run() {
						Title title = new Title(ChatColor.WHITE + "HeavenMC");
						title.setSubtitle(message);
						title.setTimingsToTicks();
						title.setFadeOutTime(10);
						title.setFadeInTime(0);
						title.setStayTime(35);
						title.send(p);
					}
				}.runTaskLaterAsynchronously(this.m, (i + 1) * 35);
			}
			if (!this.m.countStarted) {
				int count = this.m.getServer().getOnlinePlayers().length - this.m.adm.admin.size();
				if (count >= 5) {
					this.m.startCounter();
				}
			}
			int count = this.m.getServer().getOnlinePlayers().length - this.m.adm.admin.size();
			if ((this.m.PreGameTimer > 60) && (count >= 70) && (!this.m.timeChanged)) {
				this.m.PreGameTimer = 60;
				this.m.timeChanged = true;
				Bukkit.broadcastMessage("§eTempo de inicio alterado para: §b§l1 minuto");
			}
			event.setJoinMessage(null);
		}
		if (this.m.isNotPlaying(p)) {
			event.setJoinMessage(null);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void sendKitsMessage(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		StringUtils.sendCenteredMessage(p, "");
		StringUtils.sendCenteredMessage(p, "§7Bem-vindo ao §f§lHeaven§6§lHG§7!");
		StringUtils.sendCenteredMessage(p, "§7Para comprar §bVIP§7 e §bKits§7 acesse:");
		StringUtils.sendCenteredMessage(p, "§6HeavenMC.com.br");
		StringUtils.sendCenteredMessage(p, "§7Escolha seus kit clicando nos baús ou §b/kit");
//		StringUtils.sendCenteredMessage(p, "§7Digite §b/jogo§7 para mais informações!");
		StringUtils.sendCenteredMessage(p, "");

	}

	public void sendItems(Player p) {
		p.getInventory().setItem(0, this.chest);
		if (this.m.type != Type.ULTRA) {
			p.getInventory().setItem(1, ender);
		}
		if (m.type == Type.TEAM) {
			p.getInventory().setItem(4, m.team.getTeamStack());
	//		p.getInventory().setItem(5, emerald);
		} else {
	//		p.getInventory().setItem(4, emerald);
		}
	//	sendProfileItem(p, 8);
	}

	public void sendProfileItem(Player p, int slot) {
		if (((CraftPlayer) p).getHandle().playerConnection.networkManager.getVersion() < 47) {
			ItemStack head = new ItemStack(Material.NETHER_STAR);
			ItemMeta im = (ItemMeta) head.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "HeavenMC Menu §7(Clique)");
			head.setItemMeta(im);
			p.getInventory().setItem(slot, head);
		} else {
			ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

			SkullMeta im = (SkullMeta) head.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "HeavenMC Menu §7(Clique)");
			im.setOwner(p.getName());
			head.setItemMeta(im);

			p.getInventory().setItem(slot, head);
		}
	}

	
	@EventHandler
	public void onSendItems(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (this.m.stage == GameStage.PREGAME) {
			p.setHealth(20.0D);
			p.setFoodLevel(20);
			p.getInventory().clear();
			sendItems(p);

		}
	}

	@EventHandler
	public void updateTabList(PlayerJoinEvent event) {
		new BukkitRunnable() {
			public void run() {
				for (Player player : PlayerListener.this.m.getServer().getOnlinePlayers()) {
					PlayerListener.this.m.sendTabToPlayer(player);
				}
			}
		}.runTaskAsynchronously(this.m);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSetSpectating(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if ((relog.contains(p.getUniqueId())) && (!this.m.isNotPlaying(p))) {
			this.reloged.add(p.getUniqueId());
			new BukkitRunnable() {
				public void run() {
					PlayerListener.relog.remove(p.getUniqueId());
				}
			}.runTaskLater(this.m, 600L);
			return;
		}
		boolean needUpdate = true;
		if (this.m.stage == GameStage.PREGAME) {
		/*	try {
				me.skater.Main.getInstance().getClanManager().spawnTop10Hologram(p, new Location(m.getSpawn().getWorld(), 0.0D, m.getSpawn().getWorld().getHighestBlockYAt(0, 0) + 2, 0.0D));
			} catch (SQLException e) {
				System.out.println("Falha ao spawnar top 10");
				e.printStackTrace();
			}
		*/
			if (this.m.perm.isTrial(p)) {
				this.m.adm.setAdmin(p);
				needUpdate = false;
			}
		} else if ((!this.joined.contains(p.getUniqueId())) && (this.m.GameTimer < m.getPlayerVipTime(p))) {
			this.m.TotalPlayers += 1;
			p.getInventory().clear();
			p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.COMPASS, 1) });
			this.joined.add(p.getUniqueId());
			this.m.gamers.add(p.getUniqueId());
		} else {
			if (this.m.perm.isTrial(p)) {
				this.m.adm.setAdmin(p);
			} else {
				this.m.adm.setYoutuber(p);
			}
			needUpdate = false;
		}
		if (needUpdate) {
			this.m.vanish.updateVanished();
		}
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if (this.m.stage != GameStage.GAMETIME) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPing(ServerListPingEvent event) {

		event.setMaxPlayers(80 + m.adm.admin.size());
		if (this.m.stage == GameStage.PREGAME) {
			if (!this.m.countStarted) {
				event.setMotd(StringUtils.makeCenteredMotd("§6HeavenMC§8: §eHardcore Games") + "\n§r" + StringUtils.makeCenteredMotd("§bAguardando jogadores"));
			} else {
				event.setMotd(
						StringUtils.makeCenteredMotd("§6HeavenMC§8: §eHardcore Games") + "\n§r" + StringUtils.makeCenteredMotd("§bTorneio iniciando em " + this.m.getTime(Integer.valueOf(this.m.PreGameTimer))));
			}
		} else {
			event.setMotd(
					StringUtils.makeCenteredMotd("§6HeavenMC§8: §eHardcore Games") + "\n§r" + StringUtils.makeCenteredMotd("§cTorneio em andamento ha " + this.m.getTime(Integer.valueOf(this.m.GameTimer))));

		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {

		
		if (m.coliseumManager.isConstructed() && m.coliseumManager.isColiseumBlock(event.getBlock())) {
			event.setCancelled(true);
		}
		if ((!this.m.breakBlocks) && (event.getPlayer().getGameMode() != GameMode.CREATIVE)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
	
		if (m.coliseumManager.isConstructed() && m.coliseumManager.isInsideColiseum(event.getPlayer())) {
			event.setCancelled(true);
		}
		if ((!this.m.placeBlocks) && (event.getPlayer().getGameMode() != GameMode.CREATIVE)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		if (this.m.stage == GameStage.PREGAME) {
			event.setCancelled(true);
		}
		if ((!this.m.pickupItems) && (event.getPlayer().getGameMode() != GameMode.CREATIVE)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		if (!this.m.drops) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickup(PlayerShearEntityEvent event) {
		if (this.m.stage == GameStage.PREGAME) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if ((event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) || (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if ((event.getEntity() instanceof Player)) {
			if (!this.m.dano) {
				event.setCancelled(true);
			}
			if (this.m.stage != GameStage.GAMETIME) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onTab(PlayerCommandPreprocessEvent event) {
		if ((event.getMessage().toLowerCase().startsWith("/me ")) || (event.getMessage().toLowerCase().startsWith("/kill")) || (event.getMessage().toLowerCase().startsWith("/suicide"))) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent event) {
		if (((event.getDamager() instanceof Player)) && (this.m.stage == GameStage.PREGAME)) {
			event.setCancelled(true);
		}
		if (((event.getDamager() instanceof Player)) && ((event.getEntity() instanceof Player)) && (!this.m.pvp)) {
			event.setCancelled(true);
		}
		if ((!(event.getDamager() instanceof Player)) && (event.getDamage() > 7.0D)) {
			event.setDamage(7);
		}

		if (m.type == Type.TEAM) {
			if (m.stage == GameStage.GAMETIME) {
				if (event.getCause() == DamageCause.ENTITY_ATTACK) {
					if (event.getDamager() instanceof Player) {
						if (event.getEntity() instanceof Player) {
							if (m.team.hasTeam((Player) event.getDamager(), (Player) event.getEntity())) {
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (this.m.stage == GameStage.PREGAME) {
			event.setCancelled(true);
		}
		ItemStack item = event.getItemDrop().getItemStack();
		Player p = event.getPlayer();
		if ((item == null) || (item.getType() == Material.AIR)) {
			return;
		}
		boolean isItemKit = false;
		if (this.m.kit.KITSS.get(p.getUniqueId()) != null) {
			List<String> kitList = (List<String>) this.m.kit.KITSS.get(p.getUniqueId());
			for (String kitName : kitList) {
				kitName = kitName.toLowerCase();
				if (this.m.kit.items.get(kitName) != null) {
					for (ItemStack i : ((Kit) this.m.kit.items.get(kitName)).items) {
						if (item.getType() == i.getType()) {
							isItemKit = true;
							break;
						}
					}
				}
			}
		}
		if (isItemKit) {
			event.setCancelled(true);
		}
		if (item.getType() == Material.SKULL_ITEM) {
			event.getItemDrop().remove();
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onDeath(PlayerDeathEvent event) throws SQLException {
		final Player p = event.getEntity();
		if (this.m.isNotPlaying(p)) {
			event.getDrops().clear();
			event.setDroppedExp(0);
			event.setDeathMessage(null);
			return;
		}
		if (this.m.stage == GameStage.PREGAME) {
			event.setDeathMessage(null);
			return;
		}

		if (((this.m.hasVip(p)) || (this.m.teste.contains(p.getUniqueId()))) && (this.m.GameTimer < m.getPlayerVipTime(p)) && (!desistiu.contains(p.getUniqueId()))) {
			this.m.dropItems(p, event.getDrops(), p.getLocation());
			event.getDrops().clear();
			p.setHealth(20.0D);
			p.setFoodLevel(20);
			p.setSaturation(5.0F);
			Iterator<?> localIterator = p.getActivePotionEffects().iterator();
			if (localIterator.hasNext()) {
				PotionEffect pot = (PotionEffect) localIterator.next();
				p.removePotionEffect(pot.getType());
			}
			Location loc = this.m.getRespawnLocation();
			loc.getChunk().load(true);
			p.closeInventory();
			p.teleport(loc.clone().add(0.0D, 0.5D, 0.0D));
			p.setFireTicks(0);
			event.setDeathMessage(null);
			new BukkitRunnable() {
				public void run() {
					p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.COMPASS) });
					p.setFireTicks(0);
					if (m.perm.isMvp(p)) {
						m.kit.giveItem(p);
					}
				}
			}.runTaskLater(this.m, 1L);
			return;
		}
		if ((this.m.sql) && (this.m.stage == GameStage.GAMETIME)) {
			if (p.getKiller() != null) {
				final Player killer = p.getKiller();
				this.m.addKill(killer);
				me.skater.Main.getInstance().getRankManager().killReward(killer);
				if (!me.skater.Main.getInstance().getClanManager().isSameClan(p, killer)) {
					me.skater.Main.getInstance().getClanManager().clanKillerReward(killer);
					me.skater.Main.getInstance().getClanManager().clanKilledReward(p);
				}

			}
		}
		String morreu;
		if (m.type == Type.TEAM) {
			String pTeam = "Sem Time";
			TeamColors ptc = m.team.getTeam(p);
			if (ptc != null) {
				pTeam = "Time " + m.team.teamNomes.get(ptc);
			}
			morreu = "§b" + p.getName() + "§b§l(" + pTeam + "§r§c)§7";
		} else {
			morreu = "§b" + p.getName() + "§b§l[" + this.m.kit.getPlayerKit(p) + "]§r§b";
		}
		String messageDeath = "";
		if (p.getKiller() != null) {
			Player killer = p.getKiller();
			String kill;
			if (m.type == Type.TEAM) {
				String pTeam = "Sem Time";
				TeamColors ptc = m.team.getTeam(killer);
				if (ptc != null) {
					pTeam = "Time " + m.team.teamNomes.get(ptc);
				}
				kill = ChatColor.AQUA + killer.getName() + "(§l" + pTeam + "§r§b)§7";
			} else {
				kill = "§b" + killer.getName() + "§b§l[" + this.m.kit.getPlayerKit(killer) + "]§r§b";
			}
			String arma = this.m.name.getItemName(killer.getItemInHand());
			messageDeath = kill + " matou " + morreu + " usando sua " + arma;
			deathMessage(kill + " matou " + morreu + " usando sua " + arma);

		} else {
			messageDeath = morreu + " morreu e foi eliminado.";
			deathMessage(morreu + " morreu e foi eliminado.");
		}
		event.setDeathMessage(null);
		this.m.removeGamer(p);
		if (m.type == Type.TEAM) {
			m.checkTeamWinner();
		} else {
			m.checkWinner();
		}
		this.m.removeKills(p);
		new BukkitRunnable() {
			public void run() {
				for (Player playerr : PlayerListener.this.m.getServer().getOnlinePlayers()) {
					if (playerr != p) {
						PlayerListener.this.m.sendTabToPlayer(playerr);
					}
				}
			}
		}.runTaskAsynchronously(this.m);

		if (!this.m.hasVip(p)) {
			if (this.m.GameTimer <= m.getPlayerVipTime(p)) {
				p.kickPlayer("§aVoce morreu! Tente novamente na próxima\nAdquira §3PRO ou BETA§a para poder renascer antes dos §65 minutos§a de jogo!\n§aAcesse: §bheavenmc.com.br");
			} else {
				p.kickPlayer("§aVoce morreu! Tente novamente na proxima!\n§eAcesse: §bheavenmc.com.br");
				return;
			}
			this.deathMessage.put(p.getUniqueId(), "Voce perdeu!\n" + messageDeath);

		} else if (this.m.perm.isTrial(p)) {
			this.deathMessage.put(p.getUniqueId(), "Voce perdeu!\n" + messageDeath);
			this.m.adm.setAdmin(p);
			return;

		} else {
			this.deathMessage.put(p.getUniqueId(), "Voce perdeu!\n" + messageDeath);
			this.m.adm.setYoutuber(p);
			return;

		}

		this.m.dropItems(p, event.getDrops(), p.getLocation());
		this.m.kit.KITSS.remove(p.getUniqueId());
		event.getDrops().clear();
		event.setDeathMessage(null);

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final Player p = event.getPlayer();
		if (m.type == Type.TEAM) {
			if (p.getScoreboard().getTeam(p.getName()) != null) {
				p.getScoreboard().getTeam(p.getName()).unregister();
			}
		}
		this.m.adm.setPlayer(p);
		if (p.isDead()) {
			event.setQuitMessage(null);
			return;
		}
		event.setQuitMessage(ChatColor.YELLOW + p.getName() + " saiu do servidor");
		if ((this.m.isNotPlaying(p)) || (this.m.stage == GameStage.PREGAME) || (this.m.stage == GameStage.INVENCIBILITY)) {
			event.setQuitMessage(null);
			return;
		}
		if (this.m.winner == p.getUniqueId()) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			return;
		}
		if (!relog.contains(p.getUniqueId())) {
			relog.add(p.getUniqueId());
			Bukkit.getScheduler().scheduleSyncDelayedTask(this.m, new Runnable() {
				public void run() {
					if (!PlayerListener.this.reloged.contains(p.getUniqueId())) {
						PlayerListener.relog.remove(p.getUniqueId());
						String morreu = "§b" + p.getName() + "§b§l[" + m.kit.getPlayerKit(p) + "]§r§b";
						String messageDeath = morreu + " demorou para relogar e foi eliminado.";
						deathMessage(morreu + " demorou para relogar e foi eliminado.");
						deathMessage.put(p.getUniqueId(), "Você perdeu!\n" + messageDeath);
						PlayerListener.this.m.dropItems(p, p.getLocation());
						PlayerListener.this.m.removeGamer(p);
						if (m.type == Type.TEAM) {
							m.checkTeamWinner();
						} else {
							m.checkWinner();
						}
					} else {
						PlayerListener.this.reloged.remove(p.getUniqueId());
					}
				}
			}, 600L);
			return;
		}
		if ((this.m.stage == GameStage.GAMETIME) && (!p.isDead())) {
			String morreu = "§b" + p.getName() + "§b§l[" + m.kit.getPlayerKit(p) + "]§r§b";
			String messageDeath = morreu + " demorou para relogar e foi eliminado.";
			deathMessage(morreu + " demorou para relogar e foi eliminado.");
			deathMessage.put(p.getUniqueId(), "Voce perdeu!\n" + messageDeath);
			this.m.dropItems(p, p.getLocation());
			this.m.kit.KITSS.remove(p.getUniqueId());
			this.m.removeGamer(p);
			if (m.type == Type.TEAM) {
				m.checkTeamWinner();
			} else {
				this.m.checkWinner();
			}
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		event.setRespawnLocation(p.getLocation());
		if (this.m.perm.isTrial(p)) {
			this.m.adm.setAdmin(p);
		} else {
			this.m.adm.setYoutuber(p);
		}
	}

	@EventHandler
	public void onMap(MapInitializeEvent event) {
		if (this.m.winner == null) {
			return;
		}
		MapView map = event.getMap();
		for (MapRenderer r : map.getRenderers()) {
			map.removeRenderer(r);
		}
		map.addRenderer(new Map(this.m));
	}

	public void deathMessage(String message) {
		if (this.m.gamers.size() - 1 == 1)
			this.m.getServer().broadcastMessage(message + "\n§c" + (this.m.gamers.size() - 1) + " jogador restante");
		else
			this.m.getServer().broadcastMessage(message + "\n§c" + (this.m.gamers.size() - 1) + " jogadores restantes");
	}

	@EventHandler
	public void onBlockBreak(EntityExplodeEvent event) {
		if ((event.getEntity() instanceof EnderDragon)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onGenerate(EntityCreatePortalEvent event) {
		if ((event.getEntity() instanceof EnderDragon)) {
			for (BlockState b : event.getBlocks()) {
				if (b.getType() == Material.ENDER_PORTAL) {
					b.setType(Material.STATIONARY_WATER);
				}
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if ((event.getEntity() instanceof EnderDragon)) {
			event.setDroppedExp(20);
		}
	}

	public void updateTitle(Player p, String title, int size) {
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PacketPlayOutOpenWindow openWindow = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, 0, title, size, false);
		ep.playerConnection.sendPacket(openWindow);
		ep.updateInventory(ep.activeContainer);
		openWindow = null;
		ep = null;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (this.m.adm.isAdmin(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event) {
		Material skullItem = Material.SKULL_ITEM;
		if (event.getItemInHand().getType() == skullItem) {
			event.getBlock().setType(Material.AIR);
		}
	}

	@EventHandler
	public void onDamageZombiePigamn(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof PigZombie)) {
			e.setDamage(2.5D);
			return;
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (m.stage != GameStage.GAMETIME) {
			return;
		}
		if (m.isNotPlaying(p)) {
			return;
		}
		List<Entity> entities = p.getNearbyEntities(1.0D, 1.0D, 1.0D);
		for (Entity t : entities) {
			if (t.getType() == EntityType.PLAYER) {
				Player p2 = (Player) t;
				if (m.isNotPlaying(p2)) {
					return;
				}
				int tempo = 1;
				if (m.type == Type.TEAM) {
					if (m.team.getTeamName(p).equals(m.team.getTeamName(p2))) {
						BarAPI.setMessage(p, ChatColor.GREEN + p2.getName() + " - " + m.kit.getKitName(m.kit.getPlayerKit(p2)), tempo);
					} else {
						BarAPI.setMessage(p, ChatColor.RED + p2.getName() + " - " + m.kit.getKitName(m.kit.getPlayerKit(p2)), tempo);
					}
					return;
				}
				BarAPI.setMessage(p, p2.getName() + " - " + m.kit.getKitName(m.kit.getPlayerKit(p2)), tempo);
			}
		}
	}

	@EventHandler
	public void onTeamChat(PlayerChatEvent e) {
		if (chatTime.contains(e.getPlayer().getUniqueId())) {
			String rawmsg = e.getMessage();
			e.setCancelled(true);
			String msg = ChatColor.translateAlternateColorCodes('&', "§7- §b[Time] §4" + e.getPlayer().getName() + ": §f" + rawmsg + "");
			for (Player online : m.team.getPlayersTeamOnline(m.team.getTeam(e.getPlayer())))
				online.sendMessage(msg);

		}
	}

}
