package me.caio.HungerGames.Kits;

public class Illusion /* extends KitInterface */ {
/*	public Illusion(Main main) {
		super(main);
	}

	public ArrayList<UUID> semdano = new ArrayList<UUID>();

	@EventHandler
	public void SemDano(EntityDamageByEntityEvent e) {
		if (semdano.contains(e.getEntity().getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Teste(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			final Player p = (Player) e.getDamager();
			final Player p2 = (Player) e.getEntity();
			if (!hasAbility(p2)) {
				return;
			}
			if (Cooldown.isInCooldown(p2.getUniqueId(), "illusion")) {
				return;
			}
			if (getMain().isNotPlaying(p)) {
				return;
			}
			e.setCancelled(true);
			semdano.add(p2.getUniqueId());
			final Location loc = p2.getLocation();

			Location LocalDoPlayer = p.getLocation();
			Vector direção = LocalDoPlayer.getDirection().normalize();
			direção.multiply(+3);
			LocalDoPlayer.subtract(direção);

			p2.teleport(LocalDoPlayer);
			p.getWorld().playSound(loc, Sound.ENDERDRAGON_WINGS, 3, 3);
			PotionEffect pot = new PotionEffect(PotionEffectType.BLINDNESS, 4 * 20, 5);
			if (!Cooldown.isInCooldown(p.getUniqueId(), "illusion")) {
				Cooldown c = new Cooldown(p2.getUniqueId(), "illusion", 10);
				c.start();
			}
			p.addPotionEffect(pot, true);

			new BukkitRunnable() {
				@Override
				public void run() {
					semdano.remove(p2.getUniqueId());

				}
			}.runTaskLater(getMain(), 10);

			// for(Player todos:Bukkit.getOnlinePlayers()){
			// try {
			// ParticleEffect.CLOUD.display((float)0.10, (float)0.10,(float)
			// 0.10, (float)0.05,
			// 15,loc.getBlock().getLocation().clone().add(0.5,1,0.5),todos);
			// ParticleEffect.SMOKE_LARGE.display((float)0.10,
			// (float)0.10,(float) 0.10, (float)0.05,
			// 15,loc.getBlock().getLocation().clone().add(0.5,1,0.5),todos);
			// } catch (Exception e1) {
			// e1.printStackTrace();
			// }
			// }

		}
	}

	public Kit getKit() {
		return new Kit("illusion", Arrays.asList(new String[] { "Crie uma ilusão em seu adversario e aproveite a vantagem para mata-lo." }), new ArrayList<ItemStack>(), new ItemStack(Material.LOG));
	} */
}
