package us.myles.tenjava.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.BlockVector;

import us.myles.tenjava.Util;

public class CraterPopulator extends BlockPopulator {

	@Override
	public void populate(World arg0, Random arg1, Chunk arg2) {
		Location center = arg2.getBlock(8, arg2.getWorld().getHighestBlockYAt(8, 8), 0).getLocation().add(0, arg1.nextInt(2), 0);
		boolean isIcePool = arg1.nextInt(3) == 1;
		if (arg1.nextInt(3) == 1) {
			int rad = 1 + arg1.nextInt(5);
			boolean doLoot = (arg1.nextInt(50) == 1);
			for (int x = -rad; x <= rad; x++) {
				for (int y = -rad; y <= rad; y++) {
					for (int z = -rad; z <= rad; z++) {
						Location here = center.clone().add(new BlockVector(x, y, z));
						if (here.distance(center) <= rad + 0.5) {
							if (here.getBlock().getType() != Material.AIR) {
								here.getBlock().setType(isIcePool ? Material.ICE : Material.AIR);
							}
						}
					}
				}
			}
			if (doLoot) {
				Block where = center.getWorld().getHighestBlockAt(center);
				where.setType(Material.CHEST);
				Chest chest = (Chest) where.getState();
				chest.getBlockInventory().addItem(Util.getRandomArmour());
				doLoot = false;
			}
		}
	}



}
