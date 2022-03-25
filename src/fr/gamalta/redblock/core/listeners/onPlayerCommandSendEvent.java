package fr.gamalta.redblock.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import fr.gamalta.redblock.core.RedCore;

public class onPlayerCommandSendEvent implements Listener {

	RedCore main;

	public onPlayerCommandSendEvent(RedCore main) {

		this.main = main;
	}

	@EventHandler
	public void onPlayerCommandSend(PlayerCommandSendEvent event) {

		Player player = event.getPlayer();

		for (String string : main.settingsCFG.getConfigurationSection("TabCommand").getKeys(false)) {

			if (!player.hasPermission(main.settingsCFG.getString("TabCommand." + string + ".Permission"))) {

				event.getCommands().remove(main.settingsCFG.getString("TabCommand." + string + ".Command"));
			}
		}
	}
}