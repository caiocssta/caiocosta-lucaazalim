package me.caio.HungerGames.Kits;

public class Equalizer {/* extends KitInterface {

	public Equalizer(Main main) {
		super(main);
	}

	public static HashSet<FallingBlock> blocos = new HashSet<FallingBlock>();
	ArrayList<String> chao = new ArrayList<String>();

	@SuppressWarnings("deprecation")
	public void Equalizador(final Location loc, final Player p) {
		int x = 0;
		int y = 0;
		int z = 0;
		if (loc.clone().add(x, y, z).getBlock().getType() == Material.AIR) {
			for (x = -4; x < 4; x++) {
				for (z = -4; z < 4; z++) {
					for (y = 0; y < 4; y++) {
						final Block b = loc.clone().add(x, 1.0D, z).getBlock();
						FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), Material.REDSTONE_BLOCK, (byte) 0);
						fb.setDropItem(false);
						blocos.add(fb);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
							public void run() {
								if (chao.contains(p.getName()))
									chao.remove(p.getName());
							}
						}, 20 * 10);
					}
				}
			}
		}
	}

	@EventHandler
	public void bl(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if (chao.contains(p.getName())) {
			if (b.getType() == Material.REDSTONE_BLOCK) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void Boost(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location loc = new Location(p.getWorld(), e.getTo().getX(), e.getTo().getY() - 1.0D, e.getTo().getZ());

		if (chao.contains(p.getName())) {
			if (loc.getBlock().getType() == Material.REDSTONE_BLOCK) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0));
			}
		}
	}

	@EventHandler
	public void Trocar(EntityChangeBlockEvent e) {
		if (e.getEntity() instanceof FallingBlock) {
			final FallingBlock fb = (FallingBlock) e.getEntity();
			final Block cima = e.getBlock().getRelative(BlockFace.DOWN);
			if (cima.getType() != Material.GLASS) {

				if (blocos.contains(fb)) {
					e.setCancelled(true);
					fb.setDropItem(false);
					cima.setType(Material.REDSTONE_BLOCK);
					blocos.remove(fb);

					new BukkitRunnable() {
						int tempo = 10;

						public void run() {
							tempo--;
							if (tempo < 0) {
								cima.setType(Material.DIRT);
								cancel();
							}
						}
					}.runTaskTimer(Main.getInstance(), 0, 20);
				}
			}
		}
	}

	@EventHandler
	public void Om(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.getItemInHand().getType() == Material.REDSTONE) {
				if (hasAbility(p)) {
					e.setCancelled(true);
					p.updateInventory();
					if (isInvencibility()) {
						p.sendMessage("§cVoce nao pode usar este Kit antes da invencibilidade!");
						return;
					}
					if (!Cooldown.isInCooldown(p.getUniqueId(), "equalizer")) {
						Equalizador(p.getLocation(), p);
						if (!chao.contains(p.getName()))
							chao.add(p.getName());
						Cooldown c = new Cooldown(p.getUniqueId(), "equalizer", 50);
						c.start();
					} else {
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "equalizer");
						p.sendMessage("§6Equalizer em cooldown de §f" + timeleft + "§6 segundos!");
					}
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.REDSTONE, "§cEqualizer"));
		return new Kit("equalizer", Arrays.asList(new String[] { "Cria uma linha de dano para seus inimigos e voce", "ganha velocidade e regeneração,", "tempo para acabar de 10 segundos,cooldown de 1 minuto" }),
				kititems, new ItemStack(Material.REDSTONE));
	} */
}
