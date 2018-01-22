package me.caio.HungerGames.Kits;

public class FlashBack /* extends KitInterface */ {
/*	public FlashBack(Main main) {
		super(main);
	}

	public static HashMap<UUID, Location> playerLastLocation = new HashMap<UUID, Location>();
	public static HashMap<UUID, ItemStack[]> playerItems = new HashMap<UUID, ItemStack[]>();
	public static HashMap<UUID, ItemStack[]> playerArmaduras = new HashMap<UUID, ItemStack[]>();
	public static HashMap<UUID, List<Item>> dropItems = new HashMap<UUID, List<Item>>();

	@EventHandler
	public void iniciar(GameStartEvent e) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (hasAbility(player)) {
				startToPlayer(player);
			}
		}
	}

	public static void startToPlayer(final Player player) {
		playerLastLocation.put(player.getUniqueId(), player.getLocation());
		playerItems.put(player.getUniqueId(), player.getInventory().getContents());
		playerArmaduras.put(player.getUniqueId(), player.getInventory().getArmorContents());
		new BukkitRunnable() {
			int n = 1;

			@Override
			public void run() {
				if (n == 20) {
					if (dropItems.containsKey(player.getUniqueId())) {
						dropItems.remove(player.getUniqueId());
					}
					playerLastLocation.put(player.getUniqueId(), player.getLocation());
					playerItems.put(player.getUniqueId(), player.getInventory().getContents());
					playerArmaduras.put(player.getUniqueId(), player.getInventory().getArmorContents());
					n = 1;
				}
				// ParticleEffect.LARGE_SMOKE.display(
				// playerLastLocation.get(player.getUniqueId()), 0, 0, 0,
				// 0, 3);
				n++;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1 * 20);
	}

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (hasAbility(p)) {
			if (event.getAction().toString().startsWith("RIGHT")) {
				if (p.getItemInHand().getType() == Material.HOPPER) {
					if (Cooldown.isInCooldown(p.getUniqueId(), "flashback")) {
						p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "flashback");
						p.sendMessage("§6FlashBack em cooldown de §f" + timeleft + "§6 segundos!");

						return;
					} else {
						if (Gladiator.inGladiator(p)) {
							return;
						}
						Cooldown c = new Cooldown(p.getUniqueId(), "flashback", 90);
						c.start();
						p.teleport(playerLastLocation.get(p.getUniqueId()));
						p.getInventory().setContents(playerItems.get(p.getUniqueId()));
						p.getInventory().setArmorContents(playerArmaduras.get(p.getUniqueId()));
						p.updateInventory();
						if (dropItems.containsKey(p.getUniqueId()))
							for (Item i : dropItems.get(p.getUniqueId())) {
								i.remove();
							}
					}
				}
			}

		}

	}

	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.HOPPER) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void dropItem(PlayerDropItemEvent e) {
		if (hasAbility(e.getPlayer())) {
			if (dropItems.containsKey(e.getPlayer().getUniqueId())) {
				List<Item> drops = dropItems.get(e.getPlayer().getUniqueId());
				drops.add(e.getItemDrop());
				dropItems.put(e.getPlayer().getUniqueId(), drops);
			} else {
				List<Item> drops = new ArrayList<Item>();
				drops.add(e.getItemDrop());
				dropItems.put(e.getPlayer().getUniqueId(), drops);
			}
		}
	}

	// @EventHandler
	// public void Iniciou(PlayerStartGameEvent e) {
	// if (hasAbility(e.getPlayer()))
	// Itens(e.getPlayer());
	// }

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.HOPPER, ChatColor.AQUA + "Voltar na Sombra", ""));

		return new Kit("FlashBack", Arrays.asList(new String[] { "Tenha o controle do tempo em suas mãos.", "Monte suas estratégias e volte no tempo", "sempre que precisar se recuperar." }), kititems,
				createItem(Material.HOPPER, ChatColor.AQUA + "Velocidade", ""));
	} */
}
