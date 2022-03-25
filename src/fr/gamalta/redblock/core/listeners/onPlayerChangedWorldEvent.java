package fr.gamalta.redblock.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onPlayerChangedWorldEvent implements Listener {

	RedCore main;
	Utils utils;

	public onPlayerChangedWorldEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {

		Player player = event.getPlayer();
		String WorldName = player.getWorld().getName();

		if (main.settingsCFG.contains("Worlds." + WorldName + ".Execute")) {

			ConfigurationSection section = main.settingsCFG.getConfigurationSection("Worlds." + WorldName + ".Execute");

			for (String string : section.getKeys(false)) {

				String permission = section.getString(string + ".Permission");
				String command = section.getString(string + ".Command").replace("%player%", player.getName());

				if (permission.equals("null") || player.hasPermission(permission)) {

					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

				}
			}
		}
	}
}