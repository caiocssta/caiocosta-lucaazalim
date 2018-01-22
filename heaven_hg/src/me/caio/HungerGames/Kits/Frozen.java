package me.caio.HungerGames.Kits;

public class Frozen/* extends KitInterface */{
/*	Main m;

	public Frozen(Main main) {
		super(main);
		this.m = main;

	}

	@SuppressWarnings("deprecation")
	public boolean isOnGround(Player p) {
		Location l = p.getLocation();
		l = l.add(0.0D, -1.0D, 0.0D);
		return l.getBlock().getState().getTypeId() != 0;
	}

	@EventHandler
	public void RastroDeNeve(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() == Material.SNOW_BALL) {
			return;
		}
		if (!hasAbility(p)) {
			return;
		}

		if (getMain().stage != GameStage.GAMETIME) {
			return;
		}
		if (m.isNotPlaying(p)) {
			return;
		}
		Location loc = p.getLocation();
		if (loc.getBlock().isEmpty()) {
			loc.setY(loc.getY() - 1.0D);
			if (isSoil(loc.getBlock())) {
				loc.setY(loc.getY() + 1.0D);
				loc.getBlock().setType(Material.SNOW);
			}
		}
	}

	public boolean isSoil(Block block) {
		Material mat = block.getType();

		return (mat == Material.DIRT) || (mat == Material.GRASS) || (mat == Material.SANDSTONE) || (mat == Material.SAND);
	}

	@EventHandler
	public void Plataforma(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!hasAbility(p)) {
			return;
		}

		if (e.getPlayer().getItemInHand().getType() == Material.SNOW_BALL) {
			Block block = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
			if (block.getType() == Material.AIR) {

				if (e.getPlayer().getLocation().getY() > 128.0D) {
					e.getPlayer().sendMessage(ChatColor.RED + "Voce nao pode passar deste ponto!");
					return;
				}
				if (getMain().stage != GameStage.GAMETIME) {
					return;
				}
				if ((GameListener.feast != null) && (GameListener.feast.isFeastBlock(p.getLocation().getBlock()))) {
					p.sendMessage("§7[§c§l!§7] Voce nao pode usar este kit aqui!");
					return;
				}
				final Block voar = e.getPlayer().getLocation().add(0.0D, -1.0D, 0.0D).getBlock();
				final Block voar1 = e.getPlayer().getLocation().add(0.0D, -1.0D, -1.0D).getBlock();
				final Block voar2 = e.getPlayer().getLocation().add(0.0D, -1.0D, 1.0D).getBlock();
				final Block voar3 = e.getPlayer().getLocation().add(-1.0D, -1.0D, 0.0D).getBlock();
				final Block voar4 = e.getPlayer().getLocation().add(1.0D, -1.0D, 0.0D).getBlock();
				final Block voar5 = e.getPlayer().getLocation().add(-1.0D, -1.0D, -1.0D).getBlock();
				final Block voar6 = e.getPlayer().getLocation().add(-1.0D, -1.0D, 1.0D).getBlock();
				final Block voar7 = e.getPlayer().getLocation().add(1.0D, -1.0D, -1.0D).getBlock();
				final Block voar8 = e.getPlayer().getLocation().add(1.0D, -1.0D, 1.0D).getBlock();

				int x = voar.getLocation().getBlockX();
				int y = voar1.getLocation().getBlockY();
				int z = voar2.getLocation().getBlockZ();

				Block b2 = voar.getWorld().getBlockAt(x, y - 0, z);
				if (b2.getType() == Material.AIR) {
					if (voar.isEmpty()) {
						if (voar.getType() == Material.AIR) {
							voar.setType(Material.PACKED_ICE);
							ItemStack is = p.getItemInHand();
							is.setAmount(is.getAmount() - 1);
							p.setItemInHand(is);
						}
					} else if (voar.getType() == Material.PACKED_ICE) {
						voar.setType(Material.PACKED_ICE);
					}
					if (voar1.isEmpty()) {
						if (voar1.getType() == Material.AIR) {
							voar1.setType(Material.PACKED_ICE);
						}
					} else if (voar1.getType() == Material.PACKED_ICE) {
						voar1.setType(Material.PACKED_ICE);
					}
					if (voar2.isEmpty()) {
						if (voar2.getType() == Material.AIR) {
							voar2.setType(Material.PACKED_ICE);
						}
					} else if (voar2.getType() == Material.PACKED_ICE) {
						voar2.setType(Material.PACKED_ICE);
					}
					if (voar3.isEmpty()) {
						if (voar3.getType() == Material.AIR) {
							voar3.setType(Material.PACKED_ICE);
						}
					} else if (voar3.getType() == Material.PACKED_ICE) {
						voar3.setType(Material.PACKED_ICE);
					}
					if (voar4.isEmpty()) {
						if (voar4.getType() == Material.AIR) {
							voar4.setType(Material.PACKED_ICE);
						}
					} else if (voar4.getType() == Material.PACKED_ICE) {
						voar4.setType(Material.PACKED_ICE);
					}
					if (voar5.isEmpty()) {
						if (voar5.getType() == Material.AIR) {
							voar5.setType(Material.PACKED_ICE);
						}
					} else if (voar5.getType() == Material.PACKED_ICE) {
						voar5.setType(Material.PACKED_ICE);
					}
					if (voar6.isEmpty()) {
						if (voar6.getType() == Material.AIR) {
							voar6.setType(Material.PACKED_ICE);
						}
					} else if (voar6.getType() == Material.PACKED_ICE) {
						voar6.setType(Material.PACKED_ICE);
					}
					if (voar7.isEmpty()) {
						if (voar7.getType() == Material.AIR) {
							voar7.setType(Material.PACKED_ICE);
						}
					} else if (voar7.getType() == Material.PACKED_ICE) {
						voar7.setType(Material.PACKED_ICE);
					}
					if (voar8.isEmpty()) {
						if (voar8.getType() == Material.AIR) {
							voar8.setType(Material.PACKED_ICE);
						}
					} else if (voar8.getType() == Material.PACKED_ICE) {
						voar8.setType(Material.PACKED_ICE);
					}
				} else if (voar.getType() == Material.PACKED_ICE) {
				}
			}
		}
	}

	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<ItemStack>();
		kitItems.add(new ItemStack(Material.SNOW_BALL, 16));
		return new Kit("frozen", Arrays.asList(new String[] { "O verdadeiro homem de gelo, deixe rastros e crie plataformas no meio do ar com bolas de neve" }), kitItems, new ItemStack(Material.PACKED_ICE));
	} */
}
