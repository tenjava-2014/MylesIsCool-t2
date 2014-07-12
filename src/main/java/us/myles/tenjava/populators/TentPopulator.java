package us.myles.tenjava.populators;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class TentPopulator extends BlockPopulator {

	@Override
	public void populate(World arg0, Random arg1, Chunk arg2) {
		if (arg1.nextInt(40) == 1) {
			boolean doLoot = (arg1.nextInt(10) == 1);
			Block base = arg2.getWorld().getHighestBlockAt(arg2.getBlock(0, 0, 0).getLocation().subtract(0, 1, 0));
			for (int i = 0; i < 5; i++) {
				tentBlock(base.getLocation().clone().add(i, 1, 0).getBlock());
				tentBlock(base.getLocation().clone().add(i, 1, 0).getBlock());
				tentBlock(base.getLocation().clone().add(i, 2, 1).getBlock());
				tentBlock(base.getLocation().clone().add(i, 3, 2).getBlock());
				tentBlock(base.getLocation().clone().add(i, 2, 3).getBlock());
				tentBlock(base.getLocation().clone().add(i, 1, 4).getBlock());
				if (i == 3 && doLoot) {
					Block where = base.getLocation().clone().add(i, 1, 2).getBlock();
					where.setType(Material.CHEST);
					Chest chest = (Chest) where.getState();
					chest.getBlockInventory().addItem(getLoot());
					doLoot = false;
				}
				if (i == 4) {
					base.getLocation().clone().add(i, 2, 2).getBlock().setType(Material.FENCE);
					base.getLocation().clone().add(i, 1, 2).getBlock().setType(Material.FENCE);
				}
			}
			base.getLocation().clone().add(-3, 0, 2).getBlock().setType(Material.NETHERRACK);

		}
	}

	@SuppressWarnings("deprecation")
	private void tentBlock(Block block) {
		block.setType(Material.STAINED_CLAY);
		block.setData((byte) 13);
	}

	private ItemStack[] getLoot() {
		ItemStack[] is = new ItemStack[] { new ItemStack(Material.GLOWSTONE_DUST, 3), new ItemStack(Material.IRON_INGOT, 2), new ItemStack(Material.BONE, 3), new ItemStack(Material.MINECART, 1),
				new ItemStack(Material.WOOL, 1), new ItemStack(Material.WOOD, 3), new ItemStack(Material.COBBLESTONE, 4) };
		List<ItemStack> chosen = new ArrayList<ItemStack>();
		for (int i = 0; i < new Random().nextInt(10) + 1; i++) {
			chosen.add(is[new SecureRandom().nextInt(is.length)]);
		}
		if (new SecureRandom().nextBoolean()) {
			chosen.add(getRandomArmour());
		}
		return chosen.toArray(new ItemStack[0]);
	}

	private ItemStack getRandomArmour() {
		Material[] t = new Material[] { Material.LEATHER_BOOTS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS };
		ItemStack is = new ItemStack(t[new Random().nextInt(t.length)]);
		LeatherArmorMeta laMeta = (LeatherArmorMeta) is.getItemMeta();
		laMeta.setDisplayName(ChatColor.GOLD + "Lunar Armour");
		laMeta.setLore(Arrays.asList("Reduces Gravity on the moon per piece"));
		laMeta.setColor(Color.YELLOW);
		is.setItemMeta(laMeta);
		return is;
	}

}
