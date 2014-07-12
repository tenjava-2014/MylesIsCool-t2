package us.myles.tenjava.generators;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import us.myles.tenjava.populators.CraterPopulator;
import us.myles.tenjava.populators.MeteorPopulator;
import us.myles.tenjava.populators.TentPopulator;

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
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(world, 8);
		generator.setScale(1D / 64D);
		byte[] blocks = new byte[32768];

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int realX = (16 * chunkX) + x;
				int realZ = (16 * chunkZ) + z;
				double height = generator.noise(realX, realZ, 0.5, 0.5) * 16;
				if (height > 128) {
					height = 128;
				}
				if (height <= 0) {
					height = 1;
				}
				for (int y = 0; y < height + 32; y++) {
					if (y == 0)
						blocks[combineXYZ(x, y, z)] = (byte) Material.BEDROCK.getId();
					else
						blocks[combineXYZ(x, y, z)] = (byte) Material.ENDER_STONE.getId();
				}
			}
		}

		return blocks;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new CraterPopulator(), new TentPopulator(), new MeteorPopulator());
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		int x = 0;
		int z = 0;
		return world.getHighestBlockAt(x, z).getLocation();
	}

}
