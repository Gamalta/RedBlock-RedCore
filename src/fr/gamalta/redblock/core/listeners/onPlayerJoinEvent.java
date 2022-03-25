package fr.gamalta.redblock.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onPlayerJoinEvent implements Listener {

	RedCore main;
	Utils utils;

	public onPlayerJoinEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();

		if (!player.hasPermission(main.permissionsCFG.getString("Ninja.View"))) {

			main.ninja.forEach(pl -> player.hidePlayer(main, pl));
			main.ninjaDeactivation.keySet().forEach(pl -> player.hidePlayer(main, pl));
		}

		if (!player.hasPermission(main.permissionsCFG.getString("Vanish.View"))) {

			main.vanish.forEach(pl -> player.hidePlayer(main, pl));
			main.vanishDeactivation.keySet().forEach(pl -> player.hidePlayer(main, pl));
		}

		if (player.hasPermission(main.permissionsCFG.getString("JoinAdvancement"))) {

			utils.sendJoinAdvancement(player);

		}

		utils.createDataFile(player);
	}
}