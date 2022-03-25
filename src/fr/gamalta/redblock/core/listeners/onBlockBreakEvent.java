package fr.gamalta.redblock.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onBlockBreakEvent implements Listener {

	RedCore main;
	Utils utils;

	public onBlockBreakEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {

		Player player = event.getPlayer();

		if (utils.isLaunchpad(event.getBlock().getLocation())) {

			if (player.hasPermission(main.permissionsCFG.getString("Launchpads.Delete"))) {

				String id = utils.getLaunchpadID(event.getBlock().getLocation());

				if (!id.equals("ERROR")) {

					main.launchpadsCFG.set("Launchpads." + id, null);

					player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.Deleted").create());

				}
			} else {

				event.setCancelled(true);

			}
		}
	}
}