package us.myles.tenjava.populators;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.BlockVector;

public class CraterPopulator extends BlockPopulator {

	@Override
	public void populate(World arg0, Random arg1, Chunk arg2) {
		Location center = arg2.getBlock(8, arg2.getWorld().getHighestBlockYAt(8, 8), 0).getLocation().add(0, arg1.nextInt(2), 0);
		if (arg1.nextInt(8) == 1) {
			int rad = 1 + arg1.nextInt(5);
			boolean doLoot = (arg1.nextInt(100) == 1);
			for (int x = -rad; x <= rad; x++) {
				for (int y = -rad; y <= rad; y++) {
					for (int z = -rad; z <= rad; z++) {
						Location here = center.clone().add(new BlockVector(x, y, z));
						if (here.distance(center) <= rad + 0.5) {
							if (doLoot && here.distance(center) < 1) {
								here.getBlock().setType(Material.CHEST);
								Chest chest = (Chest) here.getBlock().getState();
								chest.getBlockInventory().addItem(getRandomItems());
								doLoot = false;
							} else {
								here.getBlock().setType(Material.AIR);
							}
						}
					}
				}
			}
		}
	}

	private ItemStack getRandomItems() {
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
