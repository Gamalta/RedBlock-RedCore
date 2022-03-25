package fr.gamalta.redblock.core.schelduler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import fr.gamalta.redblock.core.RedCore;

public class onLaunchpadsParticlesEvent {

	public onLaunchpadsParticlesEvent(RedCore main) {

		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {

			if (main.launchpadsCFG.isConfigurationSection("Launchpads")) {

				for (String section : main.launchpadsCFG.getConfigurationSection("Launchpads").getKeys(false)) {

					Location LaunchPadLoc = new Location(Bukkit.getWorld(main.launchpadsCFG.getString("LaunchPads." + section + ".World")), main.launchpadsCFG.getInt("Launchpads." + section + ".X") + 0.5D, main.launchpadsCFG.getInt("Launchpads." + section + ".Y"), main.launchpadsCFG.getInt("Launchpads." + section + ".Z") + 0.5D);

					for (Player pl : Bukkit.getOnlinePlayers()) {

						if (pl.getLocation().distanceSquared(LaunchPadLoc) <= 100) {

							pl.spawnParticle(Particle.valueOf(main.settingsCFG.getString("Launchpad.Particles.Type")), LaunchPadLoc, main.settingsCFG.getInt("Launchpad.Particles.Amount"));
						}
					}
				}
			}
		}, 0, 10);
	}
}