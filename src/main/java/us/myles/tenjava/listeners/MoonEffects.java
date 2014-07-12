package us.myles.tenjava.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import us.myles.tenjava.Plugin;
import us.myles.tenjava.tasks.GravityEffect;

public class MoonEffects implements Listener {
	private Plugin plugin;
	private static List<String> jumpMap = new ArrayList<String>();

	public MoonEffects(Plugin plugin) {
		this.plugin = plugin;
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
