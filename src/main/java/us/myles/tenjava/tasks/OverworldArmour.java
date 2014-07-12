package us.myles.tenjava.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import us.myles.tenjava.Util;

public class OverworldArmour implements Runnable {

	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld().getName().equals("moon"))
				continue;
			int c = Util.countLunarArmour(p);
			if (c == 4) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10 * 20, 1));
			}
		}
	}

}
