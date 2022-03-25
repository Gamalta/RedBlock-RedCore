package fr.gamalta.redblock.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onPlayerQuitEvent implements Listener {

	RedCore main;
	Utils utils;

	public onPlayerQuitEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);
	}

	public void onPlayerQuit(PlayerQuitEvent event) {

		Player player = event.getPlayer();

		if (main.ninja.contains(player)) {

			event.setQuitMessage(null);
		}
	}
}
