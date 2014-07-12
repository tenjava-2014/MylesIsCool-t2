package us.myles.tenjava.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import us.myles.tenjava.Plugin;
import us.myles.tenjava.tasks.GravityEffect;
import us.myles.tenjava.tasks.TorchBurnout;

public class MoonEffects implements Listener {
	private Plugin plugin;
	private static List<String> jumpMap = new ArrayList<String>();

	public MoonEffects(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent event) {
		if (event.getLocation().getWorld().getName().equals("moon")) {
			event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2));
			String name = event.getEntity().getType().name().toLowerCase();
			name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
			event.getEntity().setCustomName(ChatColor.GREEN + "Alien " + name);
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if (e.getItem().getItemStack().getType() == Material.ENDER_STONE) {
			if (e.getPlayer().getWorld().getName().equals("moon")) {
				ItemMeta im = e.getItem().getItemStack().getItemMeta();
				im.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + "Moonstone");
				e.getItem().getItemStack().setItemMeta(im);
			}
		}
	}

	@EventHandler
	public void onTorch(BlockPlaceEvent event) {
		if (!event.getPlayer().getWorld().getName().equals("moon"))
			return;
		if (event.getBlock().getType() == Material.TORCH) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new TorchBurnout(event.getBlock()), 5L);
		}
	}

	@EventHandler
	public void onJump(PlayerMoveEvent event) {
		if (!event.getPlayer().getWorld().getName().equals("moon"))
			return;
		if (event.getPlayer().getPassenger() != null)
			return;
		if (event.getFrom().getY() < event.getTo().getY()) {
			if (jumpMap.contains(event.getPlayer().getName())) {
				return;
			}
			if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
				return;
			GravityEffect gravity = new GravityEffect(event.getPlayer());
			int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, gravity, 1L, 1L);
			gravity.setTaskID(id);
			jumpMap.add(event.getPlayer().getName());
		}
	}

	public static void removePlayer(Player player) {
		jumpMap.remove(player.getName());
	}
}
