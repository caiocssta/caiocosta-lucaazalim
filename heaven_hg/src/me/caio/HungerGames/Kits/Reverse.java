package me.caio.HungerGames.Kits;

public class Reverse /* extends KitInterface */ {
/*	public Reverse(Main main) {
		super(main);
	}

	static ArrayList<String> JaTrocou = new ArrayList<String>();
	static HashMap<String, ItemStack> Espadas = new HashMap<String, ItemStack>();

	static ItemStack Madeira = new ItemStack(Material.WOOD_SWORD);
	static ItemStack Pedra = new ItemStack(Material.STONE_SWORD);
	static ItemStack Ouro = new ItemStack(Material.GOLD_SWORD);
	static ItemStack Ferro = new ItemStack(Material.IRON_SWORD);
	static ItemStack Diamante = new ItemStack(Material.DIAMOND_SWORD);

	@EventHandler
	public void Dano(EntityDamageByEntityEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			if (hasAbility(d)) {
				if (isInvencibility()) {
					p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
					return;
				}
				if (!JaTrocou.contains(p.getName())) {
					if (d.getItemInHand().getType() == Material.WOOD_SWORD)
						Espadas.put(d.getName(), Madeira);
					if (d.getItemInHand().getType() == Material.STONE_SWORD)
						Espadas.put(d.getName(), Pedra);
					if (d.getItemInHand().getType() == Material.GOLD_SWORD)
						Espadas.put(d.getName(), Ouro);
					if (d.getItemInHand().getType() == Material.IRON_SWORD)
						Espadas.put(d.getName(), Ferro);
					if (d.getItemInHand().getType() == Material.DIAMOND_SWORD)
						Espadas.put(d.getName(), Diamante);

					if (p.getItemInHand().getType() == Material.WOOD_SWORD)
						Espadas.put(p.getName(), Madeira);
					if (p.getItemInHand().getType() == Material.STONE_SWORD)
						Espadas.put(p.getName(), Pedra);
					if (p.getItemInHand().getType() == Material.GOLD_SWORD)
						Espadas.put(p.getName(), Ouro);
					if (p.getItemInHand().getType() == Material.IRON_SWORD)
						Espadas.put(p.getName(), Ferro);
					if (p.getItemInHand().getType() == Material.DIAMOND_SWORD)
						Espadas.put(p.getName(), Diamante);
					Material i = d.getItemInHand().getType();
					Material ii = p.getItemInHand().getType();
					if ((ii == Material.DIAMOND_SWORD) || (ii == Material.GOLD_SWORD) || (ii == Material.IRON_SWORD) || (ii == Material.WOOD_SWORD) || (ii == Material.STONE_SWORD)) {
						if ((i == Material.DIAMOND_SWORD) || (i == Material.GOLD_SWORD) || (i == Material.IRON_SWORD) || (i == Material.WOOD_SWORD) || (i == Material.STONE_SWORD)) {
							d.setItemInHand(Espadas.get(p.getName()));
							p.setItemInHand(Espadas.get(d.getName()));
							p.sendMessage("§cSua espada foi trocada pela do " + d.getName());
							d.sendMessage("§aSua espada foi trocada pela do " + p.getName());
							p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
							d.playSound(d.getLocation(), Sound.ANVIL_USE, 1, 1);
							d.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 1));
							JaTrocou.add(p.getName());
						}
					}
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		return new Kit("Reverse", Arrays.asList(new String[] { "Troque sua espada pela espada do inimigo, quando a troca é efetuada voce ficará com fraqueza 1 por 10 segundos" }), kititems,
				new ItemStack(Material.DIAMOND_SWORD));
	} */
}
