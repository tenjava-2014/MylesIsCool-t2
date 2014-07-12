package us.myles.tenjava;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Util {
	public static int countLunarArmour(Player player2) {
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
	public static ItemStack getRandomArmour() {
		Material[] t = new Material[] { Material.LEATHER_BOOTS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS };
		ItemStack is = new ItemStack(t[new Random().nextInt(t.length)]);
		LeatherArmorMeta laMeta = (LeatherArmorMeta) is.getItemMeta();
		laMeta.setDisplayName(ChatColor.GOLD + "Lunar Armour");
		laMeta.setLore(Arrays.asList("Reduces Gravity on the moon per piece", "When full set worn in overworld, reverses gravity!"));
		laMeta.setColor(Color.YELLOW);
		is.setItemMeta(laMeta);
		return is;
	}
}
