package us.myles.tenjava.generators;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import us.myles.tenjava.populators.CraterPopulator;

public class MoonGenerator extends ChunkGenerator {

	public int combineXYZ(int x, int y, int z) {
		return (x * 16 + z) * 128 + y;
	}

	@Override
	public boolean canSpawn(World world, int x, int z) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public byte[] generate(World world, Random random, int chunkX, int chunkZ) {
		// 16 * 16 * 256 / 2 = 32768;
		// NoiseGenerator noiseGenerator = new SimplexNoiseGenerator(world);

		byte[] blocks = new byte[65536 / 2];
		for (int y = 0; y < 256; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					// 0 -> 16 -8 -> 8
					// 0 -> 16 16
					int height = (int) (((Math.abs(Math.sin((x - 8) * 5)) + Math.abs(Math.sin((z - 8) * 5))) * 100D) - 90) / 10 + 10;

					if (y < height) {
						blocks[combineXYZ(x, y, z)] = (byte) Material.ENDER_STONE.getId();
					}
				}
			}
		}
		return blocks;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new CraterPopulator());
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		int x = 0;
		int z = 0;
		return world.getHighestBlockAt(x, z).getLocation();
	}

}
