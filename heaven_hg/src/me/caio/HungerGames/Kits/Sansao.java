package me.caio.HungerGames.Kits;

public class Sansao /* extends KitInterface */ {
	/*public Sansao(Main main) {
		super(main);
	}

	@EventHandler
	public void Kit2(PlayerInteractEntityEvent e) {
		if (e.getRightClicked().getType() == EntityType.PLAYER) {
			Player p = e.getPlayer();
			Player r = (Player) e.getRightClicked();
			if (p.getItemInHand().getType() == Material.SHEARS) {
				if (hasAbility(p)) {
					if (isInvencibility()) {
						p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
						return;
					}
					if (Cooldown.isInCooldown(p.getUniqueId(), "Sansao")) {
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Sansao");
						p.sendMessage("§6Sansao em cooldown de §f" + timeleft + "§6 segundos!");
						return;
					}
					if (!Cooldown.isInCooldown(p.getUniqueId(), "Sansao")) {
						Cooldown c = new Cooldown(p.getUniqueId(), "Sansao", 60);
						c.start();
					}
					r.playSound(p.getLocation(), Sound.NOTE_BASS_GUITAR, 2, 2);
					r.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 0));
					r.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 0));
					r.sendMessage("§c" + p.getName() + " usou o Kit Sansao em você!");
					p.sendMessage("§aSeu Kit foi usado em " + r.getName());
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.SHEARS, "§cSansao"));

		return new Kit("Sansao", Arrays.asList(new String[] { "Use sua tesoura e deixe seu", "oponente mais fraco e lento" }), kititems, new ItemStack(Material.SHEARS));
	} */
}