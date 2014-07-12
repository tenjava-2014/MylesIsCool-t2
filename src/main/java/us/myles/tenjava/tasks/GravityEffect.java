package us.myles.tenjava.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import us.myles.tenjava.listeners.MoonEffects;

public class GravityEffect implements Runnable {

	private Player player;
	private int id;
	private int tick = 0;

	public GravityEffect(Player player) {
		this.player = player;
	}

	public void setTaskID(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		int c = countLunarArmour(player) + 1;
		if (tick < (20 / c)) {
			player.setVelocity(new Vector(0, (0.3 - (tick == 0 ? 0 : (tick / 100))) / c, 0));
		}
		if (tick > 50) {
			Bukkit.getScheduler().cancelTask(id);
			MoonEffects.removePlayer(player);
		}
		player.setFallDistance(0f);
		tick++;
	}

	private int countLunarArmour(Player player2) {
		int c = 0;
		for (ItemStack a : player2.getInventory().getArmorContents()) {
			if (a != null) {
				if (a.getType() != Material.AIR) {
					if (a.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Lunar Armour"))
						c++;
				}
			}
		}
		return c;
	}

}
