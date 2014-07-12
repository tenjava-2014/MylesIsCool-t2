package us.myles.tenjava.tasks;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;

public class TorchBurnout implements Runnable {

	private Block block;

	public TorchBurnout(Block block) {
		this.block = block;
	}

	@Override
	public void run() {
		if (block.getType() == Material.TORCH) {
			block.breakNaturally();
			block.getWorld().playSound(block.getLocation(), Sound.FIZZ, 1L, 1L);
		}
	}

}
