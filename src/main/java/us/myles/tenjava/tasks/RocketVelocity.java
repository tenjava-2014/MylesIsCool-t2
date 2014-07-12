package us.myles.tenjava.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class RocketVelocity implements Runnable {

	private Entity block;
	private int id;

	public RocketVelocity(Entity first) {
		this.block = first;
	}

	@Override
	public void run() {
		if (block.isDead()) {
			Bukkit.getScheduler().cancelTask(this.id);
			return;
		}
		block.setVelocity(new Vector(0, 1D, 0));
		block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
	}

	public void setTaskID(int id) {
		this.id = id;
	}

}
