package fr.gamalta.redblock.core.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.gamalta.lib.RedLib;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onPlayerMoveEvent implements Listener {

	RedCore main;
	Utils utils;
	RedLib lib;

	public onPlayerMoveEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);
		lib = new RedLib();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {

		Player player = event.getPlayer();
		Location location;
		boolean bool = utils.launchpadIsPlate();

		if (bool) {

			location = event.getTo().getBlock().getLocation();

		} else {

			double x = event.getTo().getBlock().getX();
			double y = event.getTo().getBlock().getY() - 1;
			double z = event.getTo().getBlock().getZ();
			World world = event.getTo().getBlock().getWorld();

			location = new Location(world, x, y, z);
		}

		if (utils.isLaunchpad(location) && !main.launchpadCooldown.contains(player)) {

			main.launchpadCooldown.add(player);

			player.setVelocity(player.getLocation().getDirection().multiply(main.launchpadsCFG.getDouble("Launchpads." + utils.getLaunchpadID(location) + ".Velocity.X")));
			player.setVelocity(new Vector(player.getVelocity().getX(), main.launchpadsCFG.getDouble("Launchpads." + utils.getLaunchpadID(location) + ".Velocity.Y"), player.getVelocity().getZ()));
			lib.playSound(player, main.settingsCFG, "Launchpad.Sound");

			new BukkitRunnable() {

				@Override
				public void run() {

					main.launchpadCooldown.remove(player);
				}
			}.runTaskLater(main, main.settingsCFG.getLong("Launchpad.Cooldown") * 20);
		}
	}
}