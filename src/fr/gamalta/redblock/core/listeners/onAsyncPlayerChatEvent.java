package fr.gamalta.redblock.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onAsyncPlayerChatEvent implements Listener {

	RedCore main;
	Utils utils;

	public onAsyncPlayerChatEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

		Player player = event.getPlayer();

		if (main.ninja.contains(player) || main.ninjaDeactivation.containsKey(player)) {

			event.setCancelled(true);
			player.spigot().sendMessage(new Message(main, main.messageCFG, "Ninja.Chat").create());
		}
	}
}