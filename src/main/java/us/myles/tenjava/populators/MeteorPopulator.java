package us.myles.tenjava.populators;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

public class MeteorPopulator extends BlockPopulator {

	@Override
	public void populate(World arg0, Random arg1, Chunk arg2) {
		Location center = arg2.getBlock(8, arg2.getWorld().getHighestBlockYAt(8, 8), 0).getLocation().add(0, arg1.nextInt(2), 0);
		if (arg1.nextInt(50) == 1) {
			int rad = 5 + arg1.nextInt(5);
			boolean doLoot = (arg1.nextInt(3) == 1);
			for (int x = -rad; x <= rad; x++) {
				for (int y = -rad; y <= rad; y++) {
					for (int z = -rad; z <= rad; z++) {
						Location here = center.clone().add(new BlockVector(x, y, z));
						if (here.distance(center) <= rad + 0.5) {
							here.getBlock().setType(Material.NETHER_BRICK);
						}
					}
					Block here = center.getWorld().getHighestBlockAt(center.clone().add(new BlockVector(x, y, 0)));
					here.setType(Material.FIRE);
				}
			}
			if (doLoot) {
				Block where = center.getWorld().getHighestBlockAt(center).getLocation().subtract(0, 3, 0).getBlock();
				where.setType(Material.CHEST);
				Chest chest = (Chest) where.getState();
				chest.getBlockInventory().addItem(getLoot());
				doLoot = false;
			}
		}
	}

	private ItemStack[] getLoot() {
		ItemStack[] is = new ItemStack[] { new ItemStack(Material.NETHER_BRICK, 10), new ItemStack(Material.GOLD_INGOT, 5), new ItemStack(Material.BONE, 3), new ItemStack(Material.MINECART, 1),
				new ItemStack(Material.APPLE, 5), new ItemStack(Material.WOOD, 3), new ItemStack(Material.COBBLESTONE, 4) , new ItemStack(Material.REDSTONE_BLOCK, 4)};
		List<ItemStack> chosen = new ArrayList<ItemStack>();
		for (int i = 0; i < new Random().nextInt(10) + 1; i++) {
			chosen.add(is[new SecureRandom().nextInt(is.length)]);
		}
		return chosen.toArray(new ItemStack[0]);
	}

}
